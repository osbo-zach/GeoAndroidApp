package com.bignerdranch.android.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.w3c.dom.Text;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";
    private static final String TAG = "CheatActivity";
    private static final String KEY_CHEAT_CHECKER = "cheat_checker";
    private boolean mIsAnswerShown;
    private static final String KEY_ANSWER = "answer_to_question";

    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAnswer();

                setAnswerShownResult(true);
                mIsAnswerShown = true;
            }
        });
        if (savedInstanceState!=null){
            mIsAnswerShown = savedInstanceState.getBoolean(KEY_CHEAT_CHECKER, false);
            mAnswerIsTrue = savedInstanceState.getBoolean(KEY_ANSWER, false);
            if(mIsAnswerShown==true){
                setAnswerShownResult(true);
                displayAnswer();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "in onSavedInstanceState");
        savedInstanceState.putBoolean(KEY_CHEAT_CHECKER, mIsAnswerShown);
        savedInstanceState.putBoolean(KEY_ANSWER, mAnswerIsTrue);
    }

    public void displayAnswer(){
        if(mAnswerIsTrue == true){
            mAnswerTextView.setText(R.string.true_button);
        }else{
            mAnswerTextView.setText(R.string.false_button);
        }
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

}
