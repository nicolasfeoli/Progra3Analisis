package com.example.meli.buckets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

/**
 * Created by Meli on 5/22/2017.
 */

class BucketAdapter extends ArrayAdapter<String>{

    public BucketAdapter(Context context, String[] nombres) {
        super(context, R.layout.activity_buckets,nombres);
    }
}
