package com.example.pklapps;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;


public class Main2Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
        , TaskCompleted ,TaskCompletedUpdate{

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    EditText editTextKelasid,nama,jamMulai , jamAkhir,nim,stat,keterangan;
    ListView listView;
    Button btBook,btHistori,btProfil,btAvai,btUpdate;
    List<Kelas> kelasList;


    //SPINNER
    private JSONArray result,resultJam;
    Spinner spinner,spinnerJam,spinnerJamAkhir;
    private ArrayList<String> arrayList;
    private ArrayList<String> arrayListJam;

    private TextView tanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        nama = (EditText) findViewById(R.id.nama);
        jamMulai = (EditText) findViewById(R.id.jamMulai);
        jamAkhir = (EditText) findViewById(R.id.jamAkhir);
        listView = (ListView) findViewById(R.id.listView);
        editTextKelasid = (EditText) findViewById(R.id.editTextKelasid);
        nim = (EditText) findViewById(R.id.nim);
        stat = (EditText) findViewById(R.id.stat);
        keterangan = (EditText) findViewById(R.id.keterangan);
        btBook = (Button) findViewById(R.id.btBook);
        btUpdate = (Button) findViewById(R.id.btUpdate);

        btUpdate.setVisibility(View.GONE);

        //TAB
        btHistori = (Button) findViewById(R.id.btHistori);
        btProfil = (Button) findViewById(R.id.btProfil);

        //SPINNER KELAS
        spinner= (Spinner) findViewById(R.id.spnrKelas);
        arrayList = new ArrayList<String>();
        getdata();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Setting the values to textviews for a selected item
                nama.setText(getNamaKelas(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                nama.setText("");
            }
        });

        //SPINNER JAM
        spinnerJam = (Spinner) findViewById(R.id.spnrJam);
        arrayListJam = new ArrayList<String>();

        //SPINNER JAM AKHIR
        spinnerJamAkhir = (Spinner) findViewById(R.id.spnrJamAkhir);

        getdataJam();

        spinnerJam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Setting the values to textviews for a selected item
                jamMulai.setText(getJam(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                jamMulai.setText("");
            }
        });

        spinnerJamAkhir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Setting the values to textviews for a selected item
                jamAkhir.setText(getJam(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                jamAkhir.setText("");
            }
        });

        kelasList = new ArrayList<>();

        //NIM
        User user = SharedPrefManager.getInstance(this).getUser();
        nim.setText(user.getNim());

        //STATUS DEFAULT
        stat.setText("Booked");

        readKelas();

        //TAB
        btHistori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this,HistoriActivity.class);
                startActivity(intent);
            }
        });

        btProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this,ProfilActivity.class);
                startActivity(intent);
            }
        });

        //DATE PICKER
        tanggal = findViewById(R.id.tanggal);
        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        btAvai = (Button) findViewById(R.id.able);
        btAvai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this,Available.class);
                startActivity(intent);
            }
        });
    }

    public void OnAdd(View view){
        String name = nama.getText().toString().trim();
        String date = tanggal.getText().toString().trim();
        String awal = jamMulai.getText().toString().trim();
        String akhir = jamAkhir.getText().toString().trim();
        String Stat = "Booked";
        String type = "valid";
        BackgroundWorker backgroundWorker = new BackgroundWorker(Main2Activity.this);
        backgroundWorker.execute(type,name,date,Stat,awal,akhir);
    }

    public void OnUpdate(View view) {
        String name = nama.getText().toString().trim();
        String date = tanggal.getText().toString().trim();
        String awal = jamMulai.getText().toString().trim();
        String akhir = jamAkhir.getText().toString().trim();
        String Stat = "Booked";
        String type = "valid";
        String Nim = nim.getText().toString().trim();
        String id = editTextKelasid.getText().toString();
        BackgroundUpdate backgroundUpdate = new BackgroundUpdate(Main2Activity.this);
        backgroundUpdate.execute(type,name,date,Stat,awal,akhir,Nim,id);
    }

    //DATE PICKER
    //current time
    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        if (month+1<10 && dayOfMonth<10){
            String date = year + "-" + '0' + (month+1) + "-" + '0' +  dayOfMonth;
            tanggal.setText(date);
        }else if (month+1<10 && dayOfMonth>=10) {
            String date = year + "-" + '0' + (month + 1) + "-" + dayOfMonth;
            tanggal.setText(date);
        }else if (month+1>=10 && dayOfMonth<10) {
            String date = year + "-" + (month + 1) + "-" + '0' + dayOfMonth;
            tanggal.setText(date);
        }
        else{
            String date = year + "-" + (month+1) + "-" +  dayOfMonth;
            tanggal.setText(date);
        }
    }

    //SPINNERR KELAS
    private void getdata() {
        StringRequest stringRequest = new StringRequest(
                Api.SpinnerKelas,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray(Api.JSON_ARRAY);
                            kelasTable(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void kelasTable(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                arrayList.add(json.getString(Api.KelasTableArray));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // arrayList.add(0,"Select Employee");
        spinner.setAdapter(new ArrayAdapter<String>(Main2Activity.this,
                android.R.layout.simple_spinner_dropdown_item, arrayList));
    }

    //Method to get kelas name of a particular position
    private String getNamaKelas(int position){
        String name="";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);
            //Fetching name from that object
            name = json.getString(Api.namaKelas);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;
    }

    //SPINNER JAM
    private void getdataJam() {
        StringRequest stringRequest = new StringRequest(
                Api.SpinnerJam,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            resultJam = j.getJSONArray(Api.JSON_ARRAY_JAM);
                            jamTable(resultJam);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void jamTable(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                arrayListJam.add(json.getString(Api.JamArray));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // arrayList.add(0,"Select Employee");
        spinnerJam.setAdapter(new ArrayAdapter<String>(Main2Activity.this,
                android.R.layout.simple_spinner_dropdown_item, arrayListJam));
        spinnerJamAkhir.setAdapter(new ArrayAdapter<String>(Main2Activity.this,
                android.R.layout.simple_spinner_dropdown_item, arrayListJam));
    }

    //Method to get kelas name of a particular position
    private String getJam(int position){
        String name="";
        try {
            //Getting object of given index
            JSONObject json = resultJam.getJSONObject(position);
            //Fetching name from that object
            name = json.getString(Api.jam);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;
    }

    @Override
    public void onTaskComplete(String result) {
        if (result.equals("bisa")) {
            createKelas();
        }else if (result.equals("gabisa")){
            Toast.makeText(this, "Class not available",
                    Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(this, "kok aneh", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskCompleteUpdate(String result) {
        if (result.equals("bisa")){
            updateKelas();
        }else if (result.equals("gabisa")){
            Toast.makeText(this, "Class not available",
                    Toast.LENGTH_SHORT).show();
            btUpdate.setVisibility(View.GONE);
            btBook.setVisibility(View.VISIBLE);
        }else
            Toast.makeText(this, "kok aneh", Toast.LENGTH_SHORT).show();
    }

    //CREATE PEMINJAMAN
    private void createKelas() {
        String name = nama.getText().toString().trim();
        String date = tanggal.getText().toString().trim();
        String awal = jamMulai.getText().toString().trim();
        String akhir = jamAkhir.getText().toString().trim();
        String Nim = nim.getText().toString().trim();
        String ket = keterangan.getText().toString().trim();
        String Stat = "Booked";

        //validating the inputs
        if (TextUtils.isEmpty(date)){
            tanggal.setError("Please enter the date");
            tanggal.requestFocus();
            Toast.makeText(this, "Please enter the date", Toast.LENGTH_SHORT).show();
        }

        try {
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String current = df.format(c);
            Date date1 = df.parse(current);
            Date date2 = df.parse(date);
            if (date1.compareTo(date2) > 0 ) {
                tanggal.setError("Date is invalid");
                tanggal.requestFocus();
                Toast.makeText(this, "Date is invalid", Toast.LENGTH_SHORT).show();
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            String strDate = df.format(calendar.getTime());

            Toast.makeText(this, strDate, Toast.LENGTH_SHORT).show();

            String a = awal.replace(":", "");
            String b = strDate.replace(":", "");

            if (Integer.parseInt(a) < Integer.parseInt(b)) {
                jamMulai.setError("Hours is invalid");
                jamMulai.requestFocus();
                Toast.makeText(this, "Hours is invalid", Toast.LENGTH_SHORT).show();
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            String a = awal.replace(":", "");
            String b = akhir.replace(":", "");
            if (Integer.parseInt(a) >= Integer.parseInt(b)) {
                jamAkhir.setError("Hours is invalid");
                jamAkhir.requestFocus();
                Toast.makeText(this, "Hours is invalid", Toast.LENGTH_SHORT).show();
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(ket)){
            keterangan.setError("Please enter");
            keterangan.requestFocus();
        }


        //if validation passes
        HashMap<String, String> params = new HashMap<>();
        params.put("nama_ruangan", name);
        params.put("tanggal_pinjam", date);
        params.put("waktu_awal", awal);
        params.put("waktu_akhir", akhir);
        params.put("nim_mahasiswa",Nim);
        params.put("status_pinjam", Stat);
        params.put("keterangan",ket);

        //Calling the create hero API
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_KELAS,
                params, CODE_POST_REQUEST);
        request.execute();
    }

    private void test() {
        String name = nama.getText().toString().trim();
        String date = tanggal.getText().toString().trim();
        String awal = jamMulai.getText().toString().trim();
        String akhir = jamAkhir.getText().toString().trim();
        String Nim = nim.getText().toString().trim();
        String ket = keterangan.getText().toString().trim();
        String Stat = "Booked";

        //if validation passes
        HashMap<String, String> params = new HashMap<>();
        params.put("nama_ruangan", name);
        params.put("tanggal_pinjam", date);
        params.put("waktu_awal", awal);
        params.put("waktu_akhir", akhir);
        params.put("nim_mahasiswa",Nim);
        params.put("status_pinjam", Stat);
        params.put("keterangan",ket);

        //Calling the create hero API
        PerformNetworkRequest request = new PerformNetworkRequest(Api.ROOT + "/Android_login/login.php",
                params, CODE_POST_REQUEST);
        request.execute();
    }

    //inner class to perform network request extending an AsyncTask
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
                        Toast.makeText(Main2Activity.this, object.getString("message")
                                , Toast.LENGTH_SHORT).show();
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

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

    class KelasAdapter extends ArrayAdapter<Kelas> {

        //our hero list
        List<Kelas> kelasList;

        //constructor to get the list
        public KelasAdapter(List<Kelas> kelasList) {
            super(Main2Activity.this, R.layout.layout_kelas_list, kelasList);
            this.kelasList = kelasList;
        }

        //method returning list item
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_kelas_list, null, true);

            //getting the textview for displaying name
            TextView textViewName = listViewItem.findViewById(R.id.textViewName);

            //the update and delete textview
            TextView textViewUpdate = listViewItem.findViewById(R.id.textViewUpdate);
            TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);
            TextView textViewDone = listViewItem.findViewById(R.id.textViewDone);

            final Kelas kelas = kelasList.get(position);

            textViewName.setText(kelas.getNama());

            //attaching click listener to update
            textViewUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //so when it is updating we will
                    //make the isUpdating as true
//                    isUpdating = true;
                    btBook.setVisibility(View.GONE);
                    btUpdate.setVisibility(View.VISIBLE);

                    //we will set the selected hero to the UI elements
                    editTextKelasid.setText(String.valueOf(kelas.getId()));
                    spinner.setSelection(getIndex(spinner,kelas.getNama()));
                    tanggal.setText(kelas.getTanggal());
                    spinnerJam.setSelection(getIndex(spinnerJam,kelas.getjamMulai()));
                    spinnerJamAkhir.setSelection(getIndex(spinnerJamAkhir,kelas.getJamAkhir()));
                    nim.setText(kelas.getNim());
                    stat.setText("Booked");
                    keterangan.setText(kelas.getKeterangan());

                }
            });

            //when the user selected delete
            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // we will display a confirmation dialog before deleting
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);

                    builder.setTitle("Delete " + kelas.getNama())
                            .setMessage("Are you sure want to delete this?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //if the choice is yes we will delete the hero
                                    //method is commented because it is not yet created
                                    deleteKelas(kelas.getId(),kelas.getNim(),"Booked");
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });

            textViewDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // we will display a confirmation dialog before deleting
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);

                    builder.setTitle("End " + kelas.getNama())
                            .setMessage("Are you sure you want to end this?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //if the choice is yes we will delete the hero
                                    //method is commented because it is not yet created

                                    HashMap<String, String> params = new HashMap<>();
                                    params.put("id_peminjaman", String.valueOf(kelas.getId()));
                                    params.put("nama_ruangan", kelas.getNama());
                                    params.put("tanggal_pinjam", kelas.getTanggal());
                                    params.put("waktu_awal", kelas.getjamMulai());
                                    params.put("waktu_akhir", kelas.getJamAkhir());
                                    params.put("nim_mahasiswa",kelas.getNim());
                                    params.put("status_pinjam","Done");
                                    params.put("keterangan",kelas.getKeterangan());

                                    PerformNetworkRequest request = new PerformNetworkRequest(
                                            Api.URL_UPDATE_KELAS, params, CODE_POST_REQUEST);
                                    request.execute();

                                    readKelas();

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });
            return listViewItem;
        }
    }

    private void readKelas() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_KELAS +
                nim.getText().toString() + Api.STAT + "Booked", null, CODE_GET_REQUEST);
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
        KelasAdapter adapter = new KelasAdapter(kelasList);
        listView.setAdapter(adapter);
    }

    private void updateKelas() {
        String id = editTextKelasid.getText().toString();
        String name = nama.getText().toString().trim();
        String date = tanggal.getText().toString().trim();
        String awal = jamMulai.getText().toString().trim();
        String akhir = jamAkhir.getText().toString().trim();
        String Nim = nim.getText().toString().trim();
        String Stat = stat.getText().toString().trim();
        String ket = keterangan.getText().toString().trim();

        if (TextUtils.isEmpty(date)){
            tanggal.setError("Please enter the date");
            tanggal.requestFocus();
            Toast.makeText(this, "Please enter the date", Toast.LENGTH_SHORT).show();
        }

        try {
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String current = df.format(c);
            Date date1 = df.parse(current);
            Date date2 = df.parse(date);

            if (date1.compareTo(date2) > 0 ) {
                tanggal.setError("Date is invalid");
                tanggal.requestFocus();
                Toast.makeText(this, "Date is invalid", Toast.LENGTH_SHORT).show();
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            String a = awal.replace(":", "");
            String b = akhir.replace(":", "");
            if (Integer.parseInt(a) >= Integer.parseInt(b)) {
                jamAkhir.setError("Hours is invalid");
                jamAkhir.requestFocus();
                Toast.makeText(this, "Hours is invalid", Toast.LENGTH_SHORT).show();
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(ket)){
            keterangan.setError("Please enter");
            keterangan.requestFocus();
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("id_peminjaman", id);
        params.put("nama_ruangan", name);
        params.put("tanggal_pinjam", date);
        params.put("waktu_awal", awal);
        params.put("waktu_akhir", akhir);
        params.put("nim_mahasiswa",Nim);
        params.put("status_pinjam",Stat);
        params.put("keterangan",ket);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_KELAS, params, CODE_POST_REQUEST);
        request.execute();

        tanggal.setText("");
        keterangan.setText("");
        spinner.setSelection(0);
        spinnerJam.setSelection(0);
        spinnerJamAkhir.setSelection(0);

        btUpdate.setVisibility(View.GONE);
        btBook.setVisibility(View.VISIBLE);
    }

    private void deleteKelas(int id,String nim,String stat) {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_KELAS + id
                + Api.DELETE + nim + Api.STAT + stat, null, CODE_GET_REQUEST);
        request.execute();
    }
}