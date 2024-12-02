package com.example.xepartnerapp.common.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.xepartnerapp.common.utils.Constants
import com.example.xepartnerapp.common.utils.Utils.extractTime
import com.example.xepartnerapp.common.utils.Utils.getHourAndMinute
import com.example.xepartnerapp.data.TripDataDto
import com.example.xepartnerapp.databinding.LayoutItemTripsInDayBinding
import com.example.xepartnerapp.features.driver.menu.statistics.TripDetailActivity

class DriverTripOverviewAdapter(private val context: Context) :
    RecyclerView.Adapter<DriverTripOverviewAdapter.ViewHolder>() {

    private val mTripOverviews by lazy { mutableListOf<TripDataDto>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            LayoutItemTripsInDayBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mTripOverviews.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(mTripOverviews[position])

        holder.itemView.setOnClickListener {
            val newItem = mTripOverviews[position]

            val intent = Intent(context, TripDetailActivity::class.java)
            intent.putExtra("TRIP_DATA", newItem)
            context.startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(listData: List<TripDataDto>) {
        mTripOverviews.clear()
        mTripOverviews.addAll(listData)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: LayoutItemTripsInDayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(newItem: TripDataDto) {
            binding.tvBookingTime.text = newItem.bookingTime?.extractTime()
            binding.tvCanceled.isVisible =
                !(newItem.status == Constants.COMPLETED || newItem.status == Constants.ARRIVE)
            binding.tvTimeStart.text = getHourAndMinute(newItem.startTime ?: "")
            binding.tvTimeEnd.text = getHourAndMinute(newItem.endTime ?: "")
            binding.tvOriginAddress.text = newItem.originAddress
            binding.tvDestinationAddress.text = newItem.destinationAddress
        }
    }
}
