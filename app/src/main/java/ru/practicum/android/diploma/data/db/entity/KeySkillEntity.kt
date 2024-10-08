package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "key_skill_table")
data class KeySkillEntity(
    @PrimaryKey(autoGenerate = true)
    val idKeySkillBd: Int = 0,
    val idVacancy: String,
    val name: String?
)
