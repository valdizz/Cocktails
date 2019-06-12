package com.valdizz.cocktails.ui.cocktails

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.valdizz.cocktails.R
import com.valdizz.cocktails.common.Constants

/**
 * Dialog fragment to select the type of filtering cocktails.
 *
 * @author Vlad Kornev
 */
class TypeDialogFragment : DialogFragment() {

    companion object {
        const val SELECTED_TYPE_TAG = "selected_type_num"
        const val SELECTED_TYPE_ARGS = "selected_type_args"

        fun newInstance(selectedType: Int) = TypeDialogFragment().apply {
            arguments = bundleOf(SELECTED_TYPE_ARGS to selectedType)
        }
    }

    private val types = arrayOf(Constants.TYPE_ALCOHOLIC, Constants.TYPE_NON_ALCOHOLIC, Constants.TYPE_OPTIONAL_ALCOHOL)
    private var selectedType = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.getInt(SELECTED_TYPE_ARGS)?.let {
            selectedType = it
        }
        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.select_type))
            .setCancelable(false)
            .setSingleChoiceItems(types, selectedType) { _, which ->
                selectedType = which
            }
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val intent = Intent().apply {
                    putExtra(SELECTED_TYPE_TAG, selectedType)
                }
                targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                dialog.cancel()
            }
            .create()
    }
}