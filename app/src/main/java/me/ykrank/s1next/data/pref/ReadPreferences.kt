package me.ykrank.s1next.data.pref

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.ykrank.androidtools.data.BasePreferences
import com.github.ykrank.androidtools.data.PreferenceDelegates
import com.github.ykrank.androidtools.util.L
import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import me.ykrank.s1next.R
import me.ykrank.s1next.data.db.dbmodel.ReadProgress
import java.io.IOException

/**
 * A helper class retrieving the download preferences from [SharedPreferences].
 */
class ReadPreferencesImpl(context: Context, sharedPreferences: SharedPreferences,
                          private val objectMapper: ObjectMapper)
    : BasePreferences(context, sharedPreferences), ReadPreferences {

    override val isSaveAuto: Boolean by PreferenceDelegates.bool(
            R.string.pref_key_read_progress_save_auto, R.bool.pref_read_progress_save_auto_default_value)

    override val isLoadAuto: Boolean by PreferenceDelegates.bool(
            R.string.pref_key_read_progress_load_auto, R.bool.pref_read_progress_load_auto_default_value)

    override var lastReadProgress: ReadProgress? = null
        get() {
            try {
                val lastStr = getPrefString(R.string.pref_key_last_read_progress, "")
                if (!TextUtils.isEmpty(lastStr)) {
                    field = objectMapper.readValue<ReadProgress>(lastStr, ReadProgress::class.java)
                }
            } catch (e: IOException) {
                L.report(e)
            }
            return field
        }
        set(value) {
            try {
                val lastStr = if (value == null) "" else objectMapper.writeValueAsString(value)
                putPrefString(R.string.pref_key_last_read_progress, lastStr)
            } catch (e: JsonProcessingException) {
                L.report(e)
            }
        }

    override var volumeKeyPaging: Boolean by PreferenceDelegates.bool(
            R.string.pref_key_volume_key_paging, R.bool.pref_volume_key_paging_default_value)

    override var threadPadding: String? by PreferenceDelegates.string(
            R.string.pref_key_thread_padding, null)
}

interface ReadPreferences {
    val isSaveAuto: Boolean
    val isLoadAuto: Boolean
    var lastReadProgress: ReadProgress?
    var volumeKeyPaging: Boolean
    var threadPadding: String?
}

class ReadPreferencesManager(private val mPreferencesProvider: ReadPreferences) {
    private val mLastReadProgressSupplier = Supplier<ReadProgress> { mPreferencesProvider.lastReadProgress }

    @Volatile
    private var mLastReadProgressMemorized = Suppliers.memoize(mLastReadProgressSupplier)

    val isSaveAuto: Boolean
        get() = mPreferencesProvider.isSaveAuto

    val isLoadAuto: Boolean
        get() = mPreferencesProvider.isLoadAuto

    val volumeKeyPaging: Boolean
        get() = mPreferencesProvider.volumeKeyPaging

    val threadPadding: Int?
        get() = mPreferencesProvider.threadPadding?.toIntOrNull()

    val lastReadProgress: ReadProgress?
        get() = mLastReadProgressMemorized.get()

    fun invalidateLastReadProgress() {
        mLastReadProgressMemorized = Suppliers.memoize(mLastReadProgressSupplier)
    }

    fun saveLastReadProgress(readProgress: ReadProgress?) {
        mPreferencesProvider.lastReadProgress = readProgress
    }
}

