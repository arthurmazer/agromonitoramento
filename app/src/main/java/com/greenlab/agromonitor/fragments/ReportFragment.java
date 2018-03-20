package com.greenlab.agromonitor.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.greenlab.agromonitor.HomeActivity;
import com.greenlab.agromonitor.LineChartActivity;
import com.greenlab.agromonitor.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {

    private HomeActivity mActivity;
    public int chatVisible = 1;
    private Button btnProximo;

    public static Fragment newInstance(){
        Fragment reportFragment = new ReportFragment();
        return reportFragment;
    }

    public ReportFragment() {
        // Required empty public constructor
        chatVisible = 1;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_report, container, false);
        mActivity = (HomeActivity)getActivity();


        btnProximo = mView.findViewById(R.id.btn_proximo);

        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mActivity, LineChartActivity.class);
                mActivity.startActivity(it);

                mActivity.overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_right_out_activity);
            }
        });

        return mView;
    }

}
