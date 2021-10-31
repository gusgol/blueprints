package tech.hiregus.trendingprojects.detail

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import tech.hiregus.trendingprojects.FakeReposRepository
import tech.hiregus.trendingprojects.data.model.Result
import tech.hiregus.trendingprojects.domain.GetRepoLanguagesUseCase
import tech.hiregus.trendingprojects.getRepo

@ExperimentalCoroutinesApi
class GetLanguagesUseCaseTest {

    private val repoRepository = FakeReposRepository()

    private val useCase = GetRepoLanguagesUseCase(repoRepository)

    private val repo = getRepo()

    @Test
    fun useCase_invoke_returnsError() = runBlockingTest {
        repoRepository.shouldReturnError = true
        val result = useCase(repo)

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun useCase_invoke_returnsSuccess() = runBlockingTest {
        val result = useCase(repo)
        val data = (result as? Result.Success)?.data

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat(data?.count()).isEqualTo(2)
    }

    @Test
    fun useCase_invoke_returnsEmpty() = runBlockingTest {
        repoRepository.languagesResult = mapOf()
        val result = useCase(repo)
        val data = (result as? Result.Success)?.data

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat(data).isEmpty()
    }
}