package com.berkecoban.casestudy.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.berkecoban.R
import com.berkecoban.databinding.RowItemBinding
import com.berkecoban.casestudy.model.Post
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText


class RecycleAdapter constructor(con: Context) :
    RecyclerView.Adapter<RecycleAdapter.CustomViewHolder>() {

    private var mContext = con
    private val differCallback = object : DiffUtil.ItemCallback<Post>() {


        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem

        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            RowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {


        val post = differ.currentList[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class CustomViewHolder(binding: RowItemBinding) : RecyclerView.ViewHolder(binding.root) {

        var layout: LinearLayout = binding.layout
        var titleTw: TextView = binding.title
        var descTw: TextView = binding.desc
        var image: ImageView = binding.image


        fun bind(post: Post?) {


            if (post != null) {
                post?.let { element ->
                    titleTw.text = element.title
                    descTw.text = element.description
                    Glide.with(mContext).load(element.imageUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(image)


                    layout.setOnClickListener {

                        Log.d("", "")

                        val bottomSheetDialog = BottomSheetDialog(mContext)
                        bottomSheetDialog.setContentView(R.layout.bottom_sheet)


                        val button: MaterialButton? = bottomSheetDialog.findViewById(R.id.save)
                        val title: TextInputEditText? =
                            bottomSheetDialog.findViewById(R.id.edit_title)
                        val desc: TextInputEditText? =
                            bottomSheetDialog.findViewById(R.id.edit_desc)


                        button?.setOnClickListener {
                            differ.currentList[adapterPosition].title = title?.text.toString()
                            differ.currentList[adapterPosition].description = desc?.text.toString()
                            bottomSheetDialog.dismiss()
                            differ.submitList(differ.currentList)
                            notifyDataSetChanged()
                        }

                        bottomSheetDialog.show()
                    }
                }

            }

        }
    }
}
