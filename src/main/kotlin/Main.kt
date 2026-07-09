package com.github.sinanakhaei

import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import com.github.sinanakhaei.featuregenerator.CreateFeatureCommand

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main(args: Array<String>) = AndroidCommand()
    .subcommands(CreateFeatureCommand())
    .main(args)