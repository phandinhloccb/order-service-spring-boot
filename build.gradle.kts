plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.openapi.generator") version "7.4.0"
}

group = "com.loc"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

val generatedResourcesDir = "$buildDir/generated-resources"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    
    // Sử dụng phiên bản SpringDoc tương thích hơn
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    
    // Thêm JAXB cho Java 11+
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.2")
    
    // H2 database
    runtimeOnly("com.h2database:h2")
    
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    
    // Loại bỏ các dependencies gây xung đột
    configurations.all {
        exclude(group = "org.slf4j", module = "slf4j-simple")
        exclude(group = "io.swagger.core.v3", module = "swagger-annotations")
        exclude(group = "org.openapitools", module = "openapi-generator")
        exclude(group = "io.swagger.parser.v3", module = "swagger-parser")
        exclude(group = "javax.validation", module = "validation-api")
    }
}

openApiGenerate {
    inputSpec.set("$rootDir/src/main/resources/static/openapi.yaml")
    generatorName.set("kotlin-spring")
    apiPackage.set("com.loc.orderservice.api")
    packageName.set("com.loc.orderservice.api")
    modelPackage.set("com.loc.orderservice.model")
    outputDir.set(generatedResourcesDir)

    configOptions.put("useSpringBoot3", "true")
    configOptions.put("dateLibrary", "java8")
    configOptions.put("delegatePattern", "true")
    configOptions.put("interfaceOnly", "true")
    configOptions.put("modelMutable", "true")
    configOptions.put("useTags", "true")
    configOptions.put("enumPropertyNaming", "UPPERCASE")
}

kotlin.sourceSets["main"].kotlin.srcDir("$generatedResourcesDir/src/main/kotlin")
java.sourceSets["main"].java.srcDir("$generatedResourcesDir/src/main/java")

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    dependsOn(tasks.openApiGenerate)
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

