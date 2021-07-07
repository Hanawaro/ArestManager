package com.speechpeach.arestmanager.utils.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.core.view.forEach
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationView : BottomNavigationView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun observe(isVisible: LiveData<Boolean>) {
        findViewTreeLifecycleOwner()?.let { owner ->
            isVisible.observe(owner) {
                when(it) {
                    true -> show()
                    false -> hide()
                }
            }
        }
    }

    private fun hide() {

        val params = (this.layoutParams as RelativeLayout.LayoutParams)

        this.menu.forEach { it.isEnabled = false }

        this.animate()
                .setInterpolator(FastOutSlowInInterpolator())
                .setDuration(DURATION)
                .setUpdateListener {
                    (this.layoutParams as RelativeLayout.LayoutParams).setMargins(
                            params.leftMargin,
                            params.topMargin,
                            params.leftMargin,
                            - (this.height * it.animatedFraction).toInt()
                    )
                    this.requestLayout()
                }
                .start()
    }

    private fun show() {

        val params = (this.layoutParams as RelativeLayout.LayoutParams)

//        if (params.bottomMargin == 0)
//            return

        this.animate()
                .setInterpolator(FastOutSlowInInterpolator())
                .setDuration(DURATION)
                .setUpdateListener {
                    (this.layoutParams as RelativeLayout.LayoutParams).setMargins(
                            params.leftMargin,
                            params.topMargin,
                            params.leftMargin,
                            - this.height + (this.height * it.animatedFraction).toInt()
                    )
                    this.requestLayout()
                }
                .withEndAction {
                     this.menu.forEach { it.isEnabled = true }
                }
                .start()
    }

    companion object {
        const val DURATION = 500L
    }
}