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
            buildMap {
                put("text", searchText)
                put("per_page", itemsPerPage.toString())
                put("page", page.toString())
                if (area?.isNotEmpty() == true) {
                    put("area", area.toString())
                }
                if (industry?.isNotEmpty() == true) put("industry", industry.toString())
                if (salary?.isNotEmpty() == true) put("salary", salary.toString())
                put("only_with_salary", withSalary.toString())
            }
        }
    }
}
