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
fun EditableRow(
    label: String,
    item: MutableState<String>,
    row: Int = 1,
    fontSize: TextUnit = 12.sp,
    enabled: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.Top
    )
    {
        Text(
            text = "$label :",
            fontSize = fontSize,
            modifier = Modifier
                .fillMaxWidth(0.25f)
        )
        OutlinedTextField(
            value = item.value,
            onValueChange = { item.value = it },
            label = { Text(label) },
            enabled = enabled
        )
    }
}

@Composable
fun DetailsRow(label: String, item: MutableState<String>, fontSize: TextUnit = 12.sp) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Text(
            text = "$label :",
            fontSize = fontSize,
            modifier = Modifier
                .fillMaxWidth(0.25f)
        )
        Text(
            text = item.value,
            fontSize = fontSize
        )
    }
}

fun getTime(): String =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(Date())

fun getDate(): String = SimpleDateFormat(
    "yyyy-MM-dd'T'00:00:00",
    Locale.getDefault()
).format(Date())


@Composable
fun DetectionNew(
    detectionNewViewModel: DetectionNewViewModel,
    navController: NavHostController,
    mContext: Context = LocalContext.current,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    var categories by remember {
        mutableStateOf(
            detectionNewViewModel.phytosanitaries.value ?: listOf()
        )
    }
    val phytosanitaries = HashMap<PhytosanitaryEntity, Boolean>()

    detectionNewViewModel.phytosanitaries
        .observe(LocalLifecycleOwner.current) {
            categories = it
            phytosanitaries.clear()
            it.forEach { entity -> phytosanitaries[entity] = false }
        }
    val bitmaps = remember { mutableStateOf<List<Triple<Bitmap, Boolean, Int>>>(ArrayList()) }
    var contract by remember { mutableStateOf(detectionNewViewModel.selectViewModelContract.selected.value) }
    detectionNewViewModel.selectViewModelContract
        .selected
        .observe(LocalLifecycleOwner.current) {
            contract = it
            detectionNewViewModel.getPhytosanitaries()
        }


    var date by remember {
        mutableStateOf(
            getDate()
        )
    }
    var time by remember { mutableStateOf(getTime()) }
    val user = remember { mutableStateOf("") }
    detectionNewViewModel.loginViewModel.loggedUser.observe(lifecycleOwner) {
        if (it.isPresent) {
            user.value = it.get().username
        }
    }
    val note = remember {
        mutableStateOf("")
    }

    detectionNewViewModel
        .detectionViewModel
        .newDetection
        .observe(lifecycleOwner) {
            if (it) {
                date = getDate()
                time = getTime()
                note.value = ""
                categories.forEach { entity -> phytosanitaries[entity] = false }
                bitmaps.value = listOf()
            }
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
                .fillMaxWidth()
                .padding(0.dp, 55.dp),
            //.verticalScroll(ScrollState(0)),
            horizontalAlignment = Alignment.Start
        ) {
            item {
                Text(text = "Contratto", color = Color.Red, fontSize = 16.sp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp, 5.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    DetailsRow(
                        label = "Coltivatore",
                        item = contract
                            ?.coltivatore
                            ?.value
                    )
                    DetailsRow(
                        label = "Indirizzo",
                        item = contract
                            ?.indirizzo
                    )
                    DetailsRow(
                        label = "Articolo",
                        item = contract
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
                        item = ""
                    )

                    EditableRow(
                        label = "Note",
                        item = note,
                        row = 4
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
                            }
                        )
                    }
                }
            item(2) {
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
                        enabled = true,
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
                            detectionNewViewModel.saveDetection(
                                date = date,
                                startTime = time,
                                endTime = getTime(),
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

/*
private fun dispatchTakePictureIntent(mContext: Context) {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
        // Ensure that there's a camera activity to handle the intent
        takePictureIntent.resolveActivity(mContext.packageManager)?.also {
            // Create the File where the photo should go
            val photoFile: File? = try {
                createImageFile(mContext = mContext)
            } catch (ex: IOException) {
                // Error occurred while creating the File
                null
            }
            // Continue only if the File was successfully created
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    mContext,
                    "com.example.android.fileprovider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
}

@Throws(IOException::class)
private fun createImageFile(mContext: Context): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File? = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}*/

@Preview(widthDp = 800, heightDp = 500)
@Composable
fun DefaultDetectionNew() {
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
        )/*,
        remoteId = null,
        localId = null,
        user = "user",
        data = "12/11/2022",
        startTime = "10:20",
        endTime = "20:30",
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
                    )
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
                    )
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
                    )
                ),
                presence = true
            )
        )
        )*/
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
        val selectViewModelDetection = SelectViewModelContract()
        selectViewModelDetection.select(contract)
        val detectionViewModel =
            DetectionViewModel(SelectViewModelDetection(), DetectionUseCase(repository))
        val detectionNewViewModel = DetectionNewViewModel(
            loginViewModel,
            selectViewModelDetection,
            detectionViewModel,
            DetectionUseCase(repository),
            Application()
        )
        DetectionNew(
            detectionNewViewModel,
            rememberNavController()
        )
        /*loginViewModel,
        selectViewModel,
        listOf(
            PhytosanitaryEntity(
                id = 1,
                value = "test1",
                category = PhytosanitaryCategory(
                    id = 1,
                    value = "cate1"
                )
            ), PhytosanitaryEntity(
                id = 2,
                value = "test2",
                category = PhytosanitaryCategory(
                    id = 2,
                    value = "cate2"
                )
            ),
            PhytosanitaryEntity(
                id = 3,
                value = "test3",
                category = PhytosanitaryCategory(
                    id = 1,
                    value = "cate1"
                )
            )
        )
    )*/
    }
}