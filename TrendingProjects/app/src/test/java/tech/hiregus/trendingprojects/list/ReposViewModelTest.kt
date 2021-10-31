package tech.hiregus.trendingprojects.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tech.hiregus.trendingprojects.FakeReposRepository
import tech.hiregus.trendingprojects.MainCoroutineRule
import tech.hiregus.trendingprojects.collectDataForTest
import tech.hiregus.trendingprojects.ui.repos.list.ReposViewModel
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class ReposViewModelTest {

    private  lateinit var reposRepository: FakeReposRepository

    private lateinit var reposViewModel: ReposViewModel

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        reposRepository = FakeReposRepository()
        reposViewModel = ReposViewModel(reposRepository)
    }

    @Test
    fun repos_init_getLoaded() = runBlockingTest {

        val reposPage = reposViewModel.repos.first()

        val repos = reposPage.collectDataForTest()

        assertThat(repos.count()).isEqualTo(3)
    }
}