package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String CHEATER = "cheater";
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPreviousButton;
    private TextView mQuestionTextView;
    private double correct = 0;
    private double answered = 0;
    private Button mCheatButton;
    private static final int REQUEST_CODE_CHEAT = 0;

    private Question[] mQuestionBank = new Question[]
            {
                    new Question(R.string.question_australia, true),
                    new Question(R.string.question_oceans, true),
                    new Question(R.string.question_mideast, false),
                    new Question(R.string.question_africa, false),
                    new Question(R.string.question_americas, true),
                    new Question(R.string.question_asia, true),

            };

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(CHEATER, mIsCheater);
    }

    private int mCurrentIndex = 0;
    private boolean mIsCheater;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(CHEATER, false);
        }


        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDidCheat();
                //start CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();

            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                setAsAnswered();
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                setAsAnswered();
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
            }
        });

        mPreviousButton = (Button) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();

            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();

            }
        });

        updateQuestion();

    }

    private boolean checkIfAnswered()
    {
        return mQuestionBank[mCurrentIndex].isAnswered();
    }

    private boolean checkIfCheated() { return mQuestionBank[mCurrentIndex].isCheated();}

    private void setAsAnswered()
    {
        mQuestionBank[mCurrentIndex].setAnswered();
    }

    private void setDidCheat() { mQuestionBank[mCurrentIndex].setCheated();}
    private void updateQuestion()
    {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        if (checkIfAnswered())
        {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        }
        else
        {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }

    }

    private void checkAnswer(boolean userPressedTrue)
    {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        if(checkIfCheated())
            mIsCheater = true;

        int messageResId = 0;
        answered += 1;

        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        }
        
        else{

            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                correct += 1;
            }
            else
            {
                messageResId = R.string.incorrect_toast;
            }

        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();


        if (answered == 6.0) {
            double percent = (correct / answered) * 100;
            CharSequence text = percent + "%";
            Toast message = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            message.setGravity(Gravity.TOP, 0, 0);
            message.show();
        }


    }
}
