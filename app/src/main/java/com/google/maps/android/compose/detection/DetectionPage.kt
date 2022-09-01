package com.google.maps.android.compose.detection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.maps.android.compose.R

import it.bsdsoftware.germinapp_new.detail_section.SelectViewModelDetection
import it.bsdsoftware.germinapp_new.domain.entities.*
import it.bsdsoftware.germinapp_new.domain.repository.GerminaDomainRepository
import it.bsdsoftware.germinapp_new.domain.usecases.DetectionUseCase
import com.google.maps.android.compose.ui.common.LiveDataList
import it.bsdsoftware.germinapp_new.ui.navigation.NavigationHome
import it.bsdsoftware.germinapp_new.ui.theme.Germinapp_newTheme

@Composable
fun DetectionPage(
    detectionViewModel: DetectionViewModel,
    navController: NavController,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    Row(modifier = Modifier.fillMaxWidth(),) {
        Column() {
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
                            navController.navigate(NavigationHome.CONTRATTI.name)
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
                            detectionViewModel.newDetection()
                            navController.navigate(NavigationHome.NUOVA_RILEVAZIONE.name)
                        }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "back",
                            modifier = Modifier
                                .padding(all = 22.dp)
                                .size(34.dp),
                            tint = Color.Black
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LiveDataList(
                detectionViewModel.detections,
                lifecycleOwner
            ) { c ->
                DetectionPreviewItem(item = c) {
                    detectionViewModel.selectViewModelDetection.select(c)
                    navController.navigate(NavigationHome.DETTAGLI_RILEVAZIONI.name)
                }
            }
        }
    }

}

@Preview(widthDp = 800, heightDp = 500)
@Composable
fun DefaultDetectionList() {
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
        val repository = object : GerminaDomainRepository {
            override fun login(
                user: String,
                password: String,
                success: (User) -> Unit,
                error: (String) -> Unit
            ) {
                success(User(1, user, "test", "test", ""))
            }

            override suspend fun loginLastUser(
                success: (User) -> Unit,
                error: (String) -> Unit
            ) {
                TODO("Not yet implemented")
            }

            override suspend fun logoutLastUser() {
                TODO("Not yet implemented")
            }

            override suspend fun downloadContracts(
                token: String,
                success: (List<Contract>) -> Unit,
                error: (String) -> Unit
            ) {
                TODO("Not yet implemented")
            }

            override suspend fun getContractsFromDB(
                token: String,
                success: (List<Contract>) -> Unit,
                error: (String) -> Unit
            ) {
                success(listOf())
            }

            override suspend fun newDetection(
                detection: Detection,
                success: (Detection) -> Unit,
                error: (String) -> Unit
            ) {
                TODO("Not yet implemented")
            }

            override suspend fun getDetections(
                contract: Contract,
                phytosanitaries: List<PhytosanitaryEntity>,
                success: (List<Detection>) -> Unit,
                error: (String) -> Unit
            ) {
                TODO("Not yet implemented")
            }

            override suspend fun getPhytosanitaries(
                success: (List<PhytosanitaryEntity>) -> Unit,
                error: (String) -> Unit
            ) {
                TODO("Not yet implemented")
            }

            override suspend fun getPhytosanitaries(
                specie: String,
                success: (List<PhytosanitaryEntity>) -> Unit,
                error: (String) -> Unit
            ) {
                TODO("Not yet implemented")
            }

            override suspend fun synchronize(token: String,
                                             success: () -> Unit, error: (String) -> Unit
            ) {
                TODO("Not yet implemented")
            }

            override suspend fun synchronize(
                token: String,
                detecrtions: List<Detection>,
                success: (List<Detection>) -> Unit,
                error: (String) -> Unit
            ) {
                TODO("Not yet implemented")
            }
        }
        val detectionViewModel = DetectionViewModel(SelectViewModelDetection(), DetectionUseCase(repository))
        detectionViewModel.getAllDetections(contract)
        DetectionPage(detectionViewModel, rememberNavController())
    }
}