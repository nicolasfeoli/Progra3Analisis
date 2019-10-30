package com.example.meli.buckets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.content.Intent;
import android.widget.TextView;

/**
 * Created by Meli on 5/21/2017.
 */

public class InsideBucket  extends AppCompatActivity {
    Singleton sing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insidebuckets);

        sing = Singleton.getInstance();

        Intent i = getIntent();
        final long hash = i.getLongExtra("Hash", 1);
        final boolean pixels = i.getBooleanExtra("Pixels",true);
        TextView txt = (TextView) findViewById(R.id.nombre);
        if (pixels)
            txt.setText(sing.getBucketPix(hash).nombre);
        else
            txt.setText(sing.getBucketLBP(hash).nombre);
        Button btn = (Button) findViewById(R.id.btnNombre);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(InsideBucket.this, CambiaNombre.class);
                        in.putExtra("Hash", hash);
                        in.putExtra("Pixels",pixels);
                        startActivity(in);
                    }
                }
        );

        GridView grd = (GridView) findViewById(R.id.grid);
        grd.setAdapter(new ImageAdapter(this, hash,pixels));

    }
}
