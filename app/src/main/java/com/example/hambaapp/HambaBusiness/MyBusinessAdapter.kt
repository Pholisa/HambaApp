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
    private val businessList: MutableList<BusinessDetail> = mutableListOf(),
    private val onDeleteClickListener: (Int) -> Unit,
    private val onItemClickListener: (BusinessDetail) -> Unit
) : RecyclerView.Adapter<MyBusinessAdapter.MyViewHolder>() {

    //Grabbing objects from Recycler viewer
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvBusinessName)
        val location: TextView = itemView.findViewById(R.id.tvBusinessAddress)
        val businessPrice: TextView = itemView.findViewById(R.id.tvBusinessPrice)
        val businessSummary: TextView = itemView.findViewById(R.id.tvBusinessSummary)
        val btnMore: ImageView = itemView.findViewById(R.id.btnMore)
        val ivImage: ImageView = itemView.findViewById(R.id.ivImage)

        //button more on click listner
        init {
            btnMore.setOnClickListener { showPopupMenu(btnMore, adapterPosition) }
        }
    }

    //----------------------------------------------------------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_business_item, parent, false)
        return MyViewHolder(itemView)
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //assigning a value to each holder on recycler
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentBusiness = businessList[position]

        holder.title.text = currentBusiness.title
        holder.location.text = currentBusiness.location
        holder.businessPrice.text = "R"+ currentBusiness.price
        holder.businessSummary.text = currentBusiness.businessSummary

        // Check if imageString is not null or empty before decoding
        val imageString = currentBusiness.stringImage

        if (!imageString.isNullOrBlank())
        {
            val businessImage = decodeImageFromString(imageString)

            if (businessImage != null)
            {
                holder.ivImage.setImageBitmap(businessImage)
            }
            else
            {
                //placement picture
            }
        }
        else
        {
            // Set a default image
        }

        //item click listener handler that passes image to display on bottom sheet
        holder.itemView.setOnClickListener {
            onItemClickListener(currentBusiness)
        }

    }

    //----------------------------------------------------------------------------------------------
    //getting the size of businesses
    override fun getItemCount(): Int
    {
        return businessList.size
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
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
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //pop up menu which will give user the option to either delete or edit business data
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
    //----------------------------------------------------------------------------------------------
}
//------------------------------------------ooo000EndOfFile000ooo-----------------------------------