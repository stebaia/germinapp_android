package it.bsdsoftware.germinapp_new.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.bsdsoftware.germinapp_new.data.db.entity.*

@Dao
interface GerminaDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllCategory(vararg category: PhytosanitaryCategoryDB)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllSpecies(vararg species: PhytosanitarySpeciesDB)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllEntity(vararg category: PhytosanitaryEntityDB)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertContracts(vararg contract: ContractDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetections(vararg rilevazioni: RilevazioneDB): Array<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMultimedias(vararg multimedia: MultimediaDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetectedPhytosanitary(vararg detection: DetectedPhytosanitaryDB)

    @Query("SELECT * FROM contracts")
    fun getContract(): List<ContractDB>

    @Query("SELECT * FROM detections WHERE remote_id = :value")
    fun getDetection(value: Long): RilevazioneDB

    @Query(
        "SELECT * FROM detections " +
                "WHERE remote_id = null " +
                "OR modificato = 1 "
    )
    fun getDetectionsToUpdate(): List<RilevazioneDB>

    @Query(
        "SELECT * FROM detections " +
                "WHERE detections.id_articolo = :idArticolo " +
                "AND detections.id_coltivatore = :idColtivatore " +
                "AND detections.id_podere = :podere " +
                "AND detections.lotto = :lotto "
    )
    fun getDetections(
        idArticolo: String,
        idColtivatore: String,
        lotto: String,
        podere: String
    ): List<RilevazioneDB>

    @Query(
        "SELECT * " +
                "FROM detected_phytosanitaries " +
                "WHERE detected_phytosanitaries.local_id = :localId "
    )
    fun getDetectedPhytosanitaries(
        localId: Long
    ): List<DetectedPhytosanitaryDB>

    @Query("SELECT * FROM phytosanitary_entities INNER JOIN phytosanitary_categories ON phytosanitary_entities.category_id = phytosanitary_categories.id")
    fun getPhytosanitaries(): List<CategoryWithEntity>

    @Query(
        "SELECT phytosanitary_entities.* " +
                "FROM phytosanitary_entities " +
                "INNER JOIN phytosanitary_species ON phytosanitary_entities.id = phytosanitary_species.entity_id " +
                "WHERE phytosanitary_species.specie = :specie"
    )
    fun getPhytosanitariesForSpecie(specie: String): List<PhytosanitaryEntityDB>

    @Query(
        "SELECT * FROM phytosanitary_categories "
    )
    fun getPhytosanitaryCategories(): List<PhytosanitaryCategoryDB>

    @Query(
        "SELECT * " +
                "FROM multimedia " +
                "WHERE local_id = :localId "
    )
    fun getImages(
        localId: Long
    ): List<MultimediaDB>

    @Query(
        "SELECT * FROM detections " +
                "WHERE detections.remote_id IS null " +
                "OR detections.modificato "
    )
    fun getToSynchronize(): List<RilevazioneDB>
}