package com.rudder.util

import android.widget.Button
import android.widget.CheckBox

class ChangeUIState() {

    companion object {
        fun changeCheckBoxTrueState(checkBox: CheckBox) {
            checkBox.isEnabled = true
            checkBox.isChecked = true
            checkBox.isEnabled = false
        }

        fun changeCheckBoxFalseState(checkBox: CheckBox) {
            checkBox.isEnabled = true
            checkBox.isChecked = false
            checkBox.isEnabled = false
        }

        fun buttonEnable(inputButton: Button, vararg b0: Boolean) {
            var resultBoolean = true
            for (b in b0)
                resultBoolean = resultBoolean && b

            inputButton.isEnabled = resultBoolean
        }
    }
}