package com.example.oliveiras.findpic.Activities.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oliveiras.findpic.Models.ImageResult;
import com.example.oliveiras.findpic.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by oliveiras on 2/27/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {
    public ImageResultsAdapter(Context context, List<ImageResult> images) {
        super(context, R.layout.item_image_result, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ImageResult imageInfo = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);

        }
        //found image and title from layout
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        //TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        //clear up image from last result
        ivImage.setImageResource(0);
        //Populate title and remote download the image URL
        //tvTitle.setText(Html.fromHtml(imageInfo.title));
        //using Picasso to download the image url in background
        Picasso.with(getContext()).load(imageInfo.thumbUrl).into(ivImage);
        //return the completed view to be displayed
        return convertView;
    }

}
