package it.bsdsoftware.germinapp_new.domain.entities

data class Contract(
    val coltivatore: Item,
    val indirizzo: String,
    val podereId: String,
    val localita: String,
    val telefono: String,
    val mail: String,
    val zonaProduzione: String,
    val latitudine: String,
    val longitudine: String,
    val latitudine2: String,
    val longitudine2: String,
    var coltura: Coltura,
    val prodotto: Caratteristiche,
    val coltivazione: Coltivazione,
    val note: String
) {
}
