package com.example.rickverse.util.extension

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.hideError() {
    this.error = null
}

fun TextInputLayout.showError(message: String?) {
    if (message == null) return
    this.error = message
}
