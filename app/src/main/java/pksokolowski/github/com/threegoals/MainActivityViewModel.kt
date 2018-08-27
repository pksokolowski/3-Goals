package pksokolowski.github.com.threegoals

import android.arch.lifecycle.ViewModel
import pksokolowski.github.com.threegoals.model.Edition
import pksokolowski.github.com.threegoals.repository.EditionsRepository
import pksokolowski.github.com.threegoals.repository.ReportsRepository
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val reportsRepository: ReportsRepository, private val editionsRepository: EditionsRepository) : ViewModel() {

    private var selectedEdition = editionsRepository.getLatestEdition()
    private val daysData = reportsRepository.getDaysData(selectedEdition)

    private val editions = editionsRepository.getAllEditionsAsLiveData()

    var currentChartSelection = 1

    fun getData() = daysData

    fun getEditions() = editions

    fun getCurrentEdition(): Edition? {
        return editionsRepository.getCurrentEdition()
    }

    fun selectEdition(edition: Edition) {
        selectedEdition = edition
        reportsRepository.getDaysData(selectedEdition)
    }

    fun startNewEdition() {
        val newEdition = editionsRepository.createEdition()
        selectEdition(newEdition)
    }
}