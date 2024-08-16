package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.data.dto.components.Area
import ru.practicum.android.diploma.data.dto.components.Contacts
import ru.practicum.android.diploma.data.dto.components.CountryDto
import ru.practicum.android.diploma.data.dto.components.Employer
import ru.practicum.android.diploma.data.dto.components.Employment
import ru.practicum.android.diploma.data.dto.components.Experience
import ru.practicum.android.diploma.data.dto.components.KeySkill
import ru.practicum.android.diploma.data.dto.components.LogoUrls
import ru.practicum.android.diploma.data.dto.components.Phone
import ru.practicum.android.diploma.data.dto.components.RegionDto
import ru.practicum.android.diploma.data.dto.components.Salary
import ru.practicum.android.diploma.data.dto.components.Schedule
import ru.practicum.android.diploma.data.dto.components.SectorDto
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.domain.models.Sector
import ru.practicum.android.diploma.domain.models.VacanciesResponse
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

fun VacancyDto.toModel() = Vacancy(id, area.toModel(), employer?.toModel(), name, salary?.toModel())

fun SearchResponse.toModel() = VacanciesResponse(items.map { it.toModel() }, found, page, pages)

fun VacancyResponse.toModel() = VacancyDetails(
    id,
    area?.toModel(),
    employer?.toModel(),
    name,
    salary?.toModel(),
    description,
    employment?.toModel(),
    experience?.toModel(),
    contacts?.toModel(),
    schedule?.toModel(),
    keySkills?.map { it.toModel() },
    alternateUrl
)

fun Salary.toModel() = SalaryModel(currency, from, gross, to)

fun Employer.toModel() = EmployerModel(
    id,
    logoUrls?.toModel(),
    name,
    trusted,
    url,
    vacanciesUrl
)

fun LogoUrls.toModel() = LogoUrlModel(original, logo90, logo240)

fun Area.toModel(): AreaModel {
    val areas = areas?.map { it.toModel() }
    return AreaModel(id, name, countryId, areas)
}

fun Employment.toModel() = EmploymentModel(id, name)

fun Experience.toModel() = ExperienceModel(id, name)

fun Schedule.toModel() = ScheduleModel(id, name)

fun Phone.toModel() = PhonesModel(cityCode, comment, countryCode, formatted, number)

fun KeySkill.toModel() = KeySkillsModel(name)

fun Contacts.toModel(): ContactsModel {
    val phones = phones?.map { it.toModel() }
    return ContactsModel(email, name, phones)
}

fun CountryDto.toModel(): Country {
    return Country(
        id = this.id,
        name = this.name
    )
}

fun RegionDto.toModel(): Region {
    return Region(
        id = this.id,
        name = this.name,
        parentId = this.parentId
    )
}

fun List<SectorDto>.toSectorList(): List<Sector> {
    return this.map {
        Sector(
            id = it.id,
            name = it.name,
            isChecked = false
        )
    }
}

fun List<CountryDto>.toCountryList(): List<Country> {
    return this.map { it.toModel() }
}

fun List<RegionDto>.toRegionList(): List<Region> {
    return this.map { it.toModel() }
}

fun List<CountryDto>.toAllRegions(): List<Region> {
    return this.flatMap { it.areas.map { regionDto -> regionDto.toModel() } }
}
