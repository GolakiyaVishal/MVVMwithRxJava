package go.task.mvvmwithrxjava.data

import android.content.Context
import androidx.lifecycle.LiveData
import go.task.mvvmwithrxjava.model.Enterprise
import io.reactivex.Observable
import io.reactivex.Single
import org.json.JSONObject

class Repository(context: Context) {
    private val _enterpriseData = EnterpriseData.getInstance(context)

    fun getEntData(): Single<List<Enterprise>> {
        return _enterpriseData.getEntData()
    }

}
