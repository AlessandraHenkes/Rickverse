package com.example.rickverse.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.rickverse.R
import kotlinx.android.synthetic.main.title.view.*

class Title @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.title, this, true)
        attrs?.let { attributeSet ->
            context.obtainStyledAttributes(attributeSet, R.styleable.Title).run {
                view.tvTitle.text = getString(R.styleable.Title_text)
                recycle()
            }
        }
    }

}
