dependencies {
    implementation(project(":shortened-url-server-ver3:common"))

    compileOnly(project(":shortened-url-server-ver3:domain"))

    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
}
