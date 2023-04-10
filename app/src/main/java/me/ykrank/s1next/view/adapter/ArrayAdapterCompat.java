package me.ykrank.s1next.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ThemedSpinnerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * See http://stackoverflow.com/a/32066279
 */
public final class ArrayAdapterCompat<T> extends ArrayAdapter<T> implements ThemedSpinnerAdapter {

    private final ThemedSpinnerAdapter.Helper mDropDownHelper;
    @LayoutRes
    private int mDropDownResource;

    public ArrayAdapterCompat(Context context, @LayoutRes int resource, List<T> objects) {
        super(context, resource, objects);
        mDropDownResource = resource;

        mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            // inflate the drop down using the helper's LayoutInflater
            LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
            textView = (TextView) inflater.inflate(mDropDownResource, parent, false);
        } else {
            textView = (TextView) convertView;
        }

        T item = getItem(position);
        if (item instanceof CharSequence) {
            textView.setText((CharSequence) item);
        } else {
            textView.setText(item.toString());
        }

        return textView;
    }

    @Override
    public void setDropDownViewResource(@LayoutRes int resource) {
        mDropDownResource = resource;
    }

    @Override
    @Nullable
    public Resources.Theme getDropDownViewTheme() {
        return mDropDownHelper.getDropDownViewTheme();
    }

    @Override
    public void setDropDownViewTheme(Resources.Theme theme) {
        mDropDownHelper.setDropDownViewTheme(theme);
    }
}
