
Base : https://github.com/jjobes/SlideDateTimePicker
Fix for AndroidX : https://github.com/jjobes/SlideDateTimePicker/pull/46


Demo

    private val mFormatter = SimpleDateFormat("MMMM dd yyyy hh:mm aa")
    private val listener: SlideDateTimeListener = object : SlideDateTimeListener() {
        override fun onDateTimeSet(date: Date) {
            Toast.makeText(this@LoginViewController.activity,
                mFormatter.format(date).toString(), Toast.LENGTH_SHORT).show()
        }

        // Optional cancel listener
        override fun onDateTimeCancel() {
            Toast.makeText(this@LoginViewController.activity, "Canceled", Toast.LENGTH_SHORT).show()
        }
    }

    @OnClick(R.id.btnDatetime)
    internal fun doDatetime() {

        val act = activity as AppCompatActivity

        SlideDateTimePicker.Builder(act.supportFragmentManager)
            .setListener(listener)
            // .setPickerType(SlideDateTimePicker.PickerType.ONLY_DATE)
            .setInitialDate(Date())
            //.setMinDate(minDate)
            //.setMaxDate(maxDate)
            //.setIs24HourTime(true)
            //.setTheme(SlideDateTimePicker.HOLO_DARK)
            //.setIndicatorColor(Color.parseColor("#990000"))
            .build()
            .show()
    }