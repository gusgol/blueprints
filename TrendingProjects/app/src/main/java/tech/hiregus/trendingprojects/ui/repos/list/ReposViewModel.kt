package tech.hiregus.trendingprojects.ui.repos.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import tech.hiregus.trendingprojects.data.model.Repo
import tech.hiregus.trendingprojects.data.ReposRepository
import tech.hiregus.trendingprojects.ui.repos.list.filter.CreatedDate
import tech.hiregus.trendingprojects.ui.repos.list.filter.Order
import tech.hiregus.trendingprojects.ui.repos.list.filter.ReposFilter

class ReposViewModel(private val reposRepository: ReposRepository) : ViewModel() {

    private val filter: MutableStateFlow<ReposFilter> = MutableStateFlow(ReposFilter())

    @ExperimentalCoroutinesApi
    val repos: Flow<PagingData<Repo>> = filter.flatMapLatest {
        reposRepository.getRepos(filter.value)
    }.cachedIn(viewModelScope)

    fun filter(order: Order, createdDate: CreatedDate) {
        filter.value = ReposFilter(order, createdDate)
    }

}