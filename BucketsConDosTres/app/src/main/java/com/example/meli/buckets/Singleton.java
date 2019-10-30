package com.example.meli.buckets;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Meli on 5/23/2017.
 */

public class Singleton {

    // indispensable
    private static Singleton singletonAdm = null;

    // no son static para obligar que sean accedidas
    // desde el singleton
    private ArrayList<Bucket> losBucketsPix;
    private ArrayList<Bucket> losBucketsLBP;
    // indispensable
    private Singleton() {
        losBucketsPix = new ArrayList<>();
        losBucketsLBP = new ArrayList<>();
    }

    // indispensable
    public static Singleton getInstance(){
        if (singletonAdm == null){
            singletonAdm = new Singleton();
        }
        return singletonAdm;
    }

    public int sizePix(){
        return losBucketsPix.size();
    }
    public int sizeLBP(){
        return losBucketsLBP.size();
    }

    public ArrayList getBucketsPix (){
        return losBucketsPix;
    }
    public ArrayList getBucketsLBP (){
        return losBucketsLBP;
    }

    public Bucket getBucketPix(int i){
        return losBucketsPix.get(i);
    }
    public Bucket getBucketLBP(int i){
        return losBucketsLBP.get(i);
    }

    public Bucket getBucketPix (long hash){
        for (int i = 0; i < losBucketsPix.size(); i++){
            if (losBucketsPix.get(i).hash == hash)
                return losBucketsPix.get(i);
        }
        return null;
    }
    public Bucket getBucketLBP (long hash){
        for (int i = 0; i < losBucketsLBP.size(); i++){
            if (losBucketsLBP.get(i).hash == hash)
                return losBucketsLBP.get(i);
        }
        return null;
    }

    public void insertarBucketPix(Bucket b){
        losBucketsPix.add(b);
    }
    public void insertarBucketLBP(Bucket b){
        losBucketsLBP.add(b);
    }
}
