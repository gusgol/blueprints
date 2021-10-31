package tech.hiregus.trendingprojects.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tech.hiregus.trendingprojects.data.model.Repo
import tech.hiregus.trendingprojects.data.model.Result
import tech.hiregus.trendingprojects.data.datasource.ReposDataSource
import tech.hiregus.trendingprojects.ui.repos.list.filter.ReposFilter

class DefaultRepoRepository(private val remoteDataSource: ReposDataSource) : ReposRepository {

    override fun getRepos(filter: ReposFilter): Flow<PagingData<Repo>> = remoteDataSource.getRepos(filter)

    override suspend fun getRepoLanguages(
        owner: String,
        repo: String
    ): Result<Map<String, Long>> = remoteDataSource.getRepoLanguages(owner, repo)

}