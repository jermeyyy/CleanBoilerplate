package pl.jermey.clean_boilerplate.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import org.koin.android.viewmodel.ext.android.viewModel
import pl.jermey.clean_boilerplate.R
import pl.jermey.clean_boilerplate.databinding.ExampleActivityBinding
import pl.jermey.clean_boilerplate.viewmodel.ExampleViewModel

class ExampleView : AppCompatActivity() {

    private val viewModel: ExampleViewModel by viewModel()

    lateinit var binding: ExampleActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.example_activity)
        binding.viewModel = viewModel
        viewModel.state.observe(this, Observer {
            when (it) {
                is ExampleViewModel.ExampleState.DataLoaded -> Log.d("ExampleView", "title loaded:${it.data}")
                is ExampleViewModel.ExampleState.Error -> Log.d("ExampleView", "error:${it.throwable}")
            }
        })
        viewModel.getData()
    }
}