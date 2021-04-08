package com.example.sparktrials.exp.forum;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.sparktrials.R;
import com.example.sparktrials.models.Answer;
import com.example.sparktrials.models.Answer;

import java.util.ArrayList;

public class CustomAnswerList extends ArrayAdapter<Answer> {
    private ArrayList<Answer> answers;
    private Context context;

    public CustomAnswerList(Context context, ArrayList<Answer> answers) {
        super(context, 0, answers);
        this.context = context;
        this.answers = answers;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_content_forum_post, parent, false);
        }

        Answer answer = answers.get(position);
        TextView floor = view.findViewById(R.id.forum_detail_post_floor);

        TextView body = view.findViewById(R.id.forum_detail_post_body);
        TextView author = view.findViewById(R.id.forum_detail_post_author);
        TextView time = view.findViewById(R.id.forum_detail_post_time);

        floor.setText("#" + (int)(answers.indexOf(answer) + 1));
        body.setText(answer.getBody());
        author.setText(answer.getAuthor().getUsername());
        time.setText(answer.getFormattedDate(answer.getDate()));
        return view;
    }
}
