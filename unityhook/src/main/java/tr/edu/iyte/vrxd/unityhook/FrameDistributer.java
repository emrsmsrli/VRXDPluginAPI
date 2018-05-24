package tr.edu.iyte.vrxd.unityhook;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.opencv.core.Mat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import dalvik.system.DexClassLoader;
import tr.edu.iyte.vrxd.api.IPlugin;
import tr.edu.iyte.vrxd.api.data.Shape;

@SuppressWarnings("unused")
public class FrameDistributer {
    private static final String LOGTAG = FrameDistributer.class.getSimpleName();
    private static final List<IPlugin> PLUGINS = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public static boolean loadPlugins(Context context) {
        final File pluginsPath = new File(Environment.getExternalStorageDirectory(), "VRXD");
        tryCreateFolder(pluginsPath);

        final File[] pluginFiles = getPlugins(pluginsPath);
        final File pluginsCache = new File(context.getFilesDir(), ".plugin_cache");
        tryCreateFolder(pluginsCache);

        boolean isAnyOpenCv = false;
        for(File pluginFile : pluginFiles) {
            Log.i(LOGTAG, "trying to load " + pluginFile.getName());
            final File dexFolder = new File(context.getCodeCacheDir(), pluginFile.getName());
            tryCreateFolder(dexFolder);

            try {
                final File pluginCache = new File(pluginsCache, pluginFile.getName());
                extractFolder(pluginFile, pluginCache);
                final DexClassLoader loader = new DexClassLoader(pluginFile.getPath(), dexFolder.getPath(),
                        pluginCache.getPath() + File.separator + "lib" + File.separator + "armeabi-v7a",
                        IPlugin.class.getClassLoader());
                final Class<IPlugin> objectClass =
                        (Class<IPlugin>) loader.loadClass(
                                pluginFile.getName().replace(".apk", ".Main"));
                IPlugin plugin = objectClass.newInstance();

                isAnyOpenCv |= plugin.isOpenCvExclusive();

                PLUGINS.add(plugin);
            } catch(Exception ignored) {
            }   // ignore defected plugin
        }

        for(IPlugin plugin : PLUGINS) {
            Log.i(LOGTAG, "plugin loaded: " + plugin.className());
            plugin.onStart(context);
        }

        return isAnyOpenCv;
    }

    private static void tryCreateFolder(File folder) {
        if(!folder.exists() && !folder.mkdirs())
            throw new RuntimeException(folder + " mkdir failed");
    }

    private static File[] getPlugins(File path) {
        return path.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".apk");
            }
        });
    }

    public static void distribute(int frameId, int w, int h, byte[] frame) {
        for(IPlugin plugin : PLUGINS) {
            Log.i(LOGTAG, "frame data received for " + plugin.getClass().toString());
            plugin.onFrame(frameId, w, h, frame);
        }
    }

    public static void distribute(int frameId, Mat frame) {
        for(IPlugin plugin : PLUGINS) {
            Log.i(LOGTAG, "frame mat received for " + plugin.getClass().toString());
            plugin.onFrame(frameId, frame);
        }
    }

    public static String getFrameShapes(int frameId) {
        StringBuilder b = new StringBuilder(128);
        for(IPlugin plugin : PLUGINS) {
            List<Shape> shapes = plugin.getFrameShapes(frameId);
            if(shapes.isEmpty())
                continue;
            for(Shape shape : shapes)
                b.append(shape.toString()).append(":");
            b.deleteCharAt(b.length() - 1).append(";");
        }
        if(b.length() == 0)
            return b.toString();
        return b.deleteCharAt(b.length() - 1).toString();
    }

    public static void log(String msg) {
        Log.d("UnityFrameDistributer", msg);
    }

    private static void extractFolder(File zipFile, File extractFolder) {
        if(extractFolder.exists())
            return;

        final int bufferSize = 2048;
        tryCreateFolder(extractFolder);
        try(ZipFile zip = new ZipFile(zipFile)) {
            Enumeration zipFileEntries = zip.entries();

            while(zipFileEntries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                Log.i(LOGTAG, entry.getName());
                if(entry.isDirectory())
                    continue;
                //if(!entry.getName().startsWith("res/"))
                //    continue;
                File destFile = new File(extractFolder, entry.getName());
                tryCreateFolder(destFile.getParentFile());

                try(BufferedInputStream is = new BufferedInputStream(zip
                        .getInputStream(entry))) {
                    int currentByte;
                    // establish buffer for writing file
                    byte data[] = new byte[bufferSize];

                    // write the current file to disk
                    try(BufferedOutputStream dest = new BufferedOutputStream(
                            new FileOutputStream(destFile),
                            bufferSize)) {

                        // read and write until last byte is encountered
                        while((currentByte = is.read(data, 0, bufferSize)) != -1) {
                            dest.write(data, 0, currentByte);
                        }
                        dest.flush();
                    }
                }
            }
        } catch(Exception e) {
            Log.i(LOGTAG, "ERROR: " + e.getMessage());
        }
    }

    static {
        System.loadLibrary("opencv_java4");
    }
}
