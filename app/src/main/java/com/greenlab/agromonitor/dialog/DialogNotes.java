package com.greenlab.agromonitor.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Window;

import com.greenlab.agromonitor.R;

/**
 * Created by mazer on 4/27/2018.
 */

public class DialogNotes extends Dialog {


    private Context ctx;
    private Activity activity;

    public DialogNotes(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_notes);

    }
}
