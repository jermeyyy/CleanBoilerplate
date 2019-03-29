package pl.jermey.clean_boilerplate.util

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.items.ModelAbstractItem

class DataBindingAdapter<MODEL, VH, ITEM : Item<MODEL, VH>> :
    RecyclerView.Adapter<BindingHolder<ViewDataBinding>>()
        where VH : BindingHolder<*> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ViewDataBinding> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: BindingHolder<ViewDataBinding>, position: Int) {

    }


}


open class BindingHolder<Binding : ViewDataBinding>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    val binding: Binding = DataBindingUtil.bind(itemView)
        ?: throw RuntimeException("root is not from an inflated binding layout")
}


class Item<Model, VH : BindingHolder<*>>(
    model: Model,
    layoutRes: Int,
    val viewHolder: (v: View) -> VH,
    private val type: Int = layoutRes
) {

}

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
open class KModelAbstractItem<Model, Item, VH>(
    override var model: Model,
    @param:LayoutRes override val layoutRes: Int,
    val viewHolder: (v: View) -> VH,
    override val type: Int = layoutRes
) : ModelAbstractItem<Model, VH>(model)
        where Item : ModelAbstractItem<Model, VH>,
              VH : androidx.recyclerview.widget.RecyclerView.ViewHolder {
    @SuppressLint("ResourceType")

    override fun getViewHolder(v: View): VH = viewHolder(v)

}