package com.example.bzechat.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bzechat.Models.Adapters.MesseageAdapter
import com.example.bzechat.Models.Messeage
import com.example.bzechat.R
import com.example.bzechat.Repository.DataRepository
import com.example.bzechat.ViewModels.ChattingPageViewModel
import com.example.bzechat.ViewModels.ChattingPageViewModelFactory
import com.example.bzechat.databinding.ActivityChattingPageBinding
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class ChattingPage : AppCompatActivity() {
    lateinit var chattingPageBinding:ActivityChattingPageBinding
    private val db=FirebaseDatabase.getInstance()
    val Repo=DataRepository(db)
    lateinit var senderID:String
    lateinit var receiverID:String
    lateinit var chattingPageViewModel: ChattingPageViewModel
    var madapter : MesseageAdapter = MesseageAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        chattingPageBinding=ActivityChattingPageBinding.inflate(layoutInflater)
        setContentView(chattingPageBinding.root)
        chattingPageViewModel=ViewModelProvider(this,ChattingPageViewModelFactory(Repo)).get(ChattingPageViewModel::class.java)
        super.onCreate(savedInstanceState)

        //Creating Room
        senderID = intent.extras!!.get("senderID").toString()
        receiverID = intent.extras!!.get("receiverID").toString()
        var senderRoom:String = senderID+receiverID
        var receiverRoom:String = receiverID+senderID
        //Displaying Receiver Details
        chattingPageViewModel.getReceiverProfileData(receiverID).observe(this, Observer {
            chattingPageBinding.tvReceiverName.text= it.name
            Picasso.get().load(it.imgUri).into(chattingPageBinding.ChatProfileimgview)
        })
        //
        var messArr:ArrayList<Messeage> = ArrayList()
        //Set Recyclerview
        var llm = LinearLayoutManager(this)
        //llm.stackFromEnd=true
        chattingPageBinding.chattingPageRecview.layoutManager = llm
        //Set Adapter

        //Getting Messeages from DB and Setting it on Recyclerview
        chattingPageViewModel.getConversations(senderRoom).observe(this, Observer {
            //DataChanged clear array add new data to array
            messArr.clear()
            messArr.addAll(it)
            //NotifyDataSetChanged to adapter
            madapter.messgListChanged(messArr)
            chattingPageBinding.chattingPageRecview.scrollToPosition(messArr.size-1)
        })
        chattingPageBinding.chattingPageRecview.adapter=madapter

        //On Messeage Send
        chattingPageBinding.btnSendMssge.setOnClickListener {
            var mess=chattingPageBinding.edtMesseage.text.toString()
            if (mess.isNotEmpty()){
                var date=Date()
                var message:Messeage=Messeage(mess,date.time,senderID)
                chattingPageViewModel.storechatsinDB(message,senderRoom,receiverRoom,receiverID)
                chattingPageBinding.edtMesseage.setText("")
            }
            else{
                Toast.makeText(this,"Please Enter Messeage",Toast.LENGTH_SHORT).show()
            }
        }





        //ToolBar
        setSupportActionBar(chattingPageBinding.ChattingPageToolBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title=""
        getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onBackPressed()
        //finish()
        return super.onOptionsItemSelected(item)
    }
}