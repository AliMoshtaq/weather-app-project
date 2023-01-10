package co.develhope.meteoapp.ui.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import co.develhope.meteoapp.databinding.FragmentErrorBinding

class ErrorScrFragment : DialogFragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.ThemeOverlay_Material_Light)
    }

    private lateinit var binding: FragmentErrorBinding
    private var performAction: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme){
            @Deprecated("Deprecated in Java")
            override fun onBackPressed() {
            }

            override fun setCancelable(flag: Boolean) {
                super.setCancelable(false)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentErrorBinding.inflate(inflater, container, false)
        binding.btnTryAgain.setOnClickListener {
            performAction?.invoke()
            dismiss()
        }
        binding.btnCloseApp.setOnClickListener {
            dismiss()
        }
        return binding.root
    }


    companion object {
        private const val FRAGMENT_TAG = "gift_dialog"

        fun show(fragmentManager: FragmentManager, performAction:() -> Unit): ErrorScrFragment {
            val dialog = ErrorScrFragment()
            dialog.performAction = performAction
            dialog.show(fragmentManager, FRAGMENT_TAG)
            return dialog
        }
    }
}