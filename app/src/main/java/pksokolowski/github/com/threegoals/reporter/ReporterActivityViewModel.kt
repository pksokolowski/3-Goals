package pksokolowski.github.com.threegoals.reporter

import android.arch.lifecycle.ViewModel
import pksokolowski.github.com.threegoals.repository.EditionsRepository
import pksokolowski.github.com.threegoals.repository.ReportsRepository
import javax.inject.Inject

class ReporterActivityViewModel @Inject constructor(private val reportsRepository: ReportsRepository, private val editionsRepository: EditionsRepository): ViewModel() {


    fun getEditionById(id: Long) = editionsRepository.getEditionById(id)
}