package pksokolowski.github.com.threegoals.repository

import android.arch.lifecycle.LiveData
import pksokolowski.github.com.threegoals.data.EditionsDao
import pksokolowski.github.com.threegoals.model.DaysData
import pksokolowski.github.com.threegoals.model.Edition
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditionsRepository @Inject constructor(private val editionsDao: EditionsDao) {

//    fun getData(): LiveData<DaysData> {
//        return editionsDao.getInfo()
//    }
//
//    fun addDatum(edition: Edition){
//        editionsDao.insertInfo(info)
//    }
}