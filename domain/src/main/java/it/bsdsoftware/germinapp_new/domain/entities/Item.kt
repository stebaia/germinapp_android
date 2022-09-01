package it.bsdsoftware.germinapp_new.domain.entities

data class Item(val id: String, val value: String) {

    override fun toString(): String {
        return "$id - $value";
    }

    companion object {
        fun factory(values: String): Item {
            val tmp = values.split(" - ")
            return Item(tmp[0], tmp[1])
        }
    }
}