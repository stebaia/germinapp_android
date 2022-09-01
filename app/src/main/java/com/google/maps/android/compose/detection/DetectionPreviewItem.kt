package com.google.maps.android.compose.detection

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import it.bsdsoftware.germinapp_new.detail_section.getDate
import it.bsdsoftware.germinapp_new.detail_section.getTime
import it.bsdsoftware.germinapp_new.domain.entities.*
import it.bsdsoftware.germinapp_new.ui.theme.Germinapp_newTheme


@Composable
private fun DetectionPreviewItemPhone(
    item: Detection,
    details: () -> Unit
) {
    Row(

    ){
        Column(
            Modifier
                .padding(10.dp)

        ) {
            Row {
                Text(
                    text = getDate(item.data),
                    fontSize = 3.em,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    text = getTime(item.endTime),
                    fontSize = 3.em,
                    modifier = Modifier
                        .fillMaxWidth(0.15f)
                )
                Text(
                    text = "Utente: " + item.user.username,
                    fontSize = 3.em,
                    modifier = Modifier
                        .padding(10.dp, 0.dp)
                )
            }
            Text(
                text = item.contract.coltivatore.toString(),
                fontSize = 3.5.em,
            )
            Text(
                text = if (item.contract.indirizzo.isNotBlank()) {
                    item.contract.indirizzo + ", "
                } else {
                    ""
                } + item.contract.localita,
                fontSize = 3.5.em
            )
            Text(
                text = item.contract.coltura.articolo.toString(),
                fontSize = 3.5.em,
                modifier = Modifier.padding(top = 4.dp),
            )
    }
        Button(

            onClick = { details() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Red,
                contentColor = Color.White
            ),


            ) {
            Text(text = "Dettagli")
        }

    }
}

@Composable
private fun DetectionPreviewItemTablet(
    item: Detection,
    details: () -> Unit
) {

        Box(Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column(
                    Modifier
                        .padding(20.dp)
                        .fillMaxWidth(0.65f)
                ) {
                    Text(

                        text = item.contract.coltura.articolo.toString(),
                        fontSize = 3.5.em,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        Text(
                            text = getDate(item.data),
                            fontSize = 3.em,
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = getTime(item.endTime),
                            fontSize = 3.em,
                        )
                        Text(
                            text = "Utente: " + item.user.username,
                            fontSize = 3.em,
                            modifier = Modifier
                                .padding(10.dp, 0.dp)
                        )

                    }
                    Text(
                        text = item
                            .contract
                            .coltivatore
                            .toString(),
                        fontSize = 3.5.em,
                    )


                }
                Column(modifier = Modifier.fillMaxSize().padding(top = 50.dp), verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Button(
                        modifier = Modifier
                            .width(150.dp)
                            .height(46.dp)

                           ,
                        shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp, bottomEnd = 20.dp, bottomStart = 20.dp),
                        onClick = { details() },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Dettagli")
                    }
                }

            }




        }
}

@Composable
fun DetectionPreviewItem(
    item: Detection,
    details: () -> Unit
) {
    Card(
        backgroundColor = Color(249, 249, 250),
        shape = RoundedCornerShape(20.dp),

        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        BoxWithConstraints {
            if (maxWidth < 500.dp) {
                DetectionPreviewItemPhone(item, details)
            } else {
                DetectionPreviewItemTablet(item = item, details)
            }
        }
    }
}

@Preview(widthDp = 400, heightDp = 500)
@Composable
fun DefaultDetection() {
    Germinapp_newTheme {
        val detection = Detection(
            remoteId = null,
            localId = null,
            user = User(1,"user", "", "", null),
            data = "2021-01-07T00:00:00",
            startTime = "2021-01-07T16:04:00",
            endTime = "2021-01-07T16:07:00",
            images = listOf(),
            note = "",
            phytosanitaries = listOf(),
            contract = Contract(
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
        )
        //val selectViewModel = SelectViewModel()
        DetectionPreviewItem(detection) {}
    }
}