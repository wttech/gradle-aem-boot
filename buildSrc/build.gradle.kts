repositories {
    mavenLocal()
    jcenter()
    maven("https://plugins.gradle.org/m2")
    maven("http://dl.bintray.com/cognifide/maven-public")
    maven("https://dl.bintray.com/neva-dev/maven-public")
}

dependencies {
    implementation("com.cognifide.gradle:aem-plugin:11.0.10")
    implementation("com.cognifide.gradle:environment-plugin:0.1.7")
    implementation("com.neva.gradle:fork-plugin:4.2.2")
}

