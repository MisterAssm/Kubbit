plugins {
    kotlin("jvm") version "1.5.0"
    base
    `maven-publish`
}

allprojects {
    group = "fr.misterassm"
    version = "1.0"

    repositories {
        mavenCentral()
    }
}

dependencies {
    subprojects.forEach { api(it) }
}
