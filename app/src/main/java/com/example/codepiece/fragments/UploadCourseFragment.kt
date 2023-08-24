package com.example.codepiece.fragments

import android.app.Activity
import android.app.FragmentManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.codepiece.R
import com.example.codepiece.data.CourseData
import com.example.codepiece.databinding.FragmentUploadCourseBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class UploadCourseFragment : Fragment() {

    private lateinit var binding: FragmentUploadCourseBinding
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadCourseBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize Firebase Storage, Firestore, and Authentication
        firebaseStorage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storageReference = firebaseStorage.reference

        // Set click listener for the uploadImageView to select an image from gallery
        binding.uploadImageView.setOnClickListener {
            openGallery()
        }

        // Set click listener for the uploadButton to upload course data
        binding.uploadButton.setOnClickListener {
            uploadCourseData()
        }

        return view
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun uploadCourseData() {
        val title = binding.titleEditText.text.toString().trim()
        val playlistUrl = binding.urlEditText.text.toString().trim()

        if (selectedImageUri != null && title.isNotEmpty() && playlistUrl.isNotEmpty()) {
            // Load the selected image into a Bitmap
            val inputStream = requireContext().contentResolver.openInputStream(selectedImageUri!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            // Compress the Bitmap
            val compressedBitmap = compressBitmap(bitmap)

            // Create a ByteArrayOutputStream to store the compressed image
            val baos = ByteArrayOutputStream()
            compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos) // Adjust quality as needed

            // Create a byte array from the ByteArrayOutputStream
            val imageData = baos.toByteArray()

            val courseImageRef = storageReference.child("course_images")
            courseImageRef.putBytes(imageData)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnCompleteListener { imageUrlTask ->
                        val imageUrl = imageUrlTask.result.toString()

                        // Create a CourseData object with the uploaded image URL and other data
                        val courseData = CourseData(title, imageUrl, playlistUrl)

                        // Add courseData to Firestore collection
                        firestore.collection("courses")
                            .add(courseData)
                            .addOnSuccessListener {
                                // Course data uploaded successfully
                                // Implement your desired action (e.g., show a success message)
                                Toast.makeText(requireContext(), "Course upload successfully", Toast.LENGTH_SHORT).show()
                                val fragmentManager: androidx.fragment.app.FragmentManager = requireActivity().supportFragmentManager
                                val coursesFragment = CoursesFragment()

                                fragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainer, coursesFragment)
                                    .commit()
                            }
                            .addOnFailureListener {
                                // Handle upload failure
                                Toast.makeText(requireContext(), "Check your internet connection", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener {
                    // Handle image upload failure
                    Toast.makeText(requireContext(), "Check your internet connection", Toast.LENGTH_SHORT).show()
                }
        }
        // Navigate back to CoursesFragment
    }

    private fun compressBitmap(bitmap: Bitmap): Bitmap {
        // Calculate new dimensions based on your desired resolution
        val maxWidth = 800 // Adjust as needed
        val maxHeight = 600 // Adjust as needed

        val scale = (maxWidth.toFloat() / bitmap.width).coerceAtMost(maxHeight.toFloat() / bitmap.height)
        val newWidth = (bitmap.width * scale).toInt()
        val newHeight = (bitmap.height * scale).toInt()

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            binding.uploadImageView.setImageURI(selectedImageUri)
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
