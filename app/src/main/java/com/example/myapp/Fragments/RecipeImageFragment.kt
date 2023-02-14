package com.example.myapp.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.myapp.DataClass.PostRecipe
import com.example.myapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.util.*

class RecipeImageFragment : Fragment() {
    private val args: RecipeImageFragmentArgs by navArgs()
    private lateinit var dref: DatabaseReference
    var image: String? = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_image, container, false)
        val recipeimage = view?.findViewById<CircleImageView>(R.id.circleImageView5)
        val recipechoose=view?.findViewById<FloatingActionButton>(R.id.recipeimagechoose)
        recipechoose?.setOnClickListener {
            val mtintent=Intent(Intent.ACTION_GET_CONTENT)
            mtintent.setType("image/*")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activityResultLauncher.launch(mtintent)
            }
        }
        val post = view.findViewById<Button>(R.id.postrecipe_btn)
        dref = FirebaseDatabase.getInstance().getReference("Post Recipes")
        post.setOnClickListener {
            val progressDialog = ProgressDialog(requireContext())
            progressDialog.setMessage("Loading")
            progressDialog.setCancelable(false)
            progressDialog.show()
            val PersonName = args.personalName
            val Email = args.email
            val Profesion = args.profesion
            val RecipeName = args.recipeName
            val Ing = args.recipeIng
            val Desc = args.recipeDesc
            val Time = args.recipeTime
            val postRecipes = PostRecipe(PersonName, Email, Profesion, RecipeName, Ing, Desc, Time,image)
            val recipeid = dref.push().key!!
            dref.child(recipeid).setValue(postRecipes).addOnCompleteListener {
                progressDialog.hide()
            }.addOnCanceledListener {
                progressDialog.hide()
                Toast.makeText(requireContext(), "cancle", Toast.LENGTH_SHORT).show()
            }

        }
        val back = view.findViewById<FloatingActionButton>(R.id.recipe_image_back)
        back.setOnClickListener {

            Navigation.findNavController(view)
                .navigate(R.id.action_recipeImageFragment_to_personalDetailsFragment)
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data!!.data
            try {
                val inputSteam = requireContext().contentResolver?.openInputStream(uri!!)
                val bitMap = BitmapFactory.decodeStream(inputSteam)
                val stream = ByteArrayOutputStream()
                bitMap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byte = stream.toByteArray()
                image = Base64.getEncoder().encodeToString(byte)
                val recipeimage = view?.findViewById<CircleImageView>(R.id.circleImageView5)
                recipeimage?.setImageBitmap(bitMap)
                inputSteam!!.close()
            } catch (ex: java.lang.Exception) {
                Toast.makeText(requireContext(), ex.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

}