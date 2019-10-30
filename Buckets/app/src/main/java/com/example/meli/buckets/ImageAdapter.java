package com.example.meli.buckets;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Meli on 5/22/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private Bucket bucket;

    ImageAdapter (Context c, long hash){
        context = c;
        bucket = Singleton.getInstance().getBucket(hash);
    }


    @Override
    public int getCount() {
        return bucket.imagenes.size();
    }

    @Override
    public Object getItem(int position) {
        return bucket.imagenes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imgv = new ImageView(context);
        //imgv.setImageResource(images[position]);
        imgv.setImageBitmap(bucket.imagenes.get(position));
        imgv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imgv.setLayoutParams(new GridView.LayoutParams(240,240));
        return imgv;
    }
}
