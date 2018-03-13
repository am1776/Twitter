package com.amallya.twittermvvm.ui.tweets;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.amallya.twittermvvm.models.User;
import com.amallya.twittermvvm.ui.main.MainViewModel;
import com.amallya.twittermvvm.utils.Consts;
import com.amallya.twittermvvm.utils.EndlessRecyclerViewScrollListener;
import com.amallya.twittermvvm.utils.ItemClickSupport;
import com.amallya.twittermvvm.R;
import com.amallya.twittermvvm.RestApplication;
import com.amallya.twittermvvm.ui.tweet_detail.TweetDetailActivity;
import com.amallya.twittermvvm.data.remote.TwitterClient;
import com.amallya.twittermvvm.models.Entity;
import com.amallya.twittermvvm.models.Media;
import com.amallya.twittermvvm.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.wang.avi.AVLoadingIndicatorView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class TweetsFragment extends Fragment {

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

    public static TweetsFragment newInstance(int page) {
        TweetsFragment fragment = new TweetsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        final TweetViewModel viewModel =
                ViewModelProviders.of(this).get(TweetViewModel.class);
        observeViewModel(viewModel);
    }

    private void observeViewModel(TweetViewModel tweetViewModel){
        tweetViewModel.getTweets().observe(this, new Observer<List<Tweet>>() {
            @Override
            public void onChanged(@Nullable List<Tweet> tweets) {
                if (tweets != null) {
                    tweetList.addAll(tweets);
                    tweetsAdapter.notifyDataSetChanged();
                }
            }
        });
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
        //getTweets();
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
            //noInternet();
        }
        avi.hide();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    protected void getTweets(){
        client.getTweetTimelineList(max, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                processTweetJson(json, false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject j) {
                handleNetworkFailure(statusCode, throwable);
            }
        });
    }

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



    public void postTweet(String newTweet, User loggedInUser){
        Tweet t = new Tweet();
        t.setText(newTweet);
        t.setUser(loggedInUser);
        t.setEntities(new Entity());
        t.setCreatedAt(Consts.JUST_NOW);
        tweetList.add(0,t);
        tweetsAdapter.notifyDataSetChanged();
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
