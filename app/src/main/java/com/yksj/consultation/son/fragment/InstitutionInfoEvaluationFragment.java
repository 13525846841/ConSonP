package com.yksj.consultation.son.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yksj.consultation.son.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstitutionInfoEvaluationFragment extends Fragment {


    public InstitutionInfoEvaluationFragment() {
        // Required empty public constructor
    }

    public static InstitutionInfoEvaluationFragment newInstance(int Unit_Code) {

        Bundle args = new Bundle();

        InstitutionInfoEvaluationFragment fragment = new InstitutionInfoEvaluationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insitution_info_evaluation, container, false);
    }

}
