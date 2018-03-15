package com.amallya.twittermvvm.ui.tweets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.amallya.twittermvvm.utils.Consts;
import com.amallya.twittermvvm.utils.PatternEditableBuilder;
import com.amallya.twittermvvm.R;
import com.amallya.twittermvvm.utils.Utils;
import com.amallya.twittermvvm.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Pattern;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static android.view.View.GONE;

/**
 * Created by anmallya on 3/10/2018.
 */

public class TweetsAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Tweet> tweetList = null;
    private Context getContext() {
        return mContext;
    }
    private Context mContext;
    private TweetViewModel tweetViewModel;

    public TweetsAdapter(Context context, List<Tweet> tweetList, TweetViewModel tweetViewModel) {
        this.tweetList = tweetList;
        this.mContext = context;
        this.tweetViewModel = tweetViewModel;
    }

    public void setData(List<Tweet> tweetList){
        this.tweetList = tweetList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v1 = inflater.inflate(R.layout.layout_tweet_list_viewholder, parent, false);
        viewHolder = new TweetListViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        TweetListViewHolder vh1 = (TweetListViewHolder) viewHolder;
        configureViewHolder(vh1, position);
    }

    private void configureViewHolder(final TweetListViewHolder vh, int position) {
        final Tweet tweet = tweetList.get(position);
        setButtonVisibility(tweet, vh);
        setText(vh, tweet);
        setToggleButton(vh, tweet);
        setCountColor(vh, tweet);
        setImages(vh, tweet);
        setImageButtons(vh, tweet);
        setToggleListners(vh, tweet);
    }

    private void setText(TweetListViewHolder vh, Tweet tweet){
        if(tweet.getUser() != null ){
            vh.getTvProfileName().setText(tweet.getUser().getName());
            vh.getTvProfileHandler().setText(getContext().getResources().getString(R.string.screen_name, tweet.getUser().getScreenName()));
        }

        vh.getTvTweet().setText(tweet.getText());
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), getContext().getResources().getColor(R.color.linkBlue),
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {

                            }
                        }).into(vh.getTvTweet());
        vh.getTvCreatedTime().setText(Utils.getTwitterDate(tweet.getCreatedAt()));
        vh.getTvLikeCount().setText(tweet.getFavouritesCount()+"");
        vh.getTvRetweetCount().setText(tweet.getRetweetCount()+"");
    }

    private void setToggleButton(TweetListViewHolder vh, Tweet tweet){
        vh.getIvLike().setChecked(tweet.isFavorited());
        vh.getIvRetweet().setChecked(tweet.isRetweeted());
    }

    private void setCountColor(TweetListViewHolder vh, Tweet tweet){
        int favoriteCountTextColor = tweet.isFavorited()? getContext().getResources().getColor(R.color.favRed) : getContext().getResources().getColor(R.color.darkGrey);
        vh.getTvLikeCount().setTextColor(favoriteCountTextColor);
        int retweetCountTextColor = tweet.isRetweeted()? getContext().getResources().getColor(R.color.retweetGreen) : getContext().getResources().getColor(R.color.darkGrey);
        vh.getTvRetweetCount().setTextColor(retweetCountTextColor);
    }


    private void setImages(TweetListViewHolder vh, final Tweet tweet){
        if((tweet.getEntities()!= null) && (tweet.getEntities().getMedia()!=null)){
            if(tweet.getEntities().getMedia().size() > 0){
                vh.getIvMedia().setVisibility(View.VISIBLE);
                Picasso.with(mContext).
                        load(tweet.getEntities().getMedia().get(0).getMediaUrl())
                        .transform(new RoundedCornersTransformation(Consts.RL, Consts.RL)).
                        placeholder(R.color.grey).into(vh.getIvMedia());
            } else{
                vh.getIvMedia().setVisibility(GONE);
            }
        }
        else {
            vh.getIvMedia().setVisibility(GONE);
        }
    }

    private void setImageButtons(TweetListViewHolder vh, final Tweet tweet){
        ImageView ib = vh.getIvProfilePic();
        Picasso.with(mContext).load(tweet.getUser().getProfileImageUrl())
                .transform(new RoundedCornersTransformation(Consts.RL, Consts.RL)).placeholder(R.color.grey).into(ib);
    }

    private void setToggleListners(final TweetListViewHolder vh,final  Tweet tweet){
        (vh.getIvLike()).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                tweet.setFavorited(!tweet.isFavorited());
                if(tweet.isFavorited()) {
                    tweetViewModel.userActionOnTweet(TweetUserAction.FAVORITE, tweet.getId());
                    vh.getTvLikeCount().setText(tweet.getFavouritesCount()+1+"");
                    vh.getTvLikeCount().setTextColor(getContext().getResources().getColor(R.color.favRed));
                } else {
                    tweetViewModel.userActionOnTweet(TweetUserAction.UNFAVORITE, tweet.getId());
                    vh.getTvLikeCount().setText(tweet.getFavouritesCount()-1+"");
                    vh.getTvLikeCount().setTextColor(getContext().getResources().getColor(R.color.darkGrey));
                }
            }
        });

        (vh.getIvRetweet()).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                tweet.setRetweeted(!tweet.isRetweeted());
                if(tweet.isRetweeted()) {
                    tweetViewModel.userActionOnTweet(TweetUserAction.RETWEET, tweet.getId());
                    vh.getTvRetweetCount().setText(tweet.getRetweetCount()+1+"");
                    vh.getTvRetweetCount().setTextColor(getContext().getResources().getColor(R.color.retweetGreen));
                } else {
                    tweetViewModel.userActionOnTweet(TweetUserAction.UNRETWEET, tweet.getId());
                    vh.getTvRetweetCount().setText(tweet.getRetweetCount()-1+"");
                    vh.getTvRetweetCount().setTextColor(getContext().getResources().getColor(R.color.darkGrey));
                }
            }
        });
    }



    private void setButtonVisibility(Tweet tweet, TweetListViewHolder vh){
        switch(tweet.getDisplayType()){
            case MESSAGE:
                vh.getIvRetweet().setVisibility(GONE);
                vh.getTvLikeCount().setVisibility(GONE);
                vh.getTvRetweetCount().setVisibility(GONE);
                vh.getIvDirectMsg().setVisibility(GONE);
                vh.getIvLike().setVisibility(GONE);
                vh.getIvReply().setVisibility(GONE);
                break;
            default:
                vh.getIvRetweet().setVisibility(View.VISIBLE);
                vh.getTvLikeCount().setVisibility(View.VISIBLE);
                vh.getTvRetweetCount().setVisibility(View.VISIBLE);
                vh.getIvDirectMsg().setVisibility(View.VISIBLE);
                vh.getIvLike().setVisibility(View.VISIBLE);
                vh.getIvReply().setVisibility(View.VISIBLE);
        }
    }
}
