package com.udeldev.storyapp.component.edit

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udeldev.storyapp.R

class EmailEditText : AppCompatEditText {
    private var _message = MutableLiveData<String?>()
    var message: LiveData<String?> = _message

    constructor(context: Context) : super(context) {
        initComponent()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initComponent()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initComponent()
    }

    private fun initComponent() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                _message.value = validEmail(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun validEmail(email: String): String? {
        if (email.isEmpty()) return resources.getString(R.string.empty_text)
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return resources.getString(R.string.invalid_email)
        return null
    }
}