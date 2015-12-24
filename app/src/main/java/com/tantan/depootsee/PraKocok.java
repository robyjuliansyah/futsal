package com.tantan.depootsee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class PraKocok extends Activity {
    Button proseskocok;
    ListView kocok;
    DatabaseManager dm;
    ArrayAdapter<Pemain> adapter;
    List<Pemain> listPemain;
    String[] namabayar, arraynama;
    boolean[] bayar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pra_kocok);
        deklarasilayout();
        deklarasilistener();
        setData();
    }

    private void setData() {
        listPemain = dm.getAllPemain();
        adapter = new ArrayAdapter<Pemain>(this,
                android.R.layout.simple_list_item_multiple_choice, listPemain);
        kocok.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        kocok.setAdapter(adapter);
        namabayar = new String[adapter.getCount()];
        bayar = new boolean[adapter.getCount()];
        for (int i = 0; i < bayar.length; i++) {
            bayar[i] = false;
            namabayar[i] = "";
        }
    }

    private void deklarasilistener() {
        proseskocok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View x) {
                try {
                    int index = 0;
                    for (int i = 0; i < bayar.length; i++) {
                        if (bayar[i]) {
                            index++;
                        }
                    }
                    arraynama = new String[index];
                    index = 0;
                    for (int i = 0; i < bayar.length; i++) {
                        if (bayar[i]) {
                            arraynama[index] = namabayar[i];
                            index++;
                        }
                    }
                    Bundle b  = new Bundle();
                    b.putInt("jumlah", index);
                    b.putStringArray("nama", arraynama);
                    Intent myIntent = new Intent(x.getContext(), Kocok.class);
                    myIntent.putExtras(b);
                    startActivityForResult(myIntent, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(),
                            "gagal Bayar, " + e.toString(), Toast.LENGTH_LONG)
                            .show();
                }
            }

        });
        kocok.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                tulisdata(position);
            }
        });
    }

    private void deklarasilayout() {
        dm = new DatabaseManager(this);
        proseskocok = (Button) findViewById(R.id.btnproseskocok);
        kocok = (ListView) findViewById(R.id.listkocok);
        kocok.setEmptyView(findViewById(R.id.emptykocok));
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