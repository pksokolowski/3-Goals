package pksokolowski.github.com.threegoals.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "reports")
data class Report(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val id: Long,

        @ColumnInfo(name = "dayNum")
        val dayNum: Int,

        @ColumnInfo(name = "timeStamp")
        val timeStamp: Long,

        @ColumnInfo(name = "scoreTryingHard")
        val scoreTryingHard: Int,

        @ColumnInfo(name = "scorePositives")
        val scorePositives: Int,

        @ColumnInfo(name = "goal")
        val goal: Long

)