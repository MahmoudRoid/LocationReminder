package mahmoudroid.locationreminder.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import mahmoudroid.locationreminder.R

class BaseBottomSheet: BottomSheetDialogFragment() {

    var preventTouchOutsideToDismissSheet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.bottomSheetStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val bottomSheetDialog: BottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val frameLayout: FrameLayout? = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            if (frameLayout != null) {
                val bottomSheetBehavior = BottomSheetBehavior.from(frameLayout)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.isHideable = false
            }
        }
        return bottomSheetDialog
    }

    /** prevent touch outside of bottomsheet **/
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (preventTouchOutsideToDismissSheet) {
            val touchOutsideView = dialog!!.window!!.decorView.findViewById<View>(com.google.android.material.R.id.touch_outside)
            touchOutsideView.setOnClickListener(null)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun dismissBottomSheet() {
        try {
            if (this.isVisible) dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}