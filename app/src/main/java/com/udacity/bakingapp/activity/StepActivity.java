package com.udacity.bakingapp.activity;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.udacity.bakingapp.fragment.RecipeStepFragment;

import timber.log.Timber;

public class StepActivity extends AppCompatActivity {

    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private Guideline mGuideline;
    private TextView mDescription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        mPlayerView = findViewById(R.id.player_view);
        final String mediaURL = getIntent().getStringExtra(RecipeStepFragment.EXTRA_MEDIA_URL);
        if (mediaURL.length() > 0) {
            initializePlayer(mediaURL);
        } else {
            mPlayerView.setVisibility(View.GONE);
            mGuideline = findViewById(R.id.horizontalHalf);
            mGuideline.setVisibility(View.GONE);
        }

        mDescription = findViewById(R.id.tv_description);
        final String description = getIntent().getStringExtra(RecipeStepFragment.EXTRA_DESCRIPTION);
        mDescription.setText(description);
    }

    private void initializePlayer(String mediaURL) {
        if (mExoPlayer == null) {
            // Resource: https://gist.github.com/codeshifu/c26bb8a5f27f94d73b3a4888a509927c#file-mainactivity-java-L63
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(this),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());
            mPlayerView.setPlayer(mExoPlayer);

            // Resource: https://github.com/yusufcakmak/ExoPlayerSample/blob/master/app/src/main/java/com/yusufcakmak/exoplayersample/VideoPlayerActivity.java#L91
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(
                    this,
                    Util.getUserAgent(
                            this,
                            "BakingApp"),
                    (TransferListener<? super DataSource>) bandwidthMeter);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                    .createMediaSource(Uri.parse(mediaURL));
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Timber.d("onConfigurationChanged orientation: " + newConfig.orientation);

        // Checks the orientation of the screen and updates sizing of PlayerView
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
}
