package me.ykrank.s1next.view.adapter.delegate

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.github.ykrank.androidtools.ui.adapter.simple.SimpleRecycleViewHolder
import com.github.ykrank.androidtools.widget.RxBus
import me.ykrank.s1next.App
import me.ykrank.s1next.R
import me.ykrank.s1next.data.api.model.Favourite
import me.ykrank.s1next.databinding.ItemFavouriteBinding
import me.ykrank.s1next.viewmodel.FavouriteViewModel
import javax.inject.Inject

class FavouriteAdapterDelegate(context: Context) : BaseAdapterDelegate<Favourite, SimpleRecycleViewHolder<ItemFavouriteBinding>>(context, Favourite::class.java) {

    @Inject
    lateinit var mRxBus: RxBus

    init {
        App.appComponent.inject(this)
    }

    public override fun onCreateViewHolder(parent: ViewGroup): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemFavouriteBinding>(mLayoutInflater,
                R.layout.item_favourite, parent, false)
        binding.model = FavouriteViewModel()
        binding.rxBus = mRxBus
        return SimpleRecycleViewHolder<ItemFavouriteBinding>(binding)
    }

    override fun onBindViewHolderData(t: Favourite, position: Int, holder: SimpleRecycleViewHolder<ItemFavouriteBinding>, payloads: List<Any>) {
        val binding = holder.binding
        binding.model?.favourite?.set(t)
    }

}
