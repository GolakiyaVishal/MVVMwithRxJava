package go.task.mvvmwithrxjava.data

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import go.task.mvvmwithrxjava.model.Enterprise
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import org.json.JSONArray

class EnterpriseData(context: Context) {

    companion object {
        var instance: EnterpriseData? = null

        fun getInstance(context: Context): EnterpriseData {
            if (instance == null) {
                instance = EnterpriseData(context)
            }
            return instance!!
        }
    }

    val queue = Volley.newRequestQueue(context)
    val url = "https://enterprisesmail.com/json/api.json"

    fun getEntData() : Single<List<Enterprise>>{
        return Single.create<List<Enterprise>>(DataSingleOnSubscribe())
    }

    inner class DataSingleOnSubscribe :SingleOnSubscribe<List<Enterprise>>{
        val dataList = mutableListOf<Enterprise>()
        override fun subscribe(e:SingleEmitter<List<Enterprise>>){
            val jsonArrayRequest = JsonArrayRequest(url,
                Response.Listener<JSONArray> {
                    try {
                        if (it != null) {
                            for(i in 0 until it.length()){
                                dataList.add(Enterprise.fromJsonObject(it.getJSONObject(i)))
                            }
                            e.onSuccess(dataList)
                            Log.e("vo response", "${dataList.size}")
                        } else {
                            e.onSuccess(dataList)
                            Log.e("response", "empty")
                        }
                    } catch (exp: Exception) {
                        e.onError(exp)
                        exp.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    e.onError(it)
                    Log.e("error", "${it}")
                })

            queue.add(jsonArrayRequest)
        }
    }
}