package me.ykrank.s1next.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import me.ykrank.s1next.R;
import me.ykrank.s1next.data.api.model.collection.Posts;
import me.ykrank.s1next.view.adapter.ThreadAttachmentInfoListArrayAdapter;

/**
 * A dialog shows attachment.
 */
public final class ThreadAttachmentDialogFragment extends BaseDialogFragment {

    public static final String TAG = ThreadAttachmentDialogFragment.class.getName();

    private static final String ARG_ATTACHMENT_TITLE = "attachment_title";
    private static final String ARG_THREAD_ATTACHMENT_INFO_LIST = "thread_attachment_info_list";

    public static ThreadAttachmentDialogFragment newInstance(Posts.ThreadAttachment threadAttachment) {
        ThreadAttachmentDialogFragment fragment = new ThreadAttachmentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_ATTACHMENT_TITLE, threadAttachment.getTitle());
        bundle.putParcelableArrayList(ARG_THREAD_ATTACHMENT_INFO_LIST, threadAttachment.getInfoList());
        fragment.setArguments(bundle);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(getArguments().getString(ARG_ATTACHMENT_TITLE))
                .setAdapter(new ThreadAttachmentInfoListArrayAdapter(getContext(),
                        R.layout.item_two_line_text, getArguments().getParcelableArrayList(
                        ARG_THREAD_ATTACHMENT_INFO_LIST)), null)
                .setPositiveButton(R.string.dialog_button_text_done, null)
                .create();
    }
}
