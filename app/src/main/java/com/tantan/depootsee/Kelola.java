package com.tantan.depootsee;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost;

import java.util.ArrayList;

public class Kelola extends Activity
{
    DatabaseManager dm;
    EditText nomor, nama, idDel;
    Button addBtn, delBtn;
    TableLayout tabeldata;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola);

        TabHost tabs = (TabHost) findViewById(R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec spec;
        spec = tabs.newTabSpec("tag1"); // Set Tab1
        spec.setContent(R.id.layoutinput);
        spec.setIndicator("Input Pemain");
        tabs.addTab(spec);
        spec = tabs.newTabSpec("tag2"); // Set Tab2
        spec.setContent(R.id.layoutdelete);
        spec.setIndicator("Lihat Pemain");
        tabs.addTab(spec);
        tabs.setCurrentTab(0);

        deklarasilayout();
        deklarasilistener();
        kosongkanField();
        updateTable();
    }

    protected void kosongkanField() {
        nomor.setText("");
        nama.setText("");
        idDel.setText("");
    }

    private void deleteData() {
        if (idDel.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "Isi Dulu Nomornya",
                    Toast.LENGTH_LONG).show();
        } else {
            try {
                dm.deleteBarisPemain(Long.parseLong(idDel.getText().toString()));
                updateTable();
                idDel.requestFocus();
                Toast.makeText(getBaseContext(), "Data Berhasil Dihapus",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "Gagal Hapus, " + e.toString(),
                        Toast.LENGTH_LONG).show();
            }

        }
    }

    protected void simpanData() {
        if (nomor.getText().toString().equals("")
                || nama.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "Isi Dulu Datanya",
                    Toast.LENGTH_LONG).show();
        } else {
            try {
                dm.addRowPemain(nomor.getText().toString(), nama.getText()
                        .toString());
                updateTable();
                nomor.requestFocus();
                Toast.makeText(getBaseContext(), "Data Berhasil Disimpan",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(),
                        "Gagal Simpan, " + e.toString(), Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    protected void updateTable() {
        while (tabeldata.getChildCount() > 1) {
            tabeldata.removeViewAt(1);
        }
        ArrayList<ArrayList<Object>> data = dm.ambilSemuaBarisPemain();
        for (int posisi = 0; posisi < data.size(); posisi++) {
            TableRow tabelBaris = new TableRow(this);
            ArrayList<Object> baris = data.get(posisi);
            TextView NomorTxt = new TextView(this);
            NomorTxt.setText(baris.get(0).toString());
            NomorTxt.setGravity(Gravity.CENTER);
            NomorTxt.setTextSize(20);
            tabelBaris.addView(NomorTxt);
            TextView namaTxt = new TextView(this);
            namaTxt.setText(baris.get(1).toString());
            namaTxt.setTextSize(20);
            tabelBaris.addView(namaTxt);
            tabeldata.addView(tabelBaris);
        }
    }

    private void deklarasilistener() {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanData();
                kosongkanField();
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View d) {
                deleteData();
                kosongkanField();
            }
        });
    }

    private void deklarasilayout() {
        dm = new DatabaseManager(this);
        tabeldata = (TableLayout) findViewById(R.id.tabel_data);
        nomor = (EditText) findViewById(R.id.inNomor);
        nama = (EditText) findViewById(R.id.inNama);
        idDel = (EditText) findViewById(R.id.idDelete);
        addBtn = (Button) findViewById(R.id.btnAdd);
        delBtn = (Button) findViewById(R.id.btnDel);
    }
}