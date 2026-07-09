package com.github.sinanakhaei.featuregenerator

import java.io.File

object FeatureGenerator {

    fun create(featureName: String, root: File) {
        val packageName = featureName.replaceFirstChar { it.lowercase() }

        val featureDir = File(root, packageName)

        val domainRepository =
            File(featureDir, "domain/repository")

        val dataRepository =
            File(featureDir, "data/repository")

        val remoteDatasource =
            File(featureDir, "data/datasource/remote")

        val localDatasource =
            File(featureDir, "data/datasource/local")

        val presentation =
            File(featureDir, "presentation")

        listOf(
            domainRepository,
            dataRepository,
            remoteDatasource,
            localDatasource,
            presentation
        ).forEach { it.mkdirs() }

        write(
            File(domainRepository, "${featureName}Repository.kt"),
            repository(featureName)
        )

        write(
            File(dataRepository, "${featureName}RepositoryImpl.kt"),
            repositoryImpl(featureName)
        )

        write(
            File(remoteDatasource, "${featureName}RemoteDataSource.kt"),
            remoteDatasource(featureName)
        )

        write(
            File(remoteDatasource, "${featureName}RemoteDataSourceImpl.kt"),
            remoteDatasourceImpl(featureName)
        )

        write(
            File(localDatasource, "${featureName}LocalDataSource.kt"),
            localDatasource(featureName)
        )

        write(
            File(localDatasource, "${featureName}LocalDataSourceImpl.kt"),
            localDatasourceImpl(featureName)
        )

        write(
            File(presentation, "${featureName}ViewModel.kt"),
            viewModel(featureName)
        )

        write(
            File(presentation, "${featureName}UiState.kt"),
            uiState(featureName)
        )

        write(
            File(presentation, "${featureName}Screen.kt"),
            screen(featureName)
        )

        write(
            File(presentation, "${featureName}Route.kt"),
            route(featureName)
        )
    }

    private fun write(file: File, content: String) {
        if (!file.exists()) {
            file.writeText(content)
        }
    }

    private fun repository(name: String) = """
interface ${name}Repository

""".trimIndent()

    private fun repositoryImpl(name: String) = """
class ${name}RepositoryImpl(
    private val remoteDataSource: ${name}RemoteDataSource,
    private val localDataSource: ${name}LocalDataSource,
) : ${name}Repository

""".trimIndent()

    private fun remoteDatasource(name: String) = """
interface ${name}RemoteDataSource

""".trimIndent()

    private fun remoteDatasourceImpl(name: String) = """
class ${name}RemoteDataSourceImpl : ${name}RemoteDataSource

""".trimIndent()

    private fun localDatasource(name: String) = """
interface ${name}LocalDataSource

""".trimIndent()

    private fun localDatasourceImpl(name: String) = """
class ${name}LocalDataSourceImpl : ${name}LocalDataSource

""".trimIndent()

    private fun viewModel(name: String) = """
import androidx.lifecycle.ViewModel

class ${name}ViewModel : ViewModel()

""".trimIndent()

    private fun uiState(name: String) = """
data class ${name}UiState(
    val isLoading: Boolean = false
)

""".trimIndent()

    private fun screen(name: String) = """
import androidx.compose.runtime.Composable

@Composable
fun ${name}Screen() {

}

""".trimIndent()

    private fun route(name: String) = """
import androidx.compose.runtime.Composable

@Composable
fun ${name}Route() {
    ${name}Screen()
}

""".trimIndent()
}