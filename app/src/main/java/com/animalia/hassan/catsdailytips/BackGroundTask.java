package com.animalia.hassan.catsdailytips;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.animalia.hassan.catsdailytips.database.CatsTandQHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Hassan on 6/4/2016.
 */

public class BackGroundTask extends AsyncTask<String,Void,String> {
    Context ctx;

    public AsyncResponse delegate = null;
    String json_url_t = BuildConfig.json_url_t;
    String json_url_q = BuildConfig.json_url_q;

   public BackGroundTask(Context ctx)
    {
        this.ctx = ctx;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public BackGroundTask(AsyncResponse delegate){
        this.delegate = delegate;
    }


    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected String doInBackground(String... params) {

        String Passed_url = params[0];

        try {
            URL url = new URL(params[0]);
            HttpURLConnection httpURLConnection;
            httpURLConnection = (HttpURLConnection)url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line=bufferedReader.readLine())!=null)
            {
                stringBuilder.append(line+"\n");
            }
            httpURLConnection.disconnect();
            String json_data = stringBuilder.toString().trim();
            JSONObject jsonObject =new JSONObject(json_data);
            JSONArray jsonArray = jsonObject.getJSONArray("server_response");
            Log.d("JSON STRING",json_data);
            CatsTandQHelper catsTandQHelper = CatsTandQHelper.getInstance(ctx);
            SQLiteDatabase db = catsTandQHelper.getReadableDatabase();

            int count = 0;
            while (count<jsonArray.length())
            {
                JSONObject JO = jsonArray.getJSONObject(count);
                count++;
                if (Passed_url.equals(json_url_t)) {

                catsTandQHelper.putInformationCats(JO.getInt("_id"), JO.getString("question"),
                        JO.getString("answer"), JO.getString("favourite"),
                        JO.getInt("q_note_id"), JO.getString("comment"), db);
            } else if(Passed_url.equals(json_url_q)){
                    catsTandQHelper.putInformationCatsQ(JO.getInt("_id"), JO.getString("question"),
                            JO.getString("answer"), JO.getString("favourite"),
                            JO.getInt("q_note_id"), JO.getString("comment"), db);

                }

            }
            catsTandQHelper.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
