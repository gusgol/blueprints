package tech.hiregus.trendingprojects

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import tech.hiregus.trendingprojects.data.model.Repo
import tech.hiregus.trendingprojects.data.ReposRepository
import tech.hiregus.trendingprojects.ui.repos.list.filter.ReposFilter
import java.io.IOException
import tech.hiregus.trendingprojects.data.model.Result

class FakeReposRepository : ReposRepository {

    var shouldReturnError: Boolean = false

    var languagesResult: Map<String, Long> = mapOf (
        "Kotlin" to 1000,
        "Java" to  500
    )

    override fun getRepos(filter: ReposFilter): Flow<PagingData<Repo>> {
        return flowOf(
            PagingData.from(getReposList())
        )
    }

    override suspend fun getRepoLanguages(owner: String, repo: String): Result<Map<String, Long>> {
        return if (shouldReturnError) {
            Result.Error(IOException("Test exception"))
        } else {
            Result.Success(languagesResult)
        }

    }
}