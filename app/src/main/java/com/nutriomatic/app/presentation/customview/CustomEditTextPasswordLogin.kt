package com.nutriomatic.app.presentation.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout

class CustomEditTextPasswordLogin @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var textInputLayout: TextInputLayout? = null

    //    private var textInputLayoutConfirmationPw: TextInputLayout? = null
//    private var customButton: CustomButtonLogin? = null
    private var isValid: Boolean = false

    init {
//        customButton?.isEnabled = false
        setOnTouchListener(this)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

//    fun setEnableButton() {
//        customButton?.isEnabled = true
//    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    fun setTextInputLayout(textInputLayout: TextInputLayout) {
        this.textInputLayout = textInputLayout
    }

//    fun setTextInputLayoutConfirmationPw(textInputLayout: TextInputLayout) {
//        this.textInputLayoutConfirmationPw = textInputLayout
//    }

    fun getButtonIsValid(): Boolean {
        return this.isValid
    }

    fun setButton(button: CustomButtonLogin) {
//        this.customButton = button
    }


    fun validatePassword(password: String) {
        if (password.length < 8) {
//            customButton?.isEnabled = false
            isValid = false
            textInputLayout?.error = "Password must be than 8 character"
        } else {
            textInputLayout?.isErrorEnabled = false
            isValid = true
//            customButton?.isEnabled = true
        }
    }

//    fun confirmationPassword(password: String, confirmationPw: String) {
//        if (!password.equals(confirmationPw)) {
//            customButton?.isEnabled = false
//            isValid = false
//            textInputLayoutConfirmationPw?.error = "Password not same"
//
//        } else {
//            textInputLayoutConfirmationPw?.isErrorEnabled = false
//            isValid = true
//            customButton?.isEnabled = true
//        }
//    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//        if (textInputLayout?.isErrorEnabled == true) {
//            textInputLayout?.isErrorEnabled = false
//            textInputLayoutConfirmationPw?.isErrorEnabled = false
//        }
        return false
    }
}