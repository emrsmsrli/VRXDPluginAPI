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

    /**
     * -1 means frame not ready.
     * otherwise might return between 0-4
     */
    fun getFrameObjCount(frameId: Int): Int
    fun getFrameObj(frameId: Int, objIdx: Int): Shape

    fun getResources(): List<String>
}
