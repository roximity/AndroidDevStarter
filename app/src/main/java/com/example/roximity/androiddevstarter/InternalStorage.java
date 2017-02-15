package com.example.roximity.androiddevstarter;

import android.content.Context;
import android.util.Log;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

/**
 * Created by colerichards on 10/11/16.
 */

public final class InternalStorage{

    private InternalStorage() {}

    public static void writeObject(Context context, String key, Object object) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    public static Object readObject(Context context, String key) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object read = null;
        try{
            read = ois.readObject();
        }catch (EOFException e){
            //Completed reading object
            Log.d("Internal Storage","EOF reached");
        }finally{
            if (ois != null){ ois.close();}
            if (fis !=null){fis.close();}
        }

        return read;
    }

    public static void clearCache(Context context, String key) throws IOException{
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.flush();
        oos.close();
        fos.close();
    }
}
