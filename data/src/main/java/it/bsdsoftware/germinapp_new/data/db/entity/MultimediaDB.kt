package it.bsdsoftware.germinapp_new.data.db.entity

import androidx.room.*

enum class MultimediaType {
    PHOTO,
    VIDEO
}

@Entity(
    tableName = "multimedia",
    foreignKeys = [ForeignKey(
        entity = RilevazioneDB::class,
        parentColumns = arrayOf("local_id"),
        childColumns = arrayOf("local_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class MultimediaDB(
    @TypeConverters(MultimediaTypeConverter::class)
    @ColumnInfo(name = "type") val type: MultimediaType,
    @ColumnInfo(name = "path") val path: String,
    @ColumnInfo(name = "to_sync") val toSync: Boolean,
    @ColumnInfo(name = "local_id") val localId: Long,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
)

data class RilevazioneWithMultimedia(
    @Embedded val rilevazione: RilevazioneDB,
    @Relation(
        parentColumn = "local_id",
        entityColumn = "local_id"
    )
    val multimedia: List<MultimediaDB>
)

class MultimediaTypeConverter {
    @TypeConverter
    fun toType(ordinal: Int?): MultimediaType? {
        return ordinal?.let { MultimediaType.values()[it] }
    }

    @TypeConverter
    fun toOrdinal(type: MultimediaType?): Int? {
        return type?.ordinal
    }
}
