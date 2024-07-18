package br.edu.ifsp.arq.ads.dmo.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.TextView
import br.edu.ifsp.arq.ads.dmo.R

class DialogLoading(private val context: Context) {
    private var dialog: Dialog? = null

    fun showDialog(title: String) {
        dialog = Dialog(context)
        dialog?.setContentView(R.layout.dialog_loading)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)

        val titleTextView = dialog?.findViewById<TextView>(R.id.textViewLoading)
        titleTextView?.text = title

        dialog?.create()
        dialog?.show()
    }

    fun hideDialog() {
        dialog?.dismiss()
    }
}