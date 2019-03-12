package pl.jermey.clean_boilerplate.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.viewmodel.ext.android.viewModel
import pl.jermey.clean_boilerplate.viewmodel.ExampleViewModel

class ExampleView : AppCompatActivity() {

    val viewModel: ExampleViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ExampleActivity", viewModel.getData())
    }
}