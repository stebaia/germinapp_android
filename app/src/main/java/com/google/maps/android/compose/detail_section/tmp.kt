package it.bsdsoftware.germinapp_new.detail_section

import android.Manifest
import android.os.Build
import android.provider.MediaStore

class tmp {
    /*fun isStoragePermissionGranted(context: Context?): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) === PackageManager.PERMISSION_GRANTED
            ) {
                Log.debug(TAG, "Permission is granted")
                true
            } else {
                Log.debug(TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(
                    context as Activity?,
                    arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.debug(TAG, "Permission is granted")
            true
        }
    }

    fun checkPermissionREAD_EXTERNAL_STORAGE(context: Context?): Boolean {
        val currentAPIVersion: Int = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) !== PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity?,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) || ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity?,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    showDialog(
                        "External storage",
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        context as Activity?,
                        arrayOf<String>(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                    )
                }
                false
            } else {
                true
            }
        } else {
            true
        }
    }


    fun checkPermissionCamera(context: Context?): Boolean {
        val currentAPIVersion: Int = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as Activity?,
                    arrayOf<String>(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
                true
            } else {
                true
            }
        } else {
            true
        }
    }

    fun showDialog(msg: String?, context: Context?, permission: String, permissionTwo: String) {
        /*  AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                };*/
        /*  AlertDialog alert = alertBuilder.create();
        alert.show();*/
        ActivityCompat.requestPermissions(
            context as Activity?,
            arrayOf(permission, permissionTwo),
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
        )
    }


    private fun hasStoragePermission(): Boolean {
        return checkPermissionREAD_EXTERNAL_STORAGE(requireContext())
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>?,
        grantResults: IntArray?
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    fun getAllShownImagesPath(activity: Activity): ArrayList<String?>? {
        val uri: Uri
        val cursor: Cursor
        val column_index_data: Int
        val column_index_folder_name: Int
        val listOfAllImages = ArrayList<String?>()
        var absolutePathOfImage: String? = null
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf<String>(
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )
        if (hasStoragePermission()) {
            // do your stuff..
            cursor = activity.getContentResolver().query(
                uri,
                projection,
                null,
                null,
                MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC"
            )
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            column_index_folder_name =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data)
                //Log.debug(TAG, absolutePathOfImage);
                listOfAllImages.add(absolutePathOfImage)
            }
        }
        return listOfAllImages
    }

    @OnClick(R.id.float_new_photo)
    fun newPhoto() {
        if (checkPermissionCamera(requireContext())) {
            if (adapter.selectedPositions.size() >= 4) {
                val toast: Toast = Toast.makeText(
                    getActivity(),
                    "Puoi allegare massimo 4 foto",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            } else {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    var photoFile: File? = null
                    try {
                        photoFile = createImageFile()
                        mCurrentPhotoPath = photoFile.getAbsolutePath()
                    } catch (ex: IOException) {
                        Log.error(TAG, ex.getMessage(), ex.getCause())
                    }
                    // Continue only if the File was successfully created
                    try {
                        if (photoFile != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                takePictureIntent.putExtra(
                                    MediaStore.EXTRA_OUTPUT,
                                    GenericProvider.getUriForFile(
                                        getActivity(),
                                        getContext().getApplicationContext()
                                            .getPackageName() + ".provider",
                                        photoFile
                                    )
                                )
                                startActivityForResult(takePictureIntent, IMAGE_SHOOT)
                            } else {
                                takePictureIntent.putExtra(
                                    MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(photoFile)
                                )
                                startActivityForResult(takePictureIntent, IMAGE_SHOOT)
                            }
                        }
                    } catch (ex: Exception) {
                        ex.message
                    }
                }
            }
        } else Toast.makeText(
            getActivity(),
            "Non ci sono i permessi necessari per poter procedere , controlla",
            Toast.LENGTH_LONG
        ).show()
    }

    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_issue_gallery, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    fun onStart() {
        super.onStart()
        //checkPermissionREAD_EXTERNAL_STORAGE(getContext()) ;
        adapter =
            GalleryViewAdapter(getActivity(), getAllShownImagesPath(getActivity()), selectedImages)
        gridView.setAdapter(adapter)
        gridView.setOnItemClickListener(object : OnItemClickListener() {
            fun onItemClick(parent: AdapterView<*>, v: View, position: Int, id: Long) {
                val selectedIndex: Int =
                    adapter.selectedPositions.indexOf(adapter.getItem(position))
                Log.debug(TAG, "selectedIndex $selectedIndex")
                if (selectedIndex > -1) {
                    adapter.selectedPositions.remove(selectedIndex)
                    (v as GalleryItemView).display(false)
                    selectedImages.remove(parent.getItemAtPosition(position))
                    Log.debug(TAG, selectedImages.toString())
                } else {
                    if (adapter.selectedPositions.size() >= 4) {
                        val toast: Toast = Toast.makeText(
                            getActivity(),
                            "Puoi allegare massimo 4 foto",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    } else {
                        adapter.selectedPositions.add(adapter.getItem(position) as CharSequence)
                        (v as GalleryItemView).display(true)
                        selectedImages.add(parent.getItemAtPosition(position) as String)
                        Log.debug(TAG, selectedImages.toString())
                    }
                }
            }
        })
    }

    fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            mCurrentPhotoPath = savedInstanceState.getString("photoPath")
        }
        mContext = getActivity()
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putString("photoPath", mCurrentPhotoPath)
        super.onSaveInstanceState(outState)
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_SHOOT -> if (mCurrentPhotoPath != null) {
                    val map: Bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
                    if (Build.VERSION.SDK_INT < 30) {
                        addImageToGalleryPrev(mCurrentPhotoPath, getActivity())
                    } else {
                        addImageToGallery(mCurrentPhotoPath, getActivity(), map)
                    }
                    //selectedImages.add(mCurrentPhotoPath);
                    mCurrentPhotoPath = null
                }
            }
        }
    }

    fun onResume() {
        super.onResume()
        mContext = getActivity()
    }

    fun onPauseFragment() {}

    fun onResumeFragment() {
        if (mContext != null) (mContext as StepperActions).setNextAction(true)
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image: File = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath()
        return image
    }

    private fun addImageToGalleryPrev(filePath: String, context: Context) {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.MediaColumns.DATA, filePath)
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    private fun addImageToGallery(filePath: String, context: Context, map: Bitmap) {
        val resolver: ContentResolver = context.getContentResolver()
        val outputStream: FileOutputStream
        val values = ContentValues()
        val filenameWithExt = filePath.substring(filePath.lastIndexOf("/") + 1)
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, filenameWithExt)
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(
            MediaStore.MediaColumns.RELATIVE_PATH,
            (Environment.DIRECTORY_PICTURES + File.separator).toString() + "Municipium" + File.separator
        )
        values.put(MediaStore.MediaColumns.DATA, filePath)
        val uri: Uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        try {
            outputStream =
                resolver.openOutputStream(Objects.requireNonNull(uri)) as FileOutputStream
            map.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun sendData(data: String?) {}

    fun checkData(): Boolean {
        return false
    }

    fun getData(): String? {
        return GsonBuilder().create().toJson(selectedImages)
    }*/
}