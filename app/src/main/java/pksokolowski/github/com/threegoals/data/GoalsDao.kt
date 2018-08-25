package pksokolowski.github.com.threegoals.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import pksokolowski.github.com.threegoals.model.Goal

@Dao
interface GoalsDao {
    @Query("SELECT * FROM goals WHERE edition = :editionId ORDER BY position ASC")
    fun getGoals(editionId: Long): MutableList<Goal>

    @Insert
    fun insertGoal(goal: Goal): Long

    @Transaction
    fun insertGoals(goals: MutableList<Goal>) {
        goals.forEach { insertGoal(it) }
    }

    @Update
    fun updateGoal(goal: Goal)
}