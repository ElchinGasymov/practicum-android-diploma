package ru.practicum.android.diploma.data.db.converters

import ru.practicum.android.diploma.data.db.entity.AreaEntity
import ru.practicum.android.diploma.data.db.entity.KeySkillEntity
import ru.practicum.android.diploma.data.db.entity.PhoneEntity
import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.domain.models.VacancyDetails

class ConverterIntoEntity {
    fun intoVacancyEntity(vacancy: VacancyDetails): VacancyEntity {
        return VacancyEntity(
            id = vacancy.id.toInt(),
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
        val listPhone = ArrayList<PhoneEntity>()
        vacancy.contacts?.phones?.forEach { phonesModel ->
            listPhone.add(
                PhoneEntity(
                    idVacancy = vacancy.id.toInt(),
                    cityCode = phonesModel.cityCode,
                    comment = phonesModel.comment,
                    countryCode = phonesModel.countryCode,
                    formatted = phonesModel.formatted,
                    number = phonesModel.number
                )
            )
        }
        return listPhone
    }

    fun intoKeySkillEntity(vacancy: VacancyDetails): List<KeySkillEntity> {
        val listKey = ArrayList<KeySkillEntity>()
        vacancy.keySkills?.forEach { keySkillsModel ->
            listKey.add(
                KeySkillEntity(
                    idVacancy = vacancy.id.toInt(),
                    name = keySkillsModel.name
                )
            )
        }
        return listKey
    }

    fun intoAreaEntity(vacancy: VacancyDetails): List<AreaEntity> {
        val listArea = ArrayList<AreaEntity>()
        vacancy.area?.areas?.forEach { area ->
            listArea.add(
                AreaEntity(
                    idVacancy = vacancy.id.toInt(),
                    idArea = area.id,
                    name = area.name,
                    countryId = area.countryId
                )
            )
        }
        return listArea
    }
}
