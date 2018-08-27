package pksokolowski.github.com.threegoals.model

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE

@Entity(tableName = "reports",
        foreignKeys = [ForeignKey(
                entity = Goal::class,
                parentColumns = ["id"],
                childColumns = ["goal"],
                onDelete = CASCADE)],
        indices = [Index("goal")]
)
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