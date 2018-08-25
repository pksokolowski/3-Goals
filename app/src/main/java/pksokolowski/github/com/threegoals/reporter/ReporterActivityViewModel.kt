package pksokolowski.github.com.threegoals.reporter

import android.arch.lifecycle.ViewModel
import pksokolowski.github.com.threegoals.notifications.NotificationsManager
import pksokolowski.github.com.threegoals.repository.EditionsRepository
import pksokolowski.github.com.threegoals.repository.ReportsRepository
import javax.inject.Inject

class ReporterActivityViewModel @Inject constructor(private val reportsRepository: ReportsRepository, private val editionsRepository: EditionsRepository, private val notificationsManager: NotificationsManager): ViewModel() {

    fun cancelNotification(){
        notificationsManager.cancelNotification()
    }

    fun getEditionById(id: Long) = editionsRepository.getEditionById(id)
}