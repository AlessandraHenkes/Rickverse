package com.example.rickverse.util.extension

import android.widget.RadioGroup

fun RadioGroup.enabledChildren(isEnabled: Boolean) {
    if (childCount == 0) return
    for (x in 0 until childCount) {
        getChildAt(x).isEnabled = isEnabled
    }
}
