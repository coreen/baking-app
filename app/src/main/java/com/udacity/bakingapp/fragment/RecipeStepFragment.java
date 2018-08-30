package com.udacity.bakingapp.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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

import timber.log.Timber;

public class RecipeStepFragment extends Fragment {
    public static final String EXTRA_DESCRIPTION = "description";
    public static final String EXTRA_MEDIA_URL = "mediaURL";

    private static final String EXTRA_VIDEO_POSITION = "videoPosition";
    private static final String EXTRA_VIDEO_WINDOW = "videoWindow";
    private static final String EXTRA_VIDEO_AUTOPLAY = "videoAutoplay";

    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private Guideline mGuideline;
    private TextView mDescription;

    private String mediaURL;

    private Context mContext;

    private long mPlayerPosition;
    private int mPlayerWindow;
    private boolean mPlayerAutoplay;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        mPlayerView = rootView.findViewById(R.id.player_view);
        mGuideline = rootView.findViewById(R.id.horizontalHalf);
        mDescription = rootView.findViewById(R.id.tv_description);

        if (savedInstanceState == null) {
            mPlayerPosition = 0;
            mPlayerWindow = 0;
            mPlayerAutoplay = true;
        } else {
            mPlayerPosition = savedInstanceState.getLong(EXTRA_VIDEO_POSITION);
            mPlayerWindow = savedInstanceState.getInt(EXTRA_VIDEO_WINDOW);
            mPlayerAutoplay = savedInstanceState.getBoolean(EXTRA_VIDEO_AUTOPLAY);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();

        final String description = arguments.getString(EXTRA_DESCRIPTION);
        // Saved to variable for restoring exoplayer state in onResume
        mediaURL = arguments.getString(EXTRA_MEDIA_URL);

        // Grab context of the running activity
        mContext = getActivity();

        if (mediaURL.length() > 0) {
            initializePlayer(mediaURL);
        } else {
            mPlayerView.setVisibility(View.GONE);
            mGuideline.setVisibility(View.GONE);
        }
        mDescription.setText(description);
    }

    // Resource: https://developer.android.com/guide/topics/resources/runtime-changes#HandlingTheChange
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Timber.d("onConfigurationChanged orientation: " + newConfig.orientation);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mPlayerView.getLayoutParams();
        params.width = params.MATCH_PARENT;
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // set full screen
            params.height = params.MATCH_PARENT;
            mDescription.setVisibility(View.INVISIBLE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // keep original sizing
            params.height = 800;
            mDescription.setVisibility(View.VISIBLE);
        }
        mPlayerView.setLayoutParams(params);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            mPlayerWindow = mExoPlayer.getCurrentWindowIndex();
            mPlayerAutoplay = mExoPlayer.getPlayWhenReady();

            outState.putLong(EXTRA_VIDEO_POSITION, mPlayerPosition);
            outState.putInt(EXTRA_VIDEO_WINDOW, mPlayerWindow);
            outState.putBoolean(EXTRA_VIDEO_AUTOPLAY, mPlayerAutoplay);
        }
    }

    private void initializePlayer(String mediaURL) {
        if (mExoPlayer == null) {
            Timber.d("Seting up exoplayer window " + mPlayerWindow +
                    " at position " + mPlayerPosition + ", autoplay: " + mPlayerAutoplay);
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
            mExoPlayer.setPlayWhenReady(mPlayerAutoplay);
            mExoPlayer.seekTo(mPlayerWindow, mPlayerPosition);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            // Persist last known player config
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            mPlayerWindow = mExoPlayer.getCurrentWindowIndex();
            mPlayerAutoplay = mExoPlayer.getPlayWhenReady();

            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer(mediaURL);
    }
}
