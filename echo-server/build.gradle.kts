plugins {
    id("java-conventions")
}

dependencies {
    implementation(project(":core"))

    implementation(platform(libs.netty.bom))
    implementation("io.netty:netty-all")
}
