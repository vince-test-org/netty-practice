plugins {
    java
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.javax.annotation)
    implementation(libs.findbugs)
}
