package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
}
