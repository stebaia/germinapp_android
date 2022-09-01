package com.google.maps.android.compose.homepage

enum class Filters : Filter {
    ANNO {
        override val labelText: String
            get() = "Anno"

    },
    COLTIVATORE{
        override val labelText: String
            get() = "Coltivatore"

    },
    ARTICOLO{
        override val labelText: String
            get() = "Articolo"

    },
    LOTTO{
        override val labelText: String
            get() = "Lotto"

    },
    SPECIE{
        override val labelText: String
            get() = "Specie"

    },
    DESTINAZIONE{
        override val labelText: String
            get() = "Destinazione"

    },
    VARIETA{
        override val labelText: String
            get() = "Varietà"

    },
    COLTURA{
        override val labelText: String
            get() = "Coltura"

    }
}

interface Filter {
    val labelText: String
}