package com.udacity.bakingapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ui.PlayerView;
import com.udacity.bakingapp.R;

public class RecipeStepFragment extends Fragment {
    public static final String EXTRA_DESCRIPTION = "description";
    public static final String EXTRA_MEDIA_URL = "mediaURL";

    private PlayerView mPlayerView;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container);
        mPlayerView = rootView.findViewById(R.id.player_view);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();

        final String description = arguments.getString(EXTRA_DESCRIPTION);
        final String mediaURL = arguments.getString(EXTRA_MEDIA_URL);
    }
}
