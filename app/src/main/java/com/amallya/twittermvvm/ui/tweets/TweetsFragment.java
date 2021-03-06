package com.amallya.twittermvvm.ui.tweets;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.amallya.twittermvvm.ViewModelFactory;
import com.amallya.twittermvvm.models.Response;
import com.amallya.twittermvvm.ui.base.BaseFragment;
import com.amallya.twittermvvm.utils.EndlessRecyclerViewScrollListener;
import com.amallya.twittermvvm.utils.ItemClickSupport;
import com.amallya.twittermvvm.R;
import com.amallya.twittermvvm.ui.tweet_detail.TweetDetailActivity;
import com.amallya.twittermvvm.models.Tweet;
import com.wang.avi.AVLoadingIndicatorView;
import org.parceler.Parcels;
import java.util.ArrayList;
import java.util.List;


public class TweetsFragment extends BaseFragment {

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
        viewModel.getTweetsObservable().observe(this, response -> handleResponse(response));

        viewModel.getRefreshingObservable().observe(this, aBoolean -> tweetsAdapter.notifyDataSetChanged());

        viewModel.getSelectedTweetObservable().observe(this, tweet -> {
            Intent intent = new Intent(getActivity(), TweetDetailActivity.class);
            intent.putExtra(TweetDetailActivity.TWEET_EXTRA, Parcels.wrap(tweet));
            startActivity(intent);
        });

        viewModel.getTweetsActionsObservable().observe(this, response -> handleResponse(response));
    }

    @Override
    public void handleSuccess(Response response){
        List<Tweet> list = (List<Tweet>) response.getData();
        tweetsAdapter.setData(list);
        avLoadingIndicatorView.hide();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void handleError(Response response){
        showSnackBar(R.id.root1, getString(R.string.error_msg));
        avLoadingIndicatorView.hide();
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelFactory factory = ViewModelFactory.getInstance(getActivity().getApplication());
        viewModel =
                ViewModelProviders.of(this, factory).get(TweetViewModel.class);
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
        mSwipeRefreshLayout.setOnRefreshListener(() -> viewModel.refreshTweets());
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
                (recyclerView, position, v) -> viewModel.onTweetClicked(position)
        );
    }
}
