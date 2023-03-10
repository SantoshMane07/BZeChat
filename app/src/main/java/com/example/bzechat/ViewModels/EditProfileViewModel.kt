package com.example.bzechat.ViewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bzechat.Models.User
import com.example.bzechat.Models.UserProfile
import com.example.bzechat.Repository.DataRepository
import com.google.firebase.auth.FirebaseAuth


class EditProfileViewModel(val Repo:DataRepository) : ViewModel() {

    private val mauth=FirebaseAuth.getInstance()
    public var userProfileData:MutableLiveData<User> = MutableLiveData()
    var usersArray=MutableLiveData<ArrayList<User>>()

    fun getuserProfileData() : MutableLiveData<User>{
        userProfileData=Repo.getUserProfileData(mauth.currentUser!!.uid)
        return userProfileData
    }
    fun saveUserProfileData(user: User, id: String, context: Context){
        Repo.saveUserProfileData(user,mauth.currentUser!!.uid,context)
    }

}