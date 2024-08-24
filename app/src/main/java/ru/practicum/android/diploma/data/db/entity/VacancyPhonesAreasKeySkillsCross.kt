package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity

@Entity(primaryKeys = ["idVacancyBd", "idAreaBd", "idPhoneBd", "idKeySkillBd"])
data class VacancyPhonesAreasKeySkillsCross(
    val idVacancyBd: Int,
    val idAreaBd: Int,
    val idPhoneBd: Int,
    val idKeySkillBd: Int,
)
