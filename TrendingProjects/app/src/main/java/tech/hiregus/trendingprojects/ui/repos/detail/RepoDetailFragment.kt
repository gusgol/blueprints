package tech.hiregus.trendingprojects.ui.repos.detail

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import tech.hiregus.trendingprojects.R
import tech.hiregus.trendingprojects.data.model.Language
import tech.hiregus.trendingprojects.data.model.Repo
import tech.hiregus.trendingprojects.databinding.FragmentRepoDetailBinding
import tech.hiregus.trendingprojects.ui.repos.detail.languages.RepoLanguagesAdapter
import tech.hiregus.trendingprojects.utils.extensions.*

class RepoDetailFragment : Fragment() {

    private val args: RepoDetailFragmentArgs by navArgs()

    private var _binding: FragmentRepoDetailBinding? = null
    private val binding get() = _binding!!

    private val repoDetailViewModel: RepoDetailViewModel by viewModel {
        parametersOf(args.repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.host
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRepoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        collectUiState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpViews() {
        binding.toolbar.setupWithNavController(findNavController())
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.visit -> {
                    visitPage(repoDetailViewModel.getRepoUrl())
                    true
                }
                else -> false
            }
        }
        binding.avatar.setOnClickListener { visitPage(repoDetailViewModel.getOwnerUrl()) }
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repoDetailViewModel.uiState.collectLatest { uiState ->
                when (uiState) {
                    is RepoDetailUiState.Error -> showError(uiState.resource)
                    is RepoDetailUiState.Success -> showDetails(uiState)
                    RepoDetailUiState.Loading -> setLoading(true)
                }
            }
        }
    }

    private fun showDetails(uiState: RepoDetailUiState.Success) {
        showRepo(uiState.repo)
        showLanguages(uiState.languages)
    }

    private fun showRepo(repo: Repo) {
        setLoading(false)
        binding.name.text = repo.name
        binding.created.text = String.format("%s - %s", repo.owner.login, repo.createdAt.timeAgo(requireContext()))
        binding.stars.text = getString(R.string.mgs_d_stars, repo.stars)
        binding.avatar.loadCircle(repo.owner.avatar)
        binding.body.text = repo.description
    }

    private fun showLanguages(languages: List<Language>?) {
        binding.languages.isVisible = !languages.isNullOrEmpty()
        if (languages != null && languages.isNotEmpty()) {
            binding.languages.adapter = RepoLanguagesAdapter(languages)
            binding.languagesInfo.isVisible = false
        }
    }

    private fun showError(resource: Int) {
        setLoading(false)
        binding.root.showSnackBar(resource)
    }

    private fun setLoading(active: Boolean) {
        binding.loading.isVisible = active
        binding.languagesInfo.text = getString(if (active) R.string.msg_loading else R.string.msg_no_languages_available)
    }

    private fun visitPage(url: String?) {
        if (url != null) {
            requireContext().openCustomTabs(url)
        } else {
            binding.root.showSnackBar(R.string.error_load_page)
        }
    }

}