package com.example.sparktrials.exp.forum;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sparktrials.R;
import com.example.sparktrials.models.Experiment;

public class ForumFragment extends Fragment {
    View view;
    Experiment experiment;
    public ForumFragment(Experiment experiment){
        this.experiment = experiment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forum, container, false);
        return view;
    }
}
