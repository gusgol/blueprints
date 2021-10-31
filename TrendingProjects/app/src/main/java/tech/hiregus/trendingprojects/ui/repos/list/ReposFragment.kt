package tech.hiregus.trendingprojects.ui.repos.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hiregus.trendingprojects.R
import tech.hiregus.trendingprojects.data.model.Repo
import tech.hiregus.trendingprojects.databinding.FragmentReposBinding
import tech.hiregus.trendingprojects.ui.repos.list.filter.CreatedDate
import tech.hiregus.trendingprojects.ui.repos.list.filter.Order
import tech.hiregus.trendingprojects.utils.ViewHolderSpacingDecoration
import tech.hiregus.trendingprojects.utils.extensions.themeColor

class ReposFragment : Fragment() {

    private var _binding: FragmentReposBinding? = null
    private val binding get() = _binding!!

    private val reposViewModel: ReposViewModel by viewModel()

    private val adapter: ReposAdapter = ReposAdapter(::onRepoClick)

    private var filter: BottomSheetBehavior<View>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReposBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Shared transitions */
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        setupViews()
        collectUiState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViews() {
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.toggleFilter -> {
                    toggleFilter()
                    true
                }
                else -> false
            }
        }
        with(binding.repos) {
            adapter = this@ReposFragment.adapter
            addItemDecoration(ViewHolderSpacingDecoration.defaultSpacing(requireContext()))
        }
        adapter.addLoadStateListener { state ->
            render(state)
        }
        binding.refresh.isEnabled = false
        setupFilter()
    }

    @ExperimentalCoroutinesApi
    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            reposViewModel.repos.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun render(state: CombinedLoadStates) {
        val isEmpty = adapter.itemCount == 0
        binding.state.error.isVisible = state.refresh is LoadState.Error && isEmpty
        binding.state.loading.isVisible = state.refresh is LoadState.Loading && isEmpty
        binding.refresh.isRefreshing = state.refresh is LoadState.Loading && !isEmpty
        binding.state.empty.isVisible = state.refresh is LoadState.NotLoading && isEmpty
    }

    private fun onRepoClick(view: CardView, repo: Repo) {
        val repoCardTransition = getString(R.string.transition_repo_detail)
        val extras = FragmentNavigatorExtras(view to repoCardTransition)
        val directions = ReposFragmentDirections.actionRepoListToRepoDetail(repo)
        findNavController().navigate(directions, extras)
    }

    private fun setupFilter() {
        filter = BottomSheetBehavior.from(binding.filter.root)
        filter?.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                styleFilterHeader(newState)
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    filter(binding.filter.order.checkedChipId, binding.filter.cratedDate.checkedChipId)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset >= 0.4) binding.filter.header.alpha = slideOffset
                binding.filter.icon.rotation = slideOffset * 180
            }
        })
        binding.filter.header.setOnClickListener { toggleFilter() }
    }

    private fun toggleFilter() {
        filter?.let {
            if (it.state == BottomSheetBehavior.STATE_COLLAPSED) {
                it.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                it.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun filter(selectedOrder: Int, selectedCreatedDate: Int) {
        reposViewModel.filter(
            order = if (selectedOrder == R.id.desc) Order.DESC else Order.ASC,
            createdDate = CreatedDate.fromChipId(selectedCreatedDate)
        )
    }

    private fun styleFilterHeader(newState: Int) {
        val color = if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
            R.attr.filterHeaderColor
        } else {
            R.attr.colorSurface
        }
        binding.filter.header.setBackgroundColor(requireContext().themeColor(color))
    }
}