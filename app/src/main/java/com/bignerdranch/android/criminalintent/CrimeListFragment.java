package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

public class CrimeListFragment extends Fragment {
    private static final String EXTRA_CRIME_ID = "crime_id";
    private static final int START_CRIME_DETAIL = 100;
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
    }

    private void updateUI(UUID crimeId) {
        Crime crime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
        int index = mAdapter.getItemPosition(crime);
        mAdapter.notifyItemChanged(index);
    }

    /**
     * Invoked when another Fragment/Activity returns to this fragment
     */
    public static Intent newReturnIntent(UUID crimeId) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(EXTRA_CRIME_ID, crimeId);
        return returnIntent;
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;
        private ImageView mSolvedImageView;

        CrimeHolder(LayoutInflater inflater, ViewGroup parent, int layout) {
            super(inflater.inflate(layout, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved_image);
        }

        @Override
        public void onClick(View view) {
            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
            startActivityForResult(intent, START_CRIME_DETAIL);
        }

        void bind(Crime crime) {
            mCrime = crime;

            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate());
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == START_CRIME_DETAIL) {
            if(data != null) {
                UUID crimeId = (UUID) data.getSerializableExtra(EXTRA_CRIME_ID);
                if(crimeId != null) {
                    updateUI(crimeId);
                }
            } else {
                updateUI();
            }
        }
    }

    private class StandardCrimeHolder extends CrimeHolder {

        StandardCrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater, parent, R.layout.list_item_crime);
        }
    }

    private class SevereCrimeHolder extends CrimeHolder {

        SevereCrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater, parent, R.layout.list_item_severe_crime);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            CrimeHolder crimeHolder;
            if(R.layout.list_item_severe_crime == viewType) {
                crimeHolder = new SevereCrimeHolder(layoutInflater, parent);
            } else {
                crimeHolder = new StandardCrimeHolder(layoutInflater, parent);
            }
            return crimeHolder;
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemViewType(int position) {
            Crime crime = mCrimes.get(position);
            int layoutID = R.layout.list_item_crime;
            if(crime.isPoliceRequired()) {
                layoutID = R.layout.list_item_severe_crime;
            }
            return layoutID;
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public int getItemPosition(Crime selectedCrime) {
            int result = -1;
            for(Crime crime : mCrimes) {
                if (crime.equals(selectedCrime)) {
                    result = mCrimes.indexOf(crime);
                    break;
                }
            }
            return result;
        }
    }
}
