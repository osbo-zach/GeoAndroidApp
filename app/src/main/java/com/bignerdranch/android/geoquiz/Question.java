package com.bignerdranch.android.geoquiz;

public class Question {

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mAnswered = false;
    private boolean mCheated = false;

    public Question (int textResId, boolean answerTrue)
    {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    public void setCheated()
    {
        mCheated = true;
    }

    public boolean isCheated()
    {
        if (mCheated)
            return true;
        else
            return false;
    }
    public void setAnswered()
    {
        mAnswered = true;
    }
    public boolean isAnswered()
    {
        if (mAnswered)
        {
            return true;
        }
        else {
            return false;
        }
    }
    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}

