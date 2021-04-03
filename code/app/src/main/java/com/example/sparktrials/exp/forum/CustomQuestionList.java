package com.example.sparktrials.exp.forum;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.sparktrials.R;
import com.example.sparktrials.models.Question;

import java.util.ArrayList;

public class CustomQuestionList extends ArrayAdapter<Question> {
    private ArrayList<Question> questions;
    private Context context;

    public CustomQuestionList(Context context, ArrayList<Question> questions) {
        super(context, 0, questions);
        this.context = context;
        this.questions = questions;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_content_forum,parent,false);
        }

        Question question = questions.get(position);

        TextView body = view.findViewById(R.id.forum_post_body);
        TextView date = view.findViewById(R.id.forum_post_time);
        TextView title = view.findViewById(R.id.forum_post_title);
        TextView author = view.findViewById(R.id.forum_post_author);
        TextView reply_number = view.findViewById(R.id.forum_reply_number);

        body.setText(question.getBody());
        date.setText(question.getFormattedDate(question.getLastReplyTime()));
        title.setText(question.getTitle());
        author.setText(question.getAuthor().getUsername());
        reply_number.setText("" + question.getAnswerCount());

        return view;
    }

}
