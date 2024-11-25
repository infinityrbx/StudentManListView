package com.example.studentmanlistview

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var studentAdapter: StudentListAdapter
    private val students = mutableListOf(
        StudentModel("Nguyễn Văn An", "SV001"),
        StudentModel("Trần Thị Bảo", "SV002"),
        StudentModel("Lê Hoàng Cường", "SV003")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val listView = findViewById<ListView>(R.id.list_view_students)
        studentAdapter = StudentListAdapter(this, students)
        listView.adapter = studentAdapter

        registerForContextMenu(listView)

        listView.setOnItemClickListener { _, _, position, _ ->
            val student = students[position]
            Toast.makeText(this, "Selected: ${student.studentName}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_new -> {
                val intent = Intent(this, AddEditStudentActivity::class.java)
                startActivityForResult(intent, 100)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val selectedStudent = students[info.position]

        return when (item.itemId) {
            R.id.context_edit -> {
                val intent = Intent(this, AddEditStudentActivity::class.java)
                intent.putExtra("name", selectedStudent.studentName)
                intent.putExtra("id", selectedStudent.studentId)
                intent.putExtra("position", info.position)
                startActivityForResult(intent, 101)
                true
            }
            R.id.context_remove -> {
                AlertDialog.Builder(this)
                    .setTitle("Remove Student")
                    .setMessage("Are you sure you want to remove ${selectedStudent.studentName}?")
                    .setPositiveButton("Yes") { _, _ ->
                        students.removeAt(info.position)
                        studentAdapter.notifyDataSetChanged()
                    }
                    .setNegativeButton("No", null)
                    .show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val name = data.getStringExtra("name") ?: return
            val id = data.getStringExtra("id") ?: return
            val position = data.getIntExtra("position", -1)

            if (requestCode == 100) {
                students.add(StudentModel(name, id))
                studentAdapter.notifyDataSetChanged()
            } else if (requestCode == 101 && position >= 0) {
                students[position] = StudentModel(name, id)
                studentAdapter.notifyDataSetChanged()
            }
        }
    }}