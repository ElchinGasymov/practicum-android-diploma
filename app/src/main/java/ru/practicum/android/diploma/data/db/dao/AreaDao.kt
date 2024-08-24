package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.entity.AreaEntity

@Dao
interface AreaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArea(areaEntity: List<AreaEntity>)

    @Query("DELETE FROM `area_table` WHERE idVacancy=:vacancyId")
    suspend fun deleteArea(vacancyId: String)
}
