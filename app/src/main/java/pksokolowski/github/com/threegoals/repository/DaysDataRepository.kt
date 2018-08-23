package pksokolowski.github.com.threegoals.repository

import android.arch.lifecycle.LiveData
import pksokolowski.github.com.threegoals.data.DaysDataDao
import pksokolowski.github.com.threegoals.model.DaysData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DaysDataRepository @Inject constructor(private val daysDao: DaysDataDao) {

//    fun getData(): LiveData<DaysData> {
//        return daysDao.getInfo()
//    }
//
//    fun addDatum(info: Info){
//        daysDao.insertInfo(info)
//    }
}