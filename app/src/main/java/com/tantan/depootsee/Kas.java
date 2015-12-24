package com.tantan.depootsee;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Kas extends Activity {
    DatabaseManager dm;
    EditText kas, kegiatan, biaya;
    Button proseskas, clear;
    AlertDialog.Builder alert;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kas);
        deklarasilayout();
        deklarasilistener();
        kosongkanField();
        updatekas();
    }

    private void deklarasilistener() {
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String code = input.getText().toString();
                if (code.equals("bismilah")) {
                    try {
                        dm.ClearKas();
                        updatekas();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(),
                                "gagal Hapus Kas, " + e.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Password salah",
                            Toast.LENGTH_LONG).show();
                }
                exit();
            }
        });
        proseskas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View x) {
                if (kegiatan.getText().toString().equals("")
                        || biaya.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), "Isi Dulu Datanya",
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Date current = new Date();
                        SimpleDateFormat frmt = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        String dateString = frmt.format(current);
                        dm.addRowKas(dateString, (-1 * Integer.parseInt(biaya
                                .getText().toString())), kegiatan.getText()
                                .toString(), "kredit");
                        updatekas();
                        Intent myIntent = new Intent(x.getContext(), Home.class);
                        startActivityForResult(myIntent, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(),
                                "gagal simpan Kegiatan, " + e.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View x) {
                try {
                    alert.show();
                } catch (Exception e) {
                    exit();
                }
            }
        });
    }

    private void exit() {
        Intent myIntent = new Intent(this, Home.class);
        startActivityForResult(myIntent, 0);
    }

    protected void kosongkanField() {
        kas.setText("");
        kegiatan.setText("");
        biaya.setText("");
    }

    protected void updatekas() {
        kas.setText(String.valueOf(dm.ambilKas()));
    }

    private void deklarasilayout() {
        dm = new DatabaseManager(this);
        proseskas = (Button) findViewById(R.id.btnproseskegiatan);
        clear = (Button) findViewById(R.id.btnclear);
        kas = (EditText) findViewById(R.id.txtsaldo);
        kegiatan = (EditText) findViewById(R.id.txtkegiatan);
        biaya = (EditText) findViewById(R.id.txtbiaya);
        kas.setEnabled(false);
        alert = new AlertDialog.Builder(this);
        input = new EditText(this);
        alert.setTitle("DELETE");
        alert.setMessage("Masukkan Password");
        alert.setView(input);
    }
}