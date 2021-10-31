package tech.hiregus.trendingprojects.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tech.hiregus.trendingprojects.data.*
import tech.hiregus.trendingprojects.data.api.ReposService
import tech.hiregus.trendingprojects.data.api.provideGithubApi
import tech.hiregus.trendingprojects.data.model.Repo
import tech.hiregus.trendingprojects.data.datasource.ReposDataSource
import tech.hiregus.trendingprojects.data.datasource.ReposRemoteDataSource
import tech.hiregus.trendingprojects.domain.GetRepoLanguagesUseCase
import tech.hiregus.trendingprojects.ui.repos.detail.RepoDetailViewModel
import tech.hiregus.trendingprojects.ui.repos.list.ReposViewModel

val projectsModule = module {
    factory { provideGithubApi().create(ReposService::class.java) }
    factory<ReposDataSource> { ReposRemoteDataSource(get()) }
    factory<ReposRepository> { DefaultRepoRepository(get()) }
    viewModel { ReposViewModel(get()) }

    factory { GetRepoLanguagesUseCase(get()) }
    viewModel { (repo: Repo?) -> RepoDetailViewModel(repo, get()) }
}