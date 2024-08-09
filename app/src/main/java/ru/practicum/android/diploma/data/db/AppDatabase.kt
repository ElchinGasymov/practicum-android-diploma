package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.data.db.dao.AreaDao
import ru.practicum.android.diploma.data.db.dao.KeySkillDao
import ru.practicum.android.diploma.data.db.dao.PhoneDao
import ru.practicum.android.diploma.data.db.dao.VacancyDao
import ru.practicum.android.diploma.data.db.entity.AreaEntity
import ru.practicum.android.diploma.data.db.entity.KeySkillEntity
import ru.practicum.android.diploma.data.db.entity.PhoneEntity
import ru.practicum.android.diploma.data.db.entity.VacancyEntity

@Database(
    version = 2,
    entities = [
        VacancyEntity::class,
        PhoneEntity::class,
        KeySkillEntity::class,
        AreaEntity::class,
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vacancyDao(): VacancyDao

    abstract fun phoneDao(): PhoneDao

    abstract fun keySkillDao(): KeySkillDao

    abstract fun areaDao(): AreaDao
}

