package co.develhope.meteoapp.ui.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import co.develhope.meteoapp.R

class QuiteAppFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder
            .setTitle(getString(R.string.dialog_quit))
            .setMessage(getString(R.string.dialog_message))
            .setPositiveButton(getString(R.string.dialog_positive)){ _, _ -> activity?.finish()}
            .setNegativeButton(getString(R.string.dialog_negative)){ dialog, _ -> dialog.dismiss()}
        return builder.create()

    }


}