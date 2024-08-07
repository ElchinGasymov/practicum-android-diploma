package ru.practicum.android.diploma.data.db.entity

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

class ConverterDb() {
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
            id = vacancy.id,
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
            id = vacancyEntity.id,
            area = AreaModel(
                id = vacancyEntity.idAreaModel,
                name = vacancyEntity.nameAreaModel,
                countryId = vacancyEntity.countryId,
                areas = intoArea(areaEntity)
            ),
            employer = EmployerModel(
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
            ),
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
}
