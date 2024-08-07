package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.data.db.dao.VacancyDao

@Database(
    version = 1,
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

