package com.github.sinanakhaei.featuregenerator // Match this to your actual package

import java.io.File

object FeatureGenerator {

    fun execute(featureName: String) {
        val currentDir = File(System.getProperty("user.dir"))

        // 1. Auto-detect project structure
        val srcRoot = findSrcMainDirectory(currentDir)
        if (srcRoot == null) {
            println("❌ Error: Could not find 'src/main/java' or 'src/main/kotlin' in this directory or its parents.")
            return
        }

        val basePackage = detectBasePackage(srcRoot)
        if (basePackage.isEmpty()) {
            println("❌ Error: Could not determine base package name from the folder structure.")
            return
        }

        println("🚀 Target source directory found: ${srcRoot.absolutePath}")
        println("📦 Auto-detected base package: $basePackage")

        // 2. Run generation
        create(featureName, srcRoot, basePackage)
        println("✅ Feature '$featureName' successfully generated.")
    }

    private fun create(featureName: String, root: File, basePackage: String) {
        // Enforce lowercase for packages and directory names
        val featureLower = featureName.lowercase()
        val featureDir = File(root, basePackage.replace('.', '/'))
        val targetDir = File(featureDir, featureLower)

        // Lowercase package naming configurations
        val featurePackage = "$basePackage.$featureLower"
        val domainRepoPkg = "$featurePackage.domain.repository"
        val dataRepoPkg = "$featurePackage.data.repository"
        val remoteDsPkg = "$featurePackage.data.datasource.remote"
        val localDsPkg = "$featurePackage.data.datasource.local"
        val presentationPkg = "$featurePackage.presentation"
        val modelPkg = "$presentationPkg.model"

        // Directories
        val domainRepository = File(targetDir, "domain/repository")
        val dataRepository = File(targetDir, "data/repository")
        val remoteDatasource = File(targetDir, "data/datasource/remote")
        val localDatasource = File(targetDir, "data/datasource/local")
        val presentation = File(targetDir, "presentation")
        val presentationModel = File(presentation, "model")

        listOf(
            domainRepository, dataRepository, remoteDatasource,
            localDatasource, presentation, presentationModel
        ).forEach { it.mkdirs() }

        // Domain Layer (Note: Files still retain CamelCase naming convention)
        write(File(domainRepository, "${featureName}Repository.kt"), repository(domainRepoPkg, featureName))

        // Data Layer
        write(File(dataRepository, "${featureName}RepositoryImpl.kt"), repositoryImpl(dataRepoPkg, domainRepoPkg, remoteDsPkg, localDsPkg, featureName))
        write(File(remoteDatasource, "${featureName}RemoteDatasource.kt"), remoteDatasource(remoteDsPkg, featureName))
        write(File(remoteDatasource, "${featureName}RemoteDatasourceImpl.kt"), remoteDatasourceImpl(remoteDsPkg, featureName))
        write(File(localDatasource, "${featureName}LocalDatasource.kt"), localDatasource(localDsPkg, featureName))
        write(File(localDatasource, "${featureName}LocalDatasourceImpl.kt"), localDatasourceImpl(localDsPkg, featureName))

        // Presentation Layer (UiState goes strictly into the model subdirectory)
        write(File(presentationModel, "${featureName}UiState.kt"), uiState(modelPkg, featureName))
        write(File(presentation, "${featureName}ViewModel.kt"), viewModel(presentationPkg, modelPkg, featureName))
        write(File(presentation, "${featureName}Screen.kt"), screen(presentationPkg, featureName))
        write(File(presentation, "${featureName}Route.kt"), route(presentationPkg, featureName))
    }

    private fun write(file: File, content: String) {
        if (!file.exists()) file.writeText(content)
    }

    private fun findSrcMainDirectory(dir: File): File? {
        val targets = listOf("src/main/java", "src/main/kotlin", "app/src/main/java", "app/src/main/kotlin")
        for (path in targets) {
            val check = File(dir, path)
            if (check.exists() && check.isDirectory) return check
        }
        var current: File? = dir
        while (current != null) {
            if (current.path.endsWith("src${File.separator}main${File.separator}java") ||
                current.path.endsWith("src${File.separator}main${File.separator}kotlin")) {
                return current
            }
            current = current.parentFile
        }
        return null
    }

    private fun detectBasePackage(srcMainDir: File): String {
        val deepestEmptyDir = srcMainDir.walkTopDown()
            .filter { it.isDirectory() }
            .firstOrNull { dir ->
                val files = dir.listFiles() ?: emptyArray()
                files.any { it.name.endsWith(".kt") || it.name.endsWith(".java") } ||
                        files.any { it.name == "domain" || it.name == "data" || it.name == "presentation" }
            } ?: srcMainDir

        return deepestEmptyDir.relativeTo(srcMainDir)
            .path
            .replace(File.separatorChar, '.')
            .trim('.')
    }

    // --- Templates with lowercase packages ---
    private fun repository(pkg: String, name: String) = "package $pkg\n\ninterface ${name}Repository"

    private fun remoteDatasource(pkg: String, name: String) = "package $pkg\n\ninterface ${name}RemoteDatasource"

    private fun remoteDatasourceImpl(pkg: String, name: String) = "package $pkg\n\nclass ${name}RemoteDatasourceImpl : ${name}RemoteDatasource"

    private fun localDatasource(pkg: String, name: String) = "package $pkg\n\ninterface ${name}LocalDatasource"

    private fun localDatasourceImpl(pkg: String, name: String) = "package $pkg\n\nclass ${name}LocalDatasourceImpl : ${name}LocalDatasource"

    private fun uiState(pkg: String, name: String) = "package $pkg\n\ndata class ${name}UiState(\n    val isLoading: Boolean = false\n)"

    private fun repositoryImpl(pkg: String, domPkg: String, remPkg: String, locPkg: String, name: String) = """
package $pkg

import $domPkg.${name}Repository
import $remPkg.${name}RemoteDatasource
import $locPkg.${name}LocalDatasource

class ${name}RepositoryImpl(
    private val remoteDatasource: ${name}RemoteDatasource,
    private val localDatasource: ${name}LocalDatasource,
) : ${name}Repository
""".trimIndent()

    private fun viewModel(pkg: String, modelPkg: String, name: String) = """
package $pkg

import androidx.lifecycle.ViewModel
import $modelPkg.${name}UiState

class ${name}ViewModel : ViewModel()
""".trimIndent()

    private fun screen(pkg: String, name: String) = """
package $pkg

import androidx.compose.runtime.Composable

@Composable
fun ${name}Screen() {

}
""".trimIndent()

    private fun route(pkg: String, name: String) = """
package $pkg

import androidx.compose.runtime.Composable

@Composable
fun ${name}Route() {
    ${name}Screen()
}
""".trimIndent()
}