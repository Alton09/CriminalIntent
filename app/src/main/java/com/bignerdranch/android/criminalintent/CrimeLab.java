package com.bignerdranch.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

    public static CrimeLab getInstance(Context context) {
        if(sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
        UUID[] uuidArray = new UUID[100];
        for(int i = 0; i < 100; i++) {
            uuidArray[i] = UUID.randomUUID();
        }
        Arrays.sort(uuidArray);
        for(int i = 0; i < 100; i++) {
            boolean isEven = i % 2 == 0;
            Crime crime = new Crime(uuidArray[i]);
            crime.setTitle("Crime #" + i);
            crime.setSolved(isEven);
            crime.setPoliceRequired(!isEven);
            mCrimes.add(crime);
        }
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        Crime selectedCrime = new Crime(id);
        int index = Collections.binarySearch(mCrimes, selectedCrime);
        return mCrimes.get(index);
    }
}
