package com.udacity.bakingapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.model.Recipe;

import timber.log.Timber;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder> {

    private Recipe.Step[] mSteps;
    private final StepAdapterOnClickHandler mClickHandler;

    public interface StepAdapterOnClickHandler {
        void onClick(Recipe.Step selectedStep);
    }

    public StepAdapter(Recipe.Step[] mSteps, StepAdapterOnClickHandler mClickHandler) {
        this.mSteps = mSteps;
        this.mClickHandler = mClickHandler;
    }

    @NonNull
    @Override
    public StepAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.step_item, parent, false);
        return new StepAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapterViewHolder holder, int position) {
        holder.bind(holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        if (null == mSteps) {
            return 0;
        }
        return mSteps.length;
    }

    public class StepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mShortDescription;

        public StepAdapterViewHolder(View itemView) {
            super(itemView);
            mShortDescription = itemView.findViewById(R.id.tv_step_short_description);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            mShortDescription.setText(mSteps[position].getShortDescription());
        }

        @Override
        public void onClick(View v) {
            Timber.d("StepAdapterViewHolder onClick detected for position: " + getAdapterPosition());
            mClickHandler.onClick(mSteps[getAdapterPosition()]);
        }
    }
}
