plugins {
    id("com.javiersc.hubdle")
}

version = "3.6.7-SNAPSHOT"

hubdle {
    config {
        publishing()

        versioning {
            isEnabled.set(false)
        }
    }

    kotlin {
        jvm()
    }
}