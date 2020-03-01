package go.task.mvvmwithrxjava

import android.app.Application
import go.task.mvvmwithrxjava.data.EnterpriseData

class TaskApp : Application() {
    var enterpriseData: EnterpriseData? = null
    override fun onCreate() {
        super.onCreate()
        enterpriseData = EnterpriseData.getInstance(this)
    }
}