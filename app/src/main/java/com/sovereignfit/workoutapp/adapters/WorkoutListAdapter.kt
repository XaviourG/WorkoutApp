package com.sovereignfit.workoutapp.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.sovereignfit.workoutapp.*
import com.sovereignfit.workoutapp.data.exercisedb.ExerciseViewModel
import com.sovereignfit.workoutapp.data.exercisedb.Workout
import com.sovereignfit.workoutapp.databinding.WorkoutListingBinding
import com.sovereignfit.workoutapp.databinding.WorkoutListingPopupBinding


class WorkoutListAdapter(private val context: Context, private val exerciseViewModel: ExerciseViewModel)
    : RecyclerView.Adapter<WorkoutListAdapter.WorkoutListViewHolder>() {

    private var workouts = listOf<Workout>()

    class WorkoutListViewHolder(val binding: WorkoutListingBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viwType: Int): WorkoutListViewHolder {
        val binding = WorkoutListingBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutListViewHolder(binding)
    }

    /*fun updateWorkouts(newList : MutableList<Workout>) {
        for(w in newList){
            if(w !in workouts) {
                workouts.add(w)
                notifyItemInserted(workouts.size - 1)
            }
        }

        println("WITHIN ADAPTER LIST UPDATED : $workouts")
    }

     */

    override fun onBindViewHolder(holder: WorkoutListViewHolder, position: Int) {
        var warned = false
        with(holder) {
            with(workouts[position]) {
                binding.tvWorkoutListing.text = title
                binding.tvWorkoutListing.setOnClickListener {
                    holder.binding.tvWorkoutListing.setTextColor(context.getColor(R.color.purple))
                    holder.binding.tvWorkoutListing.setBackgroundResource(R.drawable.item_border_highlight)
                    val popup = PopupWindow(context)
                    val binding2 = WorkoutListingPopupBinding.inflate(LayoutInflater.from(context))
                    popup.setBackgroundDrawable(context.getDrawable(R.drawable.nothing))
                    //val view = LayoutInflater.from(context).inflate(R.layout.workout_listing_popup, null)
                    popup.contentView = binding2.root
                    binding2.btnCancel.setOnClickListener{
                        holder.binding.tvWorkoutListing.setTextColor(context.getColor(R.color.white))
                        holder.binding.tvWorkoutListing.setBackgroundResource(R.drawable.card)
                        popup.dismiss() }
                    binding2.btnEdit.setOnClickListener{
                        val i = Intent(context,
                            WorkoutEditor::class.java)
                        i.putExtra("WID", workouts[position].WID)
                        startActivity(context, i, null)
                    }
                    binding2.btnStart.setOnClickListener{
                        //start workout player for this workout
                        val i = Intent(context, WorkoutPlayer::class.java)
                        i.putExtra("WID", workouts[position].WID)
                        startActivity(context, i, null)
                    }
                    binding2.btnDelete.setOnClickListener {
                        //firstly a warning
                        if(!warned) {
                            val alertBuilder = AlertDialog.Builder(context)
                            alertBuilder.setTitle("Are you certain?")
                            alertBuilder.setMessage("Deletion is permanent!")
                            alertBuilder.show()
                            warned = true
                            binding2!!.btnDelete.setShadowLayer(50f, 0f, 0f, Color.BLACK)
                            binding2!!.btnDelete.setTextColor(Color.RED)
                        } else {
                            //delete program from database
                            exerciseViewModel.deleteWorkout(workouts[position])
                            //reload page to show new list
                            val i = Intent(context, WorkoutListActivity::class.java)
                            startActivity(context, i, null)
                        }
                    }

                    popup.showAtLocation(binding2.root, Gravity.CENTER, 0, 0)

                    /*
                    val i = Intent(context,
                        WorkoutEditor::class.java)
                    i.putExtra("WID", workouts[position].WID)
                    startActivity(context, i, null)

                     */
                }
                //ADD CODE HERE TO FLESH OUT WORKOUT BOXES } }
            }
        }
    }

    override fun getItemCount(): Int {
        return workouts.size
    }

    fun setData(newData: List<Workout>){
        workouts = newData
        notifyDataSetChanged()
    }
}