package sheridan.sharmupm.vegit_capstone.ui.dashboard

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.*
import android.widget.*
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import java.nio.ByteBuffer


class BarcodeScannerFragment : Fragment() {
    private lateinit var cameraSource:CameraSource
    private lateinit var detector:BarcodeDetector
    private lateinit var surfaceView:SurfaceView
    private val client = OkHttpClient()
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetView:View
    private lateinit var viewFinder: PreviewView
    private var imageCapture: ImageCapture?=null

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val galleryBtn = view.findViewById<ImageButton>(R.id.gallery_btn)
        val takePhotoBtn = view.findViewById<ImageButton>(R.id.btnTakePhoto)
        viewFinder = view.findViewById<PreviewView>(R.id.viewFinder)

        if (allPermissionGranted()){
            //Toast.makeText(context?.applicationContext,"We have permission",Toast.LENGTH_SHORT).show()
            startCamera()
        }else{
            requestPermissions(Constants.REQUIRED_PERMISSION,Constants.REQUEST_CODE_PERMISSION_CAMERA)

        }
        takePhotoBtn.setOnClickListener {
            takePhoto()
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




    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        imageCapture.takePicture(ContextCompat.getMainExecutor(context), object :
            ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                //get bitmap from image
                val bitmap = imageProxyToBitmap(image)
                val rotateBitmap = bitmap.rotate(90.0f)


                val barcodeReaderFragment = BarcodeReaderFragment()

                val bundle = Bundle()
                bundle.putParcelable("bitmap", rotateBitmap)
                barcodeReaderFragment.arguments = bundle
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.nav_host_fragment, barcodeReaderFragment)?.commit()




                super.onCaptureSuccess(image)
            }

        })
    }

    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val planeProxy = image.planes[0]
        val buffer: ByteBuffer = planeProxy.buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private fun allPermissionGranted()=
        Constants.REQUIRED_PERMISSION.all{
            ContextCompat.checkSelfPermission(context?.applicationContext!!,it)== PackageManager.PERMISSION_GRANTED
        }


    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, BarcodeScannerFragment.IMAGE_PICK_CODE)

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
        if (requestCode == Constants.REQUEST_CODE_PERMISSION_CAMERA){
            if (allPermissionGranted()){
                startCamera()

            }else{
                Toast.makeText(context?.applicationContext,"Permissions not granted by the user",Toast.LENGTH_SHORT).show()
                //finish()
                activity?.fragmentManager?.popBackStack()
            }
        }




    }
    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also { mPreview->
                mPreview.setSurfaceProvider(viewFinder.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try{
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this,cameraSelector,preview,imageCapture)
            }catch(e: java.lang.Exception){
                Log.d(Constants.TAG,"Start Camera Fail:",e)
            }
        },ContextCompat.getMainExecutor(requireContext()))
    }

//    private val surgaceCallBack = object : SurfaceHolder.Callback{
//        override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
//            try{
//                if (context?.let {
//                        ActivityCompat.checkSelfPermission(
//                            it,
//                            Manifest.permission.CAMERA
//                        )
//                    } != PackageManager.PERMISSION_GRANTED
//                ) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return
//                }
//                cameraSource.start(surfaceHolder)
//
//            }catch (exception: Exception){
//                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
//            }
//        }
//
//        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
//            //Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show()
//            if (context?.let {
//                    ActivityCompat.checkSelfPermission(
//                        it,
//                        Manifest.permission.CAMERA
//                    )
//                } != PackageManager.PERMISSION_GRANTED
//            ) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return
//            }
//            cameraSource.start(holder)
//        }
//
//        override fun surfaceDestroyed(holder: SurfaceHolder) {
//            cameraSource.stop()
//        }
//    }







}