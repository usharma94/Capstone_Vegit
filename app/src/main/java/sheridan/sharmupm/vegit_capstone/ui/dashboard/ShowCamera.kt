package sheridan.sharmupm.vegit_capstone.ui.dashboard

import android.content.Context
import android.content.res.Configuration
import android.hardware.Camera
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException

class ShowCamera(context: Context?, var camera: Camera) : SurfaceView(context), SurfaceHolder.Callback {



    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        if (holder.surface==null){
            return
        }
    }
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        camera.stopPreview()
        camera.release()
    }
    override fun surfaceCreated(holder: SurfaceHolder) {
        val params = camera.parameters
        if (this.resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
            params["orientation"] = "portrait"
            camera.setDisplayOrientation(90)
            params.setRotation(90)
        } else {
            params["orientation"] = "landscape"
            camera.setDisplayOrientation(0)
            params.setRotation(0)
        }
        camera.parameters = params
        try {
            camera.setPreviewDisplay(holder)
            camera.startPreview()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    init {

        holder.addCallback(this)
    }
}