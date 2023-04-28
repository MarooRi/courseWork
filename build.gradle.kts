plugins {
    kotlin("multiplatform") version "1.8.0"
    kotlin("plugin.serialization") version "1.8.0"
    application
}

group = "org.faronovama"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

val kotlinVersion = "1.8.0"
val serializationVersion = "1.5.0-RC"
val ktorVersion = "2.2.2"
val kotestVersion = "5.5.4"
val kotlinWrappers = "org.jetbrains.kotlin-wrappers"
val kotlinWrappersVersion = "1.0.0-pre.490"
val kotlinHtmlVersion = "0.7.2"
val logbackVersion = "1.4.5"

kotlin {
    jvm {
        withJava()
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-core:$ktorVersion")
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-server-html-builder-jvm:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinHtmlVersion")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
                implementation("org.litote.kmongo:kmongo:4.8.0")
                implementation("org.litote.kmongo:kmongo-async:4.8.0")
                implementation("org.litote.kmongo:kmongo-coroutine:4.8.0")
                implementation("org.litote.kmongo:kmongo-reactor:4.8.0")
                implementation("org.litote.kmongo:kmongo-serialization:4.8.0")
                implementation("org.litote.kmongo:kmongo-id-serialization:4.8.0")
                implementation("org.json:json:20230227")

                implementation ("org.apache.logging.log4j:log4j-api:2.20.0")
                implementation ("org.apache.logging.log4j:log4j-core:2.20.0")
                implementation ("org.apache.poi:poi:5.2.2")
                implementation ("org.apache.poi:poi-ooxml:5.2.2")
                implementation ("org.apache.logging.log4j:log4j-api:2.17.2")
                implementation ("org.apache.logging.log4j:log4j-core:2.17.2")
                implementation ("org.apache.xmlbeans:xmlbeans:5.0.3")
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                dependencies {
                    implementation(
                        project.dependencies.enforcedPlatform(
                            "$kotlinWrappers:kotlin-wrappers-bom:$kotlinWrappersVersion"
                        )
                    )
                    implementation("$kotlinWrappers:kotlin-emotion")
                    implementation("$kotlinWrappers:kotlin-react")
                    implementation("$kotlinWrappers:kotlin-react-dom")
                    implementation("$kotlinWrappers:kotlin-react-router-dom")
                    implementation("$kotlinWrappers:kotlin-react-redux")
                    implementation("$kotlinWrappers:kotlin-tanstack-react-query")
                    implementation("$kotlinWrappers:kotlin-tanstack-react-query-devtools")
                    implementation(npm("cross-fetch", "3.1.5"))
                }
            }
        }
        val jsTest by getting
    }
}

application {
    mainClass.set("org.faronovama.application.ServerKt")
}

tasks.named<Copy>("jvmProcessResources") {
    val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
    from(jsBrowserDistribution)
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}