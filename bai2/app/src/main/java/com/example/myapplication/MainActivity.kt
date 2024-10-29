package com.example.myapplication

import EmailAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Email
import com.example.myapplication.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Update the ID to match the one used in activity_main.xml
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewEmails)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val emails = listOf(
            Email("Edurila.com", "$19 Only - Learn Web Designing!", "12:34 PM"),
            Email("Chris Abad", "Help make Campaign Monitor better", "11:22 AM"),
            Email("Tuto.com", "8h de formation gratuite", "11:04 AM"),
            Email("Support", "Suivi de vos services - OVH", "10:26 AM"),
            Email("Matt from Ionic", "The New Ionic Creator Is Here!", "10:00 AM")
        )

        val adapter = EmailAdapter(emails)
        recyclerView.adapter = adapter
    }
}
