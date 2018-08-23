package pksokolowski.github.com.threegoals.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import pksokolowski.github.com.threegoals.model.Edition

@Dao
interface EditionsDao {
    @Query("SELECT * FROM editions ORDER BY id ASC")
    fun getEditions(): LiveData<MutableList<Edition>>

    @Insert
    fun insertEdition(edition: Edition): Long

}