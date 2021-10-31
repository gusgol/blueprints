package tech.hiregus.trendingprojects.ui.repos.list.filter

import tech.hiregus.trendingprojects.R
import tech.hiregus.trendingprojects.utils.extensions.getToday
import tech.hiregus.trendingprojects.utils.extensions.subtractDays

data class ReposFilter(var order: Order = Order.DESC, var createdDate: CreatedDate = CreatedDate.TODAY)

enum class Order(val id: String) {
    DESC("desc"), ASC("asc")
}

enum class CreatedDate {
    TODAY, LAST_WEEK, LAST_MONTH;

    fun getFromDate(): String {
        return when(this) {
            TODAY -> getToday()
            LAST_WEEK -> subtractDays(7)
            LAST_MONTH -> subtractDays(30)
        }
    }

    companion object {
        fun fromChipId(id: Int): CreatedDate {
            return when (id) {
                R.id.today -> TODAY
                R.id.lastWeek -> LAST_WEEK
                else -> LAST_MONTH
            }
        }
    }
}