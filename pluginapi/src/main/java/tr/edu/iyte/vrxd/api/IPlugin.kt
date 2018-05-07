package tr.edu.iyte.vrxd.api

import android.content.Context
import org.opencv.core.Mat

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
    fun onFrame(mat: Mat)
    fun onFrame(width: Int, height: Int, bytes: ByteArray)

    fun getResources(): List<String>
}
