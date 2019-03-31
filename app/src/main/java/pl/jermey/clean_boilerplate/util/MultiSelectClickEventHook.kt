package pl.jermey.clean_boilerplate.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter.select.SelectExtension


@Suppress("UNCHECKED_CAST")
class MultiSelectClickEventHook<VH : BindingHolder<*>, ITEM : AbstractItem<VH>>(
    private val binder: (VH) -> View?
) : ClickEventHook<ITEM>() {

    override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
        return (viewHolder as? VH)?.let { binder(it) }
    }

    override fun onClick(v: View, position: Int, fastAdapter: FastAdapter<ITEM>, item: ITEM) {
        val selectExtension = fastAdapter.getExtension<SelectExtension<ITEM>>(SelectExtension::class.java)
            ?: return
        if (item.isSelected) {
            selectExtension.deselect(position)
        } else {
            selectExtension.select(position)
        }
        fastAdapter.notifyItemChanged(position)
    }

}