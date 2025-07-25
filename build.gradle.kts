plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.openapi.generator") version "7.4.0"
    id("org.liquibase.gradle") version "2.2.0"
    id("com.github.davidmc24.gradle.plugin.avro") version "1.8.0"
}

group = "com.loc"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

val generatedResourcesDir = "${layout.buildDirectory.get()}/generated-resources"
val generatedAvroDir = "${layout.buildDirectory.get()}/generated-avro"
val generatedInventoryDir = "${layout.buildDirectory.get()}/generated-inventory-client"

repositories {
    mavenCentral()
    maven {
        url = uri("https://packages.confluent.io/maven/")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.3")
    
    // Kafka dependencies
    implementation("org.apache.kafka:kafka-clients")
    implementation("org.springframework.kafka:spring-kafka")

    // Micrometer Prometheus for /actuator/prometheus endpoint
    implementation("io.micrometer:micrometer-registry-prometheus")

    // SpringDoc OpenAPI for API docs only (no UI)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.2.0")


    // Avro dependencies
    implementation("org.apache.avro:avro:1.11.3")
    implementation("io.confluent:kafka-avro-serializer:7.4.0")
    
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.2")

    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    
    runtimeOnly("com.h2database:h2")
    implementation("org.liquibase:liquibase-core")
    implementation("mysql:mysql-connector-java:8.0.33")
    
    liquibaseRuntime("org.liquibase:liquibase-core:4.20.0")
    liquibaseRuntime("mysql:mysql-connector-java:8.0.33")
    liquibaseRuntime("info.picocli:picocli:4.6.3")
    
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.rest-assured:spring-mock-mvc:5.4.0")
    testImplementation("io.rest-assured:kotlin-extensions:5.4.0")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("io.rest-assured:rest-assured:5.4.0")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("io.rest-assured:spring-mock-mvc-kotlin-extensions:5.4.0")

    configurations.all {
        exclude(group = "org.slf4j", module = "slf4j-simple")
        exclude(group = "io.swagger.core.v3", module = "swagger-annotations")
        exclude(group = "org.openapitools", module = "openapi-generator")
        exclude(group = "io.swagger.parser.v3", module = "swagger-parser")
        exclude(group = "javax.validation", module = "validation-api")
    }
}

// Configure source sets
sourceSets {
    main {
        java {
            srcDir("$generatedResourcesDir/openapi/src/main/java")
            srcDir("$generatedResourcesDir/avro/src/main/java")
            srcDir("$generatedResourcesDir/inventory/src/main/java")
        }
        kotlin {
            srcDir("$generatedResourcesDir/openapi/src/main/kotlin")
            srcDir("$generatedResourcesDir/inventory/src/main/kotlin")
        }
    }
}

// Avro configuration
tasks.named<com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask>("generateAvroJava") {
    source("src/main/resources/static/avro")
    setOutputDir(file("$generatedResourcesDir/avro"))
}

liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "logLevel" to "info",
            "changeLogFile" to "src/main/resources/db/changelog/db.changelog-master.xml",
            "url" to "jdbc:mysql://localhost:3307/order_service",
            "username" to "root",
            "password" to "mysql",
            "driver" to "com.mysql.cj.jdbc.Driver"
        )
    }
    runList = "main"
}

// OpenAPI configuration
openApiGenerate {
    inputSpec.set("$rootDir/src/main/resources/static/openapi.yaml")
    generatorName.set("kotlin-spring")
    apiPackage.set("com.loc.orderservice.api")
    packageName.set("com.loc.orderservice.api")
    modelPackage.set("com.loc.orderservice.model")
    outputDir.set("$generatedResourcesDir/openapi")

    configOptions.put("useSpringBoot3", "true")
    configOptions.put("dateLibrary", "java8")
    configOptions.put("delegatePattern", "true")
    configOptions.put("interfaceOnly", "true")
    configOptions.put("modelMutable", "true")
    configOptions.put("useTags", "true")
    configOptions.put("enumPropertyNaming", "UPPERCASE")
}

// Inventory client configuration
tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("generateInventoryClient") {
    inputSpec.set("$rootDir/src/main/resources/static/inventory-openapi.yaml")
    generatorName.set("java")
    library.set("webclient")
    apiPackage.set("com.loc.orderservice.client.inventory.api")
    packageName.set("com.loc.orderservice.client.inventory")
    modelPackage.set("com.loc.orderservice.client.inventory.model")
    invokerPackage.set("com.loc.orderservice.client.inventory.invoke")
    outputDir.set("$generatedResourcesDir/inventory")

    configOptions.put("dateLibrary", "java8")
    configOptions.put("delegatePattern", "true")
    configOptions.put("interfaceOnly", "true")
    configOptions.put("modelMutable", "true")
    configOptions.put("enumPropertyNaming", "UPPERCASE")
    
    skipValidateSpec.set(true)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    dependsOn(tasks.openApiGenerate, tasks.named("generateAvroJava"), tasks.named("generateInventoryClient"))
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}