package tech.hiregus.trendingprojects.ui.repos.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tech.hiregus.trendingprojects.R
import tech.hiregus.trendingprojects.data.model.Repo
import tech.hiregus.trendingprojects.databinding.ItemRepoBinding
import tech.hiregus.trendingprojects.utils.extensions.loadCircle
import tech.hiregus.trendingprojects.utils.extensions.timeAgo

class ReposAdapter(val onRepoClick: (CardView, Repo) -> Unit) : PagingDataAdapter<Repo, ReposAdapter.RepoViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return RepoViewHolder(ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<Repo>() {
            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean =
                oldItem.id == newItem.id
        }
    }

    inner class RepoViewHolder(private val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(repo: Repo) {
            binding.created.text = String.format("%s - %s", repo.owner.login, repo.createdAt.timeAgo(binding.root.context))
            binding.name.text = repo.name
            repo.description?.let {
                binding.description.text = it
            } ?: let { binding.description.isVisible = false }
            binding.avatar.loadCircle(repo.owner.avatar)
            binding.card.transitionName = itemView.context.getString(R.string.transition_repo, repo.id)
            binding.root.setOnClickListener {
                onRepoClick(binding.card, repo)
            }
        }

    }

}