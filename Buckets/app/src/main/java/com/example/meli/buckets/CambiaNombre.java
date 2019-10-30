package com.example.meli.buckets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Meli on 5/23/2017.
 */

public class CambiaNombre extends AppCompatActivity {
    Singleton sing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambia_nombre);

        sing = Singleton.getInstance();

        Intent i = getIntent();
        final long hash = i.getLongExtra("Hash", 1);
        final Bucket b = sing.getBucket(hash);

        final EditText nom = (EditText)findViewById(R.id.nombre);
        nom.setText(b.nombre);

        Button btnSave = (Button) findViewById(R.id.salvar);
        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s = nom.getText().toString();
                        b.nombre = s;
                        Intent in = new Intent(CambiaNombre.this, BucketsActivity.class);
                        in.putExtra("Hash", hash);
                        startActivity(in);
                    }
                }
        );

    }
}
