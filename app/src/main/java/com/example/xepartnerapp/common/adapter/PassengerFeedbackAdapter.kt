package com.example.xepartnerapp.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.xepartnerapp.R
import com.example.xepartnerapp.common.utils.Utils.extractDate
import com.example.xepartnerapp.common.utils.Utils.getLastThreeChars
import com.example.xepartnerapp.data.PassengerFeedback
import com.example.xepartnerapp.databinding.LayoutItemFeedbackPassengerBinding

class PassengerFeedbackAdapter : RecyclerView.Adapter<PassengerFeedbackAdapter.ViewHolder>() {

    private val mPassengerFeedback by lazy { mutableListOf<PassengerFeedback>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(LayoutItemFeedbackPassengerBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return mPassengerFeedback.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(mPassengerFeedback[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(listData: MutableList<PassengerFeedback>) {
        mPassengerFeedback.clear()
        mPassengerFeedback.addAll(listData)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: LayoutItemFeedbackPassengerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(newItem: PassengerFeedback) {
            binding.tvDriverIdItem.text = itemView.context.getString(
                R.string.DriverItemId,
                getLastThreeChars(newItem.driver_ID)
            )
            binding.tvTimeItem.text = extractDate(newItem.passengerFeedbackTime)

            if (newItem.reportPassengerReason.isNotEmpty()) {
                binding.layoutReportFeedbackItem.isVisible = true
                binding.tvReportFeedbackItem.text = newItem.reportPassengerReason
            } else {
                binding.layoutReportFeedbackItem.isVisible = false
            }

            if (newItem.reportPassengerReasonDetail.isNotEmpty()) {
                binding.layoutReportFeedbackContentItem.isVisible = true
                binding.tvReportFeedbackContentItem.text = newItem.reportPassengerReasonDetail
            } else {
                binding.layoutReportFeedbackContentItem.isVisible = false
            }
        }
    }
}
