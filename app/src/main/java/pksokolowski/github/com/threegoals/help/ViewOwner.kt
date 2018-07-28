package pksokolowski.github.com.threegoals.help

import android.view.View

interface ViewOwner {
    fun owns(view: View): Boolean
}
