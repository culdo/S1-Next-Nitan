package me.ykrank.s1next.view.adapter.delegate

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ykrank.androidautodispose.AndroidRxDispose
import com.github.ykrank.androidlifecycle.AndroidLifeCycle
import com.github.ykrank.androidlifecycle.event.ViewEvent
import com.github.ykrank.androidtools.ui.adapter.simple.BindViewHolderCallback
import com.github.ykrank.androidtools.ui.adapter.simple.SimpleRecycleViewAdapter
import com.github.ykrank.androidtools.ui.adapter.simple.SimpleRecycleViewHolder
import com.github.ykrank.androidtools.util.L
import com.github.ykrank.androidtools.widget.RxBus
import me.ykrank.s1next.App
import me.ykrank.s1next.R
import me.ykrank.s1next.data.User
import me.ykrank.s1next.data.api.S1Service
import me.ykrank.s1next.data.api.model.Post
import me.ykrank.s1next.data.api.model.Rate
import me.ykrank.s1next.data.api.model.Thread
import me.ykrank.s1next.data.api.model.Vote
import me.ykrank.s1next.data.pref.GeneralPreferencesManager
import me.ykrank.s1next.databinding.ItemPostBinding
import me.ykrank.s1next.databinding.ItemRateDetailBinding
import me.ykrank.s1next.view.activity.RateDetailsListActivity
import me.ykrank.s1next.view.activity.UserHomeActivity
import me.ykrank.s1next.viewmodel.PostViewModel
import me.ykrank.s1next.widget.glide.AvatarUrlsCache
import me.ykrank.s1next.widget.span.FixedSpannableFactory
import me.ykrank.s1next.widget.span.PostMovementMethod
import javax.inject.Inject

class PostAdapterDelegate(private val fragment: androidx.fragment.app.Fragment, context: Context) :
    BaseAdapterDelegate<Post, SimpleRecycleViewHolder<ItemPostBinding>>(context, Post::class.java) {

    @Inject
    internal lateinit var mRxBus: RxBus

    @Inject
    internal lateinit var mUser: User

    @Inject
    internal lateinit var mS1Service: S1Service

    @Inject
    internal lateinit var mGeneralPreferencesManager: GeneralPreferencesManager
    private var threadInfo: Thread? = null
    private var voteInfo: Vote? = null
    private var pageNum: Int = 1

    init {
        App.appComponent.inject(this)
    }

    private fun setTextSelectable(binding: ItemPostBinding, selectable: Boolean) {
        binding.authorName.setTextIsSelectable(selectable)

        binding.tvReply.setTextIsSelectable(selectable)
        binding.tvReply.movementMethod = PostMovementMethod.getInstance()
    }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean {
        return super.isForViewType(items, position)
    }

    public override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemPostBinding>(
            mLayoutInflater,
            R.layout.item_post, parent, false
        )
        binding.postViewModel = PostViewModel(mRxBus, mUser)

        binding.tvReply.setSpannableFactory(FixedSpannableFactory())

        //Bind textview lifecycle to fragment
        AndroidLifeCycle.bindFragment(binding.tvReply, fragment)

        //If setTextIsSelectable, then should reset movement
        val selectable = mGeneralPreferencesManager.isPostSelectable
        setTextSelectable(binding, selectable)

        return SimpleRecycleViewHolder<ItemPostBinding>(binding)
    }

    override fun onBindViewHolderData(
        post: Post,
        position: Int,
        holder: SimpleRecycleViewHolder<ItemPostBinding>,
        payloads: List<Any>
    ) {
        val binding = holder.binding

        binding.quickSidebarEnable = mGeneralPreferencesManager.isQuickSideBarEnable

        val selectable = mGeneralPreferencesManager.isPostSelectable
        if (selectable != binding.tvReply.isTextSelectable) {
            setTextSelectable(binding, selectable)
        }

        binding.postViewModel?.let {
            it.thread.set(threadInfo)
            it.pageNum.set(pageNum)
            it.post.set(post)

            if ("1" == post.count) {
                it.vote.set(voteInfo)
            } else {
                it.vote.set(null)
            }
        }

        val rates = post.rates
        val context = binding.root.context
        binding.tvRateViewAll.setOnClickListener {
            if (rates?.isNotEmpty() == true) {
                RateDetailsListActivity.start(context, ArrayList(rates))
            } else {
                mS1Service.getRates(threadInfo?.id, post.id.toString())
                    .to(AndroidRxDispose.withSingle(holder.itemView, ViewEvent.DESTROY))
                    .subscribe({
                        val rates = Rate.fromHtml(it)
                        post.rates = rates
                        val adapter = binding.recycleViewRates.adapter as SimpleRecycleViewAdapter?
                        if (adapter != null) {
                            if (rates.size > 10) {
                                adapter.diffNewDataSet(rates.subList(0, 10), true)
                            } else {
                                adapter.diffNewDataSet(rates, true)
                            }
                        }
                    }, L::report)
            }
        }
        if (rates?.isNotEmpty() == true) {
            if (binding.recycleViewRates.adapter == null) {
                binding.recycleViewRates.adapter = SimpleRecycleViewAdapter(
                    context,
                    R.layout.item_rate_detail,
                    true,
                    BindViewHolderCallback { position, binding ->
                        val bind = binding as ItemRateDetailBinding?
                        bind?.model?.apply {
                            val uid = this.uid
                            val uname = this.uname
                            bind.avatar.setOnClickListener {
                                if (uid != null && uname != null) {
                                    //Clear avatar false cache
                                    AvatarUrlsCache.clearUserAvatarCache(uid)
                                    //个人主页
                                    UserHomeActivity.start(
                                        it.context as androidx.fragment.app.FragmentActivity,
                                        uid,
                                        uname,
                                        it
                                    )
                                }
                            }
                        }

                    })
                binding.recycleViewRates.layoutManager = LinearLayoutManager(context)
                binding.recycleViewRates.isNestedScrollingEnabled = false
            }
            val adapter = binding.recycleViewRates.adapter as SimpleRecycleViewAdapter

            if (rates.size > 10) {
                adapter.diffNewDataSet(rates.subList(0, 10), true)
            } else {
                adapter.diffNewDataSet(rates, true)
            }
        }

        binding.executePendingBindings()
    }

    // Bug workaround for losing text selection ability, see:
    // https://code.google.com/p/android/issues/detail?id=208169
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (mGeneralPreferencesManager.isPostSelectable) {
            val binding = (holder as SimpleRecycleViewHolder<ItemPostBinding>).binding
            binding.authorName.isEnabled = false
            binding.tvReply.isEnabled = false
            binding.authorName.isEnabled = true
            binding.tvReply.isEnabled = true
        }
    }

    fun setThreadInfo(threadInfo: Thread, pageNum: Int) {
        this.threadInfo = threadInfo
        this.pageNum = pageNum
    }

    fun setVoteInfo(voteInfo: Vote?) {
        this.voteInfo = voteInfo
    }

}

