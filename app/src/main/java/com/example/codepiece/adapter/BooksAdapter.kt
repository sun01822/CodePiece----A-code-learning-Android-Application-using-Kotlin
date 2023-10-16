package com.example.codepiece.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.codepiece.R
import com.example.codepiece.data.Book

class BooksAdapter(private val books: List<Book>) : RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.item_name)
        val authorTextView: TextView = itemView.findViewById(R.id.author_name)
        val coverImageView: ImageView = itemView.findViewById(R.id.item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_books_layout, parent, false)
        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentBook = books[position]
        holder.titleTextView.text = currentBook.title
        holder.authorTextView.text = currentBook.author

        // You can use a library like Picasso or Glide to load images efficiently
        // Example with Picasso:
        // Picasso.get().load(currentBook.coverImageUrl).into(holder.coverImageView)

        // Example with Glide:
        Glide.with(holder.coverImageView.context).load(currentBook.imageUrl).into(holder.coverImageView)

        // Set a click listener on the item view
        holder.itemView.setOnClickListener {
            // Handle item click to download the book
            downloadBook(holder.itemView.context, currentBook.downloadLink)
        }

    }
    override fun getItemCount(): Int {
        return books.size
    }

    private fun downloadBook(context: Context, downloadLink: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(downloadLink))
        context.startActivity(intent)
    }
}
