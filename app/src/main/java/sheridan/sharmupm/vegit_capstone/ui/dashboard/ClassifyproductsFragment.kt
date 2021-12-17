package sheridan.sharmupm.vegit_capstone.ui.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.Camera
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
import java.util.*


class ClassifyproductsFragment : Fragment(),DataAdapter.RecyclerViewItemClickListener {

    private lateinit var classifyproductsViewModel: ClassifyproductsViewModel
    private lateinit var ingredientLabelPicture: ImageView
    private lateinit var customDialog:CustomListViewDialog
    private lateinit var mItem:String
    private lateinit var mCategory:String
    private lateinit var mImageUrl:String
    private var mLastClickTime:Long = 0


    var showCamera: ShowCamera? = null



    companion object{
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
        private val REQUEST_CODE = 42
        val item = "item"
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
        ingredientLabelPicture = view.findViewById(R.id.imageView)
        val galleryBtn = view.findViewById<ImageButton>(R.id.gallery)
        val analyzeBtn = view.findViewById<Button>(R.id.takePicture)
        val framelayout = view.findViewById<FrameLayout>(R.id.frame_layout)
        val captureImage = view.findViewById<ImageButton>(R.id.capture_picture)
        val sharedPref = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)
        mItem = sharedPref.getString("title",null)!!
        mCategory = sharedPref.getString("category",null)!!
        mImageUrl = sharedPref.getString("imageUrl",null)!!
        println(mCategory)

// get image bitmap from camera Fragment
        val args = this.arguments
        if (args?.isEmpty == false){
            val image = args.get("bitmap")
            ingredientLabelPicture.setImageBitmap(image as Bitmap?)
            args.clear()
        }

        //Upload image from gallery.

        galleryBtn.setOnClickListener{
            framelayout.removeView(showCamera)
            if (ingredientLabelPicture.drawable != null){
                ingredientLabelPicture.setImageBitmap(null)

            }
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if (context?.applicationContext?.let { it1 -> checkSelfPermission(it1,android.Manifest.permission.READ_EXTERNAL_STORAGE) } ==PackageManager.PERMISSION_DENIED)
                {
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    pickImageFromGallery()
                }
            } else {
                pickImageFromGallery()
            }
        }
//take picture
        captureImage.setOnClickListener{


            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if (context?.applicationContext?.let { it1 -> checkSelfPermission(it1,android.Manifest.permission.CAMERA) } ==PackageManager.PERMISSION_DENIED)
                {
                    val permissions = arrayOf(android.Manifest.permission.CAMERA)
                    requestPermissions(permissions, REQUEST_CODE)
                } else {
                    captureImage()
                }
            } else {
                captureImage()
            }

            //capture the image and show in the image View Code......
            //imageView -> click "take picture" button -> picture shown in the imageView
            //imageView -> click"take picture" button -> CameraView -> click "take picture" button -> picture shown in the imageView
        }

        //Google OCR: extract text from the ingredient label and saved as string and analysis the string and return the
        //result if the food is safe to eat
        analyzeBtn.setOnClickListener {
            if (analyzeBtn.isClickable==true){
                if(SystemClock.elapsedRealtime()-mLastClickTime<1000){
                    analyzeBtn.isClickable=false
                }
                else{

                    if (ingredientLabelPicture.drawable ==null){
                        Toast.makeText(context?.applicationContext, "No Picture Detected!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    else{

                        //OCR: Google Vision API
                        val mBitmap = ingredientLabelPicture.drawable.toBitmap()
                        //detect text
                        val textRecognizer = TextRecognizer.Builder(context?.applicationContext).build()
                        // error handling, if the OCR cannot work.
                        if (!textRecognizer.isOperational) {
                            Toast.makeText(context?.applicationContext, "Could not get the text", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            //extract text
                            val frame = Frame.Builder().setBitmap(mBitmap).build()
                            val items = textRecognizer.detect(frame)

                            val ingredientList = classifyproductsViewModel.extractIngredientText(items)
                            if (ingredientList != null) {
                                //classifyproductsViewModel.searchIngredientList("unknown", ingredientList) // item name will come from barcode scan
                                classifyproductsViewModel.searchBarcodeIngredientList(mItem,ingredientList,mCategory,mImageUrl)
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

        classifyproductsViewModel.results.observe(viewLifecycleOwner,
            { results ->
                if (results != null) {
                    classifyproductsViewModel.parseResults(results)
                }
                else {
                    println("No data found")
                }
            })


        //observe the OCR result and return result 'Analyze'

        classifyproductsViewModel.ingredientList.observe(viewLifecycleOwner,
            { ingredients ->
                if (ingredients != null) {
                    classifyproductsViewModel.similarProducts.observe(viewLifecycleOwner,
                        { products ->
                            val dataAdapter = DataAdapter(ingredients, this)
                            //Dialog shows
                            customDialog = CustomListViewDialog(
                                this@ClassifyproductsFragment,
                                dataAdapter,
                                products,
                                ingredients,
                                requireContext()
                            )


                            //if we know that the particular variable not null any time ,we can assign !! (not null operator ), then  it won't check for null, if it becomes null, it willthrow exception

                            customDialog.show()

                            if (customDialog.isShowing){
                                analyzeBtn.isClickable=true

                            }

                            customDialog.setCanceledOnTouchOutside(false)
                        })
                }
                else {
                    println("No data found")
                }
            })
    }





    private fun captureImage(){

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

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentManager?.popBackStack()
    }
}