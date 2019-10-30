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
    private ArrayList<Bucket> losBuckets;

    // indispensable
    private Singleton() {
        losBuckets = new ArrayList<>();
    }

    // indispensable
    public static Singleton getInstance(){
        if (singletonAdm == null){
            singletonAdm = new Singleton();
        }
        return singletonAdm;
    }

    public int size(){
        return losBuckets.size();
    }

    public ArrayList getBuckets (){
        return losBuckets;
    }

    public Bucket getBucket(int i){
        return losBuckets.get(i);
    }

    public Bucket getBucket (long hash){
        for (int i = 0; i < losBuckets.size(); i++){
            if (losBuckets.get(i).hash == hash)
                return losBuckets.get(i);
        }
        return null;
    }

    public void insertarBucket(Bucket b){
        losBuckets.add(b);
    }
}
