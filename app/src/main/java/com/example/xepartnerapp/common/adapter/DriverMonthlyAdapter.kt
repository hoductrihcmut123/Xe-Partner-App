package com.example.xepartnerapp.common.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.xepartnerapp.R
import com.example.xepartnerapp.data.DriverMonthlyDailyStatistics
import com.example.xepartnerapp.databinding.LayoutItemStatisticsMonthlyDailyDriverBinding
import com.example.xepartnerapp.features.driver.menu.statistics.StatisticsDailyActivity

class DriverMonthlyAdapter(private val context: Context) :
    RecyclerView.Adapter<DriverMonthlyAdapter.ViewHolder>() {

    private val mDriverMonthlyStatistics by lazy { mutableListOf<DriverMonthlyDailyStatistics>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            LayoutItemStatisticsMonthlyDailyDriverBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mDriverMonthlyStatistics.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(mDriverMonthlyStatistics[position])

        holder.itemView.setOnClickListener {
            val newItem = mDriverMonthlyStatistics[position]

            val intent = Intent(context, StatisticsDailyActivity::class.java)
            intent.putExtra("MONTH_DATA", newItem.monthOrDayData)
            context.startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(listData: MutableList<DriverMonthlyDailyStatistics>) {
        mDriverMonthlyStatistics.clear()
        mDriverMonthlyStatistics.addAll(listData)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: LayoutItemStatisticsMonthlyDailyDriverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(newItem: DriverMonthlyDailyStatistics) {
            binding.tvMonth.text = newItem.monthOrDay
            binding.tvTotalRequest.text =
                itemView.context.getString(R.string.TotalRequestStatistics, newItem.totalRequest)
            binding.tvTripSuccess.text =
                itemView.context.getString(R.string.TripSuccessStatistics, newItem.tripSuccess)
            binding.tvTotalCancel.text =
                itemView.context.getString(R.string.TotalCancelStatistics, newItem.totalCancel)
            binding.tvDriverCancel.text =
                itemView.context.getString(R.string.DriverCancelStatistics, newItem.driverCancel)
            binding.tvPassengerCancel.text = itemView.context.getString(
                R.string.PassengerCancelStatistics,
                newItem.passengerCancel
            )
            binding.tvTotalDistance.text =
                itemView.context.getString(R.string.TotalDistanceStatistics, newItem.totalDistance)
            binding.tvTotalDuration.text =
                itemView.context.getString(R.string.TotalDurationStatistics, newItem.totalDuration)
            binding.tvTotalIncome.text =
                itemView.context.getString(R.string.TotalIncomeStatistics, newItem.totalIncome)
        }
    }
}
