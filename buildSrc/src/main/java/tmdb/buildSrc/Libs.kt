package tmdb.buildSrc

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.2.1"
    const val jdkDesugar = "com.android.tools:desugar_jdk_libs:1.1.5"
    const val timber = "com.jakewharton.timber:timber:4.7.1"
    //const val pallete = "com.android.support:palette-v7:28.0.0"


    object Kotlin {
        private const val version = "1.6.10"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Coroutines {
        private const val version = "1.6.0"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object OkHttp {
        private const val version = "4.9.1"
        const val okhttp = "com.squareup.okhttp3:okhttp:$version"
        const val logging = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object Hilt {
        private const val version = "2.42"

        const val gradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
        const val android = "com.google.dagger:hilt-android:$version"
        const val compiler = "com.google.dagger:hilt-compiler:$version"
        const val testing = "com.google.dagger:hilt-android-testing:$version"
    }

    object Pallete {
        private const val version = "28.0.0"
        const val pallete = "com.android.support:palette-v7:${version}"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.4.1"
        const val coreKtx = "androidx.core:core-ktx:1.7.0"

        object Navigation {
            private const val version = "2.4.1"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
            const val common = "androidx.navigation:navigation-common-ktx:$version"
            const val runtime = "androidx.navigation:navigation-runtime-ktx:$version"

            object SafeArgs {
                const val gradlePlugin =
                    "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
            }
        }

        object Fragment {
            private const val version = "1.3.0"
            const val fragment = "androidx.fragment:fragment:$version"
            const val fragmentKtx = "androidx.fragment:fragment-ktx:$version"
        }

        object Room {
            private const val version = "2.4.2"
            const val runtime =  "androidx.room:room-runtime:$version"
            const val compiler =  "androidx.room:room-compiler:$version"
            const val common =  "androidx.room:room-common:$version"
            const val roomKtx =  "androidx.room:room-ktx:$version"
            const val paging =  "androidx.room:room-paging:$version"
        }

        object Paging {
            private const val version = "3.1.1"
            const val runtime =  "androidx.paging:paging-runtime:$version"
        }

        object SwipeRefreshLayout {
            private const val version = "1.2.0-alpha01"
            const val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:$version"
        }

        object Test {
            private const val version = "1.4.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"

            object Ext {
                private const val version = "1.1.2"
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }

            const val espressoCore = "androidx.test.espresso:espresso-core:3.3.0"
        }

        object Lifecycle {
            private const val version = "2.4.1"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val compiler = "androidx.lifecycle:lifecycle-compiler:$version"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val process = "androidx.lifecycle:lifecycle-process:$version"
            const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
        }

        object Compose {
            private const val version = "1.2.1"

            const val composeUI = "androidx.compose.ui:ui:${version}"
            // Tooling support (Previews, etc.)
            const val composeUITooling = "androidx.compose.ui:ui-tooling${version}"
            // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
            const val composeFoundation ="androidx.compose.foundation:foundation:${version}"
            // Material Design
            const val composeMaterial ="androidx.compose.material:material:${version}"
            // Material design icons
            const val composeMaterialIcons = "androidx.compose.material:material-icons-core:${version}"
            const val composeMaterialIconsExtended = "androidx.compose.material:material-icons-extended:${version}"
            // Integration with observables
            const val composeLivedata = "androidx.compose.runtime:runtime-livedata:${version}"
            const val composeRxJava = "androidx.compose.runtime:runtime-rxjava2:${version}"

        }
    }

    object Glide {
        private const val version = "4.12.0"
        const val glide = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
        const val caverock = "com.caverock:androidsvg:1.4"
    }

    object Google {
        const val material = "com.google.android.material:material:1.6.0-alpha02"
        const val gson = "com.google.code.gson:gson:2.8.6"
        const val flexbox = "com.google.android.flexbox:flexbox:3.0.0"
    }

    object Retrofit {
        private const val version = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:$version"
    }

    object Threeten {
        private const val version = "1.6.0"
        const val threeten = "org.threeten:threetenbp:$version:no-tzdb"
    }

    object FireBase{
        private const val version = "30.3.0"
        const val bom = "com.google.firebase:firebase-bom:${version}"
        const val fireBaseAuthKtx = "com.google.firebase:firebase-auth-ktx:"
        const val fireBaseFireStore = "com.google.firebase:firebase-firestore-ktx:"
        const val fireBaseDataBase = "com.google.firebase:firebase-database-ktx:"
        const val googleServices ="com.google.gms.google-services"
        const val googleServicesPlugin ="com.google.gms:google-services:4.3.13"

    }
}