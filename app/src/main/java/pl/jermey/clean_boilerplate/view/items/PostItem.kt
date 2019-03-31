package pl.jermey.clean_boilerplate.view.items

import android.view.View
import pl.jermey.clean_boilerplate.R
import pl.jermey.clean_boilerplate.databinding.PostItemBinding
import pl.jermey.clean_boilerplate.util.BindingHolder
import pl.jermey.clean_boilerplate.util.KModelAbstractItem
import pl.jermey.domain.model.example.Post

class PostItem(model: Post) :
    KModelAbstractItem<Post, PostItem, PostItem.PostViewHolder>(
        model,
        R.layout.post_item,
        ::PostViewHolder
    ) {

    override fun bindView(holder: PostViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)
        holder.binding.model = model
        holder.binding.radio.isChecked = isSelected
    }
    override var identifier: Long = model.id

    class PostViewHolder(itemView: View) : BindingHolder<PostItemBinding>(itemView)

}