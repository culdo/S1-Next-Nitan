package cl.monsoon.s1next.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import cl.monsoon.s1next.App;
import cl.monsoon.s1next.R;
import cl.monsoon.s1next.data.api.Api;
import cl.monsoon.s1next.data.api.model.Quote;
import cl.monsoon.s1next.data.api.model.Result;
import cl.monsoon.s1next.data.api.model.wrapper.ResultWrapper;
import cl.monsoon.s1next.util.ToastUtil;
import rx.Observable;

/**
 * A dialog requests to reply to post.
 */
public final class ReplyRequestDialogFragment extends ProgressDialogFragment<ResultWrapper> {

    public static final String TAG = ReplyRequestDialogFragment.class.getName();

    private static final String ARG_THREAD_ID = "thread_id";
    private static final String ARG_REPLY = "reply";
    private static final String ARG_QUOTE_POST_ID = "quote_post_id";

    private static final String STATUS_REPLY_SUCCESS = "post_reply_succeed";

    public static ReplyRequestDialogFragment newInstance(String threadId, @Nullable String quotePostId, String reply) {
        ReplyRequestDialogFragment fragment = new ReplyRequestDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_THREAD_ID, threadId);
        bundle.putString(ARG_QUOTE_POST_ID, quotePostId);
        bundle.putString(ARG_REPLY, reply);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected CharSequence getProgressMessage() {
        return getText(R.string.dialog_progress_message_reply);
    }

    @Override
    protected Observable<ResultWrapper> getSourceObservable() {
        String authenticityToken = App.getAppComponent(getActivity()).getUser().getAuthenticityToken();
        String threadId = getArguments().getString(ARG_THREAD_ID);
        String quotePostId = getArguments().getString(ARG_QUOTE_POST_ID);
        String reply = getArguments().getString(ARG_REPLY);

        if (TextUtils.isEmpty(quotePostId)) {
            return mS1Service.reply(authenticityToken, threadId, reply);
        } else {
            return mS1Service.getQuoteInfo(threadId, quotePostId).switchMap(s -> {
                Quote quote = Quote.fromXmlString(s);
                return mS1Service.replyQuote(authenticityToken, threadId, reply,
                        quote.getEncodedUserId(), quote.getQuoteMessage(),
                        StringUtils.abbreviate(reply, Api.REPLY_NOTIFICATION_MAX_LENGTH));
            });
        }
    }

    @Override
    protected void onNext(ResultWrapper data) {
        Result result = data.getResult();
        ToastUtil.showByText(result.getMessage(), Toast.LENGTH_LONG);

        if (result.getStatus().equals(STATUS_REPLY_SUCCESS)) {
            getActivity().finish();
        }
    }
}