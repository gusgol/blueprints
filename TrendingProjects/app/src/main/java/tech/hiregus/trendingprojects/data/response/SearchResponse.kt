package tech.hiregus.trendingprojects.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import tech.hiregus.trendingprojects.data.model.Repo

@Parcelize
class SearchResponse(val items: List<Repo>): Parcelable