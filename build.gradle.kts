plugins {
    id("com.neva.fork")
    id("com.cognifide.aem.instance.local")
    id("com.cognifide.environment")
}

apply(from = "gradle/fork/props.gradle.kts")

description = "Gradle AEM Boot"
defaultTasks(":setup", ":await")

repositories {
    jcenter()
    maven("https://dl.bintray.com/neva-dev/maven-public")
}

aem {
    environment {
        docker {
            containers {
                "httpd" {
                    resolve {
                        resolveFiles {
                            download("http://download.macromedia.com/dispatcher/download/dispatcher-apache2.4-linux-x86_64-4.3.3.tar.gz").then {
                                copyArchiveFile(it, "**/dispatcher-apache*.so", file("modules/mod_dispatcher.so"))
                            }
                        }
                        ensureDir("cache", "logs")
                    }
                    up {
                        ensureDir(
                                "/usr/local/apache2/logs",
                                "/opt/aem/dispatcher/cache/content/example/we-retail"
                        )
                        execShell("Starting HTTPD server", "/usr/local/apache2/bin/httpd -k start")
                    }
                    reload {
                        cleanDir("/opt/aem/dispatcher/cache/content/example/we-retail")
                        execShell("Restarting HTTPD server", "/usr/local/apache2/bin/httpd -k restart")
                    }
                    dev {
                        watchConfigDir("conf")
                    }
                }
            }
        }
        hosts {
            "http://author.example.com" { tag("author") }
            "http://we-retail.example.com" { tag("publish") }
            "http://dispatcher.example.com" { tag("dispatcher") }
        }
        healthChecks {
            http("Publish page 'Home'", "http://we-retail.example.com", "Built for the coldest winter on earth")
            http("Publish page 'Women'", "http://we-retail.example.com/women", "Women")
            http("Author Sites Editor", "http://author.example.com/sites.html") { options { basicCredentials = authorInstance.credentials }; containsText("Sites") }
        }
    }

    instance {
        satisfier {
            packages {
                // "dep.vanity-urls"("pkg/vanityurls-components-1.0.2.zip")
                "dep.acs-aem-commons"("https://github.com/Adobe-Consulting-Services/acs-aem-commons/releases/download/acs-aem-commons-4.0.0/acs-aem-commons-content-4.0.0-min.zip")
                "tool.ac-tool"("https://repo1.maven.org/maven2/biz/netcentric/cq/tools/accesscontroltool", "accesscontroltool-package/2.3.2/accesscontroltool-package-2.3.2.zip", "accesscontroltool-oakindex-package/2.3.2/accesscontroltool-oakindex-package-2.3.2.zip")
                "tool.aem-easy-content-upgrade"("https://github.com/valtech/aem-easy-content-upgrade/releases/download/2.0.0/aecu.bundle-2.0.0.zip")
                "tool.search-webconsole-plugin"("com.neva.felix:search-webconsole-plugin:1.2.0")
            }
        }

        provisioner {
            step("configure-mappings") {
                condition { instance.publish && once() }
                sync {
                    repository {
                        import("/etc/map/http", configDir.get().asFile.resolve("mapping/we-retail.json"))
                    }
                }
            }
        }
    }

    localInstance {
        install {
            files {
                // https://github.com/Cognifide/gradle-aem-plugin#pre-installed-osgi-bundles-and-crx-packages
            }
        }
    }

    tasks {
        environmentUp {
            mustRunAfter(instanceUp, instanceSatisfy, instanceProvision, instanceSetup)
        }
        environmentAwait {
            mustRunAfter(instanceAwait)
        }

        /*
        register("doSomething") {
            doLast {
                // implement own task using Gradle AEM DSL
                // aem.authorInstance.sync { /* ... */ }
            }
        }
        */
    }
}
