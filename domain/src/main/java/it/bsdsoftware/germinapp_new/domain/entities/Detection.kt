package it.bsdsoftware.germinapp_new.domain.entities

data class Detection(
    val contract: Contract,
    val remoteId: Long?,
    var localId: Long?,
    val user: User,
    val data: String,
    val startTime: String,
    val endTime: String,
    val note: String,
    val phytosanitaries: List<Phytosanitary>,
    var images: List<Pair<String, Boolean>>,
    val new: Boolean = false,
    val changed: Boolean = false
)
