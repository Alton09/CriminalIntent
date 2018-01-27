package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Crime crime = (Crime) o;

        if (mSolved != crime.mSolved) return false;
        if (!mId.equals(crime.mId)) return false;
        if (!mTitle.equals(crime.mTitle)) return false;
        return mDate.equals(crime.mDate);
    }

    @Override
    public int hashCode() {
        int result = mId.hashCode();
        result = 31 * result + mTitle.hashCode();
        result = 31 * result + mDate.hashCode();
        result = 31 * result + (mSolved ? 1 : 0);
        return result;
    }
}
