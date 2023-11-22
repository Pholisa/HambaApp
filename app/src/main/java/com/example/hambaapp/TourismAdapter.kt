package com.example.hambaapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hambaapp.HambaBusiness.BusinessDetail
import com.example.hambaapp.HambaBusiness.BusinessDetailPublic
import com.example.hambaapp.HambaBusiness.BusinessDetailPublic1
import java.lang.Exception


class TourismAdapter(private val context: Context,
                     private val tourismList : MutableList<BusinessDetailPublic1> = mutableListOf(),
                     private val onItemClickListener: (Int) -> Unit): RecyclerView.Adapter<TourismAdapter.MyViewHolder>(){

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBusinessTitle: TextView = itemView.findViewById(R.id.busTitleRV)
        val tvBusinessType : TextView = itemView.findViewById(R.id.busTypeRV)
        val tvBusinessDesc : TextView = itemView.findViewById(R.id.busDescriptionRV)
        val businessImageView : ImageView = itemView.findViewById(R.id.busImageRV)
        val tvBusinessPrice : TextView = itemView.findViewById(R.id.busPriceRV)
        val tvBusinessEmail : TextView = itemView.findViewById(R.id.busEmailRV)
        val tvBusinessLocation : TextView = itemView.findViewById(R.id.busLocationRV)
        val tvBusinessNo : TextView = itemView.findViewById(R.id.busNumberRV)

        init {
            itemView.setOnClickListener { onItemClickListener(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.dashboard_list_item, parent, false)
        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentBusiness = tourismList[position]

        holder.tvBusinessTitle.text = currentBusiness.title
        holder.tvBusinessType.text = currentBusiness.category
        holder.tvBusinessDesc.text =  "Description:" +currentBusiness.businessSummary
        holder.tvBusinessEmail.text = "Email:" + currentBusiness.emailAd
        holder.tvBusinessNo.text = "Number:" + currentBusiness.telephoneNo
        holder.tvBusinessPrice.text = "R" + currentBusiness.price
        holder.tvBusinessLocation.text = currentBusiness.locationString



        val imageString = currentBusiness.stringImage
        if(!imageString.isNullOrBlank()){
            val businessImage = decodeImageFromString(imageString)

            if(businessImage != null){
                holder.businessImageView.setImageBitmap(businessImage)
            }
            else
            {

            }
        }else{

    }

    }

    override fun getItemCount(): Int {
        return tourismList.size
    }

    private fun decodeImageFromString(imageString: String): Bitmap? {


        try {
            val decodedBytes: ByteArray = Base64.decode(imageString, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }catch (e: Exception){
            e.printStackTrace()
        }
        return null

    }
}
