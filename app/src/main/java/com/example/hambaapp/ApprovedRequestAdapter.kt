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
import androidx.recyclerview.widget.RecyclerView
import com.example.hambaapp.HambaBusiness.Information
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DatabaseReference

class ApprovedRequestAdapter(
    private val context: Context,
    private val businessList: MutableList<Information> = mutableListOf(),
    private val onDeleteClickListener: (Int) -> Unit,
    private val onItemClickListener: (Information) -> Unit,
) : RecyclerView.Adapter<ApprovedRequestAdapter.MyViewHolder>() {

    //Grabbing objects from Recycler viewer
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val companyName: TextView = itemView.findViewById(R.id.tvBusinessNameCurrentList1)
        val registerNumber: TextView = itemView.findViewById(R.id.tvRegisterNumberCurrentList1)
        val emailAddress: TextView = itemView.findViewById(R.id.tvEmailAddressCurrentList1)
        val telephoneNumber: TextView = itemView.findViewById(R.id.tvtelNumberCurrentList1)
        val businessType: TextView = itemView.findViewById(R.id.tvbusinessTypeCurrentList1)
        val businessAddress: TextView = itemView.findViewById(R.id.tvBusinessAddressCurrentList1)
        val businesssCategory: TextView = itemView.findViewById(R.id.tvBusinessCategoryCurrentList1)
        val stringImage1: ImageView = itemView.findViewById(R.id.ivImageAcceptedBus)
        val btnDelete: MaterialButton = itemView.findViewById(R.id.btnRemove1)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApprovedRequestAdapter.MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_accepted_requests, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return businessList.size
    }

    override fun onBindViewHolder(holder: ApprovedRequestAdapter.MyViewHolder, position: Int) {
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

        holder.btnDelete.setOnClickListener {
            showPopupMenu(holder.btnDelete, position, currentBusiness)
        }

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
                    onDeleteClickListener(position)
                    true
                }
                R.id.menu_decline -> {
                    // Handle delete option
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