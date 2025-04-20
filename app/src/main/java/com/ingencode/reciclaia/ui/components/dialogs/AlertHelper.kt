package com.ingencode.reciclaia.ui.components.dialogs

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ingencode.reciclaia.R

/**
Created with ❤ by jesusmarvaz on 2025-04-09.
 */

object AlertHelper {
    enum class Type { Error, Success }

    class BottomAlertDialog private constructor(private val alert: BottomSheetDialog, private val delay: Long, private val action: (() -> Unit)) {
        fun show() {
            if (delay > 0) {
                object : CountDownTimer(this.delay, this .delay) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() { alert.dismiss() }
                }.start()
            }
            alert.show()
        }

        class Builder(private val c: Context, private val type: Type, private val message: String) {
            private var action: (()->Unit)? = null
            private var delay: Long = 2000
            private lateinit var alert: BottomSheetDialog

            fun setAction(action: (()->Unit)): Builder {
                this.action = action
                return this@Builder
            }

            /**
             * Passing a [delay] = 0 means infinite lasting
             * @return the builder
             */
            fun setDelay(delay: Long): Builder {
                if (delay < 0) this.delay = 0
                else this.delay = delay
                return this@Builder
            }

            fun build(): BottomAlertDialog {
                val alertView: View
                val textViewAlert: TextView
                val actionButton: AppCompatImageView
                val style: Int

                when (type) {
                    Type.Success -> {
                        style = R.style.SuccessBottomSheetTheme
                        alertView = LayoutInflater.from(c).inflate(R.layout.bottom_sheet_success, null).also {
                            textViewAlert = it.findViewById(R.id.success_bottom_sheet_text)
                            actionButton = it.findViewById(R.id.success_bottom_sheet_action)
                        }
                    }
                    Type.Error -> {
                        style = R.style.ErrorBottomSheetTheme
                        alertView = LayoutInflater.from(c).inflate(R.layout.bottom_sheet_error, null).also {
                            textViewAlert = it.findViewById(R.id.error_bottom_sheet_text)
                            actionButton = it.findViewById(R.id.error_bottom_sheet_action)
                        }
                    }
                }
                textViewAlert.text = message

                fun actionDismissing() = run {
                    action?.invoke()
                    alert.dismiss()
                }

                this.alert = BottomSheetDialog(c, style).apply {
                    setContentView(alertView)
                    actionButton.setOnClickListener { actionDismissing() }
                }
                return BottomAlertDialog(alert, delay, ::actionDismissing)
            }
        }
    }

    class Dialog private constructor(private val dialog: AlertDialog, private val c: Context) {
        fun show() = dialog.show()

        class Builder(private val c: Context, private val title: String, private val message: String, private val positiveAction: () -> Unit) {
            private lateinit var dialog: AlertDialog
            private var negativeAction: (() -> Unit)? = null
            private var positiveText: String = c.getString(R.string.ok)
            private var negativeText: String = c.getString(R.string.cancel)

            fun setNegativeAction(action: () -> Unit): Builder {
                this.negativeAction = action
                return this
            }

            fun setPositiveText(text: String): Builder {
                this.positiveText = text
                return this
            }

            fun setNegativeText(text: String): Builder {
                this.negativeText = text
                return this
            }

            fun build(): Dialog {
                val alertBuilder = AlertDialog.Builder(c, R.style.MyAlertDialog)
                alertBuilder.setTitle(title).setMessage(message)
                    .setPositiveButton(positiveText) { _,_ -> positiveAction.invoke(); dialog.dismiss() }
                if (negativeAction != null) {
                    alertBuilder.setNegativeButton(negativeText) { _,_-> negativeAction?.invoke(); dialog.dismiss() }
                }
                dialog = alertBuilder.create()
                return Dialog(dialog, c)
            }
        }
    }
}