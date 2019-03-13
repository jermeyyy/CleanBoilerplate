package pl.jermey.clean_boilerplate.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import org.koin.android.viewmodel.ext.android.viewModel
import pl.jermey.clean_boilerplate.viewmodel.ExampleViewModel
import pl.jermey.clean_boilerplate.viewmodel.ExampleViewModel2

class ExampleView : AppCompatActivity() {

    private val viewModel: ExampleViewModel by viewModel()
    private val viewModel2: ExampleViewModel2 by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ExampleActivity", viewModel.getData())
        viewModel2.state.observe(this, Observer {
            when (it) {
                is ExampleViewModel2.State.DataLoaded -> Log.d("ExampleView", "data loaded:${it.data}")
                is ExampleViewModel2.State.Error -> Log.d("ExampleView", "error:${it.throwable}")
            }
        })
        viewModel2.getData()
    }
}