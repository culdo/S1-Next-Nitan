package me.ykrank.s1next.binding;

import androidx.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.github.ykrank.androidtools.widget.glide.model.ForcePassUrl;
import com.github.ykrank.androidtools.widget.glide.viewtarget.LargeImageViewTarget;
import com.shizhefei.view.largeimage.LargeImageView;

import java.io.File;

import me.ykrank.s1next.data.api.Api;
import me.ykrank.s1next.data.pref.DownloadPreferencesManager;

public final class LargeImageViewBindingAdapter {

    private LargeImageViewBindingAdapter() {
    }

    @BindingAdapter({"url", "thumbUrl", "manager", "show"})
    public static void loadImage(LargeImageView largeImageView, String url, @Nullable String thumbUrl, DownloadPreferencesManager manager, boolean show) {
        if (!show || TextUtils.isEmpty(url)) {
            return;
        }
        RequestBuilder<File> builder = Glide.with(largeImageView)
                .download(new ForcePassUrl(url));
        //avatar signature
        if (manager != null && Api.isAvatarUrl(url)) {
            builder = builder.apply(new RequestOptions()
                    .signature(manager.getAvatarCacheInvalidationIntervalSignature()));
        }

        builder.into(new LargeImageViewTarget(largeImageView) {
            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                if (thumbUrl != null) {
                    loadImage(largeImageView, thumbUrl, null, manager, show);
                } else {
                    super.onLoadFailed(errorDrawable);
                }
            }
        });
    }
}
