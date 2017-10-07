package com.mgilangjanuar.dev.goscele.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgilangjanuar.dev.goscele.R;

import butterknife.ButterKnife;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public abstract class BaseFragment extends Fragment {

    @LayoutRes
    public abstract int findLayout();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(findLayout(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (findToolbar() != null) {
            if (findTitle() != null) {
                findToolbar().setTitle(findTitle());
            }
            ((BaseActivity) getActivity()).setSupportActionBar(findToolbar());
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

    public void showSnackbar(@StringRes int text) {
        showSnackbar(getString(text));
    }

    public void showSnackbar(String message) {
        ((BaseActivity) getActivity()).showSnackbar(message);
    }
}
