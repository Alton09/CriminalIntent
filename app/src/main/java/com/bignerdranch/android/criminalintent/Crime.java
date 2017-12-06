package com.bignerdranch.android.criminalintent;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by john.qualls on 10/20/2017.
 */

public class Crime implements Comparable<Crime> {
    private UUID mId;
    private String mTitle;
    private String mDate;
    private boolean mSolved;
    private boolean mPoliceRequired;

    public Crime(UUID uuid) {
        mId = uuid;
        mDate = DateFormat.getDateInstance(DateFormat.FULL).format(new Date());
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

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public boolean isPoliceRequired() {
        return mPoliceRequired;
    }

    public void setPoliceRequired(boolean policeRequired) {
        mPoliceRequired = policeRequired;
    }

    @Override
    public int compareTo(@NonNull Crime o) {
        return mId.compareTo(o.getId());
    }
}
