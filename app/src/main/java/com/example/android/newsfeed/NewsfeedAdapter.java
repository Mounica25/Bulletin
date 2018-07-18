package com.example.android.newsfeed;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsfeedAdapter extends ArrayAdapter<Newsfeed> {

    public NewsfeedAdapter(Activity context, ArrayList<Newsfeed> newsfeeds) {
        super(context, 0, newsfeeds);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.newsfeed_list_item, parent, false);
        }

        Newsfeed currentNewsFeed = getItem(position);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);
        titleTextView.setText(currentNewsFeed.getmTitle());

        TextView sectionNameTextView = (TextView) listItemView.findViewById(R.id.sectionName);
        sectionNameTextView.setText(currentNewsFeed.getmSectionName());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        dateTextView.setText(currentNewsFeed.getmPublicationDate());

        return listItemView;
    }
}