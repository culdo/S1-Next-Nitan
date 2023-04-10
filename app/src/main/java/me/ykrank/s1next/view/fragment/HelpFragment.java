package me.ykrank.s1next.view.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.github.ykrank.androidtools.ui.internal.CoordinatorLayoutAnchorDelegate;
import com.github.ykrank.androidtools.util.L;

import org.jetbrains.annotations.NotNull;

import me.ykrank.s1next.R;
import me.ykrank.s1next.databinding.FragmentWebviewBinding;
import me.ykrank.s1next.view.activity.OpenSourceLicensesActivity;
import me.ykrank.s1next.view.dialog.VersionInfoDialogFragment;
import me.ykrank.s1next.viewmodel.WebPageViewModel;

/**
 * A Fragment represents a help page.
 * <p>
 * Also some related controls are provided in overflow menu:
 * 1.Link our app to Android marketplaces or Google Play website.
 * 2.See open sources licenses information.
 * 3.See version number.
 */
public final class HelpFragment extends Fragment {

    public static final String TAG = HelpFragment.class.getName();
    private static final String HELP_PAGE_URL = "file:///android_asset/help/HELP.html";
    /**
     * https://developer.android.com/distribute/tools/promote/linking.html#OpeningDetails
     */
    private static final String ANDROID_APP_MARKET_LINK = "market://details?id=%s";
    private static final String ANDROID_WEB_SITE_MARKET_LINK = "http://play.google.com/store/apps/details?id=%s";
    private FragmentWebviewBinding mFragmentHelpBinding;
    private WebView mWebView;

    public static HelpFragment getInstance() {
        return new HelpFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentHelpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_webview, container,
                false);
        mWebView = mFragmentHelpBinding.webView;
        return mFragmentHelpBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        L.leaveMsg("HelpFragment");

        WebPageViewModel viewModel = new WebPageViewModel();
        mFragmentHelpBinding.setWebPageViewModel(viewModel);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                viewModel.setFinishedLoading(true);
            }
        });

        // restore the state of WebView when configuration changes
        // see http://www.devahead.com/blog/2012/01/preserving-the-state-of-an-android-webview-on-screen-orientation-change/
        if (savedInstanceState == null) {
            mWebView.loadUrl(HELP_PAGE_URL);
        } else {
            mWebView.restoreState(savedInstanceState);
            // if we haven't finished loading before
            if (mWebView.getUrl() == null) {
                mWebView.loadUrl(HELP_PAGE_URL);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_help, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_view_in_google_play_store:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String packageName = getContext().getPackageName();
                intent.setData(Uri.parse(String.format(ANDROID_APP_MARKET_LINK, packageName)));
                try {
                    // link our app in Android marketplaces
                    startActivity(intent);
                } catch (ActivityNotFoundException exception) {
                    intent.setData(Uri.parse(String.format(ANDROID_WEB_SITE_MARKET_LINK, packageName)));
                    try {
                        // link our app in Google Play website if user hasn't installed any Android marketplaces
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        // show Snackbar if user hasn't installed any Android marketplaces or browsers
                        ((CoordinatorLayoutAnchorDelegate) getActivity()).showShortSnackbar(
                                R.string.message_chooser_no_applications);
                    }
                }

                return true;
            case R.id.menu_open_source_licenses:
                OpenSourceLicensesActivity.startOpenSourceLicensesActivity(getContext());

                return true;
            case R.id.menu_version_info:
                new VersionInfoDialogFragment().show(getFragmentManager(),
                        VersionInfoDialogFragment.TAG);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mWebView.saveState(outState);
    }

    public WebView getWebView() {
        return mWebView;
    }
}
