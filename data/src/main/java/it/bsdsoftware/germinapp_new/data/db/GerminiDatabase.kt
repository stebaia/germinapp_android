package it.bsdsoftware.germinapp_new.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import it.bsdsoftware.germinapp_new.data.db.dao.GerminaDAO
import it.bsdsoftware.germinapp_new.data.db.entity.*

@Database(
    entities = [
        PhytosanitaryCategoryDB::class,
        PhytosanitaryEntityDB::class,
        ContractDB::class,
        RilevazioneDB::class,
        MultimediaDB::class,
        DetectedPhytosanitaryDB::class,
        PhytosanitarySpeciesDB::class
    ], version = 1
)
@TypeConverters(MultimediaTypeConverter::class)
abstract class GerminiDatabase : RoomDatabase() {
    abstract fun germinaDAO(): GerminaDAO

    companion object {
        @Volatile
        private var INSTANCE: GerminiDatabase? = null

        fun getDatabase(appContext: Context): GerminiDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    appContext, GerminiDatabase::class.java,
                    "GerminaDB"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}