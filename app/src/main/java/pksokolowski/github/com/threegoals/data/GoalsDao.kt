package pksokolowski.github.com.threegoals.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import pksokolowski.github.com.threegoals.model.Goal

@Dao
interface GoalsDao {
    @Query("SELECT * FROM goals WHERE edition = :editionId ORDER BY position ASC")
    fun getGoals(editionId: Long): LiveData<MutableList<Goal>>

    @Insert
    fun insertGoal(goal: Goal): Long

    @Transaction
    fun insertGoals(goals: MutableList<Goal>) {
        goals.forEach { insertGoal(it) }
    }
}