package tech.hiregus.trendingprojects.ui.repos.detail.languages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tech.hiregus.trendingprojects.data.model.Language
import tech.hiregus.trendingprojects.databinding.ItemLanguageBinding

class RepoLanguagesAdapter(val languages: List<Language>) : RecyclerView.Adapter<RepoLanguagesAdapter.LanguageViewHolder>() {

    private var total: Long = 0

    init {
        languages.forEach { total += it.size }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        return LanguageViewHolder(
            ItemLanguageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(languages[position])
    }

    override fun getItemCount(): Int = languages.count()

    inner class LanguageViewHolder(private val binding: ItemLanguageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(language: Language) {
            binding.name.text = language.name
            binding.percent.text = getPercentage(language.size).orEmpty()
        }

        private fun getPercentage(size: Long): String? {
            if (total > 0) {
                val percentage = size.toDouble() / total * 100
                return "%1$,.1f".format(percentage) + "%"
            }
            return null
        }
    }
}