package com.imme.immeclient;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by dhuta on 21/12/2015.
 */
public class GetImage {
    public static File file;
    InputStream is;
    public static void productLookup() throws IOException {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        file = new File(path, "a.jpg");

        URL url = new URL("http://cdn.id.techinasia.com/wp-content/uploads/2015/02/GEPI.jpg");
        URLConnection connection = url.openConnection();
        InputStream input = connection.getInputStream();
        FileOutputStream output = new FileOutputStream(file);
        byte[] data = new byte[1024];

        output.write(data);
        output.flush();
        output.close();
        input.close();
    }
}
