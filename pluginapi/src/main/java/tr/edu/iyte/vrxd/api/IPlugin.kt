package tr.edu.iyte.vrxd.api

import android.content.Context
import org.opencv.core.Mat
import tr.edu.iyte.vrxd.api.data.Shape

interface IPlugin {
    fun className() = this.javaClass.name!!
    fun isOpenCvExclusive(): Boolean

    fun onStart(ctx: Context)
    fun onResume(ctx: Context)
    fun onPause(ctx: Context)
    fun onStop(ctx: Context)

    /*fun onConfigurationRequested()
    fun onConfigure(configs: Con)*/

    // opencv overload
    fun onFrame(frameId: Int, mat: Mat)
    fun onFrame(frameId: Int, width: Int, height: Int, bytes: ByteArray)

    fun getFrameShapes(frameId: Int): String

    fun getResources(): List<String>
}
