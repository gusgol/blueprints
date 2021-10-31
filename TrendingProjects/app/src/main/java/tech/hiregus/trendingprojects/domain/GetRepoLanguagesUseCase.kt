package tech.hiregus.trendingprojects.domain

import tech.hiregus.trendingprojects.data.*
import tech.hiregus.trendingprojects.data.model.Language
import tech.hiregus.trendingprojects.data.model.Repo
import tech.hiregus.trendingprojects.data.model.Result

class GetRepoLanguagesUseCase(private val repoRepository: ReposRepository) {

    suspend operator fun invoke(repo: Repo): Result<List<Language>> {
        val result = repoRepository.getRepoLanguages(
            owner = repo.owner.login,
            repo = repo.name
        )
        if (result is Result.Success) {
            val languages: List<Language> = result.data.toList().map {
                Language(it.first, it.second)
            }
            return Result.Success(languages)
        }
        return getDefaultError()
    }

    private fun getDefaultError() = Result.Error(Exception("Failed to convert map to Language list"))

}