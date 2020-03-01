package go.task.mvvmwithrxjava.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import go.task.mvvmwithrxjava.R
import go.task.mvvmwithrxjava.model.Enterprise

class MainAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var dataList: List<Enterprise>? = null

    fun notifyDataChange(dataList: List<Enterprise>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.tile_enterprise_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return if (dataList != null) dataList!!.size else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h1 = holder as Holder
        h1.onBind()
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtTitle = itemView.findViewById<AppCompatTextView>(R.id.txt_item_title)
        private val txtBy = itemView.findViewById<AppCompatTextView>(R.id.txt_item_by)
        private val txtNum = itemView.findViewById<AppCompatTextView>(R.id.txt_item_num_backers)

        fun onBind() {
            val entp = dataList!![adapterPosition]
            txtTitle.text = entp.title
            txtBy.text = entp.by
            txtNum.text = entp.num_backers
        }
    }
}