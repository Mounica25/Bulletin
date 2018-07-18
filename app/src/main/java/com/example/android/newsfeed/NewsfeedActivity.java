package com.example.android.newsfeed;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsfeedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Newsfeed>> {

    private static final String NEWSFEED_REQUEST_URL =
            "https://content.guardianapis.com/search?q=news&api-key=test";

    private NewsfeedAdapter mAdapter;

    private static final int NEWSFEED_LOADER_ID = 1;

    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsfeed_activity);

        ListView newsfeedListView = (ListView) findViewById(R.id.list);
        mAdapter = new NewsfeedAdapter(this, new ArrayList<Newsfeed>());
        newsfeedListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_View);
        newsfeedListView.setEmptyView(mEmptyStateTextView);

        newsfeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Newsfeed currentNewsFeed = mAdapter.getItem(position);
                Uri newsfeedUrl = Uri.parse(currentNewsFeed.getmUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsfeedUrl);
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMngr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWSFEED_LOADER_ID, null, this);

        } else {

            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.progress_Bar);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Newsfeed>> onCreateLoader(int id, Bundle args) {
        return new NewsfeedLoader(this, NEWSFEED_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Newsfeed>> loader, List<Newsfeed> newsfeeds) {

        View progressBarView = findViewById(R.id.progress_Bar);
        progressBarView.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_newsfeed);

        mAdapter.clear();

        if (newsfeeds != null && !newsfeeds.isEmpty()) {
            mAdapter.addAll(newsfeeds);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Newsfeed>> loader) {
        mAdapter.clear();
    }
}