package pksokolowski.github.com.threegoals.utils

import android.view.View
import android.view.Window
import android.view.WindowManager

/**
 * This class is used to offer consistent fullscreen, immersive experience across the app.
 * It has two functions, each to be run on a specific callback of a given activity.
 */
class ImmersiveFullscreenHelper {
    companion object {
        /**
         * Call this in Activity's onCreate callback
         */
        fun onCreateSetup(window: Window) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        /**
         * Call this in Activity's onWindowFocusChanged callback
         */
        fun onWindowFocusChangedSetup(window: Window, hasFocus: Boolean) {
            if (!hasFocus) return

            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        }
    }
}