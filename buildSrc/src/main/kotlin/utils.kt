import org.gradle.api.Project

import org.gradle.kotlin.dsl.*

fun Project.kubbitProject() {

    this.repositories {
        maven {
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        }
    }

    this.dependencies {
        "compileOnly"("org.bukkit", "bukkit", "1.12.2-R0.1-SNAPSHOT")
    }
}
