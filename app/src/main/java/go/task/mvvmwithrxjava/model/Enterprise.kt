package go.task.mvvmwithrxjava.model

import org.json.JSONObject

class Enterprise (
    var s_no: Long = 0,
    var title: String? = null,
    var by: String? = null,
    var num_backers: String? = null
) : Comparable<Enterprise>{
    companion object {
        fun fromJsonObject(json: JSONObject): Enterprise {
            return Enterprise(
                json.getLong("s.no"),
                json.getString("title"),
                json.getString("by"),
                json.getString("num.backers")
            )
        }
    }

    override fun compareTo(other: Enterprise): Int {
        return title?.compareTo(other.title!!)!!
    }
}