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
    instance {
        provisioner {
            enableCrxDe()
            deployPackage("com.neva.felix:search-webconsole-plugin:1.2.0")
        }

        provisioner {
            step("configureMappings") {
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
                // https://github.com/wttech/gradle-aem-plugin#pre-installed-osgi-bundles-and-crx-packages
            }
        }
    }
}

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
        http("Author Sites Editor", "http://author.example.com/sites.html") { options { basicCredentials = aem.authorInstance.credentials }; containsText("Sites") }
    }
}

tasks {
    environmentUp {
        mustRunAfter(instanceUp, instanceProvision, instanceSetup)
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
