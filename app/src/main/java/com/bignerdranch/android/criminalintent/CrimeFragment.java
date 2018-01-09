package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID = "crime_ID";
    private static final String ARG_POSITION = "position";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_DATE_FRAGMENT = 100;
    private Crime mCrime;
    private int mPosition;
    private List<Crime> mCrimes;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mDateFragmentButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private Intent mReturnIntent;
    private Button mFirstCrimeButton;
    private Button mLastCrimeButton;

    public static CrimeFragment newInstance(UUID uuid, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CRIME_ID, uuid);
        bundle.putSerializable(ARG_POSITION, position);
        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(bundle);
        return crimeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments != null) {
            UUID crimeID = (UUID) arguments.getSerializable(ARG_CRIME_ID);
            mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeID);
            mPosition = arguments.getInt(ARG_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mFirstCrimeButton = v.findViewById(R.id.first_crime_button);
        mFirstCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CrimeView)getActivity()).setCurrentItem(0);
            }
        });
        mLastCrimeButton = v.findViewById(R.id.last_crime_button);
        mLastCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CrimeView)getActivity()).setCurrentItem(mCrimes.size() - 1);
            }
        });
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
                setReturnIntent();
            }

            @Override public void afterTextChanged(Editable s) {

            }
        });
        mDateButton = v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment dialog  = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fragmentManager, DIALOG_DATE);
            }
        });
        mDateFragmentButton = v.findViewById(R.id.crime_date_fragment);
        updateDateFragment();
        mDateFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DatePickerActivity.class);
                startActivityForResult(intent, REQUEST_DATE_FRAGMENT);
            }
        });

        mTimeButton = v.findViewById(R.id.crime_time);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                TimePickerFragment dialog = new TimePickerFragment();
                dialog.show(fragmentManager, null);
            }
        });
        mSolvedCheckBox = v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mCrime.setSolved(isChecked);
                setReturnIntent();
            }
        });
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        mCrimes = crimeLab.getCrimes();
        toggleCrimeNavigationButtons(mPosition);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            if(requestCode == REQUEST_DATE) {
                updateDate();
            }
            else if(requestCode == REQUEST_DATE_FRAGMENT) {
                updateDateFragment();
            }
        }
    }

    private void updateDate() {
        mDateButton.setText(getString(R.string.date_picker_title) + mCrime.getDateString());
    }

    private void updateDateFragment() {
        mDateFragmentButton.setText(getString(R.string.date_picker_title_fragment) + mCrime.getDateString());
    }

    private void toggleCrimeNavigationButtons(int position) {
            if (position == 0) {
                toggleButton(mFirstCrimeButton);
            } else if(!mFirstCrimeButton.isEnabled()) {
                toggleButton(mFirstCrimeButton);
            }
            if (position == (mCrimes.size() - 1)) {
                toggleButton(mLastCrimeButton);
            } else if(!mLastCrimeButton.isEnabled()) {
                toggleButton(mLastCrimeButton);
            }
    }

    private void toggleButton(Button button) {
        if(button.isEnabled()) {
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }
    }

    private void setReturnIntent() {
        if(mReturnIntent == null) {
            mReturnIntent = CrimeListFragment.newReturnIntent(mCrime.getId());
            getActivity().setResult(Activity.RESULT_OK, mReturnIntent);
        }
    }
}
