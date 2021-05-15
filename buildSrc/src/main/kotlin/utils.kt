import org.gradle.api.Project

import org.gradle.kotlin.dsl.*

fun Project.kubbitProject() {

    this.repositories {
        maven {
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        }
    }

    this.dependencies {
        "compileOnly"(Dependencies.bukkit)

        "implementation" (kotlin("stdlib-jdk8"))
    }
}
