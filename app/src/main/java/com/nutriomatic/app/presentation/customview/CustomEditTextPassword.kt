package com.nutriomatic.app.presentation.customview


import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.nutriomatic.app.R
import com.nutriomatic.app.presentation.helper.util.isValidPassword

class CustomEditTextPassword @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {


    private var togglePasswordIcon: Drawable
    private var startIcon: Drawable
    private var isPasswordVisible = false
    private var textInputLayout: TextInputLayout? = null

    //    private var textInputLayoutConfirmationPw: TextInputLayout? = null
//    private var customButton: CustomButton? = null
    private var isValid: Boolean = false
    private var password = ""

    init {
//        customButton?.isEnabled = false
//        setOnTouchListener(this)
//        addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                validatePassword(s?.toString() ?: "")
//                password = s?.toString() ?: ""
//            }
//
//            override fun afterTextChanged(s: Editable?) {}
//        })

        transformationMethod =
            if (!isPasswordVisible) PasswordTransformationMethod.getInstance() else null
        togglePasswordIcon = ContextCompat.getDrawable(
            context,
            if (isPasswordVisible) R.drawable.view_fill else R.drawable.view_hide_fill
        ) as Drawable
        startIcon = ContextCompat.getDrawable(context, R.drawable.lock) as Drawable

//        setButtonDrawables(startOfTheText = startIcon, endOfTheText = togglePasswordIcon)
        setOnTouchListener(this)
        textInputLayout?.isErrorEnabled = false
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isValidPassword(s.toString())) {
                    textInputLayout?.error = context.getString(R.string.minimum_password_length)
                } else {
                    textInputLayout?.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
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

    fun setButton(button: CustomButton) {
//        this.customButton = button
    }

    fun getPassword(): String {
        return this.password
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

//    override fun onTouch(v: View?, event: MotionEvent): Boolean {
//        if ((compoundDrawables[2] != null && layoutDirection == View.LAYOUT_DIRECTION_LTR) ||
//            (compoundDrawables[0] != null && layoutDirection == View.LAYOUT_DIRECTION_RTL)
//        ) {
//            val toggleButtonStart: Float
//            val toggleButtonEnd: Float
//            var isToggleButtonClicked = false
//
//            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
//                toggleButtonEnd = (togglePasswordIcon.intrinsicWidth + paddingStart).toFloat()
//                when {
//                    event.x < toggleButtonEnd -> {
//                        isToggleButtonClicked = true
//                    }
//                }
//            } else {
//                toggleButtonStart =
//                    (width - paddingEnd - togglePasswordIcon.intrinsicWidth).toFloat()
//                when {
//                    event.x > toggleButtonStart -> isToggleButtonClicked = true
//                }
//            }
//            if (isToggleButtonClicked) {
//                when (event.action) {
//                    MotionEvent.ACTION_UP -> {
//                        isPasswordVisible = !isPasswordVisible
//                        togglePasswordIcon = ContextCompat.getDrawable(
//                            context,
//                            if (isPasswordVisible) R.drawable.view_fill else R.drawable.view_hide_fill
//                        ) as Drawable
//                        transformationMethod =
//                            if (!isPasswordVisible) PasswordTransformationMethod.getInstance() else null
//                        setButtonDrawables(
//                            startOfTheText = startIcon,
//                            endOfTheText = togglePasswordIcon
//                        )
//                    }
//
//                    else -> return false
//                }
//            } else return false
//        }
//        return false
//    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null,
    ) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }
}