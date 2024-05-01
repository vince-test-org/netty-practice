plugins {
    id("java-conventions")
}

dependencies {
    implementation(project(":core"))

    implementation(platform(libs.netty.bom))
    implementation("io.netty:netty-all")

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        showStandardStreams = true
    }
}
