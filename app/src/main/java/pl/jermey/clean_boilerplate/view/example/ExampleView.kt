package pl.jermey.clean_boilerplate.view.example

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.jermey.clean_boilerplate.R
import pl.jermey.clean_boilerplate.databinding.ExampleActivityBinding
import pl.jermey.clean_boilerplate.view.example.ExampleViewModel.ExampleEvent.Action.GetData

class ExampleView : AppCompatActivity() {

  private val viewModel: ExampleViewModel by viewModel()

  lateinit var binding: ExampleActivityBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.example_activity)
    binding.lifecycleOwner = this
    binding.viewModel = viewModel

    viewModel.invokeAction(GetData)
    viewModel.event.observe(this, Observer {
      Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    })
  }
}
