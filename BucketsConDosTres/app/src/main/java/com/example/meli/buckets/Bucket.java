package com.example.meli.buckets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;




/**
 * Created by Meli on 5/22/2017.
 */

public class Bucket implements Serializable{
    public long hash;
    public String nombre;
    public ArrayList <Bitmap> imagenes;

    Bucket (String nom, long h){
        nombre = nom;
        hash = h;
        imagenes = new ArrayList<>();
    }

    Bucket (long h){
        hash = h;
    }

    public void agregarImagen(Bitmap img){
        imagenes.add(img);
    }

}
