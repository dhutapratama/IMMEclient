package com.imme.immeclient;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by sysadm@ilccourse.com on 12/9/2015.
 */

public class WriteReadFile extends Activity {
    // Initial Commit
    public void writeToFile(String varname, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(varname + ".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public String readFromFile(String varname) {
        String ret = "";

        try {
            InputStream inputStream = openFileInput(varname + ".txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("MainActivity", "File not found: " + e.toString());
            if (varname.equals("balance")) {
                writeToFile("balance", "0");
                ret = readFromFile("balance");
            } else {

            }
        } catch (IOException e) {
            Log.e("MainActivity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
