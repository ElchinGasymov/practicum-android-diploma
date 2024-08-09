package ru.practicum.android.diploma.data.db.converters

import ru.practicum.android.diploma.data.db.entity.AreaEntity
import ru.practicum.android.diploma.data.db.entity.KeySkillEntity
import ru.practicum.android.diploma.data.db.entity.PhoneEntity
import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.domain.models.components.AreaModel
import ru.practicum.android.diploma.domain.models.components.ContactsModel
import ru.practicum.android.diploma.domain.models.components.EmployerModel
import ru.practicum.android.diploma.domain.models.components.EmploymentModel
import ru.practicum.android.diploma.domain.models.components.ExperienceModel
import ru.practicum.android.diploma.domain.models.components.KeySkillsModel
import ru.practicum.android.diploma.domain.models.components.LogoUrlModel
import ru.practicum.android.diploma.domain.models.components.PhonesModel
import ru.practicum.android.diploma.domain.models.components.SalaryModel
import ru.practicum.android.diploma.domain.models.components.ScheduleModel

class ConverterIntoModel {

    fun intoPhone(phones: List<PhoneEntity>): List<PhonesModel> {
        val listPhone = ArrayList<PhonesModel>()
        phones.forEach { phonesEntity ->
            listPhone.add(
                PhonesModel(
                    cityCode = phonesEntity.cityCode,
                    comment = phonesEntity.comment,
                    countryCode = phonesEntity.countryCode,
                    formatted = phonesEntity.formatted,
                    number = phonesEntity.number
                )
            )
        }
        return listPhone
    }

    fun intoKeySkill(keySkill: List<KeySkillEntity>): List<KeySkillsModel> {
        val listKey = ArrayList<KeySkillsModel>()
        keySkill.forEach { keySkillsEntity ->
            listKey.add(
                KeySkillsModel(name = keySkillsEntity.name)
            )
        }
        return listKey
    }

    fun intoArea(areEntity: List<AreaEntity>): List<AreaModel> {
        val listArea = ArrayList<AreaModel>()
        areEntity.forEach { areEntity ->
            listArea.add(
                AreaModel(
                    id = areEntity.idArea,
                    name = areEntity.name,
                    countryId = areEntity.countryId,
                    areas = emptyList()
                )
            )
        }
        return listArea
    }

    fun intoVacancy(
        vacancy: VacancyEntity,
        areas: List<AreaEntity>
    ): Vacancy {
        return Vacancy(
            id = vacancy.id.toString(),
            area = AreaModel(
                id = vacancy.idAreaModel,
                name = vacancy.nameAreaModel,
                countryId = vacancy.countryId,
                areas = intoArea(areas)
            ),
            employer = EmployerModel(
                id = vacancy.idEmployerModel,
                logoUrls = LogoUrlModel(
                    original = vacancy.original,
                    logo90 = vacancy.logo90,
                    logo240 = vacancy.logo240
                ),
                name = vacancy.nameEmployerModel,
                trusted = vacancy.trusted,
                url = vacancy.url,
                vacanciesUrl = vacancy.vacanciesUrl,
            ),
            name = vacancy.name,
            salary = SalaryModel(
                currency = vacancy.currency,
                from = vacancy.from,
                gross = vacancy.gross,
                to = vacancy.to
            )
        )

    }

    fun intoVacancyDetail(
        vacancyEntity: VacancyEntity,
        listPhone: List<PhoneEntity>,
        listKey: List<KeySkillEntity>,
        areaEntity: List<AreaEntity>
    ): VacancyDetails {
        return VacancyDetails(
            id = vacancyEntity.id.toString(),
            area = AreaModel(
                id = vacancyEntity.idAreaModel,
                name = vacancyEntity.nameAreaModel,
                countryId = vacancyEntity.countryId,
                areas = intoArea(areaEntity)
            ),
            employer = getEmployerModel(vacancyEntity),
            name = vacancyEntity.name,
            salary = SalaryModel(
                currency = vacancyEntity.currency,
                from = vacancyEntity.from,
                gross = vacancyEntity.gross,
                to = vacancyEntity.to
            ),
            description = vacancyEntity.description,
            employment = EmploymentModel(
                id = vacancyEntity.idEmploymentModel,
                name = vacancyEntity.nameEmploymentModel
            ),
            experience = ExperienceModel(
                id = vacancyEntity.idExperienceModel,
                name = vacancyEntity.nameExperienceModel
            ),
            contacts = ContactsModel(
                email = vacancyEntity.email,
                name = vacancyEntity.nameContactsModel,
                phones = intoPhone(listPhone)
            ),
            schedule = ScheduleModel(
                id = vacancyEntity.idScheduleModel,
                name = vacancyEntity.nameScheduleModel
            ),
            keySkills = intoKeySkill(listKey),
            alternateUrl = vacancyEntity.alternateUrl
        )
    }

    private fun getEmployerModel(
        vacancyEntity: VacancyEntity
    ): EmployerModel {
        return EmployerModel(
            id = vacancyEntity.idEmployerModel,
            logoUrls = LogoUrlModel(
                original = vacancyEntity.original,
                logo90 = vacancyEntity.logo90,
                logo240 = vacancyEntity.logo240,
            ),
            name = vacancyEntity.nameEmployerModel,
            trusted = vacancyEntity.trusted,
            url = vacancyEntity.url,
            vacanciesUrl = vacancyEntity.vacanciesUrl
        )
    }
}
