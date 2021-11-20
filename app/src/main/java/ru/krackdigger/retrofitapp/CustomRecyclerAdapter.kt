package ru.krackdigger.retrofitapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.krackdigger.retrofitapp.databinding.ItemRowBinding


class CustomRecyclerAdapter(private val names: List<String>) :
        RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {

    lateinit var itemBinding: ItemRowBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        itemBinding = ItemRowBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)

        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val url: String = names[position]
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.baseline_download_black_48_2)
            .error(R.drawable.sharp_error_outline_black_48_2)
            .into(itemBinding.imageView)

        itemBinding.imageView.setOnClickListener {

            val intent = Intent(holder.itemView.getContext(), ImageBox::class.java).apply {
                putExtra("message", names[position])
            }
            holder.itemView.getContext().startActivity(intent)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = names.size

    class MyViewHolder(private val itemBinding: ItemRowBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
    }
}