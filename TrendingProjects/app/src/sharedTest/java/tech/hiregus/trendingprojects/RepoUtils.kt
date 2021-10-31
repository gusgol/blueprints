package tech.hiregus.trendingprojects

import androidx.paging.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import tech.hiregus.trendingprojects.data.model.Owner
import tech.hiregus.trendingprojects.data.model.Repo
import java.time.LocalDateTime
import java.time.Month

fun getRepo(id: String = "1", name: String = "TrendingProjects"): Repo {
    return Repo(
        id = id,
        name = name,
        owner = Owner(
            id = "1",
            login = "gusgol",
            avatar = "http://test.com.br",
            url = "http://test.com.br"
        ),
        description = "Repo unit test",
        url = "http://test.com.br",
        stars = 10,
        updatedAt = getLocalDateTime(),
        createdAt = getLocalDateTime().minusDays(7)
    )
}

fun getReposList() = listOf(
    getRepo(),
    getRepo("2", "Project 2"),
    getRepo("3", "Project 3")
)

fun getLocalDateTime(): LocalDateTime = LocalDateTime.of(
    2021, Month.JULY, 29, 19, 30, 40
)

@ExperimentalCoroutinesApi
suspend fun <T : Any> PagingData<T>.collectDataForTest(): List<T> {
    val dcb = object : DifferCallback {
        override fun onChanged(position: Int, count: Int) {}
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
    }
    val items = mutableListOf<T>()
    val dif = object : PagingDataDiffer<T>(dcb, TestCoroutineDispatcher()) {
        override suspend fun presentNewList(
            previousList: NullPaddedList<T>,
            newList: NullPaddedList<T>,
            newCombinedLoadStates: CombinedLoadStates,
            lastAccessedIndex: Int,
            onListPresentable: () -> Unit
        ): Int? {
            for (idx in 0 until newList.size)
                items.add(newList.getFromStorage(idx))
            onListPresentable()
            return null
        }
    }
    dif.collectFrom(this)
    return items
}