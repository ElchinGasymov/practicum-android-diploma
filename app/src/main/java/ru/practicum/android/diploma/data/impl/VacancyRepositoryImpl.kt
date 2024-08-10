package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.RESULT_CODE_BAD_REQUEST
import ru.practicum.android.diploma.data.dto.RESULT_CODE_NOT_FOUND
import ru.practicum.android.diploma.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SERVER_ERROR
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SUCCESS
import ru.practicum.android.diploma.data.dto.VacancyRequest
import ru.practicum.android.diploma.data.dto.VacancyResponse
import ru.practicum.android.diploma.data.dto.toModel
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.db.FavouriteVacanciesRepository
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.ResponseData

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient,
    private val favouriteVacanciesRepository: FavouriteVacanciesRepository
) : VacancyRepository {

    override fun getVacancy(id: String): Flow<ResponseData<VacancyDetails>> = flow {
        val response = networkClient.doRequest(VacancyRequest(id = id))
        when (response.resultCode) {
            RESULT_CODE_SUCCESS -> {
                with(response as VacancyResponse) {
                    val vacancyDetails = this.toModel()
                    emit(ResponseData.Data(vacancyDetails))
                }
            }

            RESULT_CODE_NO_INTERNET -> {
                val vacancyDetails = try {
                    favouriteVacanciesRepository.getVacancy(id)
                } catch (e: Exception) {
                    null
                }

                if (vacancyDetails != null) {
                    emit(ResponseData.Data(vacancyDetails))
                } else {
                    emit(ResponseData.Error(ResponseData.ResponseError.NO_INTERNET))
                }
            }

            RESULT_CODE_NOT_FOUND -> emit(ResponseData.Error(ResponseData.ResponseError.NOT_FOUND))
            RESULT_CODE_BAD_REQUEST -> emit(ResponseData.Error(ResponseData.ResponseError.CLIENT_ERROR))
            RESULT_CODE_SERVER_ERROR -> emit(ResponseData.Error(ResponseData.ResponseError.SERVER_ERROR))
            else -> emit(ResponseData.Error(ResponseData.ResponseError.SERVER_ERROR))
        }
    }
}
