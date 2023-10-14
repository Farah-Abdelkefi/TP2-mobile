package com.example.tp2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale


data class Student(val prenom: String, val nom: String, val genre: String)


class StudentsAdapter(private val data: ArrayList<Student>) : RecyclerView.Adapter<StudentsAdapter.ViewHolder>(), Filterable {

    var dataFilterList : ArrayList<Student> = data

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image : ImageView
        val text : TextView
        init {
            image = itemView.findViewById(R.id.studentImageView)
            text = itemView.findViewById(R.id.studentTextView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = "${dataFilterList[position].prenom} ${dataFilterList[position].nom}"
        if(dataFilterList[position].genre == "F"){
            holder.image.setImageResource(R.drawable.female)
        }
        else{
            holder.image.setImageResource(R.drawable.man)
        }
    }

    override fun getItemCount(): Int {
        return dataFilterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if(charSearch.isEmpty()) {
                    dataFilterList = data
                } else {
                    val resultList = ArrayList<Student>()
                    for (student in data) {
                        if (student.nom.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) ||
                            student.prenom.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))
                            ) {
                            resultList.add(student)
                        }
                    }
                    dataFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = dataFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                dataFilterList = results?.values as ArrayList<Student>
                notifyDataSetChanged()
            }

        }
    }


}

class MainActivity : AppCompatActivity() {


    var students_tp = arrayListOf<Student>(
        Student("Farah", "abdelkefi", "F"),
        Student("intidhar", "ben hnia", "F"),
        Student("nour", "ben hajla", "F"),
        Student("david", "abab", "M"),
    )

    var students_cours = arrayListOf<Student>(
        Student("david", "abab", "M"),
        Student("rose", "aizjs", "F"),
        Student("julia", "aaad", "F"),
    )

    var students = arrayListOf<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchEditText: EditText = findViewById(R.id.editTextText)


        /*val Cours_adapter = StudentAdapter(persons_cours)
        val Tp_adapter = StudentAdapter( persons_tp )*/
        var studentAdapter = StudentsAdapter(students)

        val recyclerView: RecyclerView = findViewById (R.id.recyclerView)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = studentAdapter
        }
        val spinner : Spinner by lazy { findViewById(R.id.spinner) }
        val matieres = listOf<String>("Cours","TP")

        spinner.adapter = ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,matieres)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = adapterView?.getItemAtPosition(position).toString()
                if (selectedItem == "Cours"){
                    students.clear()
                    students.addAll(students_cours)
                    recyclerView.adapter?.notifyDataSetChanged()
                    studentAdapter.filter.filter("")
                    searchEditText.text.clear()
                }
                else {
                    students.clear()
                    students.addAll(students_tp)
                    recyclerView.adapter?.notifyDataSetChanged()
                    studentAdapter.filter.filter("")
                    searchEditText.text.clear()


                }
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {

            }
        }
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                studentAdapter.filter.filter(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No implementation needed
            }
        })







    }
}