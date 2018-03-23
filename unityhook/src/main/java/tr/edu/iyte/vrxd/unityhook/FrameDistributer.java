package tr.edu.iyte.vrxd.unityhook;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexClassLoader;
import tr.edu.iyte.vrxd.api.IPlugin;

public class FrameDistributer {
    private static final String LOGTAG = FrameDistributer.class.getSimpleName();
    private static List<IPlugin> plugins = new ArrayList<>();
    private static Map<IPlugin, List<URL>> resources = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static boolean loadPlugins() {
        final File pluginsPath = new File(Environment.getExternalStorageDirectory().getPath()
                + File.separator + "VRXD");
        if(!pluginsPath.exists())
            if(!pluginsPath.mkdirs())
                throw new RuntimeException("could not generate plugin paths");

        final File[] pluginFiles = getPlugins(pluginsPath);
        for(File pluginFile: pluginFiles) {
            final String dexPath = pluginFile + File.separator + "dex";
            final DexClassLoader loader = new DexClassLoader(pluginFile.getPath(), dexPath,
                    null, IPlugin.class.getClassLoader());
            try {
                loader.getResources("");
                // TODO figure out these resources shit and objectCll"ass cast to IPLugin
                final Class<IPlugin> objectClass = (Class<IPlugin>) loader.loadClass(pluginFile.getName()+ ".Main");
            } catch(ClassNotFoundException e) {
                Log.e(LOGTAG, "CLASSNOTFOUND: " + pluginFile);
                return false;
            } catch(IOException e) {
                final String msg = "IOEXCEPTION while loading class: " + pluginFile;
                Log.e(LOGTAG, msg);
                return false;
                //throw new RuntimeException(msg);
            }
        }

        for(IPlugin plugin : plugins) {
            Log.i("FRAMEDIST", "plugin: " + plugin.className());
        }

        return true;
    }

    private static File[] getPlugins(File path) {
        return path.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".apk");
            }
        });
    }

    public static void distribute(Context context, byte[] frame) {
        /*final String libPath = "/sdcard/plugin/plugin.apk";
        final String dexPath = context.getDir("dex", 0).getPath() + "/plugin";
        final DexClassLoader loader = new DexClassLoader(libPath, dexPath, null, context.getClass().getClassLoader());
        try {
            final Class<Object> objectClass = (Class<Object>) loader.loadClass("");
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }*/
    }
}
