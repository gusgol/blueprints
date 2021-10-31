package tech.hiregus.trendingprojects.data.datasource

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tech.hiregus.trendingprojects.data.model.Result
import tech.hiregus.trendingprojects.data.model.Repo
import tech.hiregus.trendingprojects.ui.repos.list.filter.ReposFilter

interface ReposDataSource {

    fun getRepos(reposFilter: ReposFilter): Flow<PagingData<Repo>>

    suspend fun getRepoLanguages(owner: String, repo: String): Result<Map<String, Long>>

}