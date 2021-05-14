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

subprojects {
/*    kotlin {
        sourceSets.all {
            with(languageSettings) {
                useExperimentalAnnotation("kotlin.contracts.ExperimentalContracts")
            }
        }
    }

    tasks {
        test { useJUnitPlatform() }

        compileKotlin {
            kotlinOptions {
                jvmTarget = "1.8"
                suppressWarnings = true
                //freeCompilerArgs = freeCompilerArgs.plus("-Xopt-in=kotlin.time.ExperimentalTime,kotlin.ExperimentalStdlibApi,kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
        compileTestKotlin {
            kotlinOptions.jvmTarget = "1.8"
        }
    }*/
}

dependencies {
    subprojects.forEach { api(it) }
}
