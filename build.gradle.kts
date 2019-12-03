plugins {
    id("com.neva.fork")
    id("com.cognifide.aem.instance")
    id("com.cognifide.aem.environment")
}

description = "Gradle AEM Boot"
defaultTasks(":setup")

repositories {
    jcenter()
    maven { url = uri( "https://dl.bintray.com/neva-dev/maven-public") }
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
                        ensureDir("/usr/local/apache2/logs")
                        execShell("Starting HTTPD server", "/usr/local/apache2/bin/httpd -k start")
                    }
                    reload {
                        execShell("Restarting HTTPD server", "/usr/local/apache2/bin/httpd -k restart")
                    }
                    dev {
                        watchConfigDir("conf")
                    }
                }
            }
        }
        hosts {
            author("http://author.example.com")
            other("http://dispatcher.example.com")
        }
        healthChecks {
            url("Author module 'Sites'", "http://author.example.com/sites.html") {
                options { basicCredentials = authorInstance.credentials }
                containsText("Sites")
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
        instanceSatisfy {
            packages {
                 // "dep.vanity-urls"("pkg/vanityurls-components-1.0.2.zip")
                "dep.acs-aem-commons"("https://github.com/Adobe-Consulting-Services/acs-aem-commons/releases/download/acs-aem-commons-4.0.0/acs-aem-commons-content-4.0.0-min.zip")
                "tool.ac-tool"("https://repo1.maven.org/maven2/biz/netcentric/cq/tools/accesscontroltool", "accesscontroltool-package/2.3.2/accesscontroltool-package-2.3.2.zip", "accesscontroltool-oakindex-package/2.3.2/accesscontroltool-oakindex-package-2.3.2.zip")
                "tool.aem-easy-content-upgrade"("https://github.com/valtech/aem-easy-content-upgrade/releases/download/2.0.0/aecu.bundle-2.0.0.zip")
                "tool.search-webconsole-plugin"("com.neva.felix:search-webconsole-plugin:1.2.0")

            }
        }

        instanceProvision {
            // https://github.com/Cognifide/gradle-aem-plugin#task-instanceprovision
        }
    }
}

apply(from = "gradle/fork.gradle.kts")
