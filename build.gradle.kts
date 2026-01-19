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
    id("groovy")
}

extra["githubSlug"] = "build-info-gradle-plugin"
extra["githubOrg"] = "sdelamo"
extra["bintrayUserOrg"] = "groovycalamari"

if (System.getenv("BINTRAY_USER") != null) {
    extra["bintrayUsername"] = System.getenv("BINTRAY_USER")
} else if (!project.hasProperty("bintrayUsername")) {
    extra["bintrayUsername"] = "**UNDEFINED**"
}

if (System.getenv("BINTRAY_KEY") != null) {
    extra["bintrayApiKey"] = System.getenv("BINTRAY_KEY")
} else if (!project.hasProperty("bintrayApiKey")) {
    extra["bintrayApiKey"] = "**UNDEFINED**"
}

configure<org.kordamp.gradle.plugin.base.ProjectConfigurationExtension> {
    release = (rootProject.findProperty("release") as String? ?: "false").toBoolean()

    info {
        name = "Gradle Plugin to generate a build-info.properties"
        vendor = "Groovy Calamari"
        description = "Gradle Plugin to generate a build-info.properties It can be used by frameworks such as Micronaut and Spring to expose values in the info endpoint"
        inceptionYear = "2020"

        links {
            website = "https://github.com/${extra["githubOrg"]}/${extra["githubSlug"]}"
            issueTracker = "https://github.com/${extra["githubOrg"]}/${extra["githubSlug"]}/issues"
            scm = "https://github.com/${extra["githubOrg"]}/${extra["githubSlug"]}.git"
        }
        people {
            person {
                id = "sdelamo"
                name = "Sergio del Amo"
                roles = listOf("developer")
            }
        }
        ciManagement {
            system = "Github Actions"
            url = "https://github.com/${extra["githubOrg"]}/${extra["githubSlug"]}/actions"
        }
    }

    licensing {
        licenses {
            license {
                id = org.kordamp.gradle.plugin.base.model.LicenseId.APACHE_2_0
            }
        }
    }

    bintray {
        credentials {
            username = extra["bintrayUsername"] as String
            password = extra["bintrayApiKey"] as String
        }
        userOrg = extra["bintrayUserOrg"] as String
        name = extra["githubSlug"] as String
        githubRepo = "${extra["githubOrg"]}/${extra["githubSlug"]}"
        publish = true
        publicDownloadNumbers = true
    }
}

allprojects {
    apply(plugin = "idea")
    configure<org.kordamp.gradle.plugin.base.ProjectConfigurationExtension> {
        docs {
            groovydoc {
                enabled = false
            }
        }
    }
    repositories {
        mavenCentral()
        maven { url = uri("https://jcenter.bintray.com") }
    }
}

subprojects {
    tasks.withType<GroovyCompile> {
        sourceCompatibility = project.property("sourceCompatibility") as String
        targetCompatibility = project.property("targetCompatibility") as String
    }

    configure<org.kordamp.gradle.plugin.base.ProjectConfigurationExtension> {
        coverage {
            jacoco {
                enabled = project.file("src/test").exists()
            }
        }
    }
}
