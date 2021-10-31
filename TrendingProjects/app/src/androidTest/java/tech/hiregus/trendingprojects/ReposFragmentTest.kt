package tech.hiregus.trendingprojects

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import org.mockito.Mockito
import retrofit2.Response
import tech.hiregus.trendingprojects.data.datasource.ReposPagingSource
import tech.hiregus.trendingprojects.data.api.ReposService
import tech.hiregus.trendingprojects.data.response.SearchResponse
import tech.hiregus.trendingprojects.ui.repos.list.ReposFragment
import tech.hiregus.trendingprojects.ui.repos.list.filter.ReposFilter
import tech.hiregus.trendingprojects.utils.NETWORK_PAGE_SIZE

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class ReposFragmentTest {

    lateinit var mockModule: Module

    lateinit var reposService: ReposService

    @Before
    fun initRepository() {
        reposService = Mockito.mock(ReposService::class.java)
        mockModule = module {
            single(override = true) { reposService }
        }
        loadKoinModules(mockModule)
    }

    @After
    fun tearDown() {
        GlobalContext.unloadKoinModules(mockModule)
    }

    @Test
    fun noRepos_displayEmptyState() {
        // GIVEN - repository returns empty
        runBlocking {
            setResponse(Response.success(SearchResponse(listOf())))
        }
        launchFragmentInContainer<ReposFragment>(Bundle(), R.style.Theme_Base_TrendingProjects)

        // THEN - empty state is displayed
        Espresso.onView(withId(R.id.empty))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun hasRepos_displaysList() {
        // GIVEN - repository returns a list
        runBlocking {
            setResponse(Response.success(SearchResponse(getReposList())))
        }
        launchFragmentInContainer<ReposFragment>(Bundle(), R.style.Theme_Base_TrendingProjects)

        // THEN - empty state, error is not displayed, and check if item 0 is being displayed
        Espresso.onView(withId(R.id.empty))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        Espresso.onView(withId(R.id.error))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        Espresso.onView(ViewMatchers.withText("TrendingProjects"))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun error_displaysErrorState() {
        // GIVEN - repository returns a list
        val errorResponseBody = "{message=\"this test shoudl fail\"}".toResponseBody("application/json".toMediaTypeOrNull())
        runBlocking {
            setResponse(Response.error(500, errorResponseBody))
        }

        launchFragmentInContainer<ReposFragment>(Bundle(), R.style.Theme_Base_TrendingProjects)

        // THEN - empty state, error is not displayed, and check if item 0 is being displayed
        Espresso.onView(withId(R.id.empty))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        Espresso.onView(withId(R.id.error))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    private suspend fun setResponse(response: Response<SearchResponse>) {
        val filter = ReposFilter()
        Mockito.`when`(
            reposService.search(
                sort = ReposPagingSource.SORT,
                order = filter.order.id,
                query = listOf(
                    ReposPagingSource.CREATED_DATE_FORMAT.format(filter.createdDate.getFromDate())
                ),
                perPage = NETWORK_PAGE_SIZE * 3,
                page = 1
            )
        ).thenReturn(response)
    }


}