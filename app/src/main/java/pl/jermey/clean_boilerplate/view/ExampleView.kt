package pl.jermey.clean_boilerplate.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.andrewgrosner.kbinding.anko.BindingComponent
import org.jetbrains.anko.*
import org.koin.android.viewmodel.ext.android.viewModel
import pl.jermey.clean_boilerplate.R
import pl.jermey.clean_boilerplate.databinding.ExampleActivityBinding
import pl.jermey.clean_boilerplate.viewmodel.ExampleViewModel

class ExampleView : AppCompatActivity() {

    private val viewModel: ExampleViewModel by viewModel()

    lateinit var binding: ExampleActivityBinding
    lateinit var root: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.example_activity)
//        binding.viewModel = viewModel
        val state = viewModel.state.value
        linearLayout {
            orientation = LinearLayout.VERTICAL
            imageView(R.mipmap.ic_launcher)
            textView {
                text = ""
            }.lparams(width = wrapContent, height = wrapContent)
            textView {
                text = ""
            }.lparams(width = wrapContent, height = wrapContent)
        }

        viewModel.state.observe(this, Observer { state -> })
        viewModel.getData()
    }
}

//class HomeActivityItemComponent : BindingComponent<ViewGroup, ExampleViewModel>() {
//
//    override fun createViewWithBindings(ui: AnkoContext<ViewGroup>) = with(ui) {
//        linearLayout {
//            textView {
//                bind {(it.state.value as ExampleViewModel.ExampleState.DataLoaded).data.toString()}
//                bindSelf(ExampleViewModel::state) { it.state }.toText(this)
//                padding = dip(12)
//                textSize = 16.0f
//                textColor = Color.BLACK
//            }.lparams {
//                width = MATCH_PARENT
//                height = WRAP_CONTENT
//            }
//        }
//    }
//}