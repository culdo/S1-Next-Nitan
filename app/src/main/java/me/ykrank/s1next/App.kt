package me.ykrank.s1next

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.github.ykrank.androidtools.DefaultAppDataProvider
import com.github.ykrank.androidtools.GlobalData
import com.github.ykrank.androidtools.extension.toast
import com.github.ykrank.androidtools.ui.RProvider
import com.github.ykrank.androidtools.ui.UiDataProvider
import com.github.ykrank.androidtools.ui.UiGlobalData
import com.github.ykrank.androidtools.util.ErrorParser
import com.github.ykrank.androidtools.util.L
import com.github.ykrank.androidtools.util.ProcessUtil
import com.github.ykrank.androidtools.util.ResourceUtil
import com.github.ykrank.androidtools.widget.net.WifiActivityLifecycleCallbacks
import com.github.ykrank.androidtools.widget.track.DataTrackAgent
import io.reactivex.plugins.RxJavaPlugins
import me.ykrank.s1next.data.db.DbModule
import me.ykrank.s1next.data.pref.GeneralPreferencesManager
import me.ykrank.s1next.data.pref.PrefModule
import me.ykrank.s1next.util.BuglyUtils
import me.ykrank.s1next.util.ErrorUtil
import me.ykrank.s1next.widget.AppActivityLifecycleCallbacks


class App : MultiDexApplication() {

    init {
        sApp = this
    }

    private lateinit var mGeneralPreferencesManager: GeneralPreferencesManager

    private lateinit var mAppComponent: AppComponent

    private lateinit var mPreAppComponent: PreAppComponent

    private lateinit var mAppActivityLifecycleCallbacks: AppActivityLifecycleCallbacks

    var resourceContext: Context? = null

    val trackAgent: DataTrackAgent
        get() = mPreAppComponent.dataTrackAgent

    val isAppVisible: Boolean
        get() = mAppActivityLifecycleCallbacks.isAppVisible

    override fun attachBaseContext(base: Context) {
        mPreAppComponent = DaggerPreAppComponent.builder()
            .preAppModule(PreAppModule(this))
            .prefModule(PrefModule(base))
            .build()
        mGeneralPreferencesManager = mPreAppComponent.generalPreferencesManager
        super.attachBaseContext(ResourceUtil.setScaledDensity(base, mGeneralPreferencesManager.fontScale))
    }

    override fun onCreate() {
        super.onCreate()

        // enable StrictMode when debug
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }

        mPreAppComponent.dataTrackAgent.init(this)
        GlobalData.init(object : DefaultAppDataProvider() {
            override val errorParser: ErrorParser?
                get() = ErrorUtil
            override val logTag: String
                get() = LOG_TAG
            override val buildType: String
                get() = BuildConfig.BUILD_TYPE
            override val debug: Boolean
                get() = BuildConfig.DEBUG
            override val appR: Class<out Any>
                get() = R::class.java
        })
        L.init(this)
        BuglyUtils.init(this)

        mAppComponent = DaggerAppComponent.builder()
            .preAppComponent(mPreAppComponent)
            .buildTypeModule(BuildTypeModule(this))
            .appModule(AppModule())
            .dbModule(DbModule())
            .build()

        mAppActivityLifecycleCallbacks = AppActivityLifecycleCallbacks(mAppComponent.noticeCheckTask)
        registerActivityLifecycleCallbacks(mAppActivityLifecycleCallbacks)

        UiGlobalData.init(object : UiDataProvider {
            override val actLifeCallback: WifiActivityLifecycleCallbacks
                get() = mAppActivityLifecycleCallbacks
            override val trackAgent: DataTrackAgent
                get() = mPreAppComponent.dataTrackAgent
        }, object : RProvider {

        }, this::toast)

        L.l("App init")
        PreApp.onCreate(this)

        //RxJava default error handler
        RxJavaPlugins.setErrorHandler {
            if (L.showLog()) {
                toast(ErrorUtil.parse(this, it))
            }
        }

        //如果不是主进程，不做多余的初始化
        if (!ProcessUtil.isMainProcess(this))
            return

        //enable vector drawable
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        mAppComponent.imageDownloadManager.setup(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        L.l("App onConfigurationChanged")

        //如果不是主进程，不做多余的初始化
        if (!ProcessUtil.isMainProcess(this))
            return
    }

    companion object {
        val LOG_TAG = "S1NextLog"

        private lateinit var sApp: App

        fun get(): App {
            return sApp
        }

        val appComponent: AppComponent
            get() = sApp.mAppComponent

        val preAppComponent: PreAppComponent
            get() = sApp.mPreAppComponent
    }
}
