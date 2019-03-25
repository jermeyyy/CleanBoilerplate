package pl.jermey.clean_boilerplate.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.koin.android.viewmodel.ext.android.viewModel
import pl.jermey.clean_boilerplate.R
import pl.jermey.clean_boilerplate.databinding.ExampleActivityBinding
import pl.jermey.clean_boilerplate.viewmodel.ExampleViewModel
import pl.jermey.clean_boilerplate.viewmodel.ExampleViewModel.ExampleEvent.Action.GetData

class ExampleView : AppCompatActivity() {

    private val viewModel: ExampleViewModel by viewModel()

    lateinit var binding: ExampleActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.example_activity)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.invokeAction(GetData)
    }
}
