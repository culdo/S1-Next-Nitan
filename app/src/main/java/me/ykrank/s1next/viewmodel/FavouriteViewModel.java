package me.ykrank.s1next.viewmodel;

import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.ObservableField;

import com.github.ykrank.androidtools.widget.RxBus;
import com.google.common.base.Supplier;

import io.reactivex.functions.Consumer;
import me.ykrank.s1next.R;
import me.ykrank.s1next.data.api.model.Favourite;
import me.ykrank.s1next.data.api.model.Thread;
import me.ykrank.s1next.view.event.FavoriteRemoveEvent;
import me.ykrank.s1next.view.page.post.PostListActivity;


public final class FavouriteViewModel {

    public final ObservableField<Favourite> favourite = new ObservableField<>();

    private final Supplier<Thread> threadSupplier = () -> {
        Thread thread = new Thread();
        Favourite favourite = this.favourite.get();
        thread.setId(favourite.getId());
        thread.setTitle(favourite.getTitle());
        return thread;
    };

    public Consumer<View> onBind() {
        return v -> PostListActivity.Companion.bindClickStartForView(v, threadSupplier);
    }

    public View.OnLongClickListener removeFromFavourites(final RxBus rxBus) {
        return v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.setOnMenuItemClickListener((MenuItem menuitem) -> {
                switch (menuitem.getItemId()) {
                    case R.id.menu_popup_remove_favourite:
                        rxBus.post(new FavoriteRemoveEvent(favourite.get().getFavId()));
                        return true;
                    default:
                        return false;
                }
            });
            popup.inflate(R.menu.popup_favorites);
            popup.show();
            return true;
        };
    }
}
