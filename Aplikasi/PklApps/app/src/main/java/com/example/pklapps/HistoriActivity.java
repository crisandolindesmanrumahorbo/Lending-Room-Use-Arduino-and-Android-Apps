package com.example.pklapps;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HistoriActivity extends AppCompatActivity {
    Button btBook,btProfil,able;
    TextView temp;
    ListView listView;
    private static final int CODE_GET_REQUEST = 1024;
    List<Kelas> kelasList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histori);

        btBook = (Button) findViewById(R.id.btBook);
        btProfil = (Button) findViewById(R.id.btProfil);
        able = (Button) findViewById(R.id.able);
        temp = (TextView) findViewById(R.id.temp);
        listView = (ListView) findViewById(R.id.listView);

        User user = SharedPrefManager.getInstance(this).getUser();
        temp.setText(user.getNim());
        kelasList = new ArrayList<>();
        readKelas();

        btBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoriActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });

        btProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoriActivity.this,ProfilActivity.class);
                startActivity(intent);
            }
        });

        able.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoriActivity.this,Available.class);
                startActivity(intent);
            }
        });
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        //the url where we need to send the request
        String url;

        //the parameters
        HashMap<String, String> params;

        //the request code to define whether it is a GET or POST
        int requestCode;

        //constructor to initialize values
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        //when the task started displaying a progressbar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    if (!object.getString("message").equals("a"))
                        Toast.makeText(HistoriActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    //refreshing the herolist after every operation
                    //so we get an updated list
                    //we will create this method right now it is commented
                    //because we haven't created it yet
                    refreshKelasList(object.getJSONArray("kelas"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //the network operation will be performed in background
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    class KelasAdapter extends ArrayAdapter<Kelas> {

        //our hero list
        List<Kelas> kelasList;


        //constructor to get the list
        public KelasAdapter(List<Kelas> kelasList) {
            super(HistoriActivity.this, R.layout.layout_kelas_list, kelasList);
            this.kelasList = kelasList;
        }


        //method returning list item
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_kelas_list, null, true);

            //getting the textview for displaying name
            TextView textViewName = listViewItem.findViewById(R.id.textViewName);

            TextView textViewUpdate = listViewItem.findViewById(R.id.textViewUpdate);
            TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);
            TextView textViewDone = listViewItem.findViewById(R.id.textViewDone);

            textViewUpdate.setVisibility(View.GONE);
            textViewDelete.setVisibility(View.GONE);
            textViewDone.setVisibility(View.GONE);

            final Kelas kelas = kelasList.get(position);

            textViewName.setText(kelas.getNama());

            return listViewItem;
        }
    }

    private void readKelas() {
        HistoriActivity.PerformNetworkRequest request = new HistoriActivity.PerformNetworkRequest(Api.URL_READ_KELAS + temp.getText().toString() + Api.STAT + "Done", null, CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshKelasList(JSONArray kelas) throws JSONException {
        //clearing previous heroes
        kelasList.clear();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < kelas.length(); i++) {
            //getting each hero object
            JSONObject obj = kelas.getJSONObject(i);

            //adding the hero to the list
            kelasList.add(new Kelas(
                    obj.getInt("id_peminjaman"),
                    obj.getString("nama_ruangan"),
                    obj.getString("tanggal_pinjam"),
                    obj.getString("waktu_awal"),
                    obj.getString("waktu_akhir"),
                    obj.getString("nim_mahasiswa"),
                    obj.getString("status_pinjam"),
                    obj.getString("keterangan")
            ));
        }

        //creating the adapter and setting it to the listview
        HistoriActivity.KelasAdapter adapter = new HistoriActivity.KelasAdapter(kelasList);
        listView.setAdapter(adapter);
    }

}
