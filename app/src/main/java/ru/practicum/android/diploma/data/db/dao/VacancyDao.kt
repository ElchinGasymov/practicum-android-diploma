package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.practicum.android.diploma.data.db.entity.VacancyAndMany
import ru.practicum.android.diploma.data.db.entity.VacancyEntity

@Dao
interface VacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyEntity)

    @Query("DELETE FROM `vacancy_table` WHERE id=:vacancyId ")
    suspend fun deleteVacancy(vacancyId: String)

    @Query("SELECT COUNT(`vacancy_table`.`id`) FROM `vacancy_table` WHERE `vacancy_table`.`id` = :vacancyId;")
    suspend fun hasLike(vacancyId: String): Int

    @Query("SELECT * FROM `vacancy_table` WHERE `vacancy_table`.id = :vacancyId")
    suspend fun findVacancy(vacancyId: String): VacancyAndMany

    @Transaction
    @Query("SELECT * FROM vacancy_table")
    suspend fun getSelectedVacancies(): List<VacancyAndMany>

}
