package pksokolowski.github.com.threegoals.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import pksokolowski.github.com.threegoals.model.Report


@Dao
interface ReportsDao {

    @Update
    fun updateReports(reports: List<Report>)

    @Insert
    fun insertReports(reports: List<Report>)

    @Query("SELECT reports.id, dayNum, timeStamp, scoreTryingHard, scorePositives, goal FROM reports JOIN goals ON goals.id = reports.goal WHERE edition = :editionId ORDER BY reports.id ASC")
    fun getReports(editionId: Long): LiveData<MutableList<Report>>

    @Query("SELECT reports.id, dayNum, timeStamp, scoreTryingHard, scorePositives, goal FROM reports JOIN goals ON goals.id = reports.goal WHERE edition = :editionId AND reports.dayNum = :dayNumber ORDER BY reports.id ASC")
    fun getReportsForDay(editionId: Long, dayNumber: Int): MutableList<Report>


}