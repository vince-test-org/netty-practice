plugins {
    id("java-conventions")
}

application {
    mainClass.set("com.joebrothers.echoserver.EchoServer")
}

dependencies {
    implementation(project(":core"))

    implementation(platform(libs.netty.bom))
    implementation("io.netty:netty-all")
}

tasks.named<JavaExec>("run") {
    args = listOf(
        project.property("echo-server.port").toString(),
    )
}
