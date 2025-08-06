plugins {
    java
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.bookshop"
version = "0.0.1-SNAPSHOT"
extra.set("springCloudVersion", "2023.0.1")
extra.set("testcontainersVersion", "1.19.8")
extra.set("otelVersion", "1.33.3")
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {//프로젝트 빌드 시 그래들이 설정 프로세서를 이용하도록 설정한다.
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation ("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")
    implementation ("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation ("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation ("org.springframework.cloud:spring-cloud-starter-config")
    implementation ("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation ("org.springframework.session:spring-session-data-redis")
    implementation ("org.springframework.boot:spring-boot-starter-security")
    // Only on Apple Silicon. Why it's necessary: https://github.com/netty/netty/issues/11020
    runtimeOnly ("io.github.resilience4j:resilience4j-micrometer")
     runtimeOnly ("io.netty:netty-resolver-dns-native-macos:4.1.101.Final:osx-aarch_64")
    runtimeOnly ("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly ("io.opentelemetry.javaagent:opentelemetry-javaagent:${property("otelVersion")}")
    //모니터링과 관리를 위한 액추에이터 의존성 추화
    implementation("org.springframework.boot:spring-boot-starter-actuator")


    testImplementation ("org.springframework.boot:spring-boot-starter-test")
    testImplementation ("io.projectreactor:reactor-test")
    testImplementation ("org.testcontainers:junit-jupiter")
    testImplementation ("org.springframework.security:spring-security-test")
}

dependencyManagement {// 책에서는 없으나... 클라우드 디펜던시가 필요했음
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.3.1")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.1")
        mavenBom ("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
        mavenBom ("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")

    }
}
tasks.bootBuildImage {
    builder.set("paketobuildpacks/builder-jammy-java-tiny:0.0.46")
    //imagePlatform.set("linux/arm64")
    imageName.set(project.name)
    //imageName.set("ghcr.io/kingstree/${project.name}:latest")   // ★ 레지스트리·계정 포함
    environment.put("BP_JVM_VERSION", "17")

    docker {
        publishRegistry {
            username = project.findProperty("registryFUsername") as String?
            password = project.findProperty("registryToken") as String?
            url = project.findProperty("registryUrl") as String?
        }
    }
}
tasks.withType<Test> {
    useJUnitPlatform()
}
