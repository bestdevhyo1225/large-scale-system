apply(plugin = "kotlin-jpa")
apply(plugin = "kotlin-allopen")

allOpen {
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
}

dependencies {
    implementation(project(":shortened-url-server-ver3:common"))
    implementation(project(":shortened-url-server-ver3:domain"))

    // spring data jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // querydsl
    implementation("com.querydsl:querydsl-jpa:5.0.0")
    kapt("com.querydsl:querydsl-apt:5.0.0:jpa")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // resilience4j
    implementation("io.github.resilience4j:resilience4j-spring-boot2:1.7.1")
    implementation("io.github.resilience4j:resilience4j-retrofit:1.7.1")
}

kotlin.sourceSets.main {
    setBuildDir("$buildDir")
}
