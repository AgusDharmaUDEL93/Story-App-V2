package com.udeldev.storyapp.component.edit

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udeldev.storyapp.R

class PasswordEditText : AppCompatEditText {

    private var _message = MutableLiveData<String?>()
    var message: LiveData<String?> = _message

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                _message.value = validPassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun validPassword(password: String): String? {
        if (password.isEmpty()) return R.string.empty_text.toString()
        if (password.length < 8) return R.string.password_less.toString()
        return null
    }
}