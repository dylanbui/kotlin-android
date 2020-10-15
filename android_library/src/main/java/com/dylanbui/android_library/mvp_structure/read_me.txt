

// Cach su dung DbBaseDialogFragment don gian
// Su dung: DbBaseMvpDialogFragment<[view], [presenter]]>() neu can dung MVP


interface AddListingNoteListener {
    fun addNote(content: String)
}

class AddListingNoteDialog: DbBaseDialogFragment() {

    override val inflateViewLayoutId = R.layout.view_dialog_note_add_update

    companion object {

        private var addListingNoteListener: AddListingNoteListener? = null

        fun instance(listener: AddListingNoteListener? = null): AddListingNoteDialog {
            addListingNoteListener = listener
            // val a = Dialog(this.activity, DbAddListingNoteDialog::class.java)
            // args.putInt(MEETING_ID, meetingId)
            val dialog = AddListingNoteDialog()
            dialog.arguments = Bundle()
//            items.forEach { it.isChecked = false }
//            dialog.reasonItems = items
            return dialog
        }
    }

    // Run 1
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // dLog("=>> onAttach(context: Context)")
    }
    // Run 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // dLog("=>> onCreate(savedInstanceState: Bundle?)")
    }
    // Run 3
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // dLog("=>> onViewCreated(view: View, savedInstanceState: Bundle?)")

        view.headerTitleView.text = "Nhập ghi chú"
        //getString(R.string.meeting_reject_reason_header_title)

        view.btnClose.onClick {
            dismiss()
        }

        view.btnSave.onClick {
            onSaveButtonClicked()
        }
    }
    // Run 4
    override fun onDestroyView() {
        super.onDestroyView()
        // dLog("=>> onDestroyView()")
    }
    // Run 5
    override fun onDestroy() {
        super.onDestroy()
        // dLog("=>> onDestroy()")
    }

    private fun onSaveButtonClicked() {

        val content = view?.edtTypeNote?.text.toString()
        if (content.isEmpty()) {
            showToast("Bạn cần phải nhập nội dung.")
            return
        }
        addListingNoteListener?.addNote(content)
        dismiss()
    }
}