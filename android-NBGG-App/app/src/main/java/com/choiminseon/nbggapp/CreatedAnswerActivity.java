package com.choiminseon.nbggapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.choiminseon.nbggapp.api.ImmigrationApi;
import com.choiminseon.nbggapp.api.NetworkClient;
import com.choiminseon.nbggapp.api.TranslateApi;
import com.choiminseon.nbggapp.model.QuestionAnswer;
import com.choiminseon.nbggapp.model.QuestionAnswerRes;
import com.choiminseon.nbggapp.model.TranslateReq;
import com.choiminseon.nbggapp.model.TranslateRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CreatedAnswerActivity extends AppCompatActivity {


    EditText editChoiceQuestion;
    EditText editAnswer;
    EditText editChoiceLanguage;
    ImageView imgSpeak;
    Button btnTranslate;
    ArrayList<QuestionAnswer> questionAnswerArrayList = new ArrayList<>();
    TextView txtTranslateAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_answer);
        
        editChoiceQuestion = findViewById(R.id.editChoiceQuestion);
        editAnswer = findViewById(R.id.editAnswer);
        editChoiceLanguage = findViewById(R.id.editChoiceLanguage);
        imgSpeak = findViewById(R.id.imgSpeak);
        txtTranslateAnswer = findViewById(R.id.txtTranslateAnswer);
        btnTranslate = findViewById(R.id.btnTranslate);
        
        editChoiceQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다이얼로그 띄우기
                fetchQuestions();
            }
        });

        editChoiceLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다이얼로그 띄우기
                showLanguageDialog();

            }
        });

        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 번역하기

                if (editAnswer.equals("")) {
                    Toast.makeText(CreatedAnswerActivity.this, "답변내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (editChoiceLanguage.equals("")) {
                    Toast.makeText(CreatedAnswerActivity.this, "번역할 언어를 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    translateAnswer();
                }

            }
        });

        imgSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tts
                if (txtTranslateAnswer.equals("")) {
                    Toast.makeText(CreatedAnswerActivity.this, "번역한 내용이 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }



            }
        });

    }

    private void translateAnswer() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(CreatedAnswerActivity.this);
        TranslateApi api = retrofit.create(TranslateApi.class);
        // 선택한 언어
        String choiceLanguage = editChoiceLanguage.getText().toString();
        // 입력한 답변 내용
        String answer = editAnswer.getText().toString().trim();


        TranslateReq translateReq = new TranslateReq(choiceLanguage, answer);
        Call<TranslateRes> call = api.translateAnswer(translateReq);
        call.enqueue(new Callback<TranslateRes>() {
            @Override
            public void onResponse(Call<TranslateRes> call, Response<TranslateRes> response) {
                if (response.isSuccessful()) {
                    TranslateRes translateRes = response.body();
                    txtTranslateAnswer.setText(translateRes.transAnswer);
                }
            }

            @Override
            public void onFailure(Call<TranslateRes> call, Throwable throwable) {
                Toast.makeText(CreatedAnswerActivity.this, "네트워크 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchQuestions() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(CreatedAnswerActivity.this);
        ImmigrationApi api = retrofit.create(ImmigrationApi.class);
        Call<QuestionAnswerRes> call = api.getQuestionAnswerList();
        call.enqueue(new Callback<QuestionAnswerRes>() {
            @Override
            public void onResponse(Call<QuestionAnswerRes> call, Response<QuestionAnswerRes> response) {
                if (response.isSuccessful()) {
                    QuestionAnswerRes questionAnswerRes = response.body();
                    questionAnswerArrayList.addAll(questionAnswerRes.questionAnswer);
                    showQuestionDialog();
                }
            }

            @Override
            public void onFailure(Call<QuestionAnswerRes> call, Throwable throwable) {
                Toast.makeText(CreatedAnswerActivity.this, "네트워크 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showQuestionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("질문을 선택하세요");

        String[] questionsArray = new String[questionAnswerArrayList.size()];
        for (int i = 0; i < questionAnswerArrayList.size(); i++) {
            questionsArray[i] = questionAnswerArrayList.get(i).questionKor;
        }

        builder.setItems(questionsArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editChoiceQuestion.setText(questionsArray[which]);
            }
        });

        builder.show();
    }

    private void showLanguageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("언어를 선택하세요");

        final String[] languages = {"영어", "일본어", "중국어(간체)", "스페인어", "프랑스어", "독일어", "러시아어", "힌디어", "우르두어", "포르투갈어"};

        builder.setItems(languages, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editChoiceLanguage.setText(languages[which]);
            }
        });

        builder.show();
    }

}