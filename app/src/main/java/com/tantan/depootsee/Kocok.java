package com.tantan.depootsee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Kocok extends Activity {
    ExpandableListView expListView;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    DatabaseManager dm;
    List<Pemain> listPemain;
    String[] nama, namapemain;
    ArrayAdapter<Pemain> adapter;
    Button kocok;
    Bundle b = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kocok);
        b = getIntent().getExtras();
        namapemain = new String[b.getInt("jumlah")];
        namapemain = b.getStringArray("nama");
        dm = new DatabaseManager(this);
        expListView = (ExpandableListView) findViewById(R.id.expandablekocok);
        kocok = (Button) findViewById(R.id.btnkocoktim);
        nama = new String[dm.getJumlahPemain()];
        listPemain = dm.getAllPemain();
        adapter = new ArrayAdapter<Pemain>(this,
                android.R.layout.simple_list_item_multiple_choice, listPemain);
        salindata();
        kocok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View x) {
                shuffleArray(namapemain);
                prepareListData();
            }
        });
    }

    private void salindata() {
        for (int i = 0; i < dm.getJumlahPemain(); i++) {
            nama[i] = adapter.getItem(i).getnama().toString();
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        int i = 0;
        int k = 0;
        boolean pemain = true;
        while (pemain) {
            listDataHeader.add("TIM " + String.valueOf(i + 1));
            List<String> nama = new ArrayList<String>();
            for (int j = 0; j < 4; j++) {
                if (k < this.namapemain.length) {
                    nama.add(this.namapemain[k]);
                    k++;
                } else {
                    pemain = false;
                }
            }
            listDataChild.put(listDataHeader.get(i), nama);
            i++;
        }
        listAdapter = new ExpandableListAdapter(this, listDataHeader,
                listDataChild);
        expListView.setAdapter(listAdapter);
    }

    public void onBackPressed() {
        close();
    }

    private void close() {
        Intent myIntent = new Intent(this, Home.class);
        startActivityForResult(myIntent, 0);
    }

    static void shuffleArray(String[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}