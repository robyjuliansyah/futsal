package com.tantan.depootsee;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Laporan extends Activity {
    ExpandableListView expListView;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    DatabaseManager dm;
    Button hapus;
    boolean salah = true;
    AlertDialog.Builder alert;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);
        dm = new DatabaseManager(this);
        alert = new AlertDialog.Builder(this);
        input = new EditText(this);
        alert.setTitle("DELETE");
        alert.setMessage("Masukkan Password");
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String code = input.getText().toString();
                if (code.equals("bismilah")) {
                    try {
                        dm.ClearLaporan();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(),
                                "gagal Hapus Laporan, " + e.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Password salah",
                            Toast.LENGTH_LONG).show();
                }
                exit();
            }
        });
        expListView = (ExpandableListView) findViewById(R.id.expandablelaporan);
        hapus = (Button) findViewById(R.id.btnclearreport);
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader,
                listDataChild);
        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if (groupPosition == 0 && childPosition == 0) {
                } else if (groupPosition == 0 && childPosition == 1) {
                }
                return false;
            }
        });
        hapus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View x) {
                try {
                    alert.show();
                } catch (Exception e) {
                    exit();
                }
            }
        });
    }

    private void exit(){
        Intent myIntent = new Intent(this, Home.class);
        startActivityForResult(myIntent, 0);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        String[] tanggal = dm.tanggalLaporan();
        String[][] snama = new String[tanggal.length][dm.getJumlahPemain()];
        snama = dm.ambilSemuaBarisLaporan();
        int i = 0;
        while (i < tanggal.length) {
            listDataHeader.add(tanggal[i]);
            List<String> nama = new ArrayList<String>();
            for (int j = 0; j < dm.getJumlahPemain(); j++) {
                // System.out.println(i+" "+j+" "+dm.ambilSemuaBarisLaporan()[i][j]);
                if (snama[i][j] == null) {
                } else {
                    nama.add(snama[i][j]);
                }
            }
            listDataChild.put(listDataHeader.get(i), nama);
            i++;
        }
    }
}