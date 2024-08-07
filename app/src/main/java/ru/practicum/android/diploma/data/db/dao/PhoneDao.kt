package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.entity.PhoneEntity

@Dao
interface PhoneDao {
    @Query("SELECT * FROM `phone_table` WHERE `phone_table`.`idVacancy` =:vacancyId")
    suspend fun getSelectedPhone(vacancyId: Int): List<PhoneEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhone(phoneEntity: PhoneEntity)

    @Query("DELETE FROM `phone_table` WHERE idVacancy=:vacancyId")
    suspend fun deletePhone(vacancyId: Int)
}
