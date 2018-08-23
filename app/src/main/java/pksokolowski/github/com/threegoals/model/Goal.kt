package pksokolowski.github.com.threegoals.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "goals")
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

)