package za.co.gingergeek.moneta.application

import android.app.Activity
import android.app.Application
import android.os.Bundle

class LifeCycleCallbacks(val callback: (ActivityCallback) -> Unit) :
    Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        callback(ActivityCallback(ActivityState.CREATED, activity))
    }

    override fun onActivityStarted(activity: Activity) {
        callback(ActivityCallback(ActivityState.STARTED, activity))
    }

    override fun onActivityResumed(activity: Activity) {
        callback(ActivityCallback(ActivityState.RESUMED, activity))
    }

    override fun onActivityPaused(activity: Activity) {
        callback(ActivityCallback(ActivityState.PAUSED, activity))
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        callback(ActivityCallback(ActivityState.SAVE_INSTANCE_STATE, activity))
    }

    override fun onActivityStopped(activity: Activity) {
        callback(ActivityCallback(ActivityState.STOPPED, activity))
    }

    override fun onActivityDestroyed(activity: Activity) {
        callback(ActivityCallback(ActivityState.DESTROYED, activity))
    }

    class ActivityCallback(val state: ActivityState, val activity: Activity)

    enum class ActivityState() {
        CREATED, STARTED, RESUMED, PAUSED, SAVE_INSTANCE_STATE, STOPPED, DESTROYED
    }
}