/*
 * The Spring Boot plugin brings Jackson 3.1.5 into the build itself; see the
 * Jackson platform in the dependencies below for why it is lifted.
 */
buildscript {
  dependencies {
    classpath(platform("tools.jackson:jackson-bom:3.2.3"))
  }
}

plugins {
  id("java")
  id("checkstyle")
  id("org.springframework.boot") version "4.1.1"
  id("io.freefair.lombok") version "9.8.0"
  id("com.github.spotbugs") version "6.5.13"
}

group = "de.lukasbreuer.nightshift"
version = "1.0.0-SNAPSHOT"
/**
 * Compiles and runs with JDK 27 while Gradle itself may run on an older,
 * better supported JDK (the IDE's Gradle JVM).
 */
java.toolchain.languageVersion = JavaLanguageVersion.of(27)

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(platform("org.junit:junit-bom:6.1.3"))
  testImplementation("org.junit.jupiter:junit-jupiter:6.1.3")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.1.3")

  implementation("org.projectlombok:lombok:1.18.48")
  annotationProcessor("org.projectlombok:lombok:1.18.48")
  testImplementation("org.projectlombok:lombok:1.18.48")
  testAnnotationProcessor("org.projectlombok:lombok:1.18.48")

  implementation("org.json:json:20260814")

  implementation("org.postgresql:postgresql:42.7.14")
  implementation("org.hibernate.orm:hibernate-core:7.4.12.Final")

  implementation("org.springframework.boot:spring-boot-starter-web:4.1.1")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa:4.1.1")

  spotbugsPlugins("com.h3xstream.findsecbugs:findsecbugs-plugin:1.14.0")

  /*
   * Spring Boot 4.1.1 ships Jackson 3.1.5, whose jackson-core has two High
   * denial of service bugs (GHSA-p6pp-m3f8-5c89, GHSA-7hhh-6rmp-j9qf). The
   * platform lifts every Jackson module to the same fixed patch release;
   * drop it once Spring Boot brings 3.1.7 or later on its own.
   */
  implementation(platform("tools.jackson:jackson-bom:3.2.3"))

  constraints {
    /*
     * Spring Boot 4.1.1 ships Tomcat 11.0.24, which has critical access
     * control and authentication bypasses (GHSA-gcx9-497g-6cp6,
     * GHSA-9xv2-5v5q-p794, GHSA-h3x4-894j-xpx5). Drop these once Spring
     * Boot brings 11.0.25 or later on its own.
     */
    implementation("org.apache.tomcat.embed:tomcat-embed-core:11.0.27")
    implementation("org.apache.tomcat.embed:tomcat-embed-el:11.0.27")
    implementation("org.apache.tomcat.embed:tomcat-embed-websocket:11.0.27")
  }
}

checkstyle {
  toolVersion = "14.3.0"
  maxWarnings = 0
}

/**
 * Bugs and security patterns (FindSecBugs) in the compiled api; any finding
 * fails the build. The SARIF report goes to the repository's Security tab.
 */
spotbugs {
  toolVersion = "4.10.5"
  effort = com.github.spotbugs.snom.Effort.MAX
  reportLevel = com.github.spotbugs.snom.Confidence.MEDIUM
  excludeFilter = file("config/spotbugs/exclude.xml")
}

tasks.spotbugsMain {
  reports.create("sarif") { required = true }
  reports.create("html") { required = true }
}

tasks.spotbugsTest {
  enabled = false
}

tasks.test {
  useJUnitPlatform()
}

tasks.bootJar {
  mainClass = "de.lukasbreuer.nightshift.api.ApiApplication"
  archiveFileName = "api.jar"
}