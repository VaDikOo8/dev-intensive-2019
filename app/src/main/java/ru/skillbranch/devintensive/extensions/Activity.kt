package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import ru.skillbranch.devintensive.R


fun Activity.hideKeyboard() {
    val inputManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(this.currentFocus.windowToken, HIDE_NOT_ALWAYS)
    Log.d("M_Activity","hideKeyboard")
}

fun Activity.isKeyboardOpen(): Boolean {
    val r = Rect()
    val rootView = this.findViewById<View>(R.id.root_layout)
    rootView.getWindowVisibleDisplayFrame(r)
    val dm = rootView.resources.displayMetrics

    val heightDiff = rootView.bottom - r.bottom

    return heightDiff > 100 * dm.density
}

fun Activity.isKeyboardClosed(): Boolean = !isKeyboardOpen()

