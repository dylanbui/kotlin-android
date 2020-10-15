

Base on : https://github.com/miquelbeltran/conductor-viewmodel


// User DbResource
viewModel.characters.observe(viewLifecycleOwner, Observer {
	when (it.status) {
		DbResource.Status.SUCCESS -> {
			progress_bar.visibility = View.GONE
			if (!it.data.isNullOrEmpty()) adapter.setItems(ArrayList(it.data))
		}
		DbResource.Status.ERROR ->
		Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()

		DbResource.Status.LOADING ->
		progress_bar.visibility = View.VISIBLE
	}
})


// 1. Create a new Controller by extending the ViewModelController.
class MyViewModelController : ViewModelController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_viewmodel, container, false)

        // 2. Obtain your ViewModel using the viewModelProvider() method
        val viewModel = viewModelProvider().get(MyViewModel::class.java)

        // 2.1 You can also provide a factory to viewModelProvider
        // val viewModel = viewModelProvider(factory).get(MyViewModel::class.java)

        // 3. Observe your LiveData
        viewModel.getLiveData().observe(this, Observer<String> {
            //
        })

        return view
    }
}