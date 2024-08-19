package ru.practicum.android.diploma.util

data class Options(
    val searchText: String,
    val itemsPerPage: Int,
    val page: Int,
    val area: String?,
    val industry: String?,
    val salary: String?,
    val withSalary: Boolean?
) {
    companion object {
        fun toMap(options: Options): Map<String, String> = with(options) {
            listOfNotNull(
                "text" to searchText,
                "per_page" to itemsPerPage.toString(),
                "page" to page.toString(),
                if (area?.isNotEmpty() == true) "area" to area.toString() else null,
                if (industry?.isNotEmpty() == true) "industry" to area.toString() else null,
                if (salary?.isNotEmpty() == true) "salary" to area.toString() else null,
                "only_with_salary" to withSalary.toString()
            ).toMap()
        }
    }
}
