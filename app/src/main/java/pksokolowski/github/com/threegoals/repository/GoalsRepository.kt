package pksokolowski.github.com.threegoals.repository

import android.arch.lifecycle.LiveData
import pksokolowski.github.com.threegoals.data.GoalsDao
import pksokolowski.github.com.threegoals.model.Edition
import pksokolowski.github.com.threegoals.model.Goal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalsRepository @Inject constructor(private val goalsDao: GoalsDao) {

    fun getData(edition: Edition): MutableList<Goal> {
        return goalsDao.getGoals(edition.id)
    }

    fun insertGoals(goals: MutableList<Goal>){
        goalsDao.insertGoals(goals)
    }

}