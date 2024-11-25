package com.example.studentmanlistview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddEditStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_student)

        val editName = findViewById<EditText>(R.id.edit_student_name)
        val editId = findViewById<EditText>(R.id.edit_student_id)
        val btnSave = findViewById<Button>(R.id.btn_save)

        val name = intent.getStringExtra("name")
        val id = intent.getStringExtra("id")
        val position = intent.getIntExtra("position", -1)

        // Populate fields if editing
        if (name != null && id != null) {
            editName.setText(name)
            editId.setText(id)
        }

        btnSave.setOnClickListener {
            val resultIntent = Intent()
            val newName = editName.text.toString()
            val newId = editId.text.toString()

            if (newName.isNotEmpty() && newId.isNotEmpty()) {
                resultIntent.putExtra("name", newName)
                resultIntent.putExtra("id", newId)
                resultIntent.putExtra("position", position)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

