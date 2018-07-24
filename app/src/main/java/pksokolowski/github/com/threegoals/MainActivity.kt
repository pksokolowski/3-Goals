package pksokolowski.github.com.threegoals

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import pksokolowski.github.com.threegoals.editor.EditorDialogFragment
import pksokolowski.github.com.threegoals.notifications.NotificationsManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationsManager.createNotificationChannels(this)
        setContentView(R.layout.activity_main)

        pieChart.noDataMessage = getString(R.string.main_pie_no_data_message)
        pieChart.mainColor = ContextCompat.getColor(this, R.color.colorPrimary)
        pieChart.notSelectedColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

        // assign UI actions
        val edition = EditionsManager.getCurrentEdition(this)
        if(edition != null) {
            editor_imageview.setOnClickListener { EditorDialogFragment.showDialog(this, edition) }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}
