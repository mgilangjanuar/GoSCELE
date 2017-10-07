package com.mgilangjanuar.dev.goscele.base;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public abstract class BaseRecyclerViewAdapter<T extends BaseViewHolder> extends RecyclerView.Adapter<T> {

    @Override
    public void onBindViewHolder(T holder, int position) {
        initialize(holder, position);
    }

    @Override
    public int getItemCount() {
        return findList().size();
    }

    protected View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(findLayout(), parent, false);
    }

    @LayoutRes
    public abstract int findLayout();

    public abstract List<?> findList();

    public abstract void initialize(T holder, int position);

}
