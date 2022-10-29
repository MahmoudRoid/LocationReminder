package mahmoudroid.locationreminder.ext

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment

//region keyboard extensions

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

fun Activity.hideKeyboard() {
    hideKeyboard(window.decorView.rootView)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

/*fun BaseBottomSheet.hideKeyboard() {
    activity?.hideKeyboard()
}*/

fun Fragment.showKeyboard() {
    activity?.showKeyboard()
}

fun Activity.showKeyboard() {
    showKeyboard(window.decorView.rootView)
}

fun Context.showKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInputFromWindow(view.windowToken,
        InputMethodManager.SHOW_FORCED,
        0)
}

fun Activity.isSoftKeyboardShown() : Boolean {
    return try {
        val contentView =  window.decorView.rootView
        val r = Rect()
        contentView.getWindowVisibleDisplayFrame(r)
        val screenHeight = contentView.rootView.height
        // r.bottom is the position above soft keypad or device button.
        // if keypad is shown, the r.bottom is smaller than that before.
        val keypadHeight = screenHeight - r.bottom
        keypadHeight > screenHeight * 0.15

    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

//endregion keyboard extensions

//region navigation component

fun <T> Fragment.getNavigationResult(key: String = "result") = NavHostFragment.findNavController(this).currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
fun <T> Fragment.removeNavigationResult(key: String = "result") = NavHostFragment.findNavController(this).currentBackStackEntry?.savedStateHandle?.remove<T>(key)
fun <T> Fragment.setNavigationResult(key: String = "result", result: T) { NavHostFragment.findNavController(this).previousBackStackEntry?.savedStateHandle?.set(key, result) }

fun NavController.navigateSafe(direction: NavDirections) { currentDestination?.getAction(direction.actionId)?.let { navigate(direction) } }
fun NavController.navigateSafe(direction: NavDirections, navOptions: NavOptions?) { currentDestination?.getAction(direction.actionId)?.let { navigate(direction.actionId, direction.arguments, navOptions) }}

//endregion