apply(plugin = "org.springframework.boot")

dependencies {
    implementation(project(":shortened-url-server-ver3:domain"))
    implementation(project(":shortened-url-server-ver3:infrastructure:r2dbc"))

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework:spring-tx:5.3.19")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
}
