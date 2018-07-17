package pksokolowski.github.com.threegoals

import org.junit.Test
import org.junit.Assert.*
import java.util.*

class TimeHelperTests {
    @Test
    fun yearString_iscorrect() {
        val stamp = 0L
        val yearString = TimeHelper.getYear(stamp)
        assertEquals("year string is incorrect", yearString, "1970")
    }
}