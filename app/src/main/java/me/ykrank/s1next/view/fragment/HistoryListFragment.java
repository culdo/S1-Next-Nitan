package me.ykrank.s1next.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ykrank.androidtools.util.L;
import com.github.ykrank.androidtools.util.RxJavaUtil;

import javax.inject.Inject;

import me.ykrank.s1next.App;
import me.ykrank.s1next.data.db.HistoryDbWrapper;
import me.ykrank.s1next.databinding.FragmentBaseBinding;
import me.ykrank.s1next.view.adapter.HistoryCursorRecyclerViewAdapter;

/**
 * Fragment show post view history list
 */
public final class HistoryListFragment extends BaseFragment {
    public static final String TAG = HistoryListFragment.class.getName();

    private HistoryCursorRecyclerViewAdapter mRecyclerAdapter;

    @Inject
    HistoryDbWrapper historyDbWrapper;

    private FragmentBaseBinding binding;

    public static HistoryListFragment newInstance() {
        HistoryListFragment fragment = new HistoryListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBaseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        App.Companion.getAppComponent().inject(this);
        super.onViewCreated(view, savedInstanceState);
        leavePageMsg("HistoryListFragment");

        Activity activity = getActivity();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerAdapter = new HistoryCursorRecyclerViewAdapter(activity);
        binding.recyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    public void onPause() {
        mRecyclerAdapter.changeCursor(null);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        historyDbWrapper.getHistoryListCursor()
                .compose(RxJavaUtil.iOSingleTransformer())
                .subscribe(mRecyclerAdapter::changeCursor,
                        throwable -> L.e("S1next", throwable));
    }
}
