package pksokolowski.github.com.threegoals.repository

import pksokolowski.github.com.threegoals.data.ReportsDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportsRepository @Inject constructor(private val daysDao: ReportsDao) {

//    fun getData(): LiveData<DaysData> {
//        return daysDao.getInfo()
//    }
//
//    fun addDatum(info: Info){
//        daysDao.insertInfo(info)
//    }
}