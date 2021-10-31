package tech.hiregus.trendingprojects.list

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import tech.hiregus.trendingprojects.data.datasource.ReposPagingSource
import tech.hiregus.trendingprojects.data.api.ReposService
import tech.hiregus.trendingprojects.data.response.SearchResponse
import tech.hiregus.trendingprojects.getReposList
import tech.hiregus.trendingprojects.ui.repos.list.filter.ReposFilter
import tech.hiregus.trendingprojects.utils.NETWORK_PAGE_SIZE

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ReposPagingSourceTest {

    private val reposService = mock(ReposService::class.java)

    @Test
    fun firstLoad_returns_success() = runBlockingTest {
        val filter = ReposFilter()
        `when`(
            reposService.search(
                sort = ReposPagingSource.SORT,
                order = filter.order.id,
                query = listOf(
                    ReposPagingSource.CREATED_DATE_FORMAT.format(filter.createdDate.getFromDate())
                ),
                perPage = NETWORK_PAGE_SIZE,
                page = 1
            )
        ).thenReturn(Response.success(SearchResponse(getReposList())))
        val pagingSource = ReposPagingSource(filter, reposService)

        assertThat(
            LoadResult.Page(
                data = getReposList(),
                prevKey = null,
                nextKey = 2
            )
        ).isEqualTo(
            pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = NETWORK_PAGE_SIZE,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun load2ndLoad_returns_empty() = runBlockingTest {
        val filter = ReposFilter()
        `when`(
            reposService.search(
                sort = ReposPagingSource.SORT,
                order = filter.order.id,
                query = listOf(
                    ReposPagingSource.CREATED_DATE_FORMAT.format(filter.createdDate.getFromDate())
                ),
                perPage = NETWORK_PAGE_SIZE,
                page = 4
            )
        ).thenReturn(Response.success(SearchResponse(listOf())))
        val pagingSource = ReposPagingSource(filter, reposService)

        assertThat(
            LoadResult.Page(
                data = listOf(),
                prevKey = null,
                nextKey = null
            )
        ).isEqualTo(
            pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 4,
                    loadSize = NETWORK_PAGE_SIZE,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun http500_returns_error() = runBlockingTest {
        val filter = ReposFilter()
        val errorResponseBody = "{message=\"this test shoudl fail\"}".toResponseBody("application/json".toMediaTypeOrNull())
        `when`(
            reposService.search(
                sort = ReposPagingSource.SORT,
                order = filter.order.id,
                query = listOf(
                    ReposPagingSource.CREATED_DATE_FORMAT.format(filter.createdDate.getFromDate())
                ),
                perPage = NETWORK_PAGE_SIZE,
                page = 5
            )
        ).thenReturn(Response.error(500, errorResponseBody))
        val pagingSource = ReposPagingSource(filter, reposService)

        assertThat(
            pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 5,
                    loadSize = NETWORK_PAGE_SIZE,
                    placeholdersEnabled = false
                )
            )
        ).isInstanceOf(LoadResult.Error::class.java)
    }

}