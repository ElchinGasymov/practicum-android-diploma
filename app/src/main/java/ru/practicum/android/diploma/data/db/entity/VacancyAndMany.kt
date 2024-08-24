package ru.practicum.android.diploma.data.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class VacancyAndMany(
    @Embedded val vacancy: VacancyEntity,
    @Relation(
        parentColumn = "idVacancyBd",
        entityColumn = "idPhoneBd",
        associateBy = Junction(VacancyPhonesAreasKeySkillsCross::class)
    )
    val phones: List<PhoneEntity>,
    @Relation(
        parentColumn = "idVacancyBd",
        entityColumn = "idAreaBd",
        associateBy = Junction(VacancyPhonesAreasKeySkillsCross::class)
    )
    val areas: List<AreaEntity>,
    @Relation(
        parentColumn = "idVacancyBd",
        entityColumn = "idKeySkillBd",
    )
    val keySkills: List<KeySkillEntity>
)
