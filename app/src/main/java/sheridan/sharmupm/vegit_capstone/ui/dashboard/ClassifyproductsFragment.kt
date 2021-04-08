package sheridan.sharmupm.vegit_capstone.ui.dashboard
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.classifyProducts.ClassifyproductsViewModel
import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientName

class ClassifyproductsFragment : Fragment() {

    private lateinit var classifyproductsViewModel: ClassifyproductsViewModel
    private lateinit var ingredientLabelPicture: ImageView

    companion object{
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ingredientLabelPicture = view.findViewById<ImageView>(R.id.imageView)
        val galleryBtn = view.findViewById<ImageButton>(R.id.gallery)
        val analyzeBtn = view.findViewById<Button>(R.id.takePicture)
        val scanResult = view.findViewById<TextView>(R.id.text_view)

        // DEMO PURPOSE FOR Wang =========================================

        // call this passing in ingredientName list
        // you can move it to where you think is most appropriate in this class

        //classifyproductsViewModel.searchIngredientList(ingredientName list...)

        // observe the classifyproductsViewModel.ingredientResults live data for change
        // and display results when it has data
        // you can refer to login/search/user fragments for examples

        // DEMO PURPOSE FOR Wang =========================================

        //Select picture from gallery, the picture is saved locally in my simulator now


        galleryBtn.setOnClickListener{
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if (context?.applicationContext?.let { it1 -> checkSelfPermission(it1,android.Manifest.permission.READ_EXTERNAL_STORAGE) } ==PackageManager.PERMISSION_DENIED)
                {
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE);
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

        //Google OCR: extract text from the ingredient label and saved as string and analysis the string and return the
        //result if the food is safe to eat
        //**** the "analyze" is hard-code now***
        analyzeBtn.setOnClickListener {
            val mBitmap = ingredientLabelPicture.getDrawable().toBitmap()
            val textRecognizer = TextRecognizer.Builder(context?.applicationContext).build()
            if (!textRecognizer.isOperational) {
                Toast.makeText(context?.applicationContext, "Could not get the text", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val frame = Frame.Builder().setBitmap(mBitmap).build()
                val items = textRecognizer.detect(frame)

                val sb = StringBuilder()
                val ingredientNameList = arrayListOf<IngredientName>()
                var mIngredientNameList = listOf<String>()
                for (i in 0..items.size() - 1) {
                    val myItem = items.valueAt(i).value
                    sb.append(myItem)
                    sb.append("\n")
                    //ingredientName.name = myItem
                    //ingredientNameList.add(ingredientName)

                }

                mIngredientNameList = sb.split(",", ";")

                for (ingredient in mIngredientNameList){
                    val ingredientName = IngredientName()
                    ingredientName.name = ingredient
                    ingredientNameList.add(ingredientName)
                }
                classifyproductsViewModel.searchIngredientList(ingredientNameList)
//                classifyproductsViewModel.ingredientResults.observe(viewLifecycleOwner,
//                    { results ->
//                        if (results != null) {
//                           val sb3 = StringBuilder()
//                            for (i in 0..results.size-1){
//                                sb3.append(results[i].name)
//                            }
//                            Toast.makeText(context?.applicationContext, sb3, Toast.LENGTH_LONG).show()
//                        }
//                        else {
//                            println("No data found")
//                        }
//                    })

//                val sb2 = StringBuilder()
//                for (i in 0..ingredientNameList.size-1){
//                    sb2.append(ingredientNameList[i].name)
//
//                }
//                Toast.makeText(context?.applicationContext, sb2, Toast.LENGTH_LONG).show()


            }

        }
        classifyproductsViewModel.ingredientResults.observe(viewLifecycleOwner,
            { results ->
                if (results != null) {
                    val sb3 = StringBuilder()
                    for (i in 0..results.size-1){
                        sb3.append(results[i].name)
                    }
                    Toast.makeText(context?.applicationContext, sb3, Toast.LENGTH_LONG).show()
                }
                else {
                    println("No data found")
                }
            })
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)

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
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK && requestCode== IMAGE_PICK_CODE){
            ingredientLabelPicture?.setImageURI(data?.data)
        }
    }
}