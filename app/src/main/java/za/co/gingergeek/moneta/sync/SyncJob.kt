package za.co.gingergeek.moneta.sync

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build


class SyncJob : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        val service = Intent(applicationContext, SyncService::class.java)
        applicationContext.startService(service)
        scheduleJob(applicationContext)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    companion object {
        private const val syncInterval: Long = 1000 * 60 * 60 * 2 // 2 hours
        private const val minimumLatency: Long = 1000 * 60 * 30 // 30 minutes

        fun scheduleJob(context: Context) {
            val serviceComponent = ComponentName(context, SyncJob::class.java)
            val jobInfo = JobInfo.Builder(0, serviceComponent)
                .setMinimumLatency(minimumLatency)
                .setOverrideDeadline(syncInterval)
                .setRequiresCharging(false)
                .build()

            val jobScheduler = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.getSystemService(JobScheduler::class.java) as JobScheduler
            } else {
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            }

            jobScheduler.schedule(jobInfo)
        }
    }
}