package me.ykrank.s1next.view.adapter;

import android.app.Activity;
import androidx.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import me.ykrank.s1next.App;
import me.ykrank.s1next.binding.TextViewBindingAdapter;
import me.ykrank.s1next.data.api.model.Forum;
import me.ykrank.s1next.data.pref.ThemeManager;

public final class SubForumArrayAdapter extends ArrayAdapter<Forum> {
    @Inject
    ThemeManager themeManager;

    private final LayoutInflater mLayoutInflater;
    @LayoutRes
    private final int mResource;

    private final int mGentleAccentColor;

    public SubForumArrayAdapter(Activity activity, @LayoutRes int resource, List<Forum> objects) {
        super(activity, resource, objects);
        App.Companion.getAppComponent().inject(this);

        mLayoutInflater = activity.getLayoutInflater();
        this.mResource = resource;
        mGentleAccentColor = themeManager.getGentleAccentColor();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mResource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TextViewBindingAdapter.setForum(viewHolder.textView, getItem(position), mGentleAccentColor);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(getItem(position).getId());
    }

    private static final class ViewHolder {

        private TextView textView;
    }
}
