package com.udacity.bakingapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.udacity.bakingapp.R;

public class RecipeStepFragment extends Fragment {
    public static final String EXTRA_DESCRIPTION = "description";
    public static final String EXTRA_MEDIA_URL = "mediaURL";

    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private Guideline mGuideline;
    private TextView mDescription;

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container);
        mPlayerView = rootView.findViewById(R.id.player_view);
        mGuideline = rootView.findViewById(R.id.horizontalHalf);
        mDescription = rootView.findViewById(R.id.tv_description);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();

        final String description = arguments.getString(EXTRA_DESCRIPTION);
        final String mediaURL = arguments.getString(EXTRA_MEDIA_URL);

        if (mediaURL.length() > 0) {
            initializePlayer(mediaURL);
        } else {
            mPlayerView.setVisibility(View.GONE);
            mGuideline.setVisibility(View.GONE);
        }
        mDescription.setText(description);

        mContext = getContext();
    }

    private void initializePlayer(String mediaURL) {
        if (mExoPlayer == null) {
            // Resource: https://gist.github.com/codeshifu/c26bb8a5f27f94d73b3a4888a509927c#file-mainactivity-java-L63
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(mContext),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());
            mPlayerView.setPlayer(mExoPlayer);

            // Resource: https://github.com/yusufcakmak/ExoPlayerSample/blob/master/app/src/main/java/com/yusufcakmak/exoplayersample/VideoPlayerActivity.java#L91
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(
                    mContext,
                    Util.getUserAgent(
                            mContext,
                            "BakingApp"),
                    (TransferListener<? super DataSource>) bandwidthMeter);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                    .createMediaSource(Uri.parse(mediaURL));
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    // TODO figure out when to call this for fragment, if necessary
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
