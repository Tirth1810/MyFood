package com.example.myapp.Fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import com.example.myapp.R
import com.example.myapp.Activity.UserPostedActivity

class DeashBoardFragment : Fragment() {


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_deash_board, container, false)
        val search = view.findViewById<CardView>(R.id.Search)
        val recipe = view.findViewById<CardView>(R.id.recipes)
        val profile = view.findViewById<CardView>(R.id.profile)
        val addRecipe = view.findViewById<CardView>(R.id.addRecipe)
        val useruploaded = view.findViewById<CardView>(R.id.userposted)
        val trending = view.findViewById<CardView>(R.id.BuyBook)
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.dialog)
        search.startAnimation(animation)
        recipe.startAnimation(animation)
        profile.startAnimation(animation)
        addRecipe.startAnimation(animation)
        useruploaded.startAnimation(animation)
        trending.startAnimation(animation)
        profile.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_deashBoard_to_profile2)
        }
        search.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_deashBoard_to_searchFragment22)
        }
        useruploaded.setOnClickListener {
            startActivity(Intent(requireContext(), UserPostedActivity::class.java))
        }
        recipe.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_deashBoard_to_recipeFragment)
        }
        addRecipe.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_deashBoard_to_addRecipeFragment2)
        }
        trending.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_deashBoard_to_trendingFragment)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val dialog = Dialog(requireContext())
                    dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.custom_exit_dialog)
                    dialog.window?.setWindowAnimations(R.style.dialogAnimation)
                    val ok = dialog.findViewById<Button>(R.id.custom_exit_yes)
                    ok.setOnClickListener {
                        activity?.finishAffinity()
                    }
                    val cancel = dialog.findViewById<Button>(R.id.custom_exit_cancle)
                    cancel.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()

                }
            })
    }
}

