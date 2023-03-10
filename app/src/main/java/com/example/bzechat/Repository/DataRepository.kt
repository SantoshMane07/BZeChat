package com.example.bzechat.Repository

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bzechat.Models.Messeage
import com.example.bzechat.Models.User
import com.example.bzechat.Models.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class DataRepository(val db:FirebaseDatabase) {
    private var mauth:FirebaseAuth = FirebaseAuth.getInstance()
    val storage:FirebaseStorage = FirebaseStorage.getInstance()
    //
    var UsersArray :MutableLiveData<ArrayList<User>> = MutableLiveData()
    var UsersArrayH: ArrayList<User> = ArrayList()
    var UserProfile:MutableLiveData<User> = MutableLiveData()
    var UserProfileH:User = User()
    var ReceiverProfile:MutableLiveData<User> = MutableLiveData()
    var ReceiverProfileH:User = User()
    var MessArray:MutableLiveData<ArrayList<Messeage>> = MutableLiveData()
    var MessArrayH:ArrayList<Messeage> = ArrayList()
    //Functions
    //
    fun getConversations(senderRoom: String) : MutableLiveData<ArrayList<Messeage>>{
        db.getReference().child("Chats").child(senderRoom).child("messeages").addValueEventListener( object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                MessArrayH.clear()
                for (data: DataSnapshot in snapshot.children) {

                    var mess = data.getValue(Messeage::class.java)
                    if (mess != null) {
                        MessArrayH.add(mess)
                    }
                }
                MessArray.postValue(MessArrayH)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return MessArray
    }
    //
    fun storechatsinDB(message: Messeage, senderRoom:String, receiverRoom:String,receiverID:String){
        db.getReference().child("Chats").child(senderRoom).child("messeages").push().setValue(message).addOnSuccessListener {
            db.getReference().child("Chats").child(receiverRoom).child("messeages").push().setValue(message)
        }
    }
    //
    fun saveUserProfileData(user:User,id: String,context: Context){
          db.getReference().child("Users").child("${id}").setValue(user).addOnCompleteListener {
              if (it.isSuccessful){
                  Toast.makeText(context,"Profile Edited Successfully",Toast.LENGTH_SHORT).show()
              }
              else{
                  Toast.makeText(context,"Fail to Save Profile",Toast.LENGTH_SHORT).show()
              }
          }
    }
    //
    fun getReceiverProfileData(id:String):MutableLiveData<User>{
        db.getReference().child("Users").child("$id").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ReceiverProfileH.name=snapshot.child("name").value.toString()
                ReceiverProfileH.status=snapshot.child("status").value.toString()
                ReceiverProfileH.imgUri=snapshot.child("imgUri").value.toString()
                ReceiverProfileH.email=snapshot.child("email").value.toString()
                ReceiverProfileH.phone=snapshot.child("phone").value.toString()
                ReceiverProfileH.id=snapshot.child("id").value.toString()
                ReceiverProfile.postValue(ReceiverProfileH)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        //Log.d("Profileis", "getUserProfileData: ${UserProfile.value!!.name}")
        return ReceiverProfile
    }
    //
    fun getUserProfileData(id:String):MutableLiveData<User>{
        db.getReference().child("Users").child("$id").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                UserProfileH.name=snapshot.child("name").value.toString()
                UserProfileH.status=snapshot.child("status").value.toString()
                UserProfileH.imgUri=snapshot.child("imgUri").value.toString()
                UserProfileH.email=snapshot.child("email").value.toString()
                UserProfileH.phone=snapshot.child("phone").value.toString()
                UserProfileH.id=snapshot.child("id").value.toString()
                UserProfile.postValue(UserProfileH)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        //Log.d("Profileis", "getUserProfileData: ${UserProfile.value!!.name}")
        return UserProfile
    }
    //
    fun getUsers(): MutableLiveData<ArrayList<User>> {
        db.getReference().child("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                UsersArrayH.clear()
                for (data: DataSnapshot in snapshot.children) {

                    var user = data.getValue(User::class.java)
                    if (user != null) {
                        UsersArrayH.add(user)
                    }
                }
                UsersArray.postValue(UsersArrayH)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        Log.d("NOKOO", "getUsers: $UsersArray ,,,,,, $UsersArrayH ")
        return UsersArray
    }
}
