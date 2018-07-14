package com.example.d_f_h.cdistancia;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConsultURL {

    public String getUrlData(String googleUrl) throws IOException{
        String jsonData="";

        HttpURLConnection connection = null;

        try{
            URL url = new URL(googleUrl);

            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();
            BufferedReader reader= new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }

            jsonData=buffer.toString();
            stream.close();
            reader.close();
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally{
            if(connection != null){
                connection.disconnect();
            }
        }

        Log.d("json = ", jsonData);

        return jsonData;
    }

}
