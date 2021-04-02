package com.example.sparktrials.exp.forum;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sparktrials.FirebaseManager;
import com.example.sparktrials.IdManager;
import com.example.sparktrials.R;
import com.example.sparktrials.models.Experiment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashMap;

public class ForumAskQuestionFragment extends BottomSheetDialogFragment {

    public static ForumAskQuestionFragment newInstance() {
        return new ForumAskQuestionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ask_question, container, false);
        TextView cancelButton = view.findViewById(R.id.forum_ask_cancel);
        TextView postButton = view.findViewById(R.id.forum_ask_post);
        EditText titleText = view.findViewById(R.id.forum_ask_title);
        EditText bodyText = view.findViewById(R.id.forum_ask_body);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseManager firebaseManager = new FirebaseManager();
                IdManager idManager = new IdManager(getContext());
                String title = titleText.getText().toString();
                String body = bodyText.getText().toString();
                String id = idManager.generateRandomId();
                HashMap<String, Object> data = new HashMap<>();

                data.put("title", title);
                data.put("body", body);

//                firebaseManager.set("posts", id, data);
            }
        });


        return view;
    }


}
