package com.tantan.depootsee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Bayar extends Activity
{
    EditText jam;
    Button proses;
    ListView daftar;
    DatabaseManager dm;
    ArrayAdapter<Pemain> adapter;
    List<Pemain> listPemain;
    String[] namabayar;
    boolean[] bayar;
    int jumlahbayar, uangmasuk, uangkeluar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar);
        deklarasilayout();
        deklarasilistener();
        setData();
    }

    private void setData()
    {
        listPemain = dm.getAllPemain();
        adapter = new ArrayAdapter<Pemain>(this,
                android.R.layout.simple_list_item_multiple_choice, listPemain);
        daftar.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        daftar.setAdapter(adapter);
        namabayar = new String[adapter.getCount()];
        bayar = new boolean[adapter.getCount()];
        for (int i = 0; i < bayar.length; i++) {
            bayar[i] = false;
            namabayar[i] = "";
        }
        jam.setText("");
    }

    private void deklarasilistener() {
        proses.setOnClickListener(new View.OnClickListener() {
            public void onClick(View x) {
                if (jam.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), "Isi Dulu Bayarnya",
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Date current = new Date();
                        SimpleDateFormat frmt = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        String dateString = frmt.format(current);
                        for (int i = 0; i < bayar.length; i++) {
                            if (bayar[i]) {
                                jumlahbayar++;
                                dm.addRowLaporan(dateString, namabayar[i]);
                            }
                        }
                        uangkeluar = Integer.parseInt(jam.getText().toString());
                        uangmasuk = (jumlahbayar * 10000) - uangkeluar;
                        dm.addRowKas(dateString, uangmasuk, "Futsal", "Debit");
                        Intent myIntent = new Intent(x.getContext(), Home.class);
                        startActivityForResult(myIntent, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(),
                                "gagal Bayar, " + e.toString(),
                                Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
        daftar.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                tulisdata(position);
            }
        });
    }

    private void deklarasilayout() {
        dm = new DatabaseManager(this);
        proses = (Button) findViewById(R.id.btnproses);
        jam = (EditText) findViewById(R.id.txtjam);
        daftar = (ListView) findViewById(R.id.listbayar);
        daftar.setEmptyView(findViewById(R.id.empty));
    }

    private void tulisdata(int posisi) {
        if (bayar[posisi]) {
            bayar[posisi] = false;
            namabayar[posisi] = "";
        } else {
            bayar[posisi] = true;
            namabayar[posisi] = adapter.getItem(posisi).getnama();
        }
    }
}