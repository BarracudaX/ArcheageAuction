import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    id("io.gitlab.arturbosch.detekt") version("1.23.4")
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.10"
    kotlin("plugin.jpa") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.21"
}

group = "com.arslan"
version = "1.0-SNAPSHOT"

allOpen {
    annotation("jakarta.persistence.Entity")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.2.0") {
            bomProperty("hibernate.version" ,"6.4.0.Final")
        }
    }
}

detekt{
    this.config.setFrom("detekt.yml")
}

dependencies {
    implementation("org.hibernate.search:hibernate-search-mapper-orm:7.0.0.Final")
    //spring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    {
        exclude(group = "org.hibernate", module = "hibernate-core")
    }
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.session:spring-session-jdbc")

    //kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    //other
    runtimeOnly("com.mysql:mysql-connector-j")

    //metrics & tracing
    implementation("io.micrometer:micrometer-observation")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")
    implementation("io.zipkin.brave:brave-instrumentation-mysql8:6.0.2")

    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("org.liquibase:liquibase-core:4.24.0")
    implementation("org.hibernate.search:hibernate-search-backend-elasticsearch:7.0.0.Final")


    //test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")
    testImplementation("org.testcontainers:elasticsearch")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.7.2")
    testImplementation("org.seleniumhq.selenium:selenium-java:4.17.0")

    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.test {
    useJUnitPlatform()
    reports{
        junitXml.required.set(true)
    }
}