plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

val versionObj = Version(major = "4", minor = "2", revision = "1")

project.group = "fr.misterassm.kubbit"
project.version = "$versionObj"

val shadowJar: com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar by tasks
val jar: Jar by tasks
val build: Task by tasks
val clean: Task by tasks
val test: Test by tasks

shadowJar.classifier = "withDependencies"

val sourcesForRelease = task<Copy>("sourcesForRelease") {
    includeEmptyDirs = false
}

val minimalJar = task<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("minimalJar") {
    dependsOn(shadowJar)
    minimize()
    classifier = shadowJar.classifier + "-min"
    configurations = shadowJar.configurations
    from(sourceSets["main"].output)
    manifest.inheritFrom(jar.manifest)
}

val sourcesJar = task<Jar>("sourcesJar") {
    classifier = "sources"
    from("src/main/kotlin")
    from(sourcesForRelease.destinationDir)

    dependsOn(sourcesForRelease)
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    exclude("*.pom")
}

tasks.withType<JavaCompile> {
    val arguments = mutableListOf("-Xlint:deprecation", "-Xlint:unchecked")
    options.encoding = "UTF-8"
    options.isIncremental = true
    if (JavaVersion.current().isJava9Compatible) doFirst {
        arguments += "--release"
        arguments += "8"
    }
    doFirst {
        options.compilerArgs = arguments
    }
}

jar.apply {
    baseName = project.name
    manifest.attributes(mapOf(
        "Implementation-Version" to version,
        "Automatic-Module-Name" to "net.dv8tion.jda"))
}

build.apply {
    dependsOn(jar)
    dependsOn(sourcesJar)
    dependsOn(shadowJar)
    dependsOn(minimalJar)

    jar.mustRunAfter(clean)
    sourcesJar.mustRunAfter(jar)
    shadowJar.mustRunAfter(sourcesJar)
}

test.apply {
    useJUnitPlatform()
    failFast = true
}

kubbitProject()

fun getProjectProperty(propertyName: String): String {
    var property = ""
    if (hasProperty(propertyName)) {
        property = project.properties[propertyName] as? String ?: ""
    }
    return property
}

fun getBuild(): String {
    return System.getenv("BUILD_NUMBER")
        ?: System.getProperty("BUILD_NUMBER")
        ?: System.getenv("GIT_COMMIT")?.substring(0, 7)
        ?: System.getProperty("GIT_COMMIT")?.substring(0, 7)
        ?: "DEV"
}

class Version(
    val major: String,
    val minor: String,
    val revision: String) {
    override fun toString() = "$major.$minor.${revision}_${getBuild()}"
}
