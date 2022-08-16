package tmdb.buildSrc.plugins

import tmdb.buildSrc.*
import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CoreLibraryPlugin : Plugin<Project> {

    private val Project.android: BaseExtension
        get() = extensions.findByName("android") as? BaseExtension
            ?: error("Not an Android module: $name")

    override fun apply(target: Project) {
        with(target) {
            applyPlugins()
            androidConfig()
            productFlavorsConfig()
            dependenciesConfig()
        }
    }

    private fun Project.applyPlugins() {
        plugins.run {
            apply("com.android.library")
            apply("kotlin-android")
            apply("kotlin-parcelize")
            apply("kotlin-kapt")
            apply("dagger.hilt.android.plugin")
        }
    }

    private fun Project.androidConfig() {
        android.run {
            compileSdkVersion(AndroidConfig.compileSdk)
            defaultConfig {
                minSdk = AndroidConfig.minSdk
                targetSdk = AndroidConfig.targetSdk
                versionCode = AndroidConfig.versionCode
                versionName = AndroidConfig.versionName

                testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
            }

            compileOptions {
                sourceCompatibility = AndroidConfig.sourceCompatibility
                targetCompatibility = AndroidConfig.targetCompatibility
            }

            viewBinding.isEnabled = true

            packagingOptions {
                resources {
                    excludes += "META-INF/main.kotlin_module"
                }
            }
        }
    }

    private fun Project.dependenciesConfig() {
        dependencies {
            "implementation"(Libs.AndroidX.coreKtx)
            "implementation"(Libs.Google.material)

            "implementation"(Libs.Hilt.android)
            "kapt"(Libs.Hilt.compiler)

            "implementation"(Libs.Retrofit.retrofit)
            "implementation"(Libs.Retrofit.gsonConverter)

            "implementation"(Libs.OkHttp.logging)
            "implementation"(Libs.OkHttp.okhttp)

            "implementation"(Libs.Coroutines.android)
            "implementation"(Libs.Coroutines.core)

            "api"(Libs.timber)
            //"implementation"(Libs.Pallete.pallete)

            "implementation"(Libs.AndroidX.Lifecycle.runtime)
            "implementation"(Libs.AndroidX.Fragment.fragmentKtx)

            "implementation"(platform(Libs.FireBase.bom))
            "implementation"(Libs.FireBase.fireBaseAuthKtx)
            "implementation"(Libs.FireBase.fireBaseDataBase)
            "implementation"(Libs.FireBase.fireBaseFireStore)


            "api"(Libs.Glide.glide)
            "kapt"(Libs.Glide.compiler)

            "api"(Libs.Threeten.threeten)

            "implementation"(Libs.AndroidX.Room.runtime)
            "implementation"(Libs.AndroidX.Room.roomKtx)
            "implementation"(Libs.AndroidX.Room.paging)
            "kapt"(Libs.AndroidX.Room.compiler)
        }
    }
}