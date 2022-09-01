package it.bsdsoftware.germinapp_new.detail_section

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.maps.android.compose.detection.DetectionViewModel
import it.bsdsoftware.germinapp_new.domain.entities.*
import it.bsdsoftware.germinapp_new.domain.repository.GerminaDomainRepository
import it.bsdsoftware.germinapp_new.domain.usecases.DetectionUseCase
import it.bsdsoftware.germinapp_new.domain.usecases.LoginUseCase
import it.bsdsoftware.germinapp_new.login.LoginViewModel
import it.bsdsoftware.germinapp_new.ui.navigation.NavigationHome
import it.bsdsoftware.germinapp_new.ui.theme.Germinapp_newTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SwitchRow(
    label: String,
    item: MutableState<Boolean>,
    enabled: Boolean = true,
    fontSize: TextUnit = 12.sp,
    padding: Dp = 0.dp,
    action: (Boolean) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = padding)
    )
    {
        Text(
            text = "$label :",
            fontSize = fontSize,
        )
        Switch(
            checked = item.value,
            onCheckedChange = {
                item.value = it
                action(it)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Red,
                uncheckedThumbColor = Color.LightGray,
                checkedTrackColor = Color.Red,
                uncheckedTrackColor = Color.LightGray,
                disabledCheckedThumbColor = Color.Red,
                disabledUncheckedThumbColor = Color.LightGray,
                disabledCheckedTrackColor = Color.Red,
                disabledUncheckedTrackColor = Color.LightGray,
            ),
            enabled = enabled
        )
    }
}

@Composable
fun DetectionDetails(
    detectionDetailsViewModel: DetectionDetailsViewModel,
    navController: NavHostController,
    //phytosanitaries: List<PhytosanitaryEntity> = listOf(),
    mContext: Context = LocalContext.current,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {

    var mutable by remember { mutableStateOf(false) }
    var categories by remember {
        mutableStateOf(
            detectionDetailsViewModel.phytosanitaries.value ?: listOf()
        )
    }
    val phytosanitaries = HashMap<PhytosanitaryEntity, Boolean>()

    detectionDetailsViewModel.phytosanitaries
        .observe(LocalLifecycleOwner.current) {
            categories = it
            phytosanitaries.clear()
            it.forEach { entity -> phytosanitaries[entity] = false }
        }
    val bitmaps = remember { mutableStateOf<List<Triple<Bitmap, Boolean, Int>>>(ArrayList()) }
    var time by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    val user = remember { mutableStateOf("") }
    var date by remember {
        mutableStateOf(
            ""
        )
    }

    val note = remember {
        mutableStateOf("")
    }
    var detection by remember { mutableStateOf(detectionDetailsViewModel.selectViewModelDetection.selected.value) }
    detectionDetailsViewModel.selectViewModelDetection
        .selected
        .observe(lifecycleOwner) {
            detection = it
            detectionDetailsViewModel.getPhytosanitaries()
            user.value = it.user.username
            date = it.data
            time = it.startTime
            endTime = it.endTime
            note.value = it.note
            bitmaps.value =
                it.images.mapIndexed { index, path ->
                    Triple(
                        detectionDetailsViewModel
                            .getFromInternalStorage(path.first), false, index
                    )
                }.filter { image -> image.first != null }
                    .map { image -> Triple(image.first!!, image.second, image.third) }
        }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp, 5.dp),
        //.verticalScroll(ScrollState(0)),
        verticalAlignment = Alignment.Top
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(0.4f),
            horizontalAlignment = Alignment.Start
        ) {
            item {
                Button(
                    onClick = {
                        navController.navigate(NavigationHome.RILEVAZIONI.name)
                    },
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red,
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium,
                )
                {
                    Text("Indietro", color = Color.White)
                }
                Text(text = "Foto")
            }
            items(bitmaps.value) { bitmap ->
                Image(
                    bitmap = bitmap.first.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(400.dp)
                        .padding(vertical = 3.dp)
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            //.verticalScroll(ScrollState(0)),
            horizontalAlignment = Alignment.Start
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp)
                ) {
                    /*Button(
                        onClick = { },
                        enabled = true,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Red,
                            contentColor = Color.White
                        ),
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        Text(text = "Sincronizza", color = Color.White)
                    }*/
                    Button(
                        onClick = {
                            mutable = !mutable
                            if (!mutable) {
                                detectionDetailsViewModel.getPhytosanitaries()
                                user.value =detectionDetailsViewModel.selectViewModelDetection.selected.value!!.user.username
                                date = detectionDetailsViewModel.selectViewModelDetection.selected.value!!.data
                                time = detectionDetailsViewModel.selectViewModelDetection.selected.value!!.startTime
                                endTime = detectionDetailsViewModel.selectViewModelDetection.selected.value!!.endTime
                                note.value = detectionDetailsViewModel.selectViewModelDetection.selected.value!!.note
                                bitmaps.value =
                                    detectionDetailsViewModel.selectViewModelDetection.selected.value!!.images.mapIndexed { index, path ->
                                        Triple(
                                            detectionDetailsViewModel
                                                .getFromInternalStorage(path.first), false, index
                                        )
                                    }.filter { image -> image.first != null }
                                        .map { image -> Triple(image.first!!, image.second, image.third) }
                            }
                        },
                        modifier = Modifier
                            .padding(10.dp, 5.dp),
                        enabled = true,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Red,
                            contentColor = Color.White
                        ),
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        Text(text = if (mutable) "Annulla" else "Modifica", color = Color.White)
                    }
                }
                Text(text = "Contratto", color = Color.Red, fontSize = 16.sp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp, 5.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    DetailsRow(
                        label = "Coltivatore",
                        item = detection
                            ?.contract
                            ?.coltivatore
                            ?.value
                    )
                    DetailsRow(
                        label = "Indirizzo",
                        item = detection
                            ?.contract
                            ?.indirizzo
                    )
                    DetailsRow(
                        label = "Articolo",
                        item = detection
                            ?.contract
                            ?.coltura
                            ?.articolo
                            ?.value
                    )
                }
                Text(text = "Dati rilevazione", color = Color.Red, fontSize = 16.sp)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 5.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    DetailsRow(
                        label = "Utente",
                        item = user
                    )

                    DetailsRow(
                        label = "Data",
                        item = getDate(date)
                    )

                    DetailsRow(
                        label = "Ora inizio",
                        item = getTime(time)
                    )
                    DetailsRow(
                        label = "Ora fine",
                        item = getTime(endTime)
                    )

                    EditableRow(
                        label = "Note",
                        item = note,
                        row = 4,
                        enabled = mutable
                    )
                }
            }
            categories
                .groupBy { e -> e.category }
                .map { c -> c.key }
                .forEach()
                { category ->

                    item {
                        Text(
                            text = category.value,
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    }
                    items(categories
                        .filter { it.category.id == category.id }
                    ) { entity ->

                        val checkedState = remember {
                            mutableStateOf(
                                phytosanitaries[entity]!!
                            )
                        }
                        SwitchRow(
                            label = entity.value,
                            item = checkedState,
                            padding = 20.dp,
                            action = { state ->
                                phytosanitaries[entity] = state
                            },
                            enabled = mutable
                        )
                    }
                }
            item(2) {
                if (mutable) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        val coroutineScope = rememberCoroutineScope()
                        val launcher =
                            rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
                                it?.let { image ->
                                    bitmaps.value =
                                        bitmaps.value + Triple(image, true, bitmaps.value.size)
                                    /*coroutineScope.launch {
                                 withContext(Dispatchers.IO) {
                                     saveToInternalStorage(image, mContext)
                                         ?.let { tmp ->
                                             val test = tmp.isFile
                                             if (test) {
                                             }
                                             sendFileUnirest(
                                                 "http://apitestgermina.bsdsoftware.it/api/Foto",
                                                 token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1laWQiOiJjZWNjYSIsIm5iZiI6MTY1ODQxMTc1OCwiZXhwIjoxNjU4NDQwNTU4LCJpYXQiOjE2NTg0MTE3NTgsImlzcyI6IkJTRFNPRlRXQVJFIiwiYXVkIjoiR0VSTUlOQVBQX0FQSSJ9.iiKD6Jmr4-IW3-KgIOpWJ3OmgQRxra5igx2G1pRx5AE",
                                                 idRilevazione = "14",
                                                 files = listOf(tmp)
                                             )
                                         }
                                 }
                             }
*/
                                }
                            }
                        val permissionLauncher = rememberLauncherForActivityResult(
                            ActivityResultContracts.RequestPermission()
                        ) { isGranted: Boolean ->
                            if (isGranted) {
                                launcher.launch()
                                // Permission Accepted: Do something
                                Log.d("ExampleScreen", "PERMISSION GRANTED")

                            } else {
                                // Permission Denied: Do something
                                Log.d("ExampleScreen", "PERMISSION DENIED")
                            }
                        }
                        Button(
                            onClick = {
                                when (PackageManager.PERMISSION_GRANTED) {
                                    ContextCompat.checkSelfPermission(
                                        mContext,
                                        Manifest.permission.CAMERA
                                    ) -> {
                                        // Some works that require permission
                                        launcher.launch()
                                        Log.d("ExampleScreen", "Code requires permission")
                                    }
                                    else -> {
                                        // Asking for permission
                                        permissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                }

                            },
                            enabled = mutable,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Red,
                                contentColor = Color.White
                            ),
                            shape = MaterialTheme.shapes.medium,
                        ) {
                            Text(text = "Aggiungi immagine", color = Color.White)
                        }
                        Button(
                            onClick = {
                                detectionDetailsViewModel.saveDetection(
                                    date = detection!!.data,
                                    startTime = detection!!.startTime,
                                    endTime = detection!!.endTime,
                                    note = note.value,
                                    images = bitmaps.value,
                                    phytosanitaries = phytosanitaries.map {
                                        Phytosanitary(
                                            it.key,
                                            it.value
                                        )
                                    },

                                    ) {
                                    phytosanitaries.clear()
                                    categories.forEach { entity -> phytosanitaries[entity] = false }
                                    GlobalScope.launch(Dispatchers.Main) {
                                        navController.navigate(NavigationHome.RILEVAZIONI.name)
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(10.dp, 5.dp),
                            enabled = true,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Red,
                                contentColor = Color.White
                            ),
                            shape = MaterialTheme.shapes.medium,
                        ) {
                            Text(text = "Salva", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
/*Column(
    modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp, 5.dp)
        .verticalScroll(ScrollState(0)),
    horizontalAlignment = Alignment.Start
) {
    var detection by remember { mutableStateOf(selectViewModelDetection.selected.value) }
    selectViewModelDetection
        .selected
        .observe(LocalLifecycleOwner.current) {
            detection = it
        }
    var mutable by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
    ) {
        Button(
            onClick = {
                navController.navigate(NavigationHome.RILEVAZIONI.name)
            },
            enabled = true,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Red,
                contentColor = Color.White
            ),
            shape = MaterialTheme.shapes.medium,
        )
        {
            Text("Indietro", color = Color.White)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp)
        ) {
            Button(
                onClick = {  },
                enabled = true,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red,
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium,
            ) {
                Text(text = "Sincronizza", color = Color.White)
            }
            Button(
                onClick = { mutable = !mutable },
                modifier = Modifier
                    .padding(10.dp, 5.dp),
                enabled = true,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red,
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium,
            ) {
                Text(text = if (mutable) "Salva" else "Modifica", color = Color.White)
            }
        }
    }


    Text(text = "Contratto", color = Color.Red, fontSize = 16.sp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 5.dp),
        horizontalAlignment = Alignment.Start
    ) {
        DetailsRow(
            label = "Coltivatore",
            item = detection
                ?.contract
                ?.coltivatore
                ?.value
        )
        DetailsRow(
            label = "Indirizzo",
            item = detection
                ?.contract
                ?.indirizzo
        )
        DetailsRow(
            label = "Articolo",
            item = detection
                ?.contract
                ?.coltura
                ?.articolo
                ?.value
        )
    }
    Text(text = "Dati rilevazione", color = Color.Red, fontSize = 16.sp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 5.dp),
        horizontalAlignment = Alignment.Start
    ) {
        DetailsRow(
            label = "Utente",
            item = detection
                ?.user
                ?.username
        )
        DetailsRow(
            label = "Data",
            item = detection
                ?.data
                ?.let { getDate(it) }
        )
        DetailsRow(
            label = "Ora inizio",
            item = detection
                ?.startTime
                ?.let { getTime(it) }
        )
        DetailsRow(
            label = "Ora fine",
            item = detection
                ?.endTime
                ?.let { getTime(it) }
        )
        DetailsRow(
            label = "Note",
            item = detection
                ?.note
        )
        val categories = detection
            ?.phytosanitaries?.groupBy { e -> e.type.category }
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            categories?.forEach { category ->
                Text(text = category.key.value, color = Color.Red, fontSize = 14.sp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 0.dp),
                ) {
                    category.value.forEach { entity ->
                        val checkedState = remember {
                            mutableStateOf(
                                entity.presence
                            )
                        }
                        SwitchRow(
                            label = entity.type.value,
                            item = checkedState,
                            enabled = mutable
                        )
                    }
                }
            }
        }
    }
}
}*/

fun getTime(full: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val outputFormat = SimpleDateFormat("HH:mm")
        val parsedDate: Date = inputFormat.parse(full)
        outputFormat.format(parsedDate)
    } catch (e: Exception) {
        ""
    }
}

fun getDate(full: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val outputFormat = SimpleDateFormat("dd/MM/yyyy")
        val parsedDate: Date = inputFormat.parse(full)
        outputFormat.format(parsedDate)
    } catch (e: Exception) {
        ""
    }
}

@Preview(widthDp = 800, heightDp = 500)
@Composable
fun DefaultDetectionDetails() {
    Germinapp_newTheme {
        val detection = Detection(
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
            ),
            remoteId = null,
            localId = null,
            user = User(1, "user", "", "", null),
            data = "2021-01-07T00:00:00",
            startTime = "2021-01-07T16:04:00",
            endTime = "2021-01-07T16:07:00",
            images = listOf(),
            note = "",
            phytosanitaries = listOf(
                Phytosanitary(
                    type = PhytosanitaryEntity(
                        id = 1,
                        value = "test1",
                        category = PhytosanitaryCategory(
                            id = 1,
                            value = "cate1"
                        ),
                        species = listOf()
                    ),
                    presence = false
                ),
                Phytosanitary(
                    type = PhytosanitaryEntity(
                        id = 2,
                        value = "test2",
                        category = PhytosanitaryCategory(
                            id = 2,
                            value = "cate2"
                        ),
                        species = listOf()
                    ),
                    presence = false
                ),
                Phytosanitary(
                    type = PhytosanitaryEntity(
                        id = 3,
                        value = "test3",
                        category = PhytosanitaryCategory(
                            id = 1,
                            value = "cate1"
                        ),
                        species = listOf()
                    ),
                    presence = true
                )
            )
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

            override suspend fun synchronize(
                token: String,
                success: () -> Unit,
                error: (String) -> Unit
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
        val loginViewModel = LoginViewModel(LoginUseCase(repository))
        loginViewModel.login("test", "test") {}
        val selectViewModelDetection = SelectViewModelDetection()
        selectViewModelDetection.select(detection)
        val detectionViewModel =
            DetectionViewModel(SelectViewModelDetection(), DetectionUseCase(repository))
        val detectionDetailsViewModel = DetectionDetailsViewModel(
            selectViewModelDetection,
            detectionViewModel,
            DetectionUseCase(repository),
            Application()
        )
        DetectionDetails(
            detectionDetailsViewModel,
            rememberNavController(),
            /*listOf(
                PhytosanitaryEntity(
                    id = 1,
                    value = "test1",
                    category = PhytosanitaryCategory(
                        id = 1,
                        value = "cate1"
                    ),
                    species = listOf()
                ), PhytosanitaryEntity(
                    id = 2,
                    value = "test2",
                    category = PhytosanitaryCategory(
                        id = 2,
                        value = "cate2"
                    ),
                    species = listOf()
                ),
                PhytosanitaryEntity(
                    id = 3,
                    value = "test3",
                    category = PhytosanitaryCategory(
                        id = 1,
                        value = "cate1"
                    ),
                    species = listOf()
                )
            )*/
        )
    }
}