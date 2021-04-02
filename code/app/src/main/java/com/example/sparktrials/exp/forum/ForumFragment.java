package com.example.sparktrials.exp.forum;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sparktrials.R;
import com.example.sparktrials.models.Answer;
import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.Profile;
import com.example.sparktrials.models.Question;

import java.util.ArrayList;

public class ForumFragment extends Fragment {
    View view;
    Experiment experiment;
    private ArrayAdapter<Question> questionAdapter;
    private ArrayList<Question> questions;
    private ListView questionList;

    private Button askQuestionButton;
    public ForumFragment(Experiment experiment){
        this.experiment = experiment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forum, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        questionList = view.findViewById(R.id.question_list);
        askQuestionButton = view.findViewById(R.id.forum_ask_question_button);

        askQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForumAskQuestionFragment forumAskQuestionFragment = ForumAskQuestionFragment.newInstance();
                forumAskQuestionFragment.show(getActivity().getSupportFragmentManager(), "ask_question_dialog_fragment");
            }
        });

        questions = new ArrayList<>();
        Profile p1 = new Profile("12343214","user-1234",null,null);
        ArrayList<Answer> a1 = new ArrayList<>();
        Question q1 = new Question("idid","Lorem ipsum dolor sit amet, consectetur?","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc commodo erat quis lectus imperdiet, a elementum augue auctor. Suspendisse vel diam sit amet lectus laoreet lacinia quis in tortor.","expid", p1, a1);


        for (int i = 0; i < 5; i++)
            questions.add(q1);

        questionAdapter = new CustomQuestionList(getContext(), questions);
        questionList.setAdapter(questionAdapter);

    }
}
