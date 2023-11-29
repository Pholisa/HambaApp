package com.example.hambaapp.HambaAdmin

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
import com.example.hambaapp.HambaBusiness.Information
import com.example.hambaapp.R
import com.google.firebase.database.DatabaseReference

class AdminAdapter(
    private val context: Context,
    private val businessList: MutableList<Information> = mutableListOf(),
    private val onApproveClickListener: (Information,Int) -> Unit,
    private val onDeleteClickListener: (Int) -> Unit,
    private val onItemClickListener: (Information) -> Unit,
    private val approvedRequestsReference: DatabaseReference,
) : RecyclerView.Adapter<AdminAdapter.MyViewHolder>() {

    //Grabbing objects from Recycler viewer
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val companyName: TextView = itemView.findViewById(R.id.tvBusinessNameCurrentList)
        val registerNumber: TextView = itemView.findViewById(R.id.tvRegisterNumberCurrentList)
        val emailAddress: TextView = itemView.findViewById(R.id.tvEmailAddressCurrentList)
        val telephoneNumber: TextView = itemView.findViewById(R.id.tvtelNumberCurrentList)
        val businessType: TextView = itemView.findViewById(R.id.tvbusinessTypeCurrentList)
        val businessAddress: TextView = itemView.findViewById(R.id.tvBusinessAddressCurrentList)
        val businesssCategory: TextView = itemView.findViewById(R.id.tvBusinessCategoryCurrentList)
        val stringImage1: ImageView = itemView.findViewById(R.id.ivImageCurrentList)
        val btnMore: ImageView = itemView.findViewById(R.id.btnMore1)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminAdapter.MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.rc_new_account_requests, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdminAdapter.MyViewHolder, position: Int) {
        val currentBusiness = businessList[position]

        holder.companyName.text = currentBusiness.companyName
        holder.registerNumber.text = currentBusiness.registerNumber
        holder.emailAddress.text = currentBusiness.emailAddress
        holder.telephoneNumber.text = currentBusiness.telephoneNumber
        holder.businessType.text = currentBusiness.businessType
        holder.businessAddress.text = currentBusiness.businessAddress
        holder.businesssCategory.text = currentBusiness.businessCategory


        // Check if imageString is not null or empty before decoding
        val imageString = currentBusiness.stringImage

        if (!imageString.isNullOrBlank())
        {
            val businessImage = decodeImageFromString(imageString)

            if (businessImage != null)
            {
                holder.stringImage1.setImageBitmap(businessImage)
            }
            else
            {
               // holder.stringImage1.setImageResource(R.drawable.default_image)
            }
        }
        else
        {
          //  holder.stringImage1.setImageResource(R.drawable.default_image)
        }

        //item click listener handler that passes image to display on bottom sheet
        holder.itemView.setOnClickListener {
            onItemClickListener(currentBusiness)
        }

        //button more click listener
        holder.btnMore.setOnClickListener {
            showPopupMenu(holder.btnMore, position, currentBusiness)
        }
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //getting count of items
    override fun getItemCount(): Int {
        return businessList.size
    }
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
    private fun showPopupMenu(view: View, position: Int, currentBusiness: Information) {
        val popupMenu = PopupMenu(context, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.admin_more_options, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_approve -> {
                    onApproveClickListener(currentBusiness,position)
                    true
                }
                R.id.menu_decline -> {
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