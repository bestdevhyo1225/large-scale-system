import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    id("org.springframework.boot") version "2.7.2" apply false
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
    kotlin("plugin.allopen") version "1.6.21"
    kotlin("kapt") version "1.6.21"
}

allprojects {
    apply(plugin = "java")

    group = "com.hyoseok"
    version = "0.0.1-SNAPSHOT"
    java.sourceCompatibility = JavaVersion.VERSION_17

    repositories {
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "kotlin-kapt")

    dependencies {
        // Spring Doc Open Api (Swagger)
        implementation("org.springdoc:springdoc-openapi-ui:1.6.13")
        implementation("org.springdoc:springdoc-openapi-kotlin:1.6.13")

        // Kotlin 로깅
        implementation("io.github.microutils:kotlin-logging:3.0.4")

        // Kotlin
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        // Kotlin Coroutine
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

        // Spring Boot Custom Yaml Importer
        implementation("com.github.kingbbode:spring-boot-custom-yaml-importer:0.3.0")

        // H2 Datbase
        runtimeOnly("com.h2database:h2")
        runtimeOnly("io.r2dbc:r2dbc-h2")

        // MySQL Driver
        runtimeOnly("mysql:mysql-connector-java")

        // Spring Boot Starter Test
        testImplementation("org.springframework.boot:spring-boot-starter-test")

        // Spring Kafka Test
        testImplementation("org.springframework.kafka:spring-kafka-test")

        // Kotest, Mockk Test
        testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
        testImplementation("io.kotest:kotest-assertions-core:5.5.4")
        testImplementation("io.kotest:kotest-assertions-core-jvm:5.5.4")
        testImplementation("io.kotest:kotest-extensions-spring:4.4.3")
        testImplementation("io.kotest:kotest-runner-junit5-jvm:5.5.4")
        testImplementation("io.mockk:mockk:1.13.2")

        // Reactor Test
        testImplementation("io.projectreactor:reactor-test")

        // Embedded Redis Server for Test
        testImplementation("it.ozimov:embedded-redis:0.7.3") {
            // Exception in thread "main"
            // java.lang.IllegalArgumentException: LoggerFactory is not a Logback LoggerContext but Logback is on the classpath.
            // 위의 예외가 발생하지 않도록 slf4j-simple 모듈을 제외한다.
            exclude("org.slf4j", "slf4j-simple")
        }
    }

    dependencyManagement {
        imports {
            mavenBom(SpringBootPlugin.BOM_COORDINATES)
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
