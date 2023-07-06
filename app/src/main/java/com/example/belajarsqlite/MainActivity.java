package com.example.belajarsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<MahasiswaModel> mhsList;
    MahasiswaModel mm;
    DbHelper db;
    boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText edNama = (EditText) findViewById(R.id.edNama);
        EditText edNim = (EditText) findViewById(R.id.edNim);
        EditText edNomorHp = (EditText) findViewById(R.id.edNomorHp);
        Button btnSimpan = (Button) findViewById(R.id.btnSimpan);
        Button btnLihat = (Button) findViewById(R.id.btnLihat);

        mhsList = new ArrayList<>();

        isEdit = false;

        Intent intent_main = getIntent();
        if (intent_main.hasExtra("mhsData")){
            mm = intent_main.getExtras().getParcelable("mhsData");
            edNama.setText(mm.getNama());
            edNim.setText(mm.getNim());
            edNomorHp.setText(mm.getNoHp());

            isEdit = true;

            btnSimpan.setBackgroundColor(Color.GREEN);
            btnSimpan.setText("Edit");
        }

        db = new DbHelper(getApplicationContext());

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isian_nama = edNama.getText().toString();
                String isian_nim = edNim.getText().toString();
                String isian_noHp = edNomorHp.getText().toString();

                if (isian_nama.isEmpty() || isian_nim.isEmpty() || isian_noHp.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Isian Belum Terisi", Toast.LENGTH_SHORT).show();
                }else{
                    //mhsList.add(new MahasiswaModel(-1, isian_nama, isian_nim, isian_noHp));
                    boolean stts;

                    // untuk limit record
                    mhsList = db.list();
                    if  (mhsList.size() >= 5) {
                        Toast.makeText(getApplicationContext(), "Minimal Record 5!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!isEdit) {
                            mm = new MahasiswaModel(-1, isian_nama, isian_nim, isian_noHp);
                            stts = db.simpan(mm);

                            edNama.setText("");
                            edNim.setText("");
                            edNomorHp.setText("");

                        } else {
                            mm = new MahasiswaModel(mm.getId(), isian_nama, isian_nim, isian_noHp);
                            stts = db.ubah(mm);
                        }

                        if (stts) {

                            Toast.makeText(getApplicationContext(), "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Data Gagal Disimpan", Toast.LENGTH_SHORT).show();
                        }
                    }



                    //intent_list.putParcelableArrayListExtra("mhsList", mhsList);
                    //startActivity(intent_list);
                }
            }
        });

        btnLihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mhsList = db.list();

                if (mhsList.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Belum Ada Data", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent_list = new Intent(MainActivity.this, ListMhsActivity.class);
                    intent_list.putParcelableArrayListExtra("mhsList", mhsList);
                    startActivity(intent_list);
                }

            }
        });

    }
}