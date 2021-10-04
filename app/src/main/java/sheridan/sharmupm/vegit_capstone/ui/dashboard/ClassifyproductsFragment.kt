package sheridan.sharmupm.vegit_capstone.ui.dashboard
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap

import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.classifyProducts.ClassifyproductsViewModel
import sheridan.sharmupm.vegit_capstone.helpers.getDietFromCache
import android.hardware.Camera
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
import android.service.autofill.Validators.not
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ClassifyproductsFragment : Fragment(),DataAdapter.RecyclerViewItemClickListener {

    private lateinit var classifyproductsViewModel: ClassifyproductsViewModel
    private lateinit var ingredientLabelPicture: ImageView
    private lateinit var customDialog: CustomListViewDialog
    private lateinit var analyzeBtn:Button
    private var mLastClickTime:Long = 0

    private lateinit var camera: Camera

    var showCamera: ShowCamera? = null



    companion object{
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
        private val REQUEST_CODE = 42
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        classifyproductsViewModel =
                ViewModelProvider(this).get(ClassifyproductsViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_classifyproducts, container, false)
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ingredientLabelPicture = view.findViewById<ImageView>(R.id.imageView)
        val galleryBtn = view.findViewById<ImageButton>(R.id.gallery)
        analyzeBtn = view.findViewById<Button>(R.id.takePicture)
        val scanResult = view.findViewById<TextView>(R.id.text_view)
        val framelayout = view.findViewById<FrameLayout>(R.id.frame_layout)
        val captureImage = view.findViewById<Button>(R.id.capture_picture)

        val args = this.arguments
        if (args?.isEmpty == false){
            val image = args?.get("bitmap")
            ingredientLabelPicture.setImageBitmap(image as Bitmap?)
            args.clear()
        }

        galleryBtn.setOnClickListener{
            framelayout.removeView(showCamera)
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if (context?.applicationContext?.let { it1 -> checkSelfPermission(it1,android.Manifest.permission.READ_EXTERNAL_STORAGE) } ==PackageManager.PERMISSION_DENIED)
                {
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                }

                else
                {
                    pickImageFromGallery()

                }
            }
            else
            {
                pickImageFromGallery()

            }
        }

        captureImage.setOnClickListener{
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if (context?.applicationContext?.let { it1 -> checkSelfPermission(it1,android.Manifest.permission.CAMERA) } ==PackageManager.PERMISSION_DENIED)
                {
                    val permissions = arrayOf(android.Manifest.permission.CAMERA)
                    requestPermissions(permissions, REQUEST_CODE)
                }

                else
                {
                    captureImage()

                }
            }
            else
            {
                captureImage()

            }

            //capture the image and show in the image View Code......
            //imageView -> click "take picture" button -> picture shown in the imageView
            //imageView -> click"take picture" button -> CameraView -> click "take picture" button -> picture shown in the imageView

        }


        //Google OCR: extract text from the ingredient label and saved as string and analysis the string and return the
        //result if the food is safe to eat
        //**** the "analyze" is hard-code now***
        analyzeBtn.setOnClickListener {
            if (analyzeBtn.isClickable==true){
                if(SystemClock.elapsedRealtime()-mLastClickTime<1000){
                    analyzeBtn.isClickable=false
                    //analyzeBtn.postDelayed(Runnable { kotlin.run { analyzeBtn.isClickable=false } },1000)
                    //Toast.makeText(requireContext(),"The button is unclickable now",Toast.LENGTH_LONG).show()
                }
                else{
                    if (ingredientLabelPicture.getDrawable()==null){
                        Toast.makeText(context?.applicationContext, "No Picture Detected!", Toast.LENGTH_SHORT)
                                .show()
                    }
                    else{
                        val mBitmap = ingredientLabelPicture.getDrawable().toBitmap()
                        val textRecognizer = TextRecognizer.Builder(context?.applicationContext).build()
                        if (!textRecognizer.isOperational) {
                            Toast.makeText(context?.applicationContext, "Could not get the text", Toast.LENGTH_SHORT)
                                    .show()
                        } else {
                            val frame = Frame.Builder().setBitmap(mBitmap).build()
                            val items = textRecognizer.detect(frame)

                            val ingredientList = classifyproductsViewModel.extractIngredientText(items)
                            if (ingredientList != null) {
                                classifyproductsViewModel.searchIngredientList(ingredientList)
                            } else {
                                Toast.makeText(context?.applicationContext, "Failed to extract ingredients", Toast.LENGTH_SHORT).show()
                                // show error message that no data was extracted?
                                println("No data able to be extracted!")
                            }
                        }

                    }

                }
                mLastClickTime=SystemClock.elapsedRealtime()
                analyzeBtn.isClickable=true
            }



        }

        classifyproductsViewModel.ingredientResults.observe(viewLifecycleOwner,
            { results ->
                if (results != null) {
                    // display results as outlined in wireframe UI for classify product
                    val sb3 = StringBuilder()
                    val diet = getDietFromCache()
                    for (i in 0..results.size-1){
                        if (diet?.dietType == results[i].diet_type) {
                            sb3.append(results[i].name + " - " + results[i].diet_name + " - SAFE" + "\n")
                        } else {
                            sb3.append(results[i].name + " - " + results[i].diet_name + "\n")
                        }
                    }
                    //Toast.makeText(context?.applicationContext, sb3, Toast.LENGTH_LONG).show()

                    var ingredientStringList = arrayListOf<String>()
                    for (i in 0..results.size-1){
                        ingredientStringList.add(results[i].name + " - " + results[i].diet_name + "\n")
                    }
                    //Toast.makeText(context?.applicationContext, ingredientStringList[0], Toast.LENGTH_LONG).show()

                    val dataAdapter = DataAdapter(ingredientStringList, this)
                     customDialog = CustomListViewDialog(
                        this@ClassifyproductsFragment,
                        dataAdapter,
                        requireContext()
                    )

                    //if we know that the particular variable not null any time ,we can assign !! (not null operator ), then  it won't check for null, if it becomes null, it willthrow exception
                    customDialog!!.show()
                    customDialog!!.setCanceledOnTouchOutside(false)
                }
                else {
                    println("No data found")
                }
            })

    }







