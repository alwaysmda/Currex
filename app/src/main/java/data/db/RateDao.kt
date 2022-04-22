package data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import domain.model.Rate

@Dao
interface RateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(list: ArrayList<Rate>)

    @Query("SELECT * FROM tbl_balance")
    suspend fun getBalanceList(): List<Rate>
}