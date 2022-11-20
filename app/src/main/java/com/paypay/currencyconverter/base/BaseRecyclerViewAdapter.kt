package com.paypay.currencyconverter.base

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T>(private val data: MutableList<T> = mutableListOf()) :
    RecyclerView.Adapter<BaseViewHolder>() {

    protected abstract val callback: (input: List<T>) -> DiffUtil.Callback

    abstract fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder

    override fun getItemCount(): Int = data.size

    fun getData(): List<T> = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return getViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        holder.destroy()
        super.onViewDetachedFromWindow(holder)
    }

    fun addItems(data: List<T>, forceRefresh: Boolean? = false) {
        callback.invoke(data).let {
            this.data.clear()
            this.data.addAll(data)
            if (forceRefresh == true) {
                notifyDataSetChanged() //is a heavy op but has to go with this to avoid ui flicker
            } else {
                val diffResult = DiffUtil.calculateDiff(it)
                diffResult.dispatchUpdatesTo(this)
            }

        }
    }

}

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun onBind(data: Any?)
    abstract fun destroy()
}