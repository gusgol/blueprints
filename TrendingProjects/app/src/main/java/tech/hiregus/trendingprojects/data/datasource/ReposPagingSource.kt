package tech.hiregus.trendingprojects.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import tech.hiregus.trendingprojects.data.api.ReposService
import tech.hiregus.trendingprojects.data.model.Repo
import tech.hiregus.trendingprojects.ui.repos.list.filter.ReposFilter
import tech.hiregus.trendingprojects.utils.NETWORK_PAGE_SIZE
import java.io.IOException

class ReposPagingSource(private val filter: ReposFilter,
                        private val reposService: ReposService,) : PagingSource<Int, Repo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        return try {
            val index = params.key ?: 1
            val result = reposService.search(
                sort = SORT,
                order = filter.order.id,
                query = listOf(
                    CREATED_DATE_FORMAT.format(filter.createdDate.getFromDate())
                ),
                perPage = params.loadSize,
                page = index
            )
            val body = result.body()
            if (result.isSuccessful && body != null) {
                val nextPage = if (body.items.isEmpty()) null else index + (params.loadSize / NETWORK_PAGE_SIZE)
                LoadResult.Page(
                    data = body.items,
                    prevKey = null,
                    nextKey = nextPage
                )
            } else {
                LoadResult.Error(
                    IOException("It was not possible to load the repositories ${result.code()} ${result.message()}")
                )
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        const val SORT = "stars"
        const val CREATED_DATE_FORMAT = "created:>%s"
    }

}