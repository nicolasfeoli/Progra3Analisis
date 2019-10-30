package com.example.meli.buckets;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BucketsActivity extends AppCompatActivity {

    Singleton sing;
    Button btoSeleccionarFoto;
    Button btoNuevaFoto;
    Button btoPixeles;
    Button btoLBP;
    private static final int PICK_IMAGE = 1313;
    private static final int CAN_REQUEST = 1;
    Uri imageUri;
    Bitmap imgBitmap;
    int CANTIDAD_BUCKETS = 10;
    int vectorImagen[] = new int[65536];
    ArrayList<int[]> randoms = new ArrayList<>();
    int[][] hiperplanosLBP = new int[CANTIDAD_BUCKETS][256];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buckets);

        sing = Singleton.getInstance();

        Bitmap bit1 = BitmapFactory.decodeResource(getResources(), R.drawable.img1);
        Bitmap bit2 = BitmapFactory.decodeResource(getResources(), R.drawable.img2);
        Bitmap bit3 = BitmapFactory.decodeResource(getResources(), R.drawable.img3);
        Bitmap bit4 = BitmapFactory.decodeResource(getResources(), R.drawable.img4);
        Bitmap bit5 = BitmapFactory.decodeResource(getResources(), R.drawable.img5);
        Bitmap bit6 = BitmapFactory.decodeResource(getResources(), R.drawable.img6);
        Bitmap bit7 = BitmapFactory.decodeResource(getResources(), R.drawable.img7);
        /*if (sing.size() == 0) {
            //deberia de recibir esto
            Bitmap bit1 = BitmapFactory.decodeResource(getResources(), R.drawable.img1);
            Bitmap bit2 = BitmapFactory.decodeResource(getResources(), R.drawable.img2);
            Bitmap bit3 = BitmapFactory.decodeResource(getResources(), R.drawable.img3);
            Bitmap bit4 = BitmapFactory.decodeResource(getResources(), R.drawable.img4);

            Bucket b = new Bucket("buck 1", 110);
            b.agregarImagen(bit1);
            b.agregarImagen(bit2);
            sing.insertarBucket(b);
            b = new Bucket("buck 2", 111);
            b.agregarImagen(bit3);
            b.agregarImagen(bit4);
            sing.insertarBucket(b);
        }/*
        /*
        final String [] bucks = new String[buckets.size()];
        for (int j = 0; j < buckets.size(); j++){
            bucks[j] = buckets.get(j).nombre;
        }*/

        /*HashMap<String, Long> infoBuckets = new HashMap<>();
        for (int j = 0; j < sing.size(); j++){
            infoBuckets.put(sing.getBucket(j).nombre,sing.getBucket(j).hash);
        }

        List<HashMap<String,String>> listBuckets = new ArrayList<>();

        SimpleAdapter adapter = new SimpleAdapter(this, listBuckets,R.layout.item_bucket,
                new String [] {"First Line", "Second Line"},
                new int[] {R.id.textView, R.id.textView2});

        Iterator it = infoBuckets.entrySet().iterator();
        while(it.hasNext()){
            HashMap<String, String> resultMap = new HashMap<>();
            Map.Entry pair = (Map.Entry) it.next();
            resultMap.put("First Line", pair.getKey().toString());
            resultMap.put("Second Line", pair.getValue().toString());
            listBuckets.add(resultMap);
        }*/
        crearVectoresRandom();

        final ListView ListaBuckets = (ListView) findViewById(R.id.ListaBuckets);
        actualizarListView();
        //ListaBuckets.setAdapter(adapter);

        ListaBuckets.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        HashMap<String,String> msg = (HashMap<String,String>)parent.getItemAtPosition(position);
                        long hash = Long.parseLong(msg.get("Second Line"));
                        //Bucket b = buckets.get(buckets.indexOf(new Bucket(hash)));
                        //Toast.makeText(BucketsActivity.this, b.nombre, Toast.LENGTH_SHORT).show();


                        Intent i = new Intent();
                        i.setClass(BucketsActivity.this, InsideBucket.class);
                        i.putExtra("Hash", hash);
                        //i.putExtra("Bucket", b);
                        startActivity(i);
                    }
                }

        );

        btoNuevaFoto = (Button)findViewById(R.id.btoCamara);
        btoSeleccionarFoto = (Button)findViewById(R.id.btoGaleria);
        btoPixeles = (Button)findViewById(R.id.btoPixels);
        btoLBP = (Button)findViewById(R.id.btoLBP);

        btoSeleccionarFoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openGallery();
            }
        });
        btoNuevaFoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openCamera();
            }
        });
        btoPixeles.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                pixeles(imgBitmap);
            }
        });
        btoLBP.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                LBP(imgBitmap);
            }
        });

        LBP(bit1);
        LBP(bit2);
        LBP(bit4);
        LBP(bit5);
        LBP(bit6);
        LBP(bit7);
    }


    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }
    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            System.out.println("SELECCIONAAAA FOTOOOO");
            imageUri = data.getData();
            System.out.println(imageUri==null);
            try {
                imgBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgBitmap = getResizedBitmap(imgBitmap,256,256);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        else if(requestCode == CAN_REQUEST && resultCode == RESULT_OK){
            System.out.println("TOMA FOTOOOOO");
            Bundle extras = data.getExtras();
            imgBitmap = (Bitmap)extras.get("data");
            imgBitmap = getResizedBitmap(imgBitmap,256,256);
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public void LBP(Bitmap bitmap){
        int[] histograma = new int[256];
        for(int i=0; i<256; i++) histograma[i] = 0;
        System.out.println(bitmap);
        bitmap.getPixels(vectorImagen,0,256,0,0,256,256);

        for(int i=0;i<65536;i++)
            vectorImagen[i] = Math.abs(vectorImagen[i]%256);
        int hashPixel;
        for(int i=0; i<65536;i++){
            hashPixel = 0;
            if(i<256){//si esta en la fila de arriba
                if(i == 0){
                    //esta en la esquina superior izq
                    if(vectorImagen[i + 1] < vectorImagen[i]) hashPixel += 1; //pixel de derecha
                    hashPixel *= 2; //shift left 1
                    if(vectorImagen[i+257] < vectorImagen[i]) hashPixel += 1; // pixel de esquina inferior derecha
                    hashPixel *= 2;
                    if(vectorImagen[i+256] < vectorImagen[i]) hashPixel += 1; //pixel de abajo
                    hashPixel *= 4; //como esta en la esquina, no se revisan los dos ultimos.
                } else if(i == 255){ //esta en la esquina superior derecha
                    if(vectorImagen[i+256] < vectorImagen[i]) hashPixel += 1; //pixel de abajo
                    hashPixel *= 2; //shift left 1
                    if(vectorImagen[i+255] < vectorImagen[i]) hashPixel += 1;//pixel esquina inf izquierda
                    hashPixel *= 2;
                    if(vectorImagen[i - 1] < vectorImagen[i]) hashPixel += 1; //pixel izquierda
                } else{
                    if(vectorImagen[i + 1] < vectorImagen[i]) hashPixel += 1; //pixel de derecha
                    hashPixel *= 2; //shift left 1
                    if(vectorImagen[i+257] < vectorImagen[i]) hashPixel += 1; // pixel de esquina inferior derecha
                    hashPixel *= 2;
                    if(vectorImagen[i+256] < vectorImagen[i]) hashPixel += 1; //pixel de abajo
                    hashPixel *= 2;
                    if(vectorImagen[i+255] < vectorImagen[i]) hashPixel += 1; //pixel esquina inf izquierda
                    hashPixel *= 2;
                    if(vectorImagen[i - 1] < vectorImagen[i]) hashPixel += 1; //pixel izquierda
                }
            }else if(i >= 65536-256){//si esta en la fila de abajo
                if(i == 0){//esta en la esquina inferior izquierda
                    if(vectorImagen[i-256] < vectorImagen[i]) hashPixel += 1; //pixel arriba
                    hashPixel *= 2;
                    if(vectorImagen[i-257] < vectorImagen[i]) hashPixel += 1; //pixel sup derecha
                    hashPixel *= 2;
                    if(vectorImagen[i + 1] < vectorImagen[i]) hashPixel += 1; //pixel de derecha
                    hashPixel *= 16; //shift left 4, pues no se revisan los ultimos
                }else if(i == 65535){ //esta en la esquina inferior derecha
                    if(vectorImagen[i-257] < vectorImagen[i]) hashPixel += 1; //pixel superior izquierda
                    hashPixel *= 2;
                    if(vectorImagen[i-256] < vectorImagen[i]) hashPixel += 1; //pixel arriba
                    hashPixel *= 64; //shift left 6
                    if(vectorImagen[i - 1] < vectorImagen[i]) hashPixel += 1;//pixel izquierda
                }else{
                    if(vectorImagen[i-257] < vectorImagen[i]) hashPixel += 1;
                    hashPixel *= 2;
                    if(vectorImagen[i-256] < vectorImagen[i]) hashPixel += 1;
                    hashPixel *= 2;
                    if(vectorImagen[i-255] < vectorImagen[i]) hashPixel += 1;
                    hashPixel *= 2;
                    if(vectorImagen[i + 1] < vectorImagen[i]) hashPixel += 1;
                    hashPixel *= 8; //shift left 3
                    if(vectorImagen[i - 1] < vectorImagen[i]) hashPixel += 1;
                }
            }else{//esta en la carnita de la imagen
                if(i%256 == 0){//si esta en la fila de la izuierda
                    if(vectorImagen[i-256] < vectorImagen[i]) hashPixel += 1; //pixel arriba
                    hashPixel *= 2;
                    if(vectorImagen[i-255] < vectorImagen[i]) hashPixel += 1; // esquina superior derecha
                    hashPixel *= 2;
                    if(vectorImagen[i + 1] < vectorImagen[i]) hashPixel += 1; //pixel derecha
                    hashPixel *= 2;
                    if(vectorImagen[i+257] < vectorImagen[i]) hashPixel += 1; // esquina inf derecha
                    hashPixel *= 2;
                    if(vectorImagen[i+256] < vectorImagen[i]) hashPixel += 1; // esquina superior derecha
                    hashPixel *= 4;//shift left 2
                }else if(i%256 == 255){//Esta en la fila de la derecha
                    if(vectorImagen[i-257] < vectorImagen[i]) hashPixel += 1; //pixel superior izq
                    hashPixel *= 2;
                    if(vectorImagen[i-256] < vectorImagen[i]) hashPixel += 1; //pixel arriba
                    hashPixel *= 16;
                    if(vectorImagen[i+256] < vectorImagen[i]) hashPixel += 1; // pixel abajo
                    hashPixel *= 2;
                    if(vectorImagen[i+255] < vectorImagen[i]) hashPixel += 1; // pixel inf izq
                    hashPixel *= 2;
                    if(vectorImagen[i - 1] < vectorImagen[i]) hashPixel += 1; // pixel izquierda
                }
            }
            histograma[hashPixel] += 1;
        }
        long hash = productoPunto(histograma, "lbp");
        Bucket b = sing.getBucket(hash);
        if(b==null){
            b = new Bucket(Long.toString(hash),hash);
            sing.insertarBucket(b);
        }
        b.agregarImagen(bitmap);
        actualizarListView();
    }

    public void pixeles(Bitmap bitmap){
        System.out.println(bitmap);

        bitmap.getPixels(vectorImagen,0,256,0,0,256,256);
        for(int i=0;i<65536;i++){
            vectorImagen[i] = Math.abs(vectorImagen[i]%256);
        }
        //AIUDA
        long hash = productoPunto(vectorImagen, "pixels");
        Bucket b = sing.getBucket(hash);
        if(b==null){
            b = new Bucket(Long.toString(hash),hash);
            sing.insertarBucket(b);
        }
        b.agregarImagen(bitmap);
        actualizarListView();

    }
    public long productoPunto(int[] array1, String tipo){
        int mull = 1;
        long result=0;
        int contador = 256;
        if(tipo.equals("pixels")) contador = 65536;
        for(int i = 0; i< CANTIDAD_BUCKETS; i++){
            long dot =0;
            int rand[] = randoms.get(i);
            for(int e=0; e<contador; e++){
                dot += array1[e] * rand[e];
            }
            if(dot%2 == 0)
                result += 1*mull;
            mull*=10;
        }
        return result;
    }

    public void crearVectoresRandom(){
        Random r = new Random();
        for(int e = 0; e < CANTIDAD_BUCKETS; e++){
            int[] random = new int[65536];
            for(int i=0;i<65536;i++)
                random[i] = r.nextInt(256);//-128;
            for(int i=0; i<256; i++)
                hiperplanosLBP[e][i] = r.nextInt(256);
            randoms.add(random);
        }
    }
    public void actualizarListView(){
        HashMap<String, Long> infoBuckets = new HashMap<>();
        for (int j = 0; j < sing.size(); j++){
            infoBuckets.put(sing.getBucket(j).nombre,sing.getBucket(j).hash);
        }

        List<HashMap<String,String>> listBuckets = new ArrayList<>();

        SimpleAdapter adapter = new SimpleAdapter(this, listBuckets,R.layout.item_bucket,
                new String [] {"First Line", "Second Line"},
                new int[] {R.id.textView, R.id.textView2});

        Iterator it = infoBuckets.entrySet().iterator();
        while(it.hasNext()){
            HashMap<String, String> resultMap = new HashMap<>();
            Map.Entry pair = (Map.Entry) it.next();
            resultMap.put("First Line", pair.getKey().toString());
            resultMap.put("Second Line", pair.getValue().toString());
            listBuckets.add(resultMap);
        }
        ListView ListaBuckets = (ListView) findViewById(R.id.ListaBuckets);

        ListaBuckets.setAdapter(adapter);
    }


}
