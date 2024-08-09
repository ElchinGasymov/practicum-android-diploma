package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.entity.KeySkillEntity

@Dao
interface KeySkillDao {
    @Query("SELECT * FROM `key_skill_table` WHERE `key_skill_table`.`idVacancy` =:vacancyId")
    suspend fun getSelectedKeySkill(vacancyId: String): List<KeySkillEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeySkill(keySkillEntity: List<KeySkillEntity>)

    @Query("DELETE FROM `key_skill_table` WHERE idVacancy=:vacancyId")
    suspend fun deleteKeySkill(vacancyId: String)
}
