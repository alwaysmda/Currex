package data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import domain.model.Rate


@Database(entities = [Rate::class], version = 1)
abstract class BalanceDatabase : RoomDatabase() {
    abstract fun getRateDao(): RateDao
}