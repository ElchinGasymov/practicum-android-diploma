package ru.practicum.android.diploma.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.dto.SearchResponse
import ru.practicum.android.diploma.data.dto.VacancyResponse
import ru.practicum.android.diploma.data.dto.components.CountryDto
import ru.practicum.android.diploma.data.dto.components.IndustriesDto
import ru.practicum.android.diploma.data.dto.components.RegionListDto

interface HHApiService {
    @GET("vacancies/{vacancy_id}")
    suspend fun getVacancy(@Path("vacancy_id") id: String): VacancyResponse

    @GET("vacancies")
    suspend fun searchVacancies(@QueryMap options: Map<String, String>): SearchResponse

    @GET("areas")
    suspend fun getCountries(): Response<List<CountryDto>>

    @GET("areas/{area_id}")
    suspend fun getRegions(@Path("area_id") areaId: Int): Response<RegionListDto>

    @GET("industries")
    suspend fun getIndustries(): Response<List<IndustriesDto>>
}
