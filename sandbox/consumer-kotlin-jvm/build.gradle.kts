plugins {
    id("com.javiersc.hubdle") version "0.5.0-SNAPSHOT"
}

hubdle {
    config {
        versioning {
            isEnabled.set(false)
        }
    }

    kotlin {
        jvm {
            features {
                application {
                    application {
                        mainClass.set("com.javiersc.hubdle.kotlin.jvm.features.molecule.MainKt")
                    }
                }
                coroutines()
            }
        }
    }
}
