package com.google.maps.android.compose.homepage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import it.bsdsoftware.germinapp_new.domain.entities.*
import it.bsdsoftware.germinapp_new.ui.theme.Germinapp_newTheme

@Composable
private fun ContractPreviewItemPhone(
    item: Contract,
    details: () -> Unit,
    rilevazioni: () -> Unit
) {
    Column(
        Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = item.coltura.articolo.toString(),
            fontSize = 4.em,
            modifier = Modifier.padding(bottom = 6.dp),
        )
        Text(text = item.coltivatore.toString(), fontSize = 4.em, color = Color.DarkGray)
        Text(
            text = if (item.indirizzo.isNotBlank()) {
                item.indirizzo + ", "
            } else {
                ""
            } + item.localita,
            fontSize = 3.em,
            color = Color.DarkGray
        )
        Row(
            Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally)
        ) {
            Button(
                onClick = { details() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Dettagli")
            }
            Button(
                onClick = { rilevazioni() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color.Red
                )
            ) {
                Text(text = "Rilevazioni")
            }
        }
    }
}

@Composable
private fun ContractPreviewItemTablet(
    item: Contract,
    details: () -> Unit,
    rilevazioni: () -> Unit
) {
    Box(Modifier.fillMaxWidth()) {
        Column(
            Modifier
                .padding(20.dp)
                .fillMaxWidth(0.7f)
        ) {
            Text(
                text = item.coltura.articolo.toString(),
                fontSize = 3.em,
                modifier = Modifier.padding(bottom = 6.dp),
            )
            Text(text = item.coltivatore.toString(),
                fontSize = 2.em, color = Color(117,131,144))
            Text(
                text = if (item.indirizzo.isNotBlank()) {
                    item.indirizzo + ", "
                } else {
                    ""
                } + item.localita,
                fontSize = 2.em,
                color = Color(117,131,144)
            )
        }
        Column(
            Modifier
                .padding(20.dp)
                .fillMaxWidth(0.35f)
                .fillMaxHeight()
                .align(Alignment.CenterEnd),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.End)
            ) {
                Button(
                    modifier = Modifier
                        .width(150.dp),
                    shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp),
                    onClick = { details() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Dettagli")
                }
                Button(
                    shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                    modifier = Modifier
                        .width(150.dp),
                    onClick = { rilevazioni() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Red

                    )
                ) {
                    Text(text = "Rilevazioni")
                }
            }
        }
    }
}

@Composable
fun ContractPreviewItem(
    item: Contract,
    details: () -> Unit,
    rilevazioni: () -> Unit
) {
    Card(
        backgroundColor = Color(249, 249, 250),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        BoxWithConstraints {
            if (maxWidth < 500.dp) {
                ContractPreviewItemPhone(item, details, rilevazioni)
            } else {
                ContractPreviewItemTablet(item = item, details, rilevazioni)
            }
        }
    }
}

@Preview(widthDp = 800, heightDp = 500)
@Composable
fun DefaultPreviewContractsList() {
    Germinapp_newTheme {
        val contract = Contract(
            coltivatore = Item(
                "ColtivatoreId",
                "Coltivatore"
            ),
            podereId = "",
            indirizzo = "Indirizzo",
            localita = "Localita",
            telefono = "Telefono",
            mail = "Mail",
            zonaProduzione = "zonaProduzione",
            latitudine = "Latitudine",
            longitudine = "Longitudine",
            latitudine2 = "Latitudine2",
            longitudine2 = "Longitudine2",
            coltura = Coltura(
                articolo = Item(
                    "ArticoloIdkojihuigyuwefdhwdfjhswdjgfhuiashgfiuhrgfiojhearoighouiwerqhjg",
                    "Articoloqkòjwgjlehwgjhweigjklewrjgkljgwkjkgjkelwjgksdgjsalkjlkjfjeklgnlekwnhgjffbwegrwe4gweg"
                ),
                lotto = "Lotto",
                specie = Item(
                    "SpecieCodice",
                    "Specie"
                ),
                varieta = Item(
                    "VarietaCodice",
                    "Varieta"
                ),
                tipoRaccolta = Item(
                    "TipoTracCodice",
                    "TipoTrac"
                ),
                tipoLinea = Item(
                    "LineaCodice",
                    "Linea"
                ),
                sigla = Item(
                    "SiglaCodice",
                    "Sigla"
                ),
            ),
            prodotto = Caratteristiche(
                tipoProdotto = "tipo",
                coltura = "cultura",
                destinazione = "destinazione"
            ),
            coltivazione = Coltivazione(
                contratto = "contratto",
                quantitaAttesa = "qA",
                seminati = "seminati",
                dataSeminaFOP = "20/2/2020",
                dataSemina = "2020/20/1",
                nettiDopoDistruzione = "20.000",
            ),
            note = ""
        )

        ContractPreviewItem(item = contract, {}, {})
    }
}