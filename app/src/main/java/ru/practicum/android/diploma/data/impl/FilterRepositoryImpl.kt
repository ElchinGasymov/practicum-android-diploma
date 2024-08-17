package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.CountriesRequest
import ru.practicum.android.diploma.data.dto.CountriesResponse
import ru.practicum.android.diploma.data.dto.RESULT_CODE_BAD_REQUEST
import ru.practicum.android.diploma.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.data.dto.RegionsRequest
import ru.practicum.android.diploma.data.dto.RegionsResponse
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.IndustriesRequest
import ru.practicum.android.diploma.data.dto.IndustriesResponse
import ru.practicum.android.diploma.data.dto.toAllRegions
import ru.practicum.android.diploma.data.dto.toCountryList
import ru.practicum.android.diploma.data.dto.toRegionList
import ru.practicum.android.diploma.data.dto.toSectorList
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.util.ResponseData

class FilterRepositoryImpl(
    private val networkClient: NetworkClient
) : FilterRepository {

    override fun getCountries(): Flow<ResponseData<List<Country>>> = flow {
        when (val response = networkClient.doRequest(CountriesRequest)) {
            is CountriesResponse -> {
                val countriesList = response.countries.toCountryList()
                emit(ResponseData.Data(countriesList))
            }

            else -> emit(responseToError(response))
        }
    }

    override fun getRegions(id: Int): Flow<ResponseData<List<Region>>> = flow {
        when (val response = networkClient.doRequest(RegionsRequest(id))) {
            is RegionsResponse -> {
                val regionsList = response.regions.toRegionList()
                emit(ResponseData.Data(regionsList))
            }

            else -> emit(responseToError(response))
        }
    }

    override fun getAllRegions(): Flow<ResponseData<List<Region>>> = flow {
        when (val response = networkClient.doRequest(CountriesRequest)) {
            is CountriesResponse -> {
                val regionsList = response.countries.toAllRegions()
                emit(ResponseData.Data(regionsList))
            }

            else -> emit(responseToError(response))
        }
    }

    override fun getIndustries(): Flow<ResponseData<List<Industries>>> = flow {
        when (val response = networkClient.doRequest(IndustriesRequest)) {
            is IndustriesResponse -> {
                val sectorsList = response.sectors.toSectorList()
                emit(ResponseData.Data(sectorsList))
            }

            else -> emit(responseToError(response))
        }
    }

    private fun <T> responseToError(response: Response): ResponseData<T> =
        ResponseData.Error(
            when (response.resultCode) {
                RESULT_CODE_NO_INTERNET -> ResponseData.ResponseError.NO_INTERNET
                RESULT_CODE_BAD_REQUEST -> ResponseData.ResponseError.CLIENT_ERROR
                else -> ResponseData.ResponseError.SERVER_ERROR
            }
        )
}
