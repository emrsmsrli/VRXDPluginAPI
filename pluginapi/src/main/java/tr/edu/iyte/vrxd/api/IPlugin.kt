package tr.edu.iyte.vrxd.api

import android.content.Context

interface IPlugin {
    fun className() = this.javaClass.name!!

    fun onStart(ctx: Context)
    fun onResume(ctx: Context)
    fun onPause(ctx: Context)
    fun onStop(ctx: Context)

    /*fun onConfigurationRequested()
    fun onConfigure(configs: Con)*/

    fun onFrame(bytes: ByteArray)

    fun getResources(): List<String>
}
