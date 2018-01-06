package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;

import java.util.Date;

public class DatePickerActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new Date());
        return datePickerFragment;
    }
}
