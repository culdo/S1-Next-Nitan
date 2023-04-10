package me.ykrank.s1next.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import me.ykrank.s1next.R;
import me.ykrank.s1next.view.fragment.FavouriteListFragment;

/**
 * An Activity shows the thread lists.
 */
public final class FavouriteListActivity extends BaseActivity {

    public static void startFavouriteListActivity(Context context) {
        Intent intent = new Intent(context, FavouriteListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_without_drawer);
        disableDrawerIndicator();

        if (savedInstanceState == null) {
            Fragment fragment = new FavouriteListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragment,
                    FavouriteListFragment.Companion.getTAG()).commit();
        }
    }
}
