package com.example.uipj.ui.activities.profile;

import android.app.Dialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.uipj.R;
import com.example.uipj.data.dao.UserDAO;
import com.example.uipj.databinding.ActivitySettingsBinding;

import com.example.uipj.databinding.DialogChangeEmailBinding;
import com.example.uipj.databinding.DialogChangeUsernameBinding;
import com.example.uipj.preferen.UserSharePreferences;
import com.example.uipj.ui.activities.auth.signin.SignInActivity;
import com.example.uipj.ui.activities.profile.change.ChangeEmailActivity;
import com.example.uipj.ui.activities.profile.change.ChangePasswordActivity;
import com.example.uipj.ui.activities.profile.change.ChangeUsernameActivity;
import com.example.uipj.ui.activities.set.ViewSetActivity;
import com.example.uipj.utils.PasswordHasher;
import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;
    private UserSharePreferences userSharePreferences;
    private AlertDialog detailDialog;
    UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        setContentView(view);

        userSharePreferences = new UserSharePreferences(SettingsActivity.this);
        binding.usernameTv.setText(userSharePreferences.getUserName());
        binding.emailTv.setText(userSharePreferences.getEmail());

        onClickItemSetting();

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void onClickItemSetting() {
        binding.usernameCl.setOnClickListener(view -> openDialogChangeUsername());
        binding.emailCl.setOnClickListener(view -> openDialogChangeEmail());
        binding.passwordCl.setOnClickListener(view -> {
            startActivity(new Intent(SettingsActivity.this, ChangePasswordActivity.class));
        });

        binding.logOutBtn.setOnClickListener(v -> {
            PopupDialog.getInstance(SettingsActivity.this)
                    .standardDialogBuilder()
                    .createStandardDialog()
                    .setHeading("Logout")
                    .setDescription("Are you sure you want to logout?" +
                            " This action cannot be undone")
                    .setIcon(R.drawable.baseline_logout_24)
                    .build(new StandardDialogActionListener() {
                        @Override
                        public void onPositiveButtonClicked(Dialog dialog) {
                            userSharePreferences = new UserSharePreferences(SettingsActivity.this);
                            userSharePreferences.clear();
                            Intent intent = new Intent(SettingsActivity.this, SignInActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onNegativeButtonClicked(Dialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        });
    }

    private void openDialogChangeEmail() {
        userDAO = new UserDAO(SettingsActivity.this);
        DialogChangeEmailBinding changeEmailBinding = DialogChangeEmailBinding.inflate(LayoutInflater.from(SettingsActivity.this));
        View view = changeEmailBinding.getRoot();

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setView(view);
        detailDialog = builder.create();
        detailDialog.setCanceledOnTouchOutside(false);
        detailDialog.show();
        detailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        changeEmailBinding.cancelChangeEmailBtn.setOnClickListener(view1 -> detailDialog.dismiss());

        changeEmailBinding.submitChangeEmailBtn.setOnClickListener(v -> {
//            startActivity(new Intent(SettingsActivity.this, ChangeEmailActivity.class));
            String password = changeEmailBinding.passwordEt.getText().toString().trim();
            userSharePreferences = new UserSharePreferences(SettingsActivity.this);
            String id = userSharePreferences.getId();
            if (password.isEmpty()) {
                changeEmailBinding.textIL.setHelperText("Please enter your password");
            } else if (!Objects.equals(PasswordHasher.hashPassword(password), userDAO.getPasswordUser(id))) {
                Toast.makeText(this, "Pass: " + userDAO.getPasswordUser(id), Toast.LENGTH_SHORT).show();
                changeEmailBinding.textIL.setHelperText("Password is incorrect");
            } else {
                changeEmailBinding.textIL.setHelperText("");
                startActivity(new Intent(SettingsActivity.this, ChangeEmailActivity.class));
                detailDialog.dismiss();
            }
        });
    }

    private void openDialogChangeUsername() {
        userDAO = new UserDAO(SettingsActivity.this);
        DialogChangeUsernameBinding changeUsernameBinding = DialogChangeUsernameBinding.inflate(LayoutInflater.from(SettingsActivity.this));
        View view = changeUsernameBinding.getRoot();

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setView(view);
        detailDialog = builder.create();
        detailDialog.setCanceledOnTouchOutside(false);
        detailDialog.show();
        detailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        changeUsernameBinding.cancelChangeName.setOnClickListener(view1 -> detailDialog.dismiss());

        changeUsernameBinding.submitChangeName.setOnClickListener(v -> {
//            startActivity(new Intent(SettingsActivity.this, ChangeUsernameActivity.class));
            String password = changeUsernameBinding.passwordEt.getText().toString().trim();
            userSharePreferences = new UserSharePreferences(SettingsActivity.this);
            String id = userSharePreferences.getId();
            if (password.isEmpty()) {
                changeUsernameBinding.textIL.setHelperText("Please enter your password");
            } else if (!Objects.equals(PasswordHasher.hashPassword(password), userDAO.getPasswordUser(id))) {
                changeUsernameBinding.textIL.setHelperText("Password is incorrect");
            } else {
                changeUsernameBinding.textIL.setHelperText("");
                startActivity(new Intent(SettingsActivity.this, ChangeUsernameActivity.class));
                detailDialog.dismiss();
            }
        });
    }
}