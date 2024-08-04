package com.choiminseon.nbggapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.choiminseon.nbggapp.api.NetworkClient;
import com.choiminseon.nbggapp.api.TranslateApi;
import com.choiminseon.nbggapp.model.TranslateReq;
import com.choiminseon.nbggapp.model.TranslateRes;

import android.Manifest;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class DialogueTranslationActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION_CODE = 1;
    TextView txtOtherTalk;
    ImageView imgOtherMic;
    LinearLayout layoutChoiceLng;
    TextView txtOtherLngKo;
    TextView txtOtherLng;
    TextView txtMyTalk;
    ImageView imgMyMic;
    LottieAnimationView otherMicAnimation;
    LottieAnimationView myMicAnimation;
    SpeechRecognizer speechRecognizer;
    Intent speechRecognizerIntent;
    String language = "영어";
    boolean isMyMicActive = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogue_translation);

        txtOtherTalk = findViewById(R.id.txtOtherTalk);
        imgOtherMic = findViewById(R.id.imgOtherMic);
        layoutChoiceLng = findViewById(R.id.layoutChoiceLng);
        txtOtherLngKo = findViewById(R.id.txtOtherLngKo);
        txtOtherLng = findViewById(R.id.txtOtherLng);
        txtMyTalk = findViewById(R.id.txtMyTalk);
        imgMyMic = findViewById(R.id.imgMyMic);
        otherMicAnimation = findViewById(R.id.otherMicAnimation);
        myMicAnimation = findViewById(R.id.myMicAnimation);

        // 기본 액션바 설정
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setCustomView(R.layout.translate_action_bar);
            actionBar.setElevation(0);

            // Back button 클릭 리스너 설정
            View customView = actionBar.getCustomView();

            ImageButton backButton = customView.findViewById(R.id.backButton);
            backButton.setOnClickListener(v -> finish());
        }

        // 권한 체크
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION_CODE);
        }

        // SpeechRecognizer 초기화
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);


        RecognitionListener recognitionListener = new RecognitionListener() {

            // 음성 인식이 시작될 준비가 되었을 때 호출
            // 사용자가 말을 시작할 수 있는 준비가 되었음을 나타냄. 이 시점에 마이크가 열리고 음성을 받을 준비
            @Override
            public void onReadyForSpeech(Bundle params) {}

            // 사용자가 말을 시작했을 때 호출
            // 사용자가 말을 시작했음을 감지했을 때 호출. 이 시점에 음성 입력이 시작
            @Override
            public void onBeginningOfSpeech() {}

            // 입력 음성의 소리 크기(RMS dB)가 변경될 때 호출
            // 음성 입력 동안 소리 크기가 변경될 때 호출. 이 메서드는 일반적으로 소리 크기 표시(예: 음성 인식 중 마이크 아이콘 애니메이션)를 업데이트하는 데 사용
            @Override
            public void onRmsChanged(float rmsdB) {
            }

            // 더 많은 음성 입력을 받을 때 호출
            // 음성 인식기에서 추가적으로 음성 데이터를 받을 때 호출. 이 데이터는 음성 인식을 향상시키는 데 사용
            @Override
            public void onBufferReceived(byte[] buffer) {}

            // 사용자가 말을 멈췄을 때 호출
            // 사용자가 말을 멈추었거나 음성 입력이 더 이상 감지되지 않을 때 호출. 이 시점에 음성 인식기가 입력을 처리하고 결과를 도출하기 시작
            @Override
            public void onEndOfSpeech() {
                stopAllAnimation();
            }

            // 음성 인식 중 오류가 발생했을 때 호출.
            // 음성 인식 과정에서 오류가 발생했을 때 호출. 오류 코드는 SpeechRecognizer.ERROR_* 상수로 정의된 값을 가짐. 오류에 따라 적절한 처리를 할 수 있음.
            @Override
            public void onError(int error) {
                stopAllAnimation();
                Toast.makeText(DialogueTranslationActivity.this, "잠시 후 시도해주세요.", Toast.LENGTH_SHORT).show();
            }


            // 음성 인식 결과가 준비되었을 때 호출.
            // 음성 인식이 성공적으로 완료되었을 때 호출. results 번들은 인식된 텍스트 목록을 포함하며,
            // 여기서 SpeechRecognizer.RESULTS_RECOGNITION 키로 결과를 가져올 수 있음.
            @Override
            public void onResults(Bundle results) {
                stopAllAnimation();
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String text = matches.get(0);

                    if (isMyMicActive) {
                        txtMyTalk.setText(text);
                        translateText(text, true);
                    } else {
                        txtOtherTalk.setText(text);
                        translateText(text, false);
                    }
                }
            }

            // 부분적인 음성 인식 결과가 준비되었을 때 호출
            // 최종 결과가 나오기 전에 부분적인 인식 결과가 준비되었을 때 호출. 이 메서드는 중간 결과를 표시하는 데 유용.
            @Override
            public void onPartialResults(Bundle partialResults) {}

            // 음성 인식기에서 이벤트가 발생했을 때 호출.
            // 추가적인 사용자 정의 이벤트가 발생했을 때 호출. 이 메서드는 거의 사용되지 않으며, 특정 이벤트가 필요한 경우에만 사용.
            @Override
            public void onEvent(int eventType, Bundle params) {}
        };

        speechRecognizer.setRecognitionListener(recognitionListener);



        // 언어 선택
        layoutChoiceLng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguageChoiceDialog();
            }
        });


        // 상대방 말 STT
        imgOtherMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMyMicActive = false;
                startOtherMicAnimation();
                speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, getLanguageLocale(language).toString());
                speechRecognizer.startListening(speechRecognizerIntent);

                Locale locale = getLanguageLocale(language);
                String localeInfo = locale.getLanguage() + "-" + locale.getCountry();
                Log.i("LANGUAGE CODE", localeInfo);

            }
        });


        // 나의 말 STT
        imgMyMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMyMicActive = true;
                startMyMicAnimation();
                speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREAN);
                speechRecognizer.startListening(speechRecognizerIntent);
            }
        });


    }


    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION_CODE) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // 권한 거부 시의 처리
                Toast.makeText(this, "권한을 허용해야 대화번역 서비스 이용이 가능합니다. 설정 -> 애플리케이션 -> 권한 -> 권한 허용", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startMyMicAnimation() {
        myMicAnimation.setVisibility(View.VISIBLE);
        otherMicAnimation.setVisibility(View.GONE);

    }

    private void startOtherMicAnimation() {
        otherMicAnimation.setVisibility(View.VISIBLE);
        myMicAnimation.setVisibility(View.GONE);
    }

    private void stopAllAnimation() {
        myMicAnimation.setVisibility(View.GONE);
        otherMicAnimation.setVisibility(View.GONE);
    }

    private void translateText(String text, boolean isMyText) {
        Retrofit retrofit = NetworkClient.getRetrofitClient(DialogueTranslationActivity.this);
        TranslateApi api = retrofit.create(TranslateApi.class);
        TranslateReq translateReq = new TranslateReq(language, text);
        Call<TranslateRes> call;
        if (isMyText) {
            call = api.translateAnswer(translateReq);
        } else {
            call = api.translateForeignAnswer(translateReq);
        }
        call.enqueue(new Callback<TranslateRes>() {
            @Override
            public void onResponse(Call<TranslateRes> call, Response<TranslateRes> response) {
                if (response.isSuccessful()) {
                    TranslateRes translateRes = response.body();

                    if (isMyText) {
                        txtOtherTalk.setText(translateRes.transAnswer);
                    } else {
                        txtMyTalk.setText(translateRes.transAnswer);
                    }
                }
            }

            @Override
            public void onFailure(Call<TranslateRes> call, Throwable throwable) {
                Toast.makeText(DialogueTranslationActivity.this, "네트워크 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 언어 선택 다이얼로그
    private void showLanguageChoiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("언어 선택");

        String[] languages = {"영어", "일본어", "중국어(간체)", "스페인어", "프랑스어", "독일어", "러시아어", "힌디어", "우르두어", "포르투갈어"};
        builder.setItems(languages, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        language = "영어";
                        txtOtherLngKo.setText(language);
                        txtOtherLng.setText("English");
                        break;
                    case 1:
                        language = "일본어";
                        txtOtherLngKo.setText(language);
                        txtOtherLng.setText("日本語");
                        break;
                    case 2:
                        language = "중국어(간체)";
                        txtOtherLngKo.setText(language);
                        txtOtherLng.setText("中文(简体)");
                        break;
                    case 3:
                        language = "스페인어";
                        txtOtherLngKo.setText(language);
                        txtOtherLng.setText("Español");
                        break;
                    case 4:
                        language = "프랑스어";
                        txtOtherLngKo.setText(language);
                        txtOtherLng.setText("Français");
                        break;
                    case 5:
                        language = "독일어";
                        txtOtherLngKo.setText(language);
                        txtOtherLng.setText("Deutsch");
                        break;
                    case 6:
                        language = "러시아어";
                        txtOtherLngKo.setText(language);
                        txtOtherLng.setText("Русский");
                        break;
                    case 7:
                        language = "힌디어";
                        txtOtherLngKo.setText(language);
                        txtOtherLng.setText("हिंदी");
                        break;
                    case 8:
                        language = "우르두어";
                        txtOtherLngKo.setText(language);
                        txtOtherLng.setText("اردو");
                        break;
                    case 9:
                        language = "포르투갈어";
                        txtOtherLngKo.setText(language);
                        txtOtherLng.setText("Português");
                        break;

                }
            }
        });

        builder.show();
    }

    // 언어 코드를 반환하는 메서드
    private Locale getLanguageLocale(String language) {
        switch (language) {
            case "영어":
                return Locale.ENGLISH;
            case "일본어":
                return Locale.JAPAN;
            case "중국어(간체)":
                return Locale.SIMPLIFIED_CHINESE;
            case "스페인어":
                return new Locale("es", "ES");
            case "프랑스어":
                return Locale.FRANCE;
            case "독일어":
                return Locale.GERMANY;
            case "러시아어":
                return new Locale("ru", "RU");
            case "힌디어":
                return new Locale("hi", "IN");
            case "우르두어":
                return new Locale("ur", "PK");
            case "포르투갈어":
                return new Locale("pt", "PT");
            default:
                return Locale.ENGLISH; // 기본 언어를 영어로 설정
        }
    }
    
}