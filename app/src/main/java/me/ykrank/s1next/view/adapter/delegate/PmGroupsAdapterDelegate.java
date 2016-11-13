package me.ykrank.s1next.view.adapter.delegate;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import javax.inject.Inject;

import me.ykrank.s1next.App;
import me.ykrank.s1next.R;
import me.ykrank.s1next.data.User;
import me.ykrank.s1next.data.api.model.PmGroup;
import me.ykrank.s1next.databinding.ItemPmGroupBinding;
import me.ykrank.s1next.viewmodel.PmGroupViewModel;
import me.ykrank.s1next.widget.EventBus;

public final class PmGroupsAdapterDelegate extends BaseAdapterDelegate<PmGroup, PmGroupsAdapterDelegate.BindingViewHolder> {

    @Inject
    EventBus mEventBus;

    @Inject
    User mUser;

    public PmGroupsAdapterDelegate(Context context) {
        super(context);
        App.getAppComponent(context).inject(this);
    }

    @NonNull
    @Override
    protected Class<PmGroup> getTClass() {
        return PmGroup.class;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        ItemPmGroupBinding binding = DataBindingUtil.inflate(mLayoutInflater,
                R.layout.item_pm_group, parent, false);
        binding.setPmGroupViewModel(new PmGroupViewModel());
        binding.setEventBus(mEventBus);
        binding.setUser(mUser);
        return new BindingViewHolder(binding);
    }

    @Override
    public void onBindViewHolderData(PmGroup pmGroup, int position, @NonNull BindingViewHolder holder) {
        ItemPmGroupBinding binding = holder.binding;
        binding.getPmGroupViewModel().pmGroup.set(pmGroup);
        binding.executePendingBindings();
    }

    /**
     * make textview selectable
     * @param holder
     */
    @Override
    protected void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ItemPmGroupBinding binding = ((BindingViewHolder)holder).binding;
        binding.authorName.setEnabled(false);
        binding.authorName.setEnabled(true);
        binding.tvSummary.setEnabled(false);
        binding.tvSummary.setEnabled(true);
    }

    static final class BindingViewHolder extends RecyclerView.ViewHolder {

        private final ItemPmGroupBinding binding;

        public BindingViewHolder(ItemPmGroupBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}