package com.sbz.videocallasgmnt

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.Window
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.sbz.videocallasgmnt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var contactNumber: String
    private lateinit var userName: String
    private lateinit var email: String
    private lateinit var auth: FirebaseAuth
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setStatusBarProperties()
        contactNumber = intent.getStringExtra("contactNumber").toString()

        auth = FirebaseAuth.getInstance()

        binding.btnNext.setOnClickListener {
            userName = binding.userName.text.toString()
            email = binding.tvEmail.text.toString()
            if (userName.isEmpty()) {
                Toast.makeText(this, "User Name Can't be Empty $userName", Toast.LENGTH_SHORT)
                    .show()
            } else if (email.isEmpty()) {
                Toast.makeText(this, "Email Can't be Empty", Toast.LENGTH_SHORT).show()
            } else {
                updateUserInfo()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }

        binding.ivProfilePicture.setOnClickListener {
            showImageAttachMenu()
        }

    }

    private fun setStatusBarProperties() {
        val window: Window = window
        val decorView: View = window.decorView
        val wic = WindowInsetsControllerCompat(window, decorView)
        wic.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.WHITE
    }

    private fun updateUserInfo() {
        if (imageUri == null) {
            updateProfile("")
        } else {
            uploadImage()
        }
    }

    private fun uploadImage() {
        val filePathAndName = "ProfileImages/" + auth.uid

        val storageRef = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageRef.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);

                val updateImageUrl = uriTask.result.toString()

                updateProfile(updateImageUrl)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Uploading Failed!! ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateProfile(uploadImageUri: String) {
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["userName"] = userName
        hashMap["email"] = email
        hashMap["phoneNumber"] = contactNumber
        if (imageUri != null) {
            hashMap["profileImage"] = uploadImageUri
        }

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(auth.uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                Log.d("USER_DATA_UPDATED", "Update ho gya hai firebase me")
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed Saving User Info", Toast.LENGTH_SHORT).show()

            }
    }

    private fun showImageAttachMenu() {

        val popupMenu = PopupMenu(this, binding.ivProfilePicture)
        popupMenu.menu.add(Menu.NONE, 0, 0, "Camera")
        popupMenu.menu.add(Menu.NONE, 1, 1, "Gallery")
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { item ->
            val id = item.itemId
            if (id == 0) {
                pickImageCamera()
            } else if (id == 1) {
                pickImageGallery()
            }


            true
        }

    }

    private fun pickImageCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Temp_title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_description")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActionResultLauncher.launch(intent)
    }

    private val cameraActionResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
//                imageUri = data!!.data

                binding.ivProfilePicture.setImageURI(imageUri)
            } else {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }

        }
    )


    private fun pickImageGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActionResultLauncher.launch(intent)

    }

    private val galleryActionResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                imageUri = data!!.data

                binding.ivProfilePicture.setImageURI(imageUri)
            } else {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }

        }
    )
}