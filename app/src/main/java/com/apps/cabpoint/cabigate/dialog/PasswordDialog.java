package com.apps.cabpoint.cabigate.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.apps.cabpoint.cabigate.utils.utils;

/**
 * Created by Abdul Ghani on 6/10/2017.
 */

public class PasswordDialog extends Dialog {

    private EditText oldPassword, newPassword, confirmPassword;
    private Context context;
    private DialogListner dialogListner;

    public PasswordDialog(@NonNull Context context, DialogListner dialogListner) {
        super(context);
        this.context = context;
        this.dialogListner = dialogListner;
        setContentView(R.layout.set_password_dialog);
        initViews();
    }

    private void initViews() {
        oldPassword = (EditText) findViewById(R.id.old_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        Button changePassword = (Button) findViewById(R.id.change_password);
        oldPassword.setTypeface(Typeface.createFromAsset(context.getAssets(),"clanpro_news.ttf"));
        newPassword.setTypeface(Typeface.createFromAsset(context.getAssets(),"clanpro_news.ttf"));
        confirmPassword.setTypeface(Typeface.createFromAsset(context.getAssets(),"clanpro_news.ttf"));
        changePassword.setTypeface(Typeface.createFromAsset(context.getAssets(),"clanpro_news.ttf"));
        TextView backBtn = (TextView) findViewById(R.id.back_text);
        backBtn.setTypeface(Typeface.createFromAsset(context.getAssets(),"clanpro_news.ttf"));
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordFun();
            }
        });
    }

    private void changePasswordFun() {
        if (utils.validInput(oldPassword) && utils.validInput(newPassword) && utils.validInput(confirmPassword)) {
            if (oldPassword.getText().toString().equals(StoragePreference.getPASSWORD(context))) {
                if (newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                    dialogListner.getDialogValue(confirmPassword.getText().toString());
                    dismiss();
                } else {
                    utils.showInfoDialog(context, "New and Confirm Password should be same", false);
                }
            } else {
                utils.showInfoDialog(context, "Old password is Incorrect", false);
            }
        }
    }
}
