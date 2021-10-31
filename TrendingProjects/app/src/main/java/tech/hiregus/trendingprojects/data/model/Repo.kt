package tech.hiregus.trendingprojects.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Repo(
    val id: String,
    val name: String,
    val owner: Owner,
    val description: String?,
    @Json(name = "html_url") var url: String,
    @Json(name = "stargazers_count") var stars: Int,
    @Json(name = "updated_at") val updatedAt: LocalDateTime,
    @Json(name = "created_at") val createdAt: LocalDateTime,
) : Parcelable