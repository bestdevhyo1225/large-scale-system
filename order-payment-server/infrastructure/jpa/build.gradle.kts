apply(plugin = "kotlin-jpa")
apply(plugin = "kotlin-allopen")

allOpen {
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
}

dependencies {
    implementation(project(":order-payment-server:common"))

    compileOnly(project(":order-payment-server:domain"))

    // Spring Data Jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Querydsl
    implementation("com.querydsl:querydsl-jpa:5.0.0")
    kapt("com.querydsl:querydsl-apt:5.0.0:jpa")
    kapt("org.springframework.boot:spring-boot-configuration-processor")
}

kotlin.sourceSets.main {
    setBuildDir("$buildDir")
}
