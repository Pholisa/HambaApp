package com.example.hambaapp.HambaBusiness

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.hambaapp.R
import android.content.Context
import android.content.Intent


class MyBusinessAdapter(
    private val context: Context,
    private val birdList: MutableList<BusinessDetail> = mutableListOf(),
    private val onDeleteClickListener: (Int) -> Unit,
    private val onItemClickListener: (Int) -> Unit // Change the type to accept a String parameter
) : RecyclerView.Adapter<MyBusinessAdapter.MyViewHolder>() {

    //Grabbing objects from Recycler viewer
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvBusinessName)
        val location: TextView = itemView.findViewById(R.id.tvBusinessAddress)
        val businessPrice: TextView = itemView.findViewById(R.id.tvBusinessPrice)
        val businessSummary: TextView = itemView.findViewById(R.id.tvBusinessSummary)
        val btnMore: ImageView = itemView.findViewById(R.id.btnMore)
        val ivImage: ImageView = itemView.findViewById(R.id.ivImage)

        init {
            btnMore.setOnClickListener { showPopupMenu(btnMore, adapterPosition) }
            itemView.setOnClickListener { onItemClickListener(adapterPosition) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_business_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentBird = birdList[position]

        holder.title.text = currentBird.title
        holder.location.text = currentBird.location
        holder.businessPrice.text = "R"+ currentBird.price
        holder.businessSummary.text = currentBird.businessSummary

        // Check if imageString is not null or empty before decoding
        val imageString = currentBird.stringImage
       // onItemClickListener(position, currentBird.stringImage) // Pass both position and imageString to onItemClickListener

        if (!imageString.isNullOrBlank())
        {
            val businessImage = decodeImageFromString(imageString)

            if (businessImage != null)
            {
                holder.ivImage.setImageBitmap(businessImage)
            }
            else
            {
                // Handle the case when the image cannot be decoded
                // You can set a placeholder image or show an error message
            }
        }
        else
        {
            // Set a default image or handle the case when imageString is null or empty
        }
    }

    override fun getItemCount(): Int {
        return birdList.size
    }

    // Function to decode image from base64 string
    private fun decodeImageFromString(imageString: String?): Bitmap?
    {
        try {
            val decodedBytes: ByteArray = Base64.decode(imageString, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            // Handle the decoding error (e.g., log the error)
            e.printStackTrace()
        }
        return null
    }

    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(context, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.business_more_options, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                  Toast.makeText(context, "Edit clicked for item at position $position", Toast.LENGTH_SHORT).show()

                    true
                }
                R.id.menu_delete -> {
                    // Handle delete option
                    onDeleteClickListener(position)
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

}


