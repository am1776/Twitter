package com.amallya.twittermvvm.ui.tweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.amallya.twittermvvm.utils.EndlessRecyclerViewScrollListener;
import com.amallya.twittermvvm.utils.ItemClickSupport;
import com.amallya.twittermvvm.R;
import com.amallya.twittermvvm.RestApplication;
import com.amallya.twittermvvm.ui.tweet_detail.TweetDetailActivity;
import com.amallya.twittermvvm.data.remote.TwitterClient;
import com.amallya.twittermvvm.data.local.DbHelper;
import com.amallya.twittermvvm.models.Entity;
import com.amallya.twittermvvm.models.Media;
import com.amallya.twittermvvm.models.Tweet;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public abstract class TweetsFragment extends Fragment {

    protected static final String ARG_PAGE = "ARG_PAGE";
    protected ArrayList<Tweet> tweetList;
    protected TweetsAdapter tweetsAdapter;
    protected TwitterClient client;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    private RelativeLayout relativeLayout;
    protected long max = -1;
    protected int mPage;
    protected AVLoadingIndicatorView avi;

    public TweetsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
        tweetList = new ArrayList<Tweet>();
        client = RestApplication.getRestClient();
        tweetsAdapter = new TweetsAdapter(getActivity(), tweetList, client);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tweet_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.root1);
        setSwipeRefreshLayout();
        setRecyclerView();
        avi.show();
        getTweets();
    }

    private void setSwipeRefreshLayout(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                max = -1;
                tweetList.clear();
                tweetsAdapter.notifyDataSetChanged();
                getTweets();
            }
        });
    }

    protected void handleNetworkFailure(int statusCode, Throwable throwable ){
        Log.d("Failed: ", "" + statusCode);
        Log.d("Error : ", "" + throwable);
        if(throwable instanceof java.io.IOException){
            noInternet();
        }
        avi.hide();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    protected abstract void getTweets();

    protected void processTweetJson(JSONArray json, boolean save){
        avi.hide();
        ArrayList<Tweet> tweetListNew = Tweet.getTweetList(json.toString());
        tweetList.addAll(tweetListNew);
        tweetsAdapter.notifyDataSetChanged();
        // save only tweets from timeline
        if(save) {
            for (Tweet tweet : tweetListNew) {
                System.out.println(tweet.toString());
                Entity entities = tweet.getEntities();
                if (entities.getMedia() != null) {
                    if (entities.getMedia().size() > 0) {
                        for (Media media : entities.getMedia()) {
                            media.setTweetId(tweet.getId());
                            //media.save();
                        }
                    }
                }
                //tweet.getUser().save();
                //tweet.save();
            }
        }
        if (tweetListNew.size() > 0){
            max = tweetListNew.get(tweetListNew.size() - 1).getId();
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }


    protected void noInternet(){
     /*   System.out.println("No internet connection");
        if(relativeLayout == null){
            return;
        }
        Snackbar snackbar = Snackbar
                .make(relativeLayout, "No Internet connection", Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.twitterBlue));
        snackbar.show();

        List<Tweet> tweetListDb = SQLite.select().
                from(Tweet.class).queryList();
        for(Tweet t:tweetListDb){
            t.setEntities(new Entity());
            List<Media> mediaList = DbHelper.getMediaForTweet(t.getId());
            t.getEntities().setMedia(mediaList);
        }
        tweetList.addAll(tweetListDb);
        tweetsAdapter.notifyDataSetChanged();*/
    }

    private void setRecyclerView(){
        avi = (AVLoadingIndicatorView)getView().findViewById(R.id.avi);
        RecyclerView rv = (RecyclerView)getView().findViewById(R.id.rvTweets);
        rv.setAdapter(tweetsAdapter);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);
        rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(lm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getTweets();
            }
        });
        ItemClickSupport.addTo(rv).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent intent;
                        switch(tweetList.get(position).getDisplayType()){
                            default:
                                intent = new Intent(getActivity(), TweetDetailActivity.class);
                                intent.putExtra("tweet", Parcels.wrap(tweetList.get(position)));
                                startActivity(intent);
                        }
                    }
                }
        );
    }
}
