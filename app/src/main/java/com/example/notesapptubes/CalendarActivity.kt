package com.example.notesapptubes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import android.widget.Toast

class CalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        supportActionBar?.title = "Calendar"

        val calendarView : CalendarView = findViewById(R.id.calendarView)

        calendarView.setOnDateChangeListener { calendarView, i, i2, i3 ->
            var month = i2 + 1
            Toast.makeText(this, "Selected Date: $i3/$month/$i", Toast.LENGTH_SHORT).show()
        }
    }
}