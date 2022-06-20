plugins {
    id("com.javiersc.hubdle")
}

version = "1.0.0"

hubdle {
    config {
        format()

        versioning {
            isEnabled = false
        }
    }

    kotlin {
        jvm()
    }
}
