package com.todaytech2.utilitymanager;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostRegisterRequest extends AsyncTask<String, Integer, Long> {



    protected Long doInBackground(String... urls){
        long totalsize = 0;

        try{


            URL url= new URL("http://delivery.mobile-transact.com:8090/jsortcash/v1/register");
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();

            String urlParameters="sn=CO2G8416DRJM&cn=&locale=&caller=&num=12345";
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Key","Value");
            connection.setDoOutput(true);
            DataOutputStream dStream=new DataOutputStream(connection.getOutputStream());
            dStream.writeBytes(urlParameters);
            dStream.flush();
            dStream.close();

            int responseCode=connection.getResponseCode();
            String output="Request URL"+ url;
            output += System.getProperty("line.separator")+"Request Parameters " +urlParameters;
            output += System.getProperty("line.separator")+"Request Code " +responseCode;

            BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line="";
            StringBuilder responseOutput=new StringBuilder();

            while ((line=br.readLine())!=null){
                responseOutput.append(line);
            }
            br.close();
            output +=System.getProperty("line.separator")+responseOutput.toString();



        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return totalsize;
    }
    protected void onProgressUpdate(Integer... progress){

    }
    protected void onPostExecute(Long Result){


    }


}
