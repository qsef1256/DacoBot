group = "net.qsef1256"
version = "2.3.0"
description = "DacoBot"
java.sourceCompatibility = JavaVersion.VERSION_17

val jdaVersion = "5.0.0-beta.20"
val springBootVersion = "3.2.2"

plugins {
    `java-library`
    `maven-publish`

    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    id("io.freefair.lombok") version "8.4"
}

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://mvn.lumine.io/repository/maven-public/") }
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven { url = uri("https://oss.sonatype.org/content/groups/public/") }
    maven { url = uri("https://nexus.scarsz.me/content/groups/public/") }
    maven { url = uri("https://m2.dv8tion.net/releases") }
    maven { url = uri("https://m2.chew.pro/snapshots/") }
    maven { url = uri("https://jcenter.bintray.com") }
    maven {
        url = uri("http://localhost:8081/repository/dialib-releases")

        isAllowInsecureProtocol = true
    }
}

dependencies {
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("ch.qos.logback:logback-core:1.4.14")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.guava:guava:32.1.2-jre")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("com.jayway.jsonpath:json-path:2.9.0")
    implementation("commons-beanutils:commons-beanutils:1.9.4") {
        exclude(group = "commons-collections", module = "commons-collections")
    }
    implementation("commons-codec:commons-codec:1.16.0")
    implementation("io.smallrye:jandex:3.1.3")
    implementation("jakarta.el:jakarta.el-api:5.0.1")
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("net.qsef1256:DiaLib:1.1.8")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("org.apache.commons:commons-configuration2:2.9.0")
    implementation("org.apache.commons:commons-dbcp2:2.9.0")
    implementation("org.apache.commons:commons-lang3:3.13.0")
    implementation("org.apache.commons:commons-text:1.10.0")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.3-alpha1")
    implementation("org.apache.maven:maven-model:4.0.0-alpha-7")
    implementation("org.dom4j:dom4j:2.1.4")
    implementation("org.glassfish:jakarta.el:5.0.0-M1")
    implementation("org.jetbrains:annotations:24.0.1")
    implementation("org.hibernate:hibernate-core:6.4.3.Final")
    implementation("org.hibernate:hibernate-hikaricp:6.4.3.Final")
    implementation("org.hibernate.common:hibernate-commons-annotations:6.0.6.Final")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    implementation("org.hibernate.validator:hibernate-validator-annotation-processor:8.0.1.Final")
    implementation("org.mariuszgromada.math:MathParser.org-mXparser:5.2.1")
    implementation("org.projectlombok:lombok:1.18.28")
    implementation("org.reflections:reflections:0.10.2")
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.threeten:threetenbp:1.6.8")
    implementation("org.yaml:snakeyaml:2.0")
    implementation("pw.chew:jda-chewtils:2.0-SNAPSHOT")
    implementation("org.springframework.boot:spring-boot-starter-actuator:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-validation:${springBootVersion}")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:${springBootVersion}")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client:3.3.2")
    developmentOnly("org.springframework.boot:spring-boot-devtools:${springBootVersion}")

    testImplementation("org.springframework.boot:spring-boot-starter-test:${springBootVersion}") {
        exclude(group = "com.vaadin.external.google", module = "android-json")
    }
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.mockito:mockito-core:5.5.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.5.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.1")

    // TODO: Spring shell
}

springBoot {
    mainClass.set("net.qsef1256.dacobot.DacoBot")

    buildInfo()
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

configurations {
    all {
        exclude(group = "commons-logging", module = "commons-logging")
    }
}

tasks.processResources {
    filesMatching("**/project.properties") {
        expand(
                "jdaVersion" to jdaVersion,
                "springBootVersion" to springBootVersion)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = springBoot.mainClass
        "Class-Path" to configurations.runtimeClasspath.get()
                .files
                .joinToString(" ")
                { "lib/${it.name}" }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<Copy>("copyDependencies") {
    description = "Copy all dependencies to destination directory."
    group = JavaBasePlugin.BUILD_TASK_NAME
    from(configurations.runtimeClasspath)
    into("out/lib")
}
