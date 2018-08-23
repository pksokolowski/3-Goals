package pksokolowski.github.com.threegoals

import android.arch.lifecycle.ViewModel
import pksokolowski.github.com.threegoals.repository.DaysDataRepository
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val daysDataRepository: DaysDataRepository): ViewModel() {



}