repositories {
    mavenLocal()
    jcenter()
    maven("https://plugins.gradle.org/m2")
    maven("https://dl.bintray.com/cognifide/maven-public")
    maven("https://dl.bintray.com/neva-dev/maven-public")
}

dependencies {
    implementation("com.cognifide.gradle:aem-plugin:12.0.0-beta3")
    implementation("com.cognifide.gradle:environment-plugin:0.1.7")
    implementation("com.neva.gradle:fork-plugin:4.2.2")
}

