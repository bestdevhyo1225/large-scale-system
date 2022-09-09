dependencies {
    implementation(project(":shortened-url-server-ver3:common"))

    implementation(project(":shortened-url-server-ver3:domain"))

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("dev.miku:r2dbc-mysql:0.8.2.RELEASE")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    kapt("org.springframework.boot:spring-boot-configuration-processor")
}
