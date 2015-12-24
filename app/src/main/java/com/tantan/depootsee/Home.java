package com.tantan.depootsee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends Activity {
    Button bayar, kas, kelola, laporan, kocok;
    DatabaseManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        deklarasilayout();
        if (dm.cekfutsal()) {
            bayar.setEnabled(false);
        } else {
            bayar.setEnabled(true);
        }
        deklarasilistener();
    }

    private void deklarasilistener() {
        kocok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View x) {
                Intent myIntent = new Intent(x.getContext(), PraKocok.class);
                startActivityForResult(myIntent, 0);
            }
        });
        bayar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View x) {
                Intent myIntent = new Intent(x.getContext(), Bayar.class);
                startActivityForResult(myIntent, 0);
            }
        });
        kas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View x) {
                Intent myIntent = new Intent(x.getContext(), Kas.class);
                startActivityForResult(myIntent, 0);
            }
        });
        kelola.setOnClickListener(new View.OnClickListener() {
            public void onClick(View x) {
                Intent myIntent = new Intent(x.getContext(), Kelola.class);
                startActivityForResult(myIntent, 0);
            }
        });
        laporan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View x) {
                Intent myIntent = new Intent(x.getContext(), Laporan.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    private void deklarasilayout() {
        dm = new DatabaseManager(this);
        bayar = (Button) findViewById(R.id.btnbayar);
        kas = (Button) findViewById(R.id.btnkas);
        kelola = (Button) findViewById(R.id.btnkeloladata);
        laporan = (Button) findViewById(R.id.btnlaporan);
        kocok = (Button) findViewById(R.id.btnkocok);
    }

    public void onBackPressed() {
        close();
    }

    private void close() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}