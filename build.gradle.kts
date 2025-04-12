import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("org.jetbrains.intellij.platform") version "2.5.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "cn.rtast.qwerty-learner"
version = "1.0.1"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
    maven("https://repo.maven.rtast.cn/releases")
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.3.5")
//        pycharmCommunity("2024.3.5")
    }
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
    implementation("cn.rtast:rtast-util-string:0.0.1")
    implementation("cn.rtast:rtast-util-http:0.0.1")
}

tasks {
    compileJava {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    compileKotlin {
        compilerOptions.jvmTarget = JvmTarget.JVM_17
    }

    patchPluginXml {
        sinceBuild.set("222")
        untilBuild.set("299.*")
    }
}

tasks.buildPlugin {
    dependsOn(tasks.shadowJar)
}