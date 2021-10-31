package tech.hiregus.trendingprojects.ui.repos.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tech.hiregus.trendingprojects.R
import tech.hiregus.trendingprojects.data.model.Language
import tech.hiregus.trendingprojects.data.model.Repo
import tech.hiregus.trendingprojects.data.model.Result
import tech.hiregus.trendingprojects.domain.GetRepoLanguagesUseCase

class RepoDetailViewModel(repo: Repo?,
                          private val getRepoLanguagesUseCase: GetRepoLanguagesUseCase) : ViewModel() {

    private val _repo: MutableStateFlow<Repo?> = MutableStateFlow(repo)
    private val _languages: MutableStateFlow<List<Language>?> = MutableStateFlow(null)
    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var uiState: StateFlow<RepoDetailUiState> = combine(_repo, _languages, _loading) { repo, languages, _loading ->
        when {
            _loading -> RepoDetailUiState.Loading
            repo != null -> RepoDetailUiState.Success(repo, languages)
            else -> RepoDetailUiState.Error(R.string.error_repo_detail)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, getDefaultUiState(repo))

    init {
        if (repo != null) getLanguages(repo)
    }
    
    fun getOwnerUrl(): String? {
        return (uiState.value as? RepoDetailUiState.Success)?.repo?.owner?.url
    }

    fun getRepoUrl(): String? {
        return (uiState.value as? RepoDetailUiState.Success)?.repo?.url
    }

    fun getLanguages(repo: Repo) {
        viewModelScope.launch {
            setLoading(true)
            val result = getRepoLanguagesUseCase(repo)
            if (result is Result.Success) {
                _languages.value = result.data
            }
            setLoading(false)
        }
    }

    private fun setLoading(active: Boolean) {
        _loading.value = active
    }

    private fun getDefaultUiState(repo: Repo?): RepoDetailUiState {
        return if (repo != null) {
            RepoDetailUiState.Success(repo, null)
        } else {
            RepoDetailUiState.Error(R.string.error_repo_detail)
        }
    }

}

sealed class RepoDetailUiState {
    data class Success(val repo: Repo, val languages: List<Language>? = null) : RepoDetailUiState()
    data class Error(val resource: Int) : RepoDetailUiState()
    object Loading : RepoDetailUiState()
}