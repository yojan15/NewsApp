package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.data.Article

class NewsAdapter(private val onItemListener: OnItemClickListener) :
    ListAdapter<Article, NewsAdapter.NewsViewHolder>(ArticleDiffCallback()) {

    private var savedArticle: List<Article> = emptyList()

    fun setSavedArticles(articles: List<Article>) {
        savedArticle = articles
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onTitleClick(article: Article)
        fun onImageOrDescriptionClick(article: Article)
        fun onTitleLongClick(article: Article, view: View): Boolean
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = currentList[position]
        holder.bind(article)
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView =
            itemView.findViewById(R.id.descriptionTextView)
        private val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)

        private var isTitleClicked = false

        init {
            itemView.setOnClickListener(this)
            titleTextView.setOnClickListener(this)
            titleTextView.setOnLongClickListener {
                onItemListener.onTitleLongClick(getItem(adapterPosition), itemView)
            }
        }

        fun bind(article: Article) {
            Glide.with(itemView.context)
                .load(article.urlToImage)
                .placeholder(R.drawable.placeholder_image)
                .into(imageView)

            titleTextView.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (isTitleClicked) R.color.blue else R.color.grey
                )
            )

            titleTextView.text = article.title
            descriptionTextView.text = article.description
            authorTextView.text = article.author
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                when (v?.id) {
                    R.id.titleTextView -> {
                        isTitleClicked = !isTitleClicked
                        titleTextView.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                if (isTitleClicked) R.color.blue else R.color.grey
                            )
                        )
                        onItemListener.onTitleClick(getItem(position))
                    }
                    else -> onItemListener.onImageOrDescriptionClick(getItem(position))
                }
            }
        }
    }
}

class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}
