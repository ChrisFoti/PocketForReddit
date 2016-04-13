package edu.illinois.foti2.pocketforreddit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by Chris on 4/12/2016.
 */
public class RemoteData {

    public static HttpURLConnection getConnection(String url){
        System.out.println("URL: " + url);
        HttpURLConnection con = null;
        try{
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setReadTimeout(30000); //30 seconds
        }
        catch (MalformedURLException e){
            Log.e("getConnection()", "Invalid URL: " + e.toString());
        }
        catch (IOException e){
            Log.e("getConnection()", "Could not connect: " + e.toString());
        }

        return con;
    }

    public static String readContents(String url){
        HttpURLConnection con = getConnection(url);
        if(con==null){
            return null;
        }

        try {
            StringBuilder sb = new StringBuilder(8192);
            String tmp = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((tmp=br.readLine())!=null){
                sb.append(tmp).append("\n");
            }
            br.close();
            return sb.toString();
        }
        catch(IOException e){
            Log.d("READ FAILED", e.toString());
            return null;
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}


