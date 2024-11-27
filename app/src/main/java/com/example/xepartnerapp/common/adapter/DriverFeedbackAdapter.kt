package com.example.xepartnerapp.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.xepartnerapp.R
import com.example.xepartnerapp.common.utils.Utils.extractDate
import com.example.xepartnerapp.common.utils.Utils.getLastThreeChars
import com.example.xepartnerapp.data.DriverFeedback
import com.example.xepartnerapp.databinding.LayoutItemFeedbackDriverBinding

class DriverFeedbackAdapter : RecyclerView.Adapter<DriverFeedbackAdapter.ViewHolder>() {

    private val mDriverFeedback by lazy { mutableListOf<DriverFeedback>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(LayoutItemFeedbackDriverBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return mDriverFeedback.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(mDriverFeedback[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(listData: MutableList<DriverFeedback>) {
        mDriverFeedback.clear()
        mDriverFeedback.addAll(listData)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: LayoutItemFeedbackDriverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(newItem: DriverFeedback) {
            binding.tvPassengerIdItem.text = itemView.context.getString(
                R.string.PassengerItemId,
                getLastThreeChars(newItem.passenger_ID)
            )
            binding.tvTimeItem.text = extractDate(newItem.driverFeedbackTime)

            if (newItem.star != 0) {
                binding.tvRatingFeedbackItem.isVisible = true
                binding.tvRatingFeedbackItem.text = newItem.star.toString()
            } else {
                binding.tvRatingFeedbackItem.isVisible = false
            }

            if (newItem.feedback.isNotEmpty()) {
                binding.tvFeedbackContentItem.isVisible = true
                binding.tvFeedbackContentItem.text = newItem.feedback
            } else {
                binding.tvFeedbackContentItem.isVisible = false
            }

            if (newItem.reportDriverReason.isNotEmpty()) {
                binding.layoutReportFeedbackItem.isVisible = true
                binding.tvReportFeedbackItem.text = newItem.reportDriverReason
            } else {
                binding.layoutReportFeedbackItem.isVisible = false
            }

            if (newItem.reportDriverReasonDetail.isNotEmpty()) {
                binding.layoutReportFeedbackContentItem.isVisible = true
                binding.tvReportFeedbackContentItem.text = newItem.reportDriverReasonDetail
            } else {
                binding.layoutReportFeedbackContentItem.isVisible = false
            }
        }
    }
}