//    private val mPictureCallBack = Camera.PictureCallback { data, _ ->
//        val pictureFile: File = getOutputMediaFile(MEDIA_TYPE_IMAGE) ?: run {
//            Log.d(TAG, ("Error creating media file, check storage permissions"))
//            return@PictureCallback
//        }
//
//        try {
//            val fos = FileOutputStream(pictureFile)
//            fos.write(data)
//            fos.close()
//        } catch (e: FileNotFoundException) {
//            Log.d(TAG, "File not found: ${e.message}")
//        } catch (e: IOException) {
//            Log.d(TAG, "Error accessing file: ${e.message}")
//        }
//    }
//
//    private fun getOutputMediaFile(type: Int): File? {
//        // To be safe, you should check that the SDCard is mounted
//        // using Environment.getExternalStorageState() before doing this.
//
//        val mediaStorageDir = File(
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//            "MyCameraApp"
//        )
//         //This location works best if you want the created images to be shared
//         //between applications and persist after your app has been uninstalled.
//
//         //Create the storage directory if it does not exist
//        mediaStorageDir.apply {
//            if (!exists()) {
//                if (!mkdirs()) {
//                    Log.d("MyCameraApp", "failed to create directory")
//                    return null
//                }
//            }
//        }
//
//        // Create a media file name
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        return when (type) {
//            MEDIA_TYPE_IMAGE -> {
//                File("${mediaStorageDir.path}${File.separator}IMG_$timeStamp.jpg")
//            }
//            MEDIA_TYPE_VIDEO -> {
//                File("${mediaStorageDir.path}${File.separator}VID_$timeStamp.mp4")
//            }
//            else -> null
//        }
//    }
//
//    /** A safe way to get an instance of the Camera object. */
//    fun getCameraInstance(): Camera? {
//        return try {
//            Camera.open()// attempt to get a Camera instance
//        } catch (e: Exception) {
//            // Camera is not available (in use or does not exist)
//            null // returns null if camera is unavailable
//        }
//    }



    private fun captureImage(){
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (takePictureIntent.resolveActivity(requireContext().packageManager)!=null){
//            startActivityForResult(takePictureIntent,REQUEST_CODE)
//        }else{
//            Toast.makeText(context?.applicationContext,"Unable to Open Camera", Toast.LENGTH_SHORT).show()
//        }

        val cameraFragment = CameraFragment()
        val transaction:FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.classifyProductFragment,cameraFragment)
        transaction.commit()

    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)

    }

    override fun clickOnItem(data: String) {
        //Synthetic property without calling findViewById() method and supports view caching to improve performance.

        if (customDialog != null) {
            customDialog.dismiss()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                }else{
                    //Toast.makeText(this,"permission denied",Toast.LENGTH_SHORT).show()
                    Toast.makeText(context?.applicationContext,"permission denied",Toast.LENGTH_SHORT).show()
                }
            }
            REQUEST_CODE->{
                if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    captureImage()
                }else{
                    //Toast.makeText(this,"permission denied",Toast.LENGTH_SHORT).show()
                    Toast.makeText(context?.applicationContext,"permission denied",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK && requestCode== IMAGE_PICK_CODE){
            ingredientLabelPicture.setImageURI(data?.data)
        }
        else if (requestCode== REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val takeImage = data?.extras?.get("data") as Bitmap
            ingredientLabelPicture.setImageBitmap(takeImage)

        }
    }
}