package com.example.xepartnerapp.features.driver.menu.support

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.xepartnerapp.R
import com.example.xepartnerapp.databinding.ActivityDriverFaqBinding

class FAQDriverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDriverFaqBinding

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        binding.llAccountItem1.setOnClickListener {
            Toast.makeText(this, getString(R.string.WebSupport), Toast.LENGTH_SHORT).show()
        }
        binding.llAccountItem2.setOnClickListener {
            Toast.makeText(this, getString(R.string.WebSupport), Toast.LENGTH_SHORT).show()
        }
        binding.llAccountItem3.setOnClickListener {
            Toast.makeText(this, getString(R.string.WebSupport), Toast.LENGTH_SHORT).show()
        }
        binding.llPaymentItem1.setOnClickListener {
            Toast.makeText(this, getString(R.string.WebSupport), Toast.LENGTH_SHORT).show()
        }
        binding.llPaymentItem2.setOnClickListener {
            Toast.makeText(this, getString(R.string.WebSupport), Toast.LENGTH_SHORT).show()
        }
        binding.llPaymentItem3.setOnClickListener {
            Toast.makeText(this, getString(R.string.WebSupport), Toast.LENGTH_SHORT).show()
        }
        binding.llPaymentItem4.setOnClickListener {
            Toast.makeText(this, getString(R.string.WebSupport), Toast.LENGTH_SHORT).show()
        }
        binding.llPaymentItem5.setOnClickListener {
            Toast.makeText(this, getString(R.string.WebSupport), Toast.LENGTH_SHORT).show()
        }
    }
}
