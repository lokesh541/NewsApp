package com.example.android.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lokesh on 29/6/16.
 */
public class ArticleAdapter extends ArrayAdapter<Article> {


    public ArticleAdapter(Context context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);

        final Article currentArticle = getItem(position);

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.article_thumbnail);

        Picasso.with(getContext()).load(currentArticle.getThumbNailURL()).resize(100, 100).into(imageView);

        //Article title text view
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.article_title);
        titleTextView.setText(currentArticle.getTitle());

   // Intent to open url
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the text message with a string
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse(currentArticle.getmWebUrl()));
                getContext().startActivity(sendIntent);
            }
        });

        return listItemView;

    }
}
