package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.converters.ConverterIntoEntity
import ru.practicum.android.diploma.data.db.converters.ConverterIntoModel
import ru.practicum.android.diploma.domain.FavouriteVacanciesRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails

class FavouriteVacanciesRepositoryImpl(
    private val vacanciesDatabase: AppDatabase,
    private val converterIntoModel: ConverterIntoModel,
    private val converterIntoEntity: ConverterIntoEntity
) : FavouriteVacanciesRepository {
    override suspend fun getVacancy(vacancyId: String): VacancyDetails? {
        val vacancy = vacanciesDatabase.vacancyDao().findVacancy(vacancyId)
        if (vacancy != null) {
            return converterIntoModel.intoVacancyDetail(
                listPhone = vacancy.phones,
                listKey = vacancy.keySkills,
                vacancyEntity = vacancy.vacancy,
                areaEntity = vacancy.areas
            )
        } else {
            return null
        }
    }

    override suspend fun addVacancy(vacancy: VacancyDetails) {
        vacanciesDatabase.vacancyDao().insertVacancy(converterIntoEntity.intoVacancyEntity(vacancy))
        vacanciesDatabase.areaDao().insertArea(converterIntoEntity.intoAreaEntity(vacancy))
        vacanciesDatabase.phoneDao().insertPhone(converterIntoEntity.intoPhoneEntity(vacancy))
        vacanciesDatabase.keySkillDao().insertKeySkill(converterIntoEntity.intoKeySkillEntity(vacancy))
    }

    override suspend fun deleteVacancy(vacancyId: String) {
        vacanciesDatabase.vacancyDao().deleteVacancy(vacancyId)
        vacanciesDatabase.areaDao().deleteArea(vacancyId)
        vacanciesDatabase.phoneDao().deletePhone(vacancyId)
        vacanciesDatabase.keySkillDao().deleteKeySkill(vacancyId)
    }

    override fun listVacancies(): Flow<List<Vacancy>> = flow {
        val listVacancy = ArrayList<Vacancy>()
        vacanciesDatabase.vacancyDao().getSelectedVacancies().forEach { vacancyEntity ->
            listVacancy.add(
                converterIntoModel.intoVacancy(
                    vacancy = vacancyEntity.vacancy,
                    areas = vacancyEntity.areas
                )
            )
        }
        emit(listVacancy)
    }

    override suspend fun hasLike(vacancyId: String): Boolean {
        return vacanciesDatabase.vacancyDao().hasLike(vacancyId) > 0
    }
}
