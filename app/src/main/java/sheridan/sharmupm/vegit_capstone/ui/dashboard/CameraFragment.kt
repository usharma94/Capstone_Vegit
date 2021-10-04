package sheridan.sharmupm.vegit_capstone.ui.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.databinding.FragmentCameraBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.lang.Exception
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.fixedRateTimer


class CameraFragment : Fragment() {

    private var imageCapture: ImageCapture?=null
    private lateinit var viewFinder:PreviewView
    private lateinit var outputDirectory: File

    companion object{
    private val PERMISSION_CODE = 1001
        private val IMAGE_PICK_CODE = 1000
        private val REQUEST_CODE = 42}



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewFinder = view.findViewById<PreviewView>(R.id.viewFinder)
        val takePhotoBtn = view.findViewById<ImageButton>(R.id.btnTakePhoto)
        val galleryBtn = view.findViewById<ImageButton>(R.id.mgallery)

         if (allPermissionGranted()){
             //Toast.makeText(context?.applicationContext,"We have permission",Toast.LENGTH_SHORT).show()
             startCamera()
         }else{
             requestPermissions(Constants.REQUIRED_PERMISSION,Constants.REQUEST_CODE_PERMISSION)

         }
        takePhotoBtn.setOnClickListener {
            takePhoto()
        }
        galleryBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if (context?.applicationContext?.let { it1 -> ContextCompat.checkSelfPermission(it1, Manifest.permission.READ_EXTERNAL_STORAGE) } ==PackageManager.PERMISSION_DENIED)
                {
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    requestPermissions(permissions, CameraFragment.PERMISSION_CODE)
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
        startActivityForResult(intent, CameraFragment.IMAGE_PICK_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode== Activity.RESULT_OK && requestCode== CameraFragment.IMAGE_PICK_CODE){
            try{
                val inputStream = activity?.contentResolver?.openInputStream(data?.data!!)
                val takePicture = BitmapFactory.decodeStream(inputStream)
                val bundle = Bundle()
                val classifyproductsFragment = ClassifyproductsFragment()
                bundle.putParcelable("bitmap", takePicture)
                classifyproductsFragment.arguments = bundle
                fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment,classifyproductsFragment)?.commit()

            } catch (e: FileNotFoundException){
                e.printStackTrace()
            }

        }

        if (requestCode== CameraFragment.REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Toast.makeText(requireContext(),"hi",Toast.LENGTH_LONG).show()
            val takeImage = data?.extras?.get("data") as Bitmap
            val classifyproductsFragment = ClassifyproductsFragment()
            val bundle = Bundle()
            bundle.putParcelable("bitmap",takeImage)
            classifyproductsFragment.arguments = bundle
            fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment,classifyproductsFragment)?.commit()

        }
    }



    private fun takePhoto(){
        val imageCapture = imageCapture?:return
        imageCapture.takePicture(ContextCompat.getMainExecutor(context), object :
            ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                //get bitmap from image
                val bitmap = imageProxyToBitmap(image)
                val classifyproductsFragment = ClassifyproductsFragment()

                val bundle = Bundle()
                bundle.putParcelable("bitmap",bitmap)
                classifyproductsFragment.arguments = bundle
                fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment,classifyproductsFragment)?.commit()




                super.onCaptureSuccess(image)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
            }

        })

//        imageCapture.takePicture(
//            ContextCompat.getMainExecutor(context), // Defines where the callbacks are run
//            object : ImageCapture.OnImageCapturedCallback() {
//                @SuppressLint("UnsafeOptInUsageError")
//                override fun onCaptureSuccess(imageProxy: ImageProxy) {
//                    val image: Image? = imageProxy.image // Do what you want with the image
//
//                    imageProxy.close() // Make sure to close the image
//                }
//
//                override fun onError(exception: ImageCaptureException) {
//                    // Handle exception
//                }
//            }
//        )



    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val planeProxy = image.planes[0]
        val buffer: ByteBuffer = planeProxy.buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider:ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {mPreview->
                mPreview.setSurfaceProvider(viewFinder.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try{
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this,cameraSelector,preview,imageCapture)
            }catch(e:Exception){
                Log.d(Constants.TAG,"Start Camera Fail:",e)
            }
        },ContextCompat.getMainExecutor(requireContext()))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_PERMISSION){
            if (allPermissionGranted()){
                startCamera()

            }else{
                Toast.makeText(context?.applicationContext,"Permissions not granted by the user",Toast.LENGTH_SHORT).show()
                //finish()
                getActivity()?.getFragmentManager()?.popBackStack()
            }
        }

    }

    private fun allPermissionGranted()=
        Constants.REQUIRED_PERMISSION.all{
            ContextCompat.checkSelfPermission(context?.applicationContext!!,it)== PackageManager.PERMISSION_GRANTED
        }





}