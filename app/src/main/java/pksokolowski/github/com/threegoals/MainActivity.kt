package pksokolowski.github.com.threegoals

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import pksokolowski.github.com.threegoals.notifications.NotificationsManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationsManager.createNotificationChannels(this)
        setContentView(R.layout.activity_main)
    }
}
