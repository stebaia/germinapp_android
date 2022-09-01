package it.bsdsoftware.germinapp_new.detail_section

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Tune

import com.google.maps.android.compose.R
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import it.bsdsoftware.germinapp_new.domain.entities.*
import it.bsdsoftware.germinapp_new.ui.navigation.NavigationHome
import it.bsdsoftware.germinapp_new.ui.theme.Germinapp_newTheme

@Composable
fun <T> DetailsRow(label: String, item: T?, fontSize: TextUnit = 20.sp, labelColor: Color = Color(117,131,144)) {
    Row(
        modifier = Modifier.height(36.dp),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Text(
            text = "$label :",
            fontSize = fontSize,
            modifier = Modifier
                .fillMaxWidth(0.25f),
            color = labelColor
        )
        item
            ?.let {
                Text(
                    text = it.toString(),
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold
                )
            }
    }
}


@Composable
fun ContractDetails(
    selectViewModelDetection: SelectViewModelContract,
    navControllerHome: NavHostController,
    ) {
    Row(modifier = Modifier.fillMaxWidth(),) {
        Box(
            modifier = Modifier.padding(all = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = colorResource(id = R.color.element_ligth_gray))
            ) {
                IconButton(
                    onClick = {
                        navControllerHome.navigate(NavigationHome.CONTRATTI.name)
                    }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "back",
                        modifier = Modifier
                            .padding(all = 22.dp)
                            .size(34.dp),
                        tint = Color.Red

                    )
                }
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp, 5.dp)
                .verticalScroll(ScrollState(0)),
            horizontalAlignment = Alignment.Start
        ) {
            var contract by remember { mutableStateOf(selectViewModelDetection.selected.value) }
            selectViewModelDetection
                .selected
                .observe(LocalLifecycleOwner.current) {
                    contract = it
                }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .clickable { },
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),


                    horizontalAlignment = Alignment.Start
                ) {
                    Box(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Contratto",
                            color = Color.Black,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(colorResource(id = R.color.element_ligth_gray))
                    )
                    Box(modifier = Modifier.padding(20.dp)) {
                        Column() {
                            DetailsRow(
                                label = "Podere",
                                item = contract
                                    ?.coltivatore
                                    ?.value
                            )

                            DetailsRow(
                                label = "Località",
                                contract
                                    ?.localita
                            )
                            DetailsRow(
                                label = "Articolo",
                                item = contract
                                    ?.coltura?.articolo
                            )
                        }
                    }


                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .clickable { },
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),


                    horizontalAlignment = Alignment.Start
                ) {
                    Box(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Dati Rilevazione",
                            color = Color.Black,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(colorResource(id = R.color.element_ligth_gray))
                    )
                    Box(modifier = Modifier.padding(20.dp)) {
                        Column() {

                            DetailsRow(
                                label = "Lotto",
                                item = contract
                                    ?.coltura
                                    ?.lotto
                            )
                            DetailsRow(
                                label = "Specie",
                                item = contract
                                    ?.coltura
                                    ?.specie
                            )
                            DetailsRow(
                                label = "Varietà",
                                item = contract
                                    ?.coltura
                                    ?.varieta
                            )
                            DetailsRow(
                                label = "Tipo Raccolta",
                                item = contract
                                    ?.coltura
                                    ?.tipoRaccolta
                            )
                            DetailsRow(
                                label = "Tipo linea",
                                item = contract
                                    ?.coltura
                                    ?.tipoLinea
                            )
                            DetailsRow(
                                label = "Sigla (Anno)",
                                item = contract
                                    ?.coltura
                                    ?.sigla
                            )
                            DetailsRow(
                                label = "Tipo prodotto",
                                item = contract
                                    ?.prodotto
                                    ?.tipoProdotto
                            )
                            DetailsRow(
                                label = "Coltura",
                                item = contract
                                    ?.prodotto
                                    ?.coltura
                            )
                            DetailsRow(
                                label = "Destinazione",
                                item = contract
                                    ?.prodotto
                                    ?.destinazione
                            )
                            DetailsRow(
                                label = "HA Contratto",
                                item = contract
                                    ?.coltivazione
                                    ?.contratto
                            )
                            DetailsRow(
                                label = "Qta Attesa",
                                item = contract
                                    ?.coltivazione
                                    ?.quantitaAttesa
                            )
                            DetailsRow(
                                label = "HA Seminati",
                                item = contract
                                    ?.coltivazione
                                    ?.contratto
                            )
                            DetailsRow(
                                label = "Data Semina F/OP",
                                item = contract
                                    ?.coltivazione
                                    ?.dataSeminaFOP
                            )
                            DetailsRow(
                                label = "Data Semina M",
                                item = contract
                                    ?.coltivazione
                                    ?.dataSemina
                            )
                            DetailsRow(
                                label = "HA netti dopo distruzione",
                                item = contract
                                    ?.coltivazione
                                    ?.nettiDopoDistruzione
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),

                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = "Note :",
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    color = Color(117, 131, 144)
                                )
                                contract
                                    ?.note
                                    ?.let {
                                        Text(
                                            text = it,
                                            fontSize = 20.sp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        )
                                    }
                            }
                        }
                    }
                }
            }
        }
    }


}

@Preview(widthDp = 400, heightDp = 500)
@Composable
fun DefaultContractDetails() {
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
        val selectViewModelDetection = SelectViewModelContract()
        selectViewModelDetection.select(contract)
        ContractDetails(selectViewModelDetection, rememberNavController())
    }
}