package tech.hiregus.trendingprojects.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tech.hiregus.trendingprojects.data.model.Repo
import tech.hiregus.trendingprojects.data.model.Result
import tech.hiregus.trendingprojects.ui.repos.list.filter.ReposFilter

interface ReposRepository {

    fun getRepos(filter: ReposFilter): Flow<PagingData<Repo>>

    suspend fun getRepoLanguages(owner: String, repo: String): Result<Map<String, Long>>

}