package com.openxc.enabler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.crittercism.app.Crittercism;

import com.openxc.enabler.utils.AppUtils;

public class ErrorReporter {
    private static final String CRITTERCISM_APP_ID = "52ac713a8b2e330ff3000003";
    private static final int CRITTERCISM_INIT_VERSION_CODE = 14;
    private static final int CRITTERCISM_DIALOG_DECISION_DEFAULT = 0;
    private static final int CRITTERCISM_DIALOG_DECISION_YES = 1;
    private static final int CRITTERCISM_DIALOG_DECISION_NO = 2;
    private static final String CRITTERCISM_DIALOG_DECISION_KEY = "CrittercismDialogDecisionKey";

    /**
     * Process Crittercism. If user previously selects YES - initialize. If NO - skip. If none of above
     * provide a dialog.
     */
    public static void processCrittercismInitDialog(Context context) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int crittercismPreferenceValue = preferences.getInt(CRITTERCISM_DIALOG_DECISION_KEY,
                CRITTERCISM_DIALOG_DECISION_DEFAULT);

        if (crittercismPreferenceValue == CRITTERCISM_DIALOG_DECISION_NO) {
            return;
        }
        if (crittercismPreferenceValue == CRITTERCISM_DIALOG_DECISION_YES) {
            Crittercism.initialize(context, CRITTERCISM_APP_ID);
            return;
        }
        int appVersionCode = AppUtils.getAppVersionCode(context);
        if (appVersionCode < CRITTERCISM_INIT_VERSION_CODE) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(CRITTERCISM_DIALOG_DECISION_KEY, CRITTERCISM_DIALOG_DECISION_YES);
                editor.commit();

                Crittercism.initialize(context, CRITTERCISM_APP_ID);
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(CRITTERCISM_DIALOG_DECISION_KEY, CRITTERCISM_DIALOG_DECISION_NO);
                editor.commit();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setTitle(context.getString(R.string.crittercism_dialog_title));
        dialog.setMessage(context.getString(R.string.crittercism_dialog_message));
        dialog.show();
    }
}
