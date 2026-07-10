package com.github.sinanakhaei

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.versionOption
import com.github.sinanakhaei.android_cli.BuildConfig

class AndroidCommand : CliktCommand(
    name = "android",
) {
    init {
        versionOption(
            version = BuildConfig.APP_VERSION, // This is now dynamically injected by Gradle!
            names = setOf("-v", "--version"),
            message = { "android-cli version $it" }
        )
    }

    override fun run() {}
}