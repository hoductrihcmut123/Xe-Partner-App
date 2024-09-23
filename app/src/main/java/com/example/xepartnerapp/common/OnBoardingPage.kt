package com.example.xepartnerapp.common

import android.content.Context
import androidx.annotation.DrawableRes
import com.example.xepartnerapp.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title: Int,
    val description: Int
) {
    object First : OnBoardingPage(
        image = R.drawable.onboarding_first,
        title = R.string.Title1,
        description = R.string.Description1
    )

    object Second : OnBoardingPage(
        image = R.drawable.onboarding_second,
        title = R.string.Title2,
        description = R.string.Description2
    )

    object Third : OnBoardingPage(
        image = R.drawable.onboarding_third,
        title = R.string.Title3,
        description = R.string.Description3
    )

    fun getTitle(context: Context): String {
        return context.getString(title)
    }

    fun getDescription(context: Context): String {
        return context.getString(description)
    }
}
