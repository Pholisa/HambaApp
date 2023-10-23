package com.example.hambaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BusinessAdapter(private val businessList:ArrayList<BusinessStorage> ):RecyclerView.Adapter<BusinessAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tvBusinessName:TextView = itemView.findViewById(R.id.tvBusinessName)
        val tvBusinessAddress:TextView = itemView.findViewById(R.id.tvBusinessAddress)
        val tvBusinessPrices:TextView = itemView.findViewById(R.id.tvBusinessPrices)
        //val tvImage:TextView = itemView.findViewById(R.id.)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.business_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      //  holder.tv.text = businessList[position].
        //holder.tvBusinessAddress.text = businessList[position].birdSpecies
        //holder.tvBusinessPrices.text = businessList[position].date
        //holder.tvLocation.text = businessList[position].location

    }
    override fun getItemCount(): Int {

        return businessList.size
    }


}

//-------------------------------------ooo000EndOfFile000ooo----------------------------------------



//class BusinessAdapter(private val context: android.content.Context, private var businessList: List<BusinessStorage>) : RecyclerView.Adapter<MyViewHolder>() {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.business_list_item, parent, false)
//        return MyViewHolder(view)
//    }
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        Glide.with(context).load(businessList[position].image)
//            .into(holder.recImage)
//        holder.tvBusinessName.text = businessList[position].tvBusinessName
//        holder.tvBusinessAddress.text = businessList[position].tvBusinessAddress
//        holder.tvBusinessPrices.text = businessList[position].tvBusinessPrices
//    }
//
//    override fun getItemCount(): Int {
//        return arrayList.size
//    }
//
//    fun searchDataList(searchList: List<BirdInfo>){
//        arrayList = searchList
//        notifyDataSetChanged()
//    }
//}
//class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    var recImage: ImageView
//    var tvBusinessName: TextView
//    var tvBusinessAddress: TextView
//    var tvBusinessPrices: TextView
//
//    init {
//        recImage = itemView.findViewById(R.id.ivPicture)
//        tvBusinessName = itemView.findViewById(R.id.tvBusinessName)
//        tvBusinessAddress = itemView.findViewById(R.id.tvBusinessAddress)
//        tvBusinessPrices = itemView.findViewById(R.id.tvBusinessPrices)
//    }
//}
//
//
//








//class BusinessAdapter(private val businessList : ArrayList<BusinessStorage>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>()
//{
//
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.business_list_item, parent, false)
//        return MyViewHolder(itemView)
//    }
//
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//
//        val currentItem = businessList[position]
//
//        holder.tvBusinessName.text = currentItem.tvBusinessName
//        holder.tvBusinessAddress.text = currentItem.tvBusinessAddress
//        holder.tvBusinessPrices.text = currentItem.tvBusinessPrices
//
//    }
//
//    override fun getItemCount(): Int {
//        return businessList.size
//    }

//    class MyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
//
//        val tvBusinessName : TextView = itemView.findViewById(R.id.tvBusinessName)
//        val tvBusinessAddress : TextView = itemView.findViewById(R.id.tvBusinessAddress)
//        val tvBusinessPrices :  TextView = itemView.findViewById(R.id.tvBusinessPrices)
//
//
//
//
//    }







//(private val context: Activity, private val businessList : ArrayList<BusinessStorage> ): ArrayAdapter<SavingData>(context, R.layout.activity_bird_list_item, arrayList)
//{

//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//
//        val inflater : LayoutInflater = LayoutInflater.from(context)
//        val view : View = inflater.inflate(R.layout.business_list_item, null)
//
//        val imageView : ImageView = view.findViewById(R.id.ivBirdPicture)
//        val tvBusinessName : TextView = view.findViewById(R.id.tvBusinessName)
//        val tvBusinessAddress : TextView = view.findViewById(R.id.tvBusinessAddress)
//        val tvBusinessPrices : TextView = view.findViewById(R.id.tvBusinessPrices)
//
//
//        imageView.setImageResource(businessList[position].dataImage)
//        tvBusinessName.text = businessList[position].tvBusinessName
//        tvBusinessAddress.text = businessList[position].tvBusinessAddress
//        tvBusinessPrices.text = businessList[position].tvBusinessPrices
//
//        return view
//    }