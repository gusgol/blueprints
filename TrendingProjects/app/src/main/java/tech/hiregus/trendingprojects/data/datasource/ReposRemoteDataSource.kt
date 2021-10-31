package tech.hiregus.trendingprojects.data.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import tech.hiregus.trendingprojects.data.api.ReposService
import tech.hiregus.trendingprojects.data.model.Result
import tech.hiregus.trendingprojects.data.model.Repo
import tech.hiregus.trendingprojects.ui.repos.list.filter.ReposFilter
import tech.hiregus.trendingprojects.utils.NETWORK_PAGE_SIZE
import java.io.IOException

class ReposRemoteDataSource(private val reposService: ReposService,
                            private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : ReposDataSource {

    override fun getRepos(reposFilter: ReposFilter): Flow<PagingData<Repo>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ReposPagingSource(reposFilter, reposService)
            }
        ).flow
    }

    override suspend fun getRepoLanguages(owner: String, repo: String): Result<Map<String, Long>> = withContext(ioDispatcher){
        return@withContext try {
            val response = reposService.getRepoLanguages(owner, repo)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body() ?: mapOf())
            } else {
                Result.Error(
                    IOException("Unable to get the project ($repo) languages ${response.code()} ${response.message()}")
                )
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}