package com.udacity.bakingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.adapter.IngredientAdapter;
import com.udacity.bakingapp.adapter.StepAdapter;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.utilities.JsonUtils;

public class InstructionListFragment
        extends Fragment
        implements StepAdapter.StepAdapterOnClickHandler {
    public static final String EXTRA_INGREDIENTS = "ingredients";
    public static final String EXTRA_STEPS = "steps";

    private ListView mIngredientsList;
    private RecyclerView mStepsRecyclerView;

    // Callback for communication between fragments
    private OnStepClickListener mCallback;

    @Override
    public void onClick(Recipe.Step selectedStep) {
        // Trigger the callback method and pass in the selected step
        mCallback.onStepSelected(selectedStep);
    }

    public interface OnStepClickListener {
        void onStepSelected(Recipe.Step step);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement OnStepClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_instruction_list, container, false);
        mIngredientsList = rootView.findViewById(R.id.ingredients_listview);
        mStepsRecyclerView = rootView.findViewById(R.id.steps_recyclerview);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();

        final Recipe.Ingredient[] ingredients =
                (Recipe.Ingredient[]) arguments.getParcelableArray(EXTRA_INGREDIENTS);
        final IngredientAdapter ingredientAdapter =
                new IngredientAdapter(getContext(), ingredients);
        mIngredientsList.setAdapter(ingredientAdapter);

        final Recipe.Step[] steps = (Recipe.Step[]) arguments.getParcelableArray(EXTRA_STEPS);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mStepsRecyclerView.setLayoutManager(layoutManager);
        final StepAdapter stepAdapter = new StepAdapter(steps, this);
        mStepsRecyclerView.setAdapter(stepAdapter);
    }
}
