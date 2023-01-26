package com.example.bzechat.Views

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bzechat.Models.Adapters.UsersAadapter
import com.example.bzechat.Models.Adapters.iUserAdapter
import com.example.bzechat.Models.User
import com.example.bzechat.R
import com.example.bzechat.Repository.DataRepository
import com.example.bzechat.ViewModels.HomePageViewModel
import com.example.bzechat.ViewModels.HomePageViewModelFactory
import com.example.bzechat.databinding.ActivityHomePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class HomePage : AppCompatActivity() , iUserAdapter{

    //Initializations
    private var db: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var mauth: FirebaseAuth = Firebase.auth
    private var storageDB: FirebaseStorage = FirebaseStorage.getInstance()

    lateinit var usersArray:ArrayList<User>
    val mAdapter:UsersAadapter=UsersAadapter(this)
    lateinit var homePageBinding: ActivityHomePageBinding
    lateinit var homePageViewModel: HomePageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homePageBinding=ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(homePageBinding.root)

        val Repo:DataRepository=DataRepository(db)
        homePageViewModel =ViewModelProvider(this,HomePageViewModelFactory(Repo)).get(HomePageViewModel::class.java)
        homePageBinding.homePageRecview.layoutManager=LinearLayoutManager(this)
        //SettingToolBar
        setSupportActionBar(homePageBinding.homeToolBar)
        supportActionBar!!.title="Your Chats"
        //
        usersArray= ArrayList<User>()
        //Getting Data in Array and sending to Adapter
        homePageViewModel.getUserArray().observe(this, Observer {
            //DataChanged
            usersArray.clear()
            usersArray.addAll(it)
            //NotifyDataSetChanged
            mAdapter.usersListChanged(usersArray)
        })
        homePageBinding.homePageRecview.adapter=mAdapter
    }

    //ToolBar Creation
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.homemenus,menu)
        return super.onCreateOptionsMenu(menu)
    }

    //ToolBar Option Selection
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemId=item.itemId
        if (itemId== R.id.edit_profile_btn){
            //
            startActivity( Intent(this, EditProfile::class.java))
        }
        else{
            //
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.logout_dialog)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.show()
            dialog.findViewById<Button>(R.id.btn_yes).setOnClickListener {
                mauth.signOut()
                dialog.dismiss()
                startActivity(Intent(this,LogIn::class.java))
                finish()
            }
            dialog.findViewById<Button>(R.id.btn_no).setOnClickListener {
                dialog.dismiss()
            }
            //
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRowClicked(senderID: String, receiverID: String) {
        val intent:Intent = Intent(this,ChattingPage::class.java)
        intent.putExtra("senderID","${senderID}")
        intent.putExtra("receiverID","${receiverID}")
        startActivity(intent)
    }
}