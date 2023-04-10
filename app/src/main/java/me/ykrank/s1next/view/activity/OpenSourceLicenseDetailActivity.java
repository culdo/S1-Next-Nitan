package me.ykrank.s1next.view.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import me.ykrank.s1next.R;
import me.ykrank.s1next.databinding.ActivityOpenSourceLicenseDetailBinding;
import me.ykrank.s1next.viewmodel.TextAssetViewModel;

/**
 * An Activity shows the open source license for corresponding library or file.
 */
public final class OpenSourceLicenseDetailActivity extends BaseActivity {
    public static final String TAG = OpenSourceLicenseDetailActivity.class.getName();

    private static final String EXTRA_LIBRARY_OR_FILE_NAME = "library_or_file_name";
    private static final String EXTRA_LICENSE_FILE_PATH = "license_file_path";

    public static void startOpenSourceLicenseDetailActivity(Context context, String libraryOrFileName, String licenseFilePath) {
        Intent intent = new Intent(context, OpenSourceLicenseDetailActivity.class);
        intent.putExtra(EXTRA_LIBRARY_OR_FILE_NAME, libraryOrFileName);
        intent.putExtra(EXTRA_LICENSE_FILE_PATH, licenseFilePath);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityOpenSourceLicenseDetailBinding binding = DataBindingUtil.setContentView(this,
                R.layout.activity_open_source_license_detail);

        Intent intent = getIntent();
        String libraryOrFileName = intent.getStringExtra(EXTRA_LIBRARY_OR_FILE_NAME);
        setTitle(libraryOrFileName);

        binding.setTextAssetViewModel(new TextAssetViewModel(intent.getStringExtra(
                EXTRA_LICENSE_FILE_PATH)));
    }
}
