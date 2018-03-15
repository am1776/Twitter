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
import com.amallya.twittermvvm.utils.Consts;
import com.amallya.twittermvvm.utils.EndlessRecyclerViewScrollListener;
import com.amallya.twittermvvm.utils.ItemClickSupport;
import com.amallya.twittermvvm.R;
import com.amallya.twittermvvm.RestApplication;
import com.amallya.twittermvvm.ui.tweet_detail.TweetDetailActivity;
import com.amallya.twittermvvm.data.remote.TwitterClient;
import com.amallya.twittermvvm.models.Entity;
import com.amallya.twittermvvm.models.Tweet;
import com.wang.avi.AVLoadingIndicatorView;
import org.parceler.Parcels;
import java.util.ArrayList;
import java.util.List;



public class TweetsFragment extends Fragment {

    private TweetsAdapter tweetsAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RelativeLayout relativeLayout;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    private TweetViewModel viewModel;

    public TweetsFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    private void observeViewModels(){
        viewModel.getTweetsObservable().observe(this, new Observer<List<Tweet>>() {
            @Override
            public void onChanged(@Nullable List<Tweet> tweets) {
                if (tweets != null) {
                    tweetsAdapter.setData(tweets);
                    avLoadingIndicatorView.hide();
                    mSwipeRefreshLayout.setRefreshing(false);                }
            }
        });

        viewModel.getRefreshingObservable().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                tweetsAdapter.notifyDataSetChanged();
            }
        });

        viewModel.getSelectedTweetObservable().observe(this, new Observer<Tweet>() {
            @Override
            public void onChanged(@Nullable Tweet tweet) {
                Intent intent = new Intent(getActivity(), TweetDetailActivity.class);
                intent.putExtra(TweetDetailActivity.TWEET_EXTRA, Parcels.wrap(tweet));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel =
                ViewModelProviders.of(this).get(TweetViewModel.class);
        observeViewModels();
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
        avLoadingIndicatorView.show();
    }

    private void setSwipeRefreshLayout(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refreshTweets();
            }
        });
    }

    private void setRecyclerView(){
        tweetsAdapter = new TweetsAdapter(getActivity(), new ArrayList<Tweet>(), viewModel);
        avLoadingIndicatorView = (AVLoadingIndicatorView)getView().findViewById(R.id.avi);
        RecyclerView rv = (RecyclerView)getView().findViewById(R.id.rvTweets);
        rv.setAdapter(tweetsAdapter);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);
        rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(lm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                viewModel.loadMoreTweets();
            }
        });
        ItemClickSupport.addTo(rv).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                            viewModel.onTweetClicked(position);
                        }
                    }
        );
    }
}
