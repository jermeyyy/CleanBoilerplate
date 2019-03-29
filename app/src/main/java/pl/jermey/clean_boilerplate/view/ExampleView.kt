package pl.jermey.clean_boilerplate.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ModelAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.jermey.clean_boilerplate.R
import pl.jermey.clean_boilerplate.databinding.ExampleActivityBinding
import pl.jermey.clean_boilerplate.view.items.PostItem
import pl.jermey.clean_boilerplate.viewmodel.ExampleViewModel
import pl.jermey.clean_boilerplate.viewmodel.ExampleViewModel.ExampleEvent.Action.GetData
import pl.jermey.domain.model.example.Post


class ExampleView : AppCompatActivity() {

    private val viewModel: ExampleViewModel by viewModel()

    lateinit var binding: ExampleActivityBinding

    val itemAdapter = ModelAdapter { model: Post ->
        PostItem(model)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.example_activity)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.adapter = FastAdapter.with(itemAdapter)

        viewModel.invokeAction(GetData)
        viewModel.items.observe(this, Observer { data ->
            FastAdapterDiffUtil[itemAdapter] = data.map { PostItem(it) }
        })
    }
}
