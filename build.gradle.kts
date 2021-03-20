import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.*

plugins {
    application
    kotlin("jvm") version "1.4.31"
    id("com.google.protobuf") version "0.8.15"
}

group = "me.anmolverma"
version = "1.0-SNAPSHOT"


sourceSets.main {
    resources.srcDir("src/main/proto")
    withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
        kotlin.srcDirs("build/generated/source/proto/main/grpc",
            "build/generated/source/proto/main/grpckt",
            "build/generated/source/proto/main/java")
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.12.0"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.36.0"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.0.0:jdk7@jar"
        }
    }

    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Apply the "grpc" plugin whose spec is defined above, without options.
                id("grpc")
                id("grpckt")
            }
        }
    }
}


dependencies {
    implementation("io.grpc:grpc-netty-shaded:1.36.0")
    implementation("io.grpc:grpc-protobuf:1.36.0")
    implementation("io.grpc:grpc-kotlin-stub:1.0.0")
    implementation("com.google.protobuf:protobuf-java:3.15.3")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("com.google.guava:guava:30.1-jre")
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation(kotlin("test-junit"))
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "me.anmolverma.MainGrpcServer"
    }

    // To add all of the dependencies
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}



repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}