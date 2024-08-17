package ru.practicum.android.diploma.data.network

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.CountriesRequest
import ru.practicum.android.diploma.data.dto.CountriesResponse
import ru.practicum.android.diploma.data.dto.RESULT_CODE_BAD_REQUEST
import ru.practicum.android.diploma.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SERVER_ERROR
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SUCCESS
import ru.practicum.android.diploma.data.dto.RegionsRequest
import ru.practicum.android.diploma.data.dto.RegionsResponse
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.SearchRequest
import ru.practicum.android.diploma.data.dto.IndustriesRequest
import ru.practicum.android.diploma.data.dto.IndustriesResponse
import ru.practicum.android.diploma.data.dto.VacancyRequest
import ru.practicum.android.diploma.util.isInternetAvailable

class RetrofitNetworkClient(
    private val hhApiService: HHApiService,
    private val context: Context
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isInternetAvailable(context)) {
            return Response().apply { resultCode = RESULT_CODE_NO_INTERNET }

        }
        return getNetworkResponse(dto = dto)
    }

    private suspend fun getNetworkResponse(dto: Any): Response {
        return withContext(Dispatchers.IO) {
            try {
                when (dto) {
                    is VacancyRequest -> getVacancyResponse(dto)
                    is SearchRequest -> getSearchResponse(dto)
                    is CountriesRequest -> getCountriesResponse()
                    is RegionsRequest -> getRegionsResponse(dto)
                    is IndustriesRequest -> getIndustriesResponse()
                    else -> {
                        Response().apply { resultCode = RESULT_CODE_BAD_REQUEST }
                    }
                }

            } catch (e: HttpException) {
                println("RetrofitClient error code: ${e.code()} message: ${e.message}")
                Response().apply { resultCode = RESULT_CODE_SERVER_ERROR }
            }
        }
    }

    private suspend fun getVacancyResponse(dto: VacancyRequest): Response {
        val response = hhApiService.getVacancy(dto.id)
        return response.apply { resultCode = RESULT_CODE_SUCCESS }
    }

    private suspend fun getSearchResponse(dto: SearchRequest): Response {
        val response = hhApiService.searchVacancies(options = dto.options)
        return response.apply { resultCode = RESULT_CODE_SUCCESS }
    }

    private suspend fun getCountriesResponse(): Response {
        val networkResponse = hhApiService.getCountries()
        if (networkResponse.isSuccessful) {
            val countriesResponse = CountriesResponse(networkResponse.body() ?: emptyList())
            countriesResponse.resultCode = networkResponse.code()
            return countriesResponse
        } else {
            return Response().apply { resultCode = networkResponse.code() }
        }
    }

    private suspend fun getRegionsResponse(dto: RegionsRequest): Response {
        val networkResponse = hhApiService.getRegions(dto.id.toString())
        if (networkResponse.isSuccessful) {
            val regionsResponse = RegionsResponse(networkResponse.body()?.areas ?: emptyList())
            regionsResponse.resultCode = networkResponse.code()
            return regionsResponse
        } else {
            return Response().apply { resultCode = networkResponse.code() }
        }
    }

    private suspend fun getIndustriesResponse(): Response {
        val networkResponse = hhApiService.getIndustries()
        if (networkResponse.isSuccessful) {
            val sectorsResponse = IndustriesResponse(networkResponse.body() ?: emptyList())
            sectorsResponse.resultCode = networkResponse.code()
            return sectorsResponse
        } else {
            return Response().apply { resultCode = networkResponse.code() }
        }
    }

}
