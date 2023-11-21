package com.example.hambaapp

import android.content.Context
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
import com.example.hambaapp.HambaBusiness.BusinessDetail
import com.example.hambaapp.HambaBusiness.BusinessDetailPublic

class FavouriteBusinessAdapter (private val context: Context,
                                private val businessList: MutableList<BusinessDetailPublic> = mutableListOf(),
                                private val onItemClickListener: (Int) -> Unit) :
    RecyclerView.Adapter<FavouriteBusinessAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouriteBusinessAdapter.MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_dashboard_recycle_view, parent, false)
        return MyViewHolder(itemView)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.busTitleRV)
        val businessType: TextView = itemView.findViewById(R.id.busTypeRV)
        val description: TextView = itemView.findViewById(R.id.busDescriptionRV)
        val ivImage: ImageView = itemView.findViewById(R.id.busImageRV)
        val ivFav: ImageView = itemView.findViewById(R.id.ivFavRV)

        init {
            ivImage.setOnClickListener { showPopupMenu(ivImage, adapterPosition) }
            itemView.setOnClickListener { onItemClickListener(adapterPosition) }
            ivFav.setOnClickListener { onItemClickListener(adapterPosition) }
        }
    }

    override fun onBindViewHolder(holder: FavouriteBusinessAdapter.MyViewHolder, position: Int) {
        val likedBusiness = businessList[position]

        holder.title.text = likedBusiness.title
        holder.businessType.text = likedBusiness.category
        holder.description.text = likedBusiness.businessSummary



        // Check if imageString is not null or empty before decoding
        val imageString = likedBusiness.stringImage
        if (!imageString.isNullOrBlank())
        {
            val businessImage = decodeImageFromString(imageString)

            if (businessImage != null)
            {
                holder.ivImage.setImageBitmap(businessImage)
            }
            else
            {

            }
        }
        else
        {

        }
    }

    override fun getItemCount(): Int {
        return businessList.size
    }

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
                    /*
                    val intent = Intent(context, BusinessPrev::class.java)
                    intent.putExtra("position", position)
                    context.startActivity(intent)
                     */
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }

}