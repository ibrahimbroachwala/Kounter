package kounter.apps.ib.kounter.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_count.view.*
import kounter.apps.ib.kounter.utils.GetTime
import kounter.apps.ib.kounter.R
import kounter.apps.ib.kounter.db.Count
import kounter.apps.ib.kounter.utils.Theme
import kounter.apps.ib.kounter.utils.Theme.Companion.setTheme
import kounter.apps.ib.kounter.utils.Themes

class CountsAdapter(val context: Context, val searchResult: List<Count>, val listener: ItemClick) : androidx.recyclerview.widget.RecyclerView.Adapter<CountViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountViewHolder = CountViewHolder(parent.inflate(R.layout.item_count))

    override fun getItemCount(): Int = searchResult.size

    override fun onBindViewHolder(holder: CountViewHolder, position: Int) = holder.bind(searchResult[position], listener)
}

interface ItemClick {

    fun updateCount(item: Count)

    fun deleteCount(item: Count)
}


class CountViewHolder(itemView: View?) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView!!) {

    init {
        setEditTextTheme()
    }

    private fun setEditTextTheme() = with(itemView) {

        if(Theme.selectedTheme == Themes.DARK){
            item_name.setTextColor(context.resources.getColor(R.color.edittext_selector_light))
            item_count_text.setTextColor(context.resources.getColor(R.color.edittext_selector_light))
        }else{
            item_name.setTextColor(context.resources.getColor(R.color.edittext_selector_dark))
            item_count_text.setTextColor(context.resources.getColor(R.color.edittext_selector_dark))
        }

    }

    fun bind(item: Count, listener: ItemClick) = with(itemView) {

        var count = item.count

        setItemSelected(item)


        item_count_text.setText(item.count.toString())
        item_name.setText(item.name)
        item_date.text = GetTime.getTime(item.timestamp)


        item_count_up.setOnClickListener {
            item_count_text.setText((++count).toString())
        }

        item_count_down.setOnClickListener {
            if (count - 1 > -1)
                item_count_text.setText((--count).toString())

        }

        item_count_delete.setOnClickListener {
            listener.deleteCount(item)
        }

        item_count_update.setOnClickListener {

            item.name = item_name.text.toString()
            item.count = Integer.valueOf(item_count_text.text.toString())

            listener.updateCount(item)
        }

        setOnClickListener {
            setItemSelected(item)
        }

    }


    fun setItemSelected(item: Count) = with(itemView) {
        if (!item.selected) {

            item.selected = true
            item_count_up.visibility = View.VISIBLE
            item_count_down.visibility = View.VISIBLE
            item_count_delete.visibility = View.VISIBLE
            item_count_update.visibility = View.VISIBLE
            item_count_text.isEnabled = true
            item_name.isEnabled = true

        } else {

            item.selected = false
            item_count_up.visibility = View.GONE
            item_count_down.visibility = View.GONE
            item_count_delete.visibility = View.GONE
            item_count_update.visibility = View.GONE
            item_count_text.isEnabled = false
            item_name.isEnabled = false
        }
    }


}


fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}
