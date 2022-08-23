apply(plugin = "org.springframework.boot")
apply(plugin = "kotlin-jpa")
apply(plugin = "kotlin-allopen")

allOpen {
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
}

dependencies {
    implementation(project(":order-payment-server:common"))
    implementation(project(":order-payment-server:domain"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}
