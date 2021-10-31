package tech.hiregus.trendingprojects.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tech.hiregus.trendingprojects.FakeReposRepository
import tech.hiregus.trendingprojects.MainCoroutineRule
import tech.hiregus.trendingprojects.domain.GetRepoLanguagesUseCase
import tech.hiregus.trendingprojects.getRepo
import tech.hiregus.trendingprojects.ui.repos.detail.RepoDetailUiState
import tech.hiregus.trendingprojects.ui.repos.detail.RepoDetailViewModel
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class RepoDetailViewModelTest {

    private val successUC = GetRepoLanguagesUseCase(FakeReposRepository())
    private val failedUC = GetRepoLanguagesUseCase(FakeReposRepository().apply {
        shouldReturnError = true
    })
    private val repo = getRepo()
    private lateinit var repoDetailViewModel: RepoDetailViewModel

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        repoDetailViewModel = RepoDetailViewModel(repo, successUC)
    }

    @Test
    fun uiState_init_returnRepo() = runBlockingTest {
        val state = repoDetailViewModel.uiState.first()
        val firstRepo = (state as? RepoDetailUiState.Success)?.repo

        assertThat(state).isInstanceOf(RepoDetailUiState.Success::class.java)
        assertThat(firstRepo?.id).isEqualTo(repo.id)
    }

    @Test
    fun uiState_noRepo_returnError() = runBlockingTest {
        repoDetailViewModel = RepoDetailViewModel(null, failedUC)
        val state = repoDetailViewModel.uiState.first()
        assertThat(state).isInstanceOf(RepoDetailUiState.Error::class.java)
    }

    @Test
    fun uiState_getLanguages_returns2() = runBlockingTest {
        repoDetailViewModel.getLanguages(repo)

        val states = mutableListOf<RepoDetailUiState>()
        val job = launch {
            repoDetailViewModel.uiState.toList(states)
        }

        val languages = (states.first() as? RepoDetailUiState.Success)?.languages

        assertThat(states.first()).isInstanceOf(RepoDetailUiState.Success::class.java)
        assertThat(languages?.count()).isEqualTo(2)

        job.cancel()
    }

    @Test
    fun uiState_getLanguagesFail_languagesIsNull() = runBlockingTest {
        repoDetailViewModel = RepoDetailViewModel(repo, failedUC)
        repoDetailViewModel.getLanguages(repo)

        val states = mutableListOf<RepoDetailUiState>()
        val job = launch {
            repoDetailViewModel.uiState.toList(states)
        }

        val languages = (states.first() as? RepoDetailUiState.Success)?.languages
        assertThat(languages).isNull()

        job.cancel()
    }



    // repo init returns MainCoroutineRule
}