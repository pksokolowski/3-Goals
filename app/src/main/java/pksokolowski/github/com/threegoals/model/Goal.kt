package pksokolowski.github.com.threegoals.model

import android.arch.persistence.room.*

@Entity(tableName = "goals",
        foreignKeys = [ForeignKey(
                entity = Edition::class,
                parentColumns = ["id"],
                childColumns = ["edition"],
                onDelete = ForeignKey.CASCADE)],
        indices = [Index("edition")]
)
data class Goal(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val id: Long,

        @ColumnInfo(name = "name")
        val name: String,

        @ColumnInfo(name = "initial")
        val initial: String,

        @ColumnInfo(name = "position")
        val position: Int,

        @ColumnInfo(name = "edition")
        val edition: Long

) {
    fun changeCustomName(name: String): Goal {
        return Goal(id, name, initial, position, edition)
    }
}