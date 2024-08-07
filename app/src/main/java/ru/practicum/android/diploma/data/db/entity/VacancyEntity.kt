package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "vacancy_table")
data class VacancyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val employer: Employer,
    val salary: Salary?,
    val area: Area,
    @SerializedName("alternate_url")
    val alternateUrl: String,
    val description: String,
    val employment: Employment?,
    val experience: Experience?,
    val schedule: Schedule?,
    val contacts: Contacts?,
    @SerializedName("key_skills")
    val keySkills: List<KeySkill>,
    val address: Address?
)

