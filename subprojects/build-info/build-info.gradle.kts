/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020 Sergio del Amo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
plugins {
    id("com.gradle.plugin-publish") version "0.12.0"
    id("java-gradle-plugin")
    groovy
}

configure<Any>("config") {
    withGroovyBuilder {
        "bintray" { setProperty("enabled", true) }
        "publishing" { setProperty("enabled", true) }
    }
}

repositories {
    jcenter()
}

group = "groovycalamari"
version = project.property("projectVersion").toString()

dependencies {
    implementation(gradleApi())
}

configure<Any>("pluginBundle") {
    withGroovyBuilder {
        setProperty("website", "https://github.com/sdelamo/build-info-gradle-plugin")
        setProperty("vcsUrl", "https://github.com/sdelamo/build-info-gradle-plugin")
        setProperty("description", "Generates a build-info.properties file")
        setProperty("tags", listOf("build", "micronaut", "spring-boot", "actuator", "management"))
        "plugins" {
            "buildinfoPlugin" {
                setProperty("displayName", "Build Info Gradle plugin")
            }
        }
    }
}

gradlePlugin {
    plugins {
        create("buildinfoPlugin") {
            id = "groovycalamari.build-info"
            implementationClass = "groovycalamari.buildinfo.BuildInfoPlugin"
        }
    }
}
