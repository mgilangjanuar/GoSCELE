package com.mgilangjanuar.dev.goscele.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public abstract class BaseActivity extends AppCompatActivity {

    @LayoutRes
    public abstract int findLayout();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(findLayout());
        ButterKnife.bind(this);

        if (findToolbar() != null) {
            if (findTitle() != null) {
                findToolbar().setTitle(findTitle());
            }
            setSupportActionBar(findToolbar());
        }

        initialize(savedInstanceState);
    }

    public void initialize(Bundle savedInstanceState) {
    }

    public Toolbar findToolbar() {
        return null;
    }

    public String findTitle() {
        return null;
    }

    @MenuRes
    public int findOptionsMenu() {
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (findOptionsMenu() != 0) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(findOptionsMenu(), menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void redirect(Intent intent) {
        startActivity(intent);
    }

    public void redirect(Class<? extends BaseActivity> activity, boolean force) {
        Intent intent = new Intent(this, activity);
        if (force) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        redirect(intent);
    }

    public void redirect(Class<? extends BaseActivity> activity) {
        redirect(activity, false);
    }

    public void showSnackbar(@StringRes int text) {
        showSnackbar(getString(text));
    }

    public void showSnackbar(String message) {
        View view = findViewById(android.R.id.content);
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View sbView = snack.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snack.show();
    }

    public void showToast(@StringRes int text) {
        showToast(getString(text));
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public ProgressDialog showProgress(@StringRes int title) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(title));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
