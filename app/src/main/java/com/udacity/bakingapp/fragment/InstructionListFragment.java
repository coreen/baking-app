package com.udacity.bakingapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.adapter.IngredientAdapter;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.utilities.JsonUtils;

public class InstructionListFragment extends Fragment {
    public static final String EXTRA_INGREDIENTS = "ingredients";
    public static final String EXTRA_STEPS = "steps";

    private ListView mIngredientsList;
    private ListView mStepsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_instruction_list, container, false);
        mIngredientsList = rootView.findViewById(R.id.ingredients_listview);
        mStepsList = rootView.findViewById(R.id.steps_listview);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();

        final Recipe.Ingredient[] ingredients = (Recipe.Ingredient[]) arguments.getParcelableArray(EXTRA_INGREDIENTS);
        final IngredientAdapter ingredientAdapter = new IngredientAdapter(getContext(), ingredients);
        mIngredientsList.setAdapter(ingredientAdapter);

        final Recipe.Step[] steps = (Recipe.Step[]) arguments.getParcelableArray(EXTRA_STEPS);
        final String[] stepShortDescriptions = JsonUtils.getStepShortDescriptions(steps);
        final ArrayAdapter<String> stepAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        stepAdapter.addAll(stepShortDescriptions);
        mStepsList.setAdapter(stepAdapter);
    }
}
