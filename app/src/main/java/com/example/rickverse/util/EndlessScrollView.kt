package com.example.rickverse.util

import androidx.core.widget.NestedScrollView

abstract class EndlessScrollView : NestedScrollView.OnScrollChangeListener {

    private var currentPage = 1

    override fun onScrollChange(
        v: NestedScrollView?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        (v?.getChildAt(v.childCount - 1))?.let { lastChild ->
            if ((scrollY >= (lastChild.measuredHeight - v.measuredHeight)) &&
                scrollY > oldScrollY
            ) {
                currentPage++
                onLoadMore(currentPage)
            }
        }

    }

    abstract fun onLoadMore(page: Int)

}