package tech.hiregus.trendingprojects.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Owner(
    val id: String,
    val login: String,
    @Json(name = "avatar_url") var avatar: String,
    @Json(name = "html_url") var url: String
) : Parcelable