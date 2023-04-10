package me.ykrank.s1next.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import me.ykrank.s1next.R
import me.ykrank.s1next.view.fragment.ReplyFragment
import org.apache.commons.lang3.StringUtils

/**
 * An Activity which used to send a reply.
 */
class ReplyActivity : BaseActivity() {

    private lateinit var mReplyFragment: ReplyFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_with_ime_panel)

        setupNavCrossIcon()

        val intent = intent
        val threadId = intent.getStringExtra(ARG_THREAD_ID)
        val quotePostId = intent.getStringExtra(ARG_QUOTE_POST_ID)
        leavePageMsg("ReplyActivity##threadId:$threadId,quotePostId:$quotePostId")

        val titlePrefix = if (TextUtils.isEmpty(quotePostId))
            getString(R.string.reply_activity_title_prefix)
        else
            getString(R.string.reply_activity_quote_title_prefix,
                    intent.getStringExtra(ARG_QUOTE_POST_COUNT))
        title = titlePrefix + StringUtils.defaultString(intent.getStringExtra(ARG_THREAD_TITLE),
                StringUtils.EMPTY)

        val fragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentByTag(ReplyFragment.TAG)
        if (fragment == null) {
            mReplyFragment = ReplyFragment.newInstance(threadId!!,
                    quotePostId)
            fragmentManager.beginTransaction().add(R.id.frame_layout, mReplyFragment,
                    ReplyFragment.TAG).commit()
        } else {
            mReplyFragment = fragment as ReplyFragment
        }
    }

    /**
     * Show [android.support.v7.app.AlertDialog] when reply content is not empty.
     */
    override fun onBackPressed() {
        if (mReplyFragment.isToolsKeyboardShowing) {
            mReplyFragment.hideToolsKeyboard()
        } else {
            super.onBackPressed()
        }
    }

    companion object {

        private const val ARG_THREAD_ID = "thread_id"
        private const val ARG_THREAD_TITLE = "thread_title"

        private const val ARG_QUOTE_POST_ID = "quote_post_id"
        private const val ARG_QUOTE_POST_COUNT = "quote_post_count"

        fun startReplyActivityForResultMessage(activity: Activity, threadId: String, threadTitle: String?,
                                               quotePostId: String?, quotePostCount: String?) {
            val intent = Intent(activity, ReplyActivity::class.java)
            intent.putExtra(ARG_THREAD_ID, threadId)
            intent.putExtra(ARG_THREAD_TITLE, threadTitle)

            intent.putExtra(ARG_QUOTE_POST_ID, quotePostId)
            intent.putExtra(ARG_QUOTE_POST_COUNT, quotePostCount)

            BaseActivity.startActivityForResultMessage(activity, intent)
        }
    }
}
