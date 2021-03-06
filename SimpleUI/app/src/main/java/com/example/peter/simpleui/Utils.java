package com.example.peter.simpleui;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Peter on 2016/4/28.
 */
public class Utils {

    public static void writeFile(Context context, String fileName, String content){

        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_APPEND);
            //Context.MODE_APPEND → 模式:讀寫[疊加]
            fos.write(content.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String readFile(Context context, String fileName){

        try {
            FileInputStream fis = context.openFileInput(fileName);
            byte[] buffer = new byte[1024];
            fis.read(buffer, 0, buffer.length);
            fis.close();
            return new String(buffer) ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
//Utils → 工具類class的通常命名