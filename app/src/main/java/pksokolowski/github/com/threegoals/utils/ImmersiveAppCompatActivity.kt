package pksokolowski.github.com.threegoals.utils

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity

abstract class ImmersiveAppCompatActivity: AppCompatActivity(){
     override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        ImmersiveFullscreenHelper.onCreateSetup(window)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersiveFullscreenHelper.onCreateSetup(window)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        ImmersiveFullscreenHelper.onWindowFocusChangedSetup(window, hasFocus)
    }
}