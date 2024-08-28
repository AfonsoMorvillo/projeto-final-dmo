package br.edu.ifsp.arq.ads.dmo.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import br.edu.ifsp.arq.ads.dmo.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ActionBottomSheetFragment : BottomSheetDialogFragment() {

    interface OnActionSelectedListener {
        fun onActionSelected(action: String)
    }

    private var listener: OnActionSelectedListener? = null

    fun setOnActionSelectedListener(listener: OnActionSelectedListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_action_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btn_add_group).setOnClickListener {
            dismiss()
            listener?.onActionSelected("ADD_GROUP")
        }

        view.findViewById<Button>(R.id.btn_view_groups).setOnClickListener {
            dismiss()
            listener?.onActionSelected("JOIN_GROUP")
        }
    }
}
