package com.choiminseon.nbggapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.choiminseon.nbggapp.R;
import com.choiminseon.nbggapp.model.QuestionAnswer;

import java.util.ArrayList;

public class ImmigrationAdapter extends RecyclerView.Adapter<ImmigrationAdapter.ViewHolder>{

    Context context;
    ArrayList<QuestionAnswer> questionAnswerArrayList;

    public ImmigrationAdapter(Context context, ArrayList<QuestionAnswer> questionAnswerArrayList) {
        this.context = context;
        this.questionAnswerArrayList = questionAnswerArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.immigration_question_answer_list_row, parent, false);
        return new ImmigrationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionAnswer questionAnswer = questionAnswerArrayList.get(position);

        holder.txtQuestionEng.setText("Q. " + questionAnswer.questionEng);
        holder.txtQuestionKor.setText("(" + questionAnswer.questionKor + ")");
        holder.txtAnswerEng.setText("A. " + questionAnswer.answerEng);
        holder.txtAnswerKor.setText("(" + questionAnswer.answerKor + ")");
    }

    @Override
    public int getItemCount() {
        return questionAnswerArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtQuestionEng;
        TextView txtQuestionKor;
        TextView txtAnswerEng;
        TextView txtAnswerKor;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtQuestionEng = itemView.findViewById(R.id.txtQuestionEng);
            txtQuestionKor = itemView.findViewById(R.id.txtQuestionKor);
            txtAnswerEng = itemView.findViewById(R.id.txtAnswerEng);
            txtAnswerKor = itemView.findViewById(R.id.txtAnswerKor);

        }
    }

}
