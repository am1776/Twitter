package com.amallya.twittermvvm.ui.tweet_detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.amallya.twittermvvm.R;
import com.amallya.twittermvvm.ViewModelFactory;
import com.amallya.twittermvvm.models.Response;
import com.amallya.twittermvvm.models.Tweet;
import com.amallya.twittermvvm.models.User;
import com.amallya.twittermvvm.ui.base.BaseActivity;
import com.amallya.twittermvvm.ui.main.MainViewModel;
import com.amallya.twittermvvm.ui.tweets.TweetUserAction;
import com.amallya.twittermvvm.utils.Consts;
import com.amallya.twittermvvm.utils.Utils;
import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetDetailActivity extends BaseActivity {

    @BindView(R.id.iv_profile_pic) ImageButton ivProfilePic;
    @BindView(R.id.iv_reply) ImageButton ivReply;
    @BindView(R.id.iv_direct_msg) ImageButton ivDirectMsg;
    @BindView(R.id.iv_retweet) ToggleButton ivRetweet;
    @BindView(R.id.iv_like) ToggleButton ivLike;
    @BindView(R.id.iv_media) ImageView ivMedia;
    @BindView(R.id.et_reply) EditText etReply;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_profile_name) TextView tvProfileName;
    @BindView(R.id.tv_profile_handle) TextView tvProfileHandler;
    @BindView(R.id.tv_created_time) TextView tvCreatedTime;
    @BindView(R.id.tv_tweet) TextView tvTweet;
    @BindView(R.id.tv_retweet_count) TextView tvRetweetCount;
    @BindView(R.id.tv_like_count) TextView tvLikeCount;
    @BindView(R.id.root) RelativeLayout relativeLayout;

    private Tweet tweet;
    private TweetDetailViewModel viewModel;
    public static final String TWEET_EXTRA = "tweet";
    private static final String TITLE = "tweet";
    private static final String REPLY_TO = "Reply to ";
    private static final String BLANK_TEXT = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        ButterKnife.bind(this);
        setupViewModel();
        setupToolbar();
        tweet = Parcels.unwrap(getIntent().getParcelableExtra(TWEET_EXTRA));
        setupViews(tweet);
    }

    private void setupViewModel(){
        ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        viewModel =
                ViewModelProviders.of(this, factory).get(TweetDetailViewModel.class);
        observeViewModel(viewModel);
    }

    private void observeViewModel(TweetDetailViewModel tweetDetailViewModel){
        tweetDetailViewModel.getTweetsActionsObservable().observe(this, response -> handleResponse(response));
    }

    @Override
    public void handleSuccess(Response response){
        showSnackBar(R.id.root, getString(R.string.success_msg));
    }

    @Override
    public void handleError(Response response){
       showSnackBar(R.id.root, getString(R.string.error_msg));
    }

    private void setupViews(final Tweet tweet) {
        getSupportActionBar().setTitle(TITLE);
        tvCreatedTime.setText(Utils.getTwitterDateVerbose(tweet.getCreatedAt()));
        tvLikeCount.setText(tweet.getFavouritesCount()+"");
        tvProfileHandler.setText(getResources().getString(R.string.screen_name, tweet.getUser().getScreenName()));
        tvProfileName.setText(tweet.getUser().getName());
        tvRetweetCount.setText(tweet.getRetweetCount()+"");
        tvTweet.setText(tweet.getText());
        etReply.setHint(REPLY_TO+tweet.getUser().getName());
        ivLike.setChecked(tweet.isFavorited());
        ivRetweet.setChecked(tweet.isRetweeted());
        setToggleButtons();
        setImages();
    }

    private void setImages(){
        if((tweet.getEntities().getMedia()!=null) && (tweet.getEntities().getMedia().size() > 0)){
            ivMedia.setVisibility(View.VISIBLE);
            Glide.with(this).load(tweet.getEntities().getMedia().get(0).getMediaUrl())
                    .bitmapTransform(new RoundedCornersTransformation(this, Consts.RL, Consts.RL))
                    .placeholder(R.color.grey).into(ivMedia);
        } else {
            ivMedia.setVisibility(View.GONE);
        }
        final String screenName = tweet.getUser().getScreenName();
        etReply.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                etReply.setText("@"+screenName+" ");
            }
        });
        Glide.with(this).load(tweet.getUser().getProfileImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(this, Consts.RS, Consts.RS))
                .placeholder(R.color.grey).into(ivProfilePic);
    }

    private void setupToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(Consts.TOOLBAR_HEIGHT);
    }

    private void setToggleButtons(){
        ivLike.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                viewModel.userActionOnTweet(TweetUserAction.FAVORITE, tweet.getId());
                tvLikeCount.setText(tweet.getFavouritesCount()+1+"");
                tvLikeCount.setTextColor(getResources().getColor(R.color.favRed));
            } else {
                viewModel.userActionOnTweet(TweetUserAction.UNFAVORITE, tweet.getId());
                tvLikeCount.setText(tweet.getFavouritesCount()-1+"");
                tvLikeCount.setTextColor(getResources().getColor(R.color.darkGrey));
            }
        });

        ivRetweet.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                viewModel.userActionOnTweet(TweetUserAction.RETWEET, tweet.getId());
                tvRetweetCount.setText(tweet.getRetweetCount()+1+"");
                tvRetweetCount.setTextColor(getResources().getColor(R.color.retweetGreen));
            } else {
                viewModel.userActionOnTweet(TweetUserAction.UNRETWEET, tweet.getId());
                tvRetweetCount.setText(tweet.getRetweetCount()-1+"");
                tvRetweetCount.setTextColor(getResources().getColor(R.color.darkGrey));
            }
        });
    }

    public void replyClicked(View view){
        String tweetResponse = etReply.getText().toString();
        etReply.setText(BLANK_TEXT);
        viewModel.userReplyOnTweet(TweetUserAction.REPLY, tweet.getId(), tweetResponse);
    }
}
