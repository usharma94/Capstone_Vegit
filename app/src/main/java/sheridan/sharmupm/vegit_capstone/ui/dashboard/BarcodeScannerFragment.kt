package sheridan.sharmupm.vegit_capstone.ui.dashboard

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.SparseArray
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import androidx.fragment.app.Fragment
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONObject
import sheridan.sharmupm.vegit_capstone.R
import java.io.FileNotFoundException
import java.io.IOException


class BarcodeScannerFragment : Fragment() {
    private lateinit var cameraSource:CameraSource
    private lateinit var detector:BarcodeDetector
    private lateinit var surfaceView:SurfaceView
    private val client = OkHttpClient()
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetView:View

    private var completed = false


    companion object{
        private val PERMISSION_CODE = 1001
        private val IMAGE_PICK_CODE = 1000
        private val requestCodeCameraPermission = 1002
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_barcode_scanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val galleryBtn = view.findViewById<ImageButton>(R.id.gallery_btn)
        surfaceView = view.findViewById(R.id.camera_preview)






        surfaceView.visibility = View.GONE
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            askForCameraPermission()
            surfaceView.visibility = View.VISIBLE
        }else{
            surfaceView.visibility = View.VISIBLE
            setupControls()

        }
        galleryBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if (context?.applicationContext?.let { it1 -> ContextCompat.checkSelfPermission(
                        it1,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) } ==PackageManager.PERMISSION_DENIED)
                {
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, BarcodeScannerFragment.PERMISSION_CODE)
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
    }


    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, BarcodeScannerFragment.IMAGE_PICK_CODE)

    }

    private fun setupControls(){
        detector = BarcodeDetector.Builder(context).build()
        cameraSource = CameraSource.Builder(context, detector).setAutoFocusEnabled(true).build()
        surfaceView.holder.addCallback(surgaceCallBack)
        detector.setProcessor(processor)


    }

    private fun askForCameraPermission(){
        requestPermissions(arrayOf(Manifest.permission.CAMERA), requestCodeCameraPermission)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode== Activity.RESULT_OK && requestCode== BarcodeScannerFragment.IMAGE_PICK_CODE){
            try{
                val inputStream = activity?.contentResolver?.openInputStream(data?.data!!)
                val takePicture = BitmapFactory.decodeStream(inputStream)
                val bundle = Bundle()
                val barcodeReaderFragment = BarcodeReaderFragment()
                bundle.putParcelable("bitmap", takePicture)
                barcodeReaderFragment.arguments = bundle
                fragmentManager?.beginTransaction()?.replace(
                    R.id.nav_host_fragment,
                    barcodeReaderFragment
                )?.commit()

            } catch (e: FileNotFoundException){
                e.printStackTrace()
            }

        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_PERMISSION){
            when(requestCode){
                PERMISSION_CODE -> {
                    if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        pickImageFromGallery()
                    } else {
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                setupControls()
            }else{
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show()
            }
        }

    }

    private val surgaceCallBack = object : SurfaceHolder.Callback{
        override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
            try{
                if (context?.let {
                        ActivityCompat.checkSelfPermission(
                            it,
                            Manifest.permission.CAMERA
                        )
                    } != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                cameraSource.start(surfaceHolder)

            }catch (exception: Exception){
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            //Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show()
            if (context?.let {
                    ActivityCompat.checkSelfPermission(
                        it,
                        Manifest.permission.CAMERA
                    )
                } != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            cameraSource.start(holder)
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            cameraSource.stop()
        }
    }

    private val processor = object : Detector.Processor<Barcode>{
        override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
            if (detections!=null && detections.detectedItems.isNotEmpty()){
                val barcodes:SparseArray<Barcode> = detections.detectedItems
                val code = barcodes.valueAt(0)
                val url = "https://api.upcitemdb.com/prod/trial/lookup?upc="+code.rawValue
                //AsyncTaskHandleJson().execute(url)
                run(url)
                //Toast.makeText(context,code.displayValue,Toast.LENGTH_LONG).show()
            }

        }

        override fun release() {
            //bottomSheetDialog.dismiss()

        }
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

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful && !completed) {
                    activity?.runOnUiThread {
                        completed = true
                        // Toast.makeText(this@MainActivity, response.body()?.string(), Toast.LENGTH_SHORT).show()
                        val jsonObject = JSONObject(response.body()?.string())
                        //val jsonObject = JSONObject(Gson().toJson(response.body()))
                        val itemsArr = jsonObject.getJSONArray("items")
                        if (itemsArr.length() == 0) {
                            return@runOnUiThread
                        }
                        val items = itemsArr.getJSONObject(0)
                        val title = items.getString("title")
                        val category = items.getString("category")
                        val lowestPrice = items.getDouble("lowest_recorded_price")
                        val highestPrice = items.getDouble("highest_recorded_price")
                        val pictureArray = items.getJSONArray("images")
                        val picture = pictureArray.getString(0)
                        val upc = items.getString("upc")

                        val sharedPref = activity!!.getSharedPreferences("myPref", Context.MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        editor.apply{
                            putString("title",title)
                            putString("category",category)
                            putString("imageUrl",picture)
                            apply()
                        }
                        //Toast.makeText(context,title,Toast.LENGTH_LONG).show()
                        bottomSheetDialog = BottomSheetDialog(
                            requireContext(),
                            R.style.BottomSheetDialogTheme
                        )
                        bottomSheetView = LayoutInflater.from(context).inflate(
                            R.layout.barcode_dialog_layout,
                            view?.findViewById(R.id.barcode_dialog)
                        )
                        bottomSheetDialog.setContentView(bottomSheetView)
                        val txtProductName =
                            bottomSheetView.findViewById<TextView>(R.id.product_name)
                        val txtProductCategory =
                            bottomSheetView.findViewById<TextView>(R.id.product_category)
                        val txtLowestPrice =
                            bottomSheetView.findViewById<TextView>(R.id.product_lowest_price)
                        val txtHighestPrice =
                            bottomSheetView.findViewById<TextView>(R.id.product_highest_price)
                        val txtUPC = bottomSheetView.findViewById<TextView>(R.id.product_upc)
                        val productImg =
                            bottomSheetView.findViewById<ImageView>(R.id.product_picture)
                        val barcodeBtn = bottomSheetView.findViewById<Button>(R.id.btn_scan_barcode)
                        val ingredientBtn =
                            bottomSheetView.findViewById<Button>(R.id.btn_scan_ingredient)
                        val barcodeScannerFragment = BarcodeScannerFragment()
                        val cameraFragment = CameraFragment()
                        txtProductName.text = title
                        txtProductCategory.text = category
                        txtUPC.text = upc
                        txtLowestPrice.text = lowestPrice.toString()
                        txtHighestPrice.text = highestPrice.toString()
                        Picasso.get().load(Uri.parse(picture)).into(productImg)
                        barcodeBtn.setOnClickListener {
                            fragmentManager?.beginTransaction()?.replace(
                                R.id.nav_host_fragment,
                                barcodeScannerFragment
                            )?.commit()
                            completed = false
                            bottomSheetDialog.dismiss()
                        }
                        ingredientBtn.setOnClickListener {
                            fragmentManager?.beginTransaction()?.replace(
                                R.id.nav_host_fragment,
                                cameraFragment
                            )?.commit()
                            completed = false
                            bottomSheetDialog.dismiss()
                        }


                        bottomSheetDialog.show()

                    }
                }
            }
        })
    }


}