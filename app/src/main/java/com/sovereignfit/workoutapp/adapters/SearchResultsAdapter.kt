package com.sovereignfit.workoutapp.adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sovereignfit.workoutapp.*
import com.sovereignfit.workoutapp.data.exercisedb.Exercise
import com.sovereignfit.workoutapp.data.exercisedb.ExerciseViewModel
import com.sovereignfit.workoutapp.databinding.FragmentExerciseBinding


class SearchResultsAdapter(private val context: Context, private val wlAdapter: WorkoutBuildAdapter,
                           private val viewModel: ExerciseViewModel, private val parent: Activity) : RecyclerView.Adapter<SearchResultsAdapter.SearchResultsViewHolder>() {

    var shownData = emptyList<Exercise>()
    var fullData = emptyList<Exercise>()

    class SearchResultsViewHolder(val binding: FragmentExerciseBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viwType: Int): SearchResultsViewHolder {
        val binding = FragmentExerciseBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultsViewHolder(binding)
    }

    /*
    fun updateWorkouts(newList : MutableList<Workout>) {
        for(w in newList){
            if(w !in workouts) {
                workouts.add(w)
                notifyItemInserted(workouts.size - 1)
            }
        }
    } */

    override fun onBindViewHolder(holder: SearchResultsViewHolder, position: Int) {
        holder.binding.tvExName.text = shownData[position].name
        if(shownData[position].name.contains("Create New Exercise:")){
            holder.binding.tvExName.setOnClickListener {
                if(shownData[position].name.contains("kg")) {
                    var exTitle = shownData[position].name.substring(21)
                    val bits = exTitle.split("kg")
                    exTitle = bits[0] + bits[1]
                    val newExercise = Exercise(name = exTitle, unit = 0)
                    viewModel.insert(newExercise)
                    wlAdapter.addExercise(newExercise)
                    if(parent is BuildWorkoutActivity) {
                        parent.closeSearch()
                    } else if (parent is WorkoutEditor) {
                        parent.closeSearch()
                    }
                } else if (shownData[position].name.contains("lbs")) {
                    var exTitle = shownData[position].name.substring(21)
                    val bits = exTitle.split("lbs")
                    exTitle = bits[0] + bits[1]
                    val newExercise = Exercise(name = exTitle, unit = 1)
                    viewModel.insert(newExercise)
                    wlAdapter.addExercise(newExercise)
                    if(parent is BuildWorkoutActivity) {
                        parent.closeSearch()
                    } else if (parent is WorkoutEditor) {
                        parent.closeSearch()
                    }
                } else {
                    val alertBuilder = AlertDialog.Builder(context)
                    alertBuilder.setTitle("Cannot Create Exercise")
                    alertBuilder.setMessage("Please include \"kg\" or \"lbs\" in the title.")
                    alertBuilder.show()
                }
            }
        } else {
            holder.binding.tvExName.setOnClickListener {
                wlAdapter.addExercise(shownData[position])
                //var name = shownData[position].name
                //println("Adding $name to workout!!!")

                //close search results, clear search text & hide keyboard
                if(parent is BuildWorkoutActivity) {
                    parent.closeSearch()
                } else if (parent is WorkoutEditor) {
                    parent.closeSearch()
                }
            }
        }
    }

    fun setData(newData: List<Exercise>){
        shownData = newData
        fullData = newData
        notifyDataSetChanged()
    }

    fun filter (query : String){
        val createPrompt = Exercise(name = "Create New Exercise: $query")
        val l = mutableListOf<Exercise>(createPrompt)
        shownData = (fullData.filter {query.lowercase() in it.name.lowercase()}
                + l)

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return shownData.size
    }
}