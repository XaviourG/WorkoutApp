package com.sovereignfit.workoutapp.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sovereignfit.workoutapp.ProgramEditorActivity
import com.sovereignfit.workoutapp.data.exercisedb.ExerciseViewModel
import com.sovereignfit.workoutapp.data.exercisedb.Workout
import com.sovereignfit.workoutapp.databinding.FragmentExerciseBinding

class ProgramSearchAdapter(private val context: Context, private val programBuildAdapter: ProgramBuildAdapter,
                           private val viewModel: ExerciseViewModel, private val parent: ProgramEditorActivity
) : RecyclerView.Adapter<ProgramSearchAdapter.ProgramSearchViewHolder>() {

    var shownData = emptyList<Workout>()
    var fullData = emptyList<Workout>()
    class ProgramSearchViewHolder(val binding: FragmentExerciseBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viwType: Int): ProgramSearchViewHolder {
        val binding = FragmentExerciseBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ProgramSearchViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ProgramSearchViewHolder, position: Int) {
        holder.binding.tvExName.text = shownData[position].title
        holder.binding.tvExName.setOnClickListener {
            programBuildAdapter.addWorkout(shownData[position])
            parent.closeSearch()
        }
    }

    fun setData(newData: List<Workout>){
        shownData = newData
        fullData = newData
        notifyDataSetChanged()
    }

    fun filter (query : String){
        shownData = fullData.filter {query.lowercase() in it.title.lowercase()}
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return shownData.size
    }
    fun Fragment.hideKeyboard() {
        view?.let {activity?.hideKeyboard(it)}
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}