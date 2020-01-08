package com.example.pklapps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundWorker extends AsyncTask<String,Void,String> {
    Context context;
    private TaskCompleted mCallback;
    BackgroundWorker (Context ctx) {
        context = ctx;
        this.mCallback = (TaskCompleted) ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url = Api.ROOT + "/Android_login/login.php";
        if(type.equals("valid")) {
            try {
                String nama = params[1];
                String tanggal = params[2];
                String stat = params[3];
                String jamMulai = params[4];
                String jamAkhir = params[5];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("nama_ruangan","UTF-8")+"="+URLEncoder.encode(nama,"UTF-8")+"&"
                        +URLEncoder.encode("tanggal_pinjam","UTF-8")+"="+URLEncoder.encode(tanggal,"UTF-8")+"&"
                        +URLEncoder.encode("status_pinjam","UTF-8")+"="+URLEncoder.encode(stat,"UTF-8")+"&"
                        +URLEncoder.encode("waktu_awal","UTF-8")+"="+URLEncoder.encode(jamMulai,"UTF-8")+"&"
                        +URLEncoder.encode("waktu_akhir","UTF-8")+"="+URLEncoder.encode(jamAkhir,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String result) {
        mCallback.onTaskComplete(result);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}