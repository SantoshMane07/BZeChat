package com.example.bzechat.Models.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bzechat.Models.User
import com.example.bzechat.R
import com.example.bzechat.Views.HomePage
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.zip.Inflater


class UsersAadapter(val listener: iUserAdapter) : RecyclerView.Adapter<UsersAadapter.UserViewHolder>() {

    private val mauth = FirebaseAuth.getInstance()
    var UsersArray:ArrayList<User> = ArrayList<User>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        var viewholder=UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chatrow,parent,false))
        //onClick Actions
        viewholder.itemView.setOnClickListener {
            var senderID = mauth.currentUser!!.uid.toString()
            var receiverID = UsersArray[viewholder.adapterPosition].id.toString()
            listener.onRowClicked(senderID,receiverID)
        }
        //
        return viewholder
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.tv_user_name.text=UsersArray[position].name
        holder.tv_user_status.text=UsersArray[position].status
        Picasso.get().load(UsersArray[position].imgUri).into(holder.cimgv_profile_image)
    }

    override fun getItemCount(): Int {
        return UsersArray.size
    }
    class UserViewHolder(itemView: View) : ViewHolder(itemView){
        var cimgv_profile_image=itemView.findViewById<CircleImageView>(R.id.cimgv_profile_img)
        var tv_user_name=itemView.findViewById<TextView>(R.id.tv_user_name)
        var tv_user_status=itemView.findViewById<TextView>(R.id.tv_user_status)
    }
    fun usersListChanged(UsersNewArray: ArrayList<User>){
        UsersArray.clear()
        UsersArray.addAll(UsersNewArray)
        notifyDataSetChanged()
    }

}
interface iUserAdapter{
    fun onRowClicked(senderID:String , receiverID:String)
}