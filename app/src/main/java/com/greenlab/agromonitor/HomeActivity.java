package com.greenlab.agromonitor;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.greenlab.agromonitor.fragments.HomeFragment;
import com.greenlab.agromonitor.fragments.ReportFragment;
import com.greenlab.agromonitor.fragments.SpreadsheetFragment;
import com.greenlab.agromonitor.utils.Constants;

public class HomeActivity extends BaseActivity {

    private BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTitle(R.string.title_home);
                    pushFragment(HomeFragment.newInstance());
                    return true;
                case R.id.navigation_spreadsheet:
                    setTitle(R.string.title_spreadsheet);
                    pushFragment(SpreadsheetFragment.newInstance());
                    return true;
                case R.id.navigation_reports:
                    setTitle(R.string.title_reports);
                    pushFragment(ReportFragment.newInstance());
                    return true;

            }
            return false;
        }
    };

    /**
     * Method to push any fragment into given id.
     *
     * @param fragment An instance of Fragment to show into the given id.
     */
    protected void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.root_layout, fragment);
                ft.commit();
            }
        }
    }

    public void displayErrorMessage(){
        AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
        alertDialog.setTitle("Alerta");
        alertDialog.setMessage("Você ainda não tem um projeto em aberto");
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                    }
                });
        alertDialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle(R.string.title_home);
        pushFragment(HomeFragment.newInstance());
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_NEW_PROJECT) {
            if (resultCode == Constants.RESULT_NEW_PROJECT) {
                Boolean isSaved = data.getBooleanExtra("success", false);
                if (isSaved)
                    showSnackBar(getResources().getString(R.string.project_save_success));
                else
                    showSnackBar(getResources().getString(R.string.project_save_error));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
