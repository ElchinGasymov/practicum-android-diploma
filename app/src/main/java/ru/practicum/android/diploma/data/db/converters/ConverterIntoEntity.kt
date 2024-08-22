package ru.practicum.android.diploma.data.db.converters

import ru.practicum.android.diploma.data.db.entity.AreaEntity
import ru.practicum.android.diploma.data.db.entity.KeySkillEntity
import ru.practicum.android.diploma.data.db.entity.PhoneEntity
import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.domain.models.VacancyDetails

class ConverterIntoEntity {
    fun intoVacancyEntity(vacancy: VacancyDetails): VacancyEntity {
        return VacancyEntity(
            id = vacancy.id,
            idAreaModel = vacancy.area?.id,
            nameAreaModel = vacancy.area?.name,
            countryId = vacancy.area?.countryId,
            idEmploymentModel = vacancy.employment?.id,
            original = vacancy.employer?.logoUrls?.original,
            logo90 = vacancy.employer?.logoUrls?.logo90,
            logo240 = vacancy.employer?.logoUrls?.logo240,
            nameEmployerModel = vacancy.employer?.name,
            trusted = vacancy.employer?.trusted,
            url = vacancy.employer?.url,
            vacanciesUrl = vacancy.employer?.vacanciesUrl,
            name = vacancy.name,
            currency = vacancy.salary?.currency,
            from = vacancy.salary?.from,
            gross = vacancy.salary?.gross,
            to = vacancy.salary?.to,
            description = vacancy.description,
            idEmployerModel = vacancy.employment?.id,
            nameEmploymentModel = vacancy.employment?.name,
            idExperienceModel = vacancy.experience?.id,
            nameExperienceModel = vacancy.experience?.name,
            email = vacancy.contacts?.email,
            nameContactsModel = vacancy.contacts?.name,
            idScheduleModel = vacancy.schedule?.id,
            nameScheduleModel = vacancy.schedule?.name,
            alternateUrl = vacancy.alternateUrl
        )
    }

    fun intoPhoneEntity(vacancy: VacancyDetails): List<PhoneEntity> {
        return buildList {
            vacancy.contacts?.phones?.forEach { phonesModel ->
                add(
                    PhoneEntity(
                        idVacancy = vacancy.id,
                        cityCode = phonesModel.cityCode,
                        comment = phonesModel.comment,
                        countryCode = phonesModel.countryCode,
                        formatted = phonesModel.formatted,
                        number = phonesModel.number
                    )
                )
            }
        }
    }

    fun intoKeySkillEntity(vacancy: VacancyDetails): List<KeySkillEntity> {
        return buildList {
            vacancy.keySkills?.forEach { keySkillsModel ->
                add(
                    KeySkillEntity(
                        idVacancy = vacancy.id,
                        name = keySkillsModel.name
                    )
                )
            }
        }
    }

    fun intoAreaEntity(vacancy: VacancyDetails): List<AreaEntity> {
        return buildList {
            vacancy.area?.areas?.forEach { area ->
                add(
                    AreaEntity(
                        idVacancy = vacancy.id,
                        idArea = area.id,
                        name = area.name,
                        countryId = area.countryId
                    )
                )
            }
        }
    }
}
