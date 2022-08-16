import tmdb.buildSrc.Modules.CORE
import tmdb.buildSrc.Modules.CORE_UI
import tmdb.buildSrc.Modules.DOMAIN

plugins {
    id("ui-library-plugin")
}

dependencies{
    implementation(project(CORE))
    implementation(project(DOMAIN))
    implementation(project(CORE_UI))

    implementation(tmdb.buildSrc.Libs.Pallete.pallete)
}