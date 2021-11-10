package sheridan.sharmupm.vegit_capstone.ui.dashboard

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONObject
import sheridan.sharmupm.vegit_capstone.R
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match

class BarcodeReaderFragment : Fragment() {
    private val client = OkHttpClient()
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetView:View
    private lateinit var barcodeImg:ImageView

    companion object
    {private val PERMISSION_CODE = 1001
        private val IMAGE_PICK_CODE = 1000}

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_barcode_reader, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val barcodeImageView = view.findViewById<ImageView>(R.id.barcode_view)
        val analyzeBtn = view.findViewById<Button>(R.id.analyze_btn)
        val galleryBtn = view.findViewById<ImageButton>(R.id.img_pick_btn)
        barcodeImg = view.findViewById<ImageView>(R.id.barcode_view)

        val args = this.arguments
        if (args?.isEmpty == false){
            val image = args.get("bitmap")
            barcodeImageView.setImageBitmap(image as Bitmap?)
            args.clear()
        }

        galleryBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if (context?.applicationContext?.let { it1 ->
                        ContextCompat.checkSelfPermission(
                            it1,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    } == PackageManager.PERMISSION_DENIED)
                {
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, BarcodeReaderFragment.PERMISSION_CODE)
                } else {
                    pickImageFromGallery()
                }
            } else {
                pickImageFromGallery()
            }
        }

        analyzeBtn.setOnClickListener {
            if (barcodeImageView.drawable == null){
                Toast.makeText(context,"No Picture Detected!", Toast.LENGTH_SHORT)
            }
            else{
                val mBitmap = barcodeImageView.drawable.toBitmap()
                val barcodeDetector =  BarcodeDetector.Builder(context).build()
                if (barcodeDetector.isOperational){
                    val frame = Frame.Builder().setBitmap(mBitmap).build()
                    val sparseArr = barcodeDetector.detect(frame)
                    if (sparseArr != null && sparseArr.size()>0){
//                        for (i in 0..sparseArr.size()-1){
//                            Log.d(LOG_TAG,"Value: " + sparseArr.valueAt(i).rawValue+"---"+sparseArr.valueAt(i).displayValue)
//                            Toast.makeText(LOG_TAG,sparseArr.valueAt(i).rawValue,Toast.LENGTH_LONG).show()
//                            val myItem = items.valueAt(i)
//                            sb.append(myItem.value)
//                            sb.append("\n")
//                        }
                        var code = sparseArr.valueAt(0)
                        val url = "https://api.upcitemdb.com/prod/trial/lookup?upc="+code.rawValue
                        //AsyncTaskHandleJson().execute(url)
                        run(url)


                    }
                    else{
                        Toast.makeText(context,"No Barcode detected",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, BarcodeReaderFragment.IMAGE_PICK_CODE)
    }


    fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()



        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onResponse(call: Call, response: Response){
                if (response.isSuccessful){
                    activity?.runOnUiThread {
                        // Toast.makeText(this@MainActivity, response.body()?.string(), Toast.LENGTH_SHORT).show()
                        val jsonObject = JSONObject(response.body()?.string())
                        //val jsonObject = JSONObject(Gson().toJson(response.body()))
                        val itemsArr = jsonObject.getJSONArray("items")
                        val items = itemsArr.getJSONObject(0)
                        val title = items.getString("title")
                        val category = items.getString("category")
                        val lowestPrice = items.getDouble("lowest_recorded_price")
                        val highestPrice = items.getDouble("highest_recorded_price")
                        val pictureArray = items.getJSONArray("images")
                        val picture = pictureArray.getString(0)
                        val upc = items.getString("upc")
                        //Toast.makeText(context,title,Toast.LENGTH_LONG).show()
                        bottomSheetDialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialogTheme)
                        bottomSheetView = LayoutInflater.from(context).inflate(R.layout.barcode_dialog_layout,
                            view?.findViewById(R.id.barcode_dialog)
                        )
                        bottomSheetDialog.setContentView(bottomSheetView)
                        val txtProductName = bottomSheetView.findViewById<TextView>(R.id.product_name)
                        val txtProductCategory = bottomSheetView.findViewById<TextView>(R.id.product_category)
                        val txtLowestPrice = bottomSheetView.findViewById<TextView>(R.id.product_lowest_price)
                        val txtHighestPrice = bottomSheetView.findViewById<TextView>(R.id.product_highest_price)
                        val txtUPC = bottomSheetView.findViewById<TextView>(R.id.product_upc)
                        val productImg = bottomSheetView.findViewById<ImageView>(R.id.product_picture)
                        val barcodeBtn = bottomSheetView.findViewById<Button>(R.id.btn_scan_barcode)
                        val ingredientBtn = bottomSheetView.findViewById<Button>(R.id.btn_scan_ingredient)
                        val barcodeScannerFragment = BarcodeScannerFragment()
                        val cameraFragment = CameraFragment()
                        txtProductName.setText(title)
                        txtProductCategory.setText(category)
                        txtUPC.setText(upc)
                        txtLowestPrice.setText(lowestPrice.toString())
                        txtHighestPrice.setText(highestPrice.toString())
                        Picasso.get().load(Uri.parse(picture)).into(productImg)
                        barcodeBtn.setOnClickListener {
                            fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment,barcodeScannerFragment)?.commit()
                            bottomSheetDialog.dismiss()
                        }
                        ingredientBtn.setOnClickListener { fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment,cameraFragment)?.commit()
                            bottomSheetDialog.dismiss()
                        }


                        bottomSheetDialog.show()

                    }
                }
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            BarcodeReaderFragment.PERMISSION_CODE -> {
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
        if (resultCode== Activity.RESULT_OK && requestCode== BarcodeReaderFragment.IMAGE_PICK_CODE){
            barcodeImg.setImageURI(data?.data)
        }

    }
}