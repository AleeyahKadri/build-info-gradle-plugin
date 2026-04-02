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
import org.gradle.api.tasks.compile.GroovyCompile

plugins {
    id("org.kordamp.gradle.project") version "0.40.0"
    id("org.kordamp.gradle.bintray") version "0.40.0"
    groovy
}

val githubSlug = "build-info-gradle-plugin"
val githubOrg = "sdelamo"
val bintrayUserOrg = "groovycalamari"

val bintrayUsername = System.getenv("BINTRAY_USER")
    ?: (findProperty("bintrayUsername") as String? ?: "**UNDEFINED**")
val bintrayApiKey = System.getenv("BINTRAY_KEY")
    ?: (findProperty("bintrayApiKey") as String? ?: "**UNDEFINED**")

extra["githubSlug"] = githubSlug
extra["githubOrg"] = githubOrg
extra["bintrayUserOrg"] = bintrayUserOrg
extra["bintrayUsername"] = bintrayUsername
extra["bintrayApiKey"] = bintrayApiKey

configure<Any>("config") {
    withGroovyBuilder {
        setProperty("release", (rootProject.findProperty("release")?.toString() ?: "false").toBoolean())

        "info" {
            setProperty("name", "Gradle Plugin to generate a build-info.properties")
            setProperty("vendor", "Groovy Calamari")
            setProperty(
                "description",
                "Gradle Plugin to generate a build-info.properties It can be used by frameworks such as Micronaut and Spring to expose values in the info endpoint"
            )
            setProperty("inceptionYear", "2020")

            "links" {
                setProperty("website", "https://github.com/$githubOrg/$githubSlug")
                setProperty("issueTracker", "https://github.com/$githubOrg/$githubSlug/issues")
                setProperty("scm", "https://github.com/$githubOrg/$githubSlug.git")
            }
            "people" {
                "person" {
                    setProperty("id", "sdelamo")
                    setProperty("name", "Sergio del Amo")
                    setProperty("roles", listOf("developer"))
                }
            }
            "ciManagement" {
                setProperty("system", "Github Actions")
                setProperty("url", "https://github.com/$githubOrg/$githubSlug/actions")
            }
        }

        "licensing" {
            "licenses" {
                "license" {
                    setProperty("id", "Apache-2.0")
                }
            }
        }

        "bintray" {
            "credentials" {
                setProperty("username", bintrayUsername)
                setProperty("password", bintrayApiKey)
            }
            setProperty("userOrg", bintrayUserOrg)
            setProperty("name", githubSlug)
            setProperty("githubRepo", "$githubOrg/$githubSlug")
            setProperty("publish", true)
            setProperty("publicDownloadNumbers", true)
        }
    }
}

allprojects {
    apply(plugin = "idea")
    configure<Any>("config") {
        withGroovyBuilder {
            "docs" {
                "groovydoc" {
                    setProperty("enabled", false)
                }
            }
        }
    }
    repositories {
        mavenCentral()
        maven(url = "https://jcenter.bintray.com")
    }
}

subprojects {
    tasks.withType<GroovyCompile>().configureEach {
        sourceCompatibility = project.property("sourceCompatibility").toString()
        targetCompatibility = project.property("targetCompatibility").toString()
    }

    configure<Any>("config") {
        withGroovyBuilder {
            "coverage" {
                "jacoco" {
                    setProperty("enabled", file("src/test").exists())
                }
            }
        }
    }
}
