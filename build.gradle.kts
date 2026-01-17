import xyz.jpenilla.runpaper.task.RunServer
import xyz.jpenilla.runvelocity.task.RunVelocity

plugins {
    kotlin("jvm") version "2.0.20-Beta1"
    kotlin("kapt") version "2.0.20-Beta1"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("xyz.jpenilla.run-velocity") version "2.3.1"
}

group = "net.Mirik9724"
version = "0.1"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-releases/") }
    maven { url = uri("https://repo.papermc.io/repository/maven-snapshots") }
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://maven.elmakers.com/repository/") }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.7.8-R0.1-SNAPSHOT")
    compileOnly("com.velocitypowered:velocity-api:3.1.0")
    compileOnly("com.github.Mirik9724:MirikAPI:v0.1.5.9")
    kapt("com.velocitypowered:velocity-api:3.1.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation(kotlin("test"))
    testImplementation("com.github.Mirik9724:MirikAPI:v0.1.5.9")
}

tasks {
    register<RunServer>("run12") {
        group = "run"
        minecraftVersion("1.12.2")
        runDirectory.set(layout.projectDirectory.dir("run/run-1.12"))
        jvmArgs("-Xmx2G", "-Xms1G")
    }

    register<RunServer>("run20") {
        group = "run"
        minecraftVersion("1.20.1")
        runDirectory.set(layout.projectDirectory.dir("run/run-1.20"))
        jvmArgs("-Xmx2G", "-Xms2G")
    }

    register<RunVelocity>("runVe") {
        group = "run"
        velocityVersion("3.1.0")
        runDirectory.set(layout.projectDirectory.dir("run/run-velocity"))
        jvmArgs("-Xmx2G", "-Xms2G")
    }
}

val targetJavaVersion = 17
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

val templateSource = file("src/main/templates")
val templateDest = layout.buildDirectory.dir("generated/sources/templates")
val generateTemplates = tasks.register<Copy>("generateTemplates") {
    val props = mapOf("version" to project.version)
    inputs.properties(props)

    from(templateSource)
    into(templateDest)
    expand(props)
}
sourceSets.main.configure { java.srcDir(generateTemplates.map { it.outputs }) }
