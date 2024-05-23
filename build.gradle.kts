plugins {
    id("fabric-loom") version "1.6-SNAPSHOT"
    `maven-publish`
    java
}

version = property("mod_version")!!

val port_lib_modules: String by extra

loom {
    accessWidenerPath = file("src/main/resources/lodestone.accesswidener")
}

repositories {
    mavenCentral()
    maven("https://maven.theillusivec4.top/") { name = "Curios maven" }
    maven("https://dvs1.progwml6.com/files/maven") { name = "JEI maven" }
    maven("https://maven.tterrag.com/") { name = "tterrag maven" }
    maven("https://maven.blamejared.com/") { name = "BlameJared maven" }
    maven("https://cursemaven.com") {
        name = "Curse Maven"
        content {
            includeGroup("curse.maven")
        }
    }
    maven(url = "https://maven.ladysnake.org/releases")
    maven("https://maven.terraformersmc.com/")
    maven(url = "https://maven.parchmentmc.org")
    maven("https://mvn.devos.one/snapshots/")
    maven(url = "https://mvn.devos.one/releases/")
    maven( "https://maven.jamieswhiteshirt.com/libs-release")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")

    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-1.20.1:2023.09.03@zip")
    })

    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")

    modCompileOnlyApi("mezz.jei:jei-${property("minecraft_version")}-common-api:${property("jei_version")}")
    modCompileOnlyApi("mezz.jei:jei-${property("minecraft_version")}-fabric-api:${property("jei_version")}")
    // at runtime, use the full JEI jar for Fabric
    modRuntimeOnly("mezz.jei:jei-${property("minecraft_version")}-fabric:${property("jei_version")}")

    modImplementation("dev.emi:trinkets:${property("trinkets_version")}")

    modApi("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${property("cca_version")}")
    modApi("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:${property("cca_version")}")
    modApi("dev.onyxstudios.cardinal-components-api:cardinal-components-world:${property("cca_version")}")

    port_lib_modules.split(",").forEach { module ->
        modApi(("io.github.fabricators_of_create.Porting-Lib:$module:${property("port_lib_version")}"))
    }

    modApi("com.jamieswhiteshirt:reach-entity-attributes:${property("reach_entity_attributes_version")}")
}

tasks {

    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(getProperties())
            expand(mutableMapOf("version" to project.version))
        }
    }

    jar {
        from("LICENSE")
    }


}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}