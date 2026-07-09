package com.github.sinanakhaei.featuregenerator

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import java.io.File

class CreateFeatureCommand : CliktCommand(
    name = "create-feature"
) {

    private val feature by argument()

    override fun run() {
        println("Creating $feature")

        FeatureGenerator.create(
            feature,
            File(".")
        )
    }
}