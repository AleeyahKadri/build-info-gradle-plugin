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
    groovy
    idea
}

val githubSlug by extra("build-info-gradle-plugin")
val githubOrg by extra("sdelamo")
val bintrayUserOrg by extra("groovycalamari")

val bintrayUsername: String by extra {
    System.getenv("BINTRAY_USER") ?: project.findProperty("bintrayUsername") as? String ?: "**UNDEFINED**"
}

val bintrayApiKey: String by extra {
    System.getenv("BINTRAY_KEY") ?: project.findProperty("bintrayApiKey") as? String ?: "**UNDEFINED**"
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "groovy")
    apply(plugin = "idea")

    tasks.withType<GroovyCompile> {
        sourceCompatibility = project.property("sourceCompatibility") as String
        targetCompatibility = project.property("targetCompatibility") as String
    }
}
