package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.entity.KeySkillEntity
import ru.practicum.android.diploma.data.db.entity.PhoneEntity
import ru.practicum.android.diploma.data.db.entity.VacancyEntity

@Dao
interface VacancyDao {
    @Query("SELECT * FROM `vacancy_table`")
    suspend fun getSelectedVacancies(): List<VacancyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyEntity)

    @Query("DELETE FROM `vacancy_table` WHERE id=:vacancyId")
    suspend fun deleteVacancy(vacancyId: Int)

    @Query("SELECT * FROM `vacancy_table` WHERE `vacancy_table`.id = :vacancyId")
    suspend fun findVacancy(vacancyId: Int): VacancyEntity

    @Query("SELECT COUNT(`vacancy_table`.`id`) FROM `vacancy_table` WHERE `vacancy_table`.`id` = :vacancyId;")
    suspend fun hasLike(vacancyId: Int)

    @Query("SELECT * FROM `phone_table` WHERE `phone_table`.`idVacancy` =:vacancyId")
    suspend fun getSelectedPhone(vacancyId: Int): List<PhoneEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhone(phoneEntity: PhoneEntity)

    @Query("DELETE FROM `phone_table` WHERE idVacancy=:vacancyId")
    suspend fun deletePhone(vacancyId: Int)

    @Query("SELECT * FROM `key_skill_table` WHERE `key_skill_table`.`idVacancy` =:vacancyId")
    suspend fun getSelectedKeySkill(vacancyId: Int): List<KeySkillEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeySkill(keySkillEntity: KeySkillEntity)

    @Query("DELETE FROM `key_skill_table` WHERE idVacancy=:vacancyId")
    suspend fun deleteKeySkill(vacancyId: Int)
}
