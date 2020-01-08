package com.example.pklapps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class ProfilActivity extends AppCompatActivity {
    Button btBook , btHistori,btOut,able;
    TextView nim,nama,fakultas,prodi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        btBook = (Button) findViewById(R.id.btBook);
        btHistori = (Button) findViewById(R.id.btHistori);
        able = (Button) findViewById(R.id.able);
        btOut = (Button) findViewById(R.id.btOut);
        nama = (TextView) findViewById(R.id.tvNama);
        nim = (TextView) findViewById(R.id.tvNim);
        fakultas = (TextView) findViewById(R.id.tvFakultas);
        prodi = (TextView) findViewById(R.id.tvProdi);

        User user = SharedPrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        nim.setText(user.getNim());
        nama.setText(user.getNama());
        fakultas.setText(user.getFakultas());
        prodi.setText(user.getProdi());

        btBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });

        btHistori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilActivity.this,HistoriActivity.class);
                startActivity(intent);
            }
        });

        btOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });

        able.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilActivity.this,Available.class);
                startActivity(intent);
            }
        });
    }
}
