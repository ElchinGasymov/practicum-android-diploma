package ru.practicum.android.diploma.data.db

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.db.entity.ConverterDb
import ru.practicum.android.diploma.domain.db.SelectedVacanciesRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails

class SelectedVacanciesRepositoryImpl(
    private val vacanciesDatabase: AppDatabase,
    private val converter: ConverterDb
) : SelectedVacanciesRepository {
    override suspend fun getVacancy(vacancyId: Int): VacancyDetails {
        return converter.intoVacancyDetail(
            listPhone = vacanciesDatabase.phoneDao().getSelectedPhone(vacancyId),
            listKey = vacanciesDatabase.keySkillDao().getSelectedKeySkill(vacancyId),
            vacancyEntity = vacanciesDatabase.vacancyDao().findVacancy(vacancyId),
            areaEntity = vacanciesDatabase.areaDao().getSelectedArea(vacancyId)
        )
    }

    override suspend fun addVacancy(vacancy: VacancyDetails) {
        vacanciesDatabase.vacancyDao().insertVacancy(converter.intoVacancyEntity(vacancy))
        vacanciesDatabase.areaDao().insertArea(converter.intoAreaEntity(vacancy))
        vacanciesDatabase.phoneDao().insertPhone(converter.intoPhoneEntity(vacancy))
        vacanciesDatabase.keySkillDao().insertKeySkill(converter.intoKeySkillEntity(vacancy))
    }

    override suspend fun deleteVacancy(vacancyId: Int) {
        vacanciesDatabase.vacancyDao().deleteVacancy(vacancyId)
        vacanciesDatabase.areaDao().deleteArea(vacancyId)
        vacanciesDatabase.phoneDao().deletePhone(vacancyId)
        vacanciesDatabase.keySkillDao().deleteKeySkill(vacancyId)
    }

    override fun listVacancies(): Flow<List<Vacancy>> = flow {
        val listVacancy = ArrayList<Vacancy>()
        vacanciesDatabase.vacancyDao().getSelectedVacancies().forEach { vacancyEntity ->
            listVacancy.add(
                converter.intoVacancy(
                    vacancy = vacancyEntity,
                    areas = vacanciesDatabase.areaDao().getSelectedArea(vacancyEntity.id.toInt())
                )
            )
        }
        emit(listVacancy)
    }

    override suspend fun hasLike(vacancyId: Int): Boolean {
        return vacanciesDatabase.vacancyDao().hasLike(vacancyId) > 0
    }
}
