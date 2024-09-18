package com.djhb.petopia.presentation.album

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.djhb.petopia.R

class AlbumReadAdapter(context: Context, private val items: List<String>) : ArrayAdapter<String>(context, 0, items) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.layout_album_read, parent, false)
            val imageView: ImageView = view.findViewById(R.id.album_read_layout_iv)

            Glide.with(context)
                .load(getItem(position)?.toUri())
                .centerCrop()
                .into(imageView)

            return view
        }
    }
