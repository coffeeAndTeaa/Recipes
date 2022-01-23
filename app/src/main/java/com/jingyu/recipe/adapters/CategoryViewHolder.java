package com.jingyu.recipe.adapters;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.jingyu.recipe.R;
import com.jingyu.recipe.models.Recipe;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    CircleImageView categoryImage;
    TextView categoryTitle;
    OnRecipeListener listener;
    RequestManager requestManager;

    public CategoryViewHolder(@NonNull View itemView, OnRecipeListener listener, RequestManager requestManager) {
        super(itemView);
        categoryImage = itemView.findViewById(R.id.category_image);
        categoryTitle = itemView.findViewById(R.id.category_title);
        this.listener = listener;
        this.requestManager = requestManager;

        itemView.setOnClickListener(this);
    }

    public void onBind(Recipe recipe) {
        Uri path = Uri.parse("android.resource://com.jingyu.recipe/drawable/" + recipe.getImageUrl());
        Log.d("CategoryViewHolder", "onBind: " + path);
        Log.d("CategoryViewHolder", "onBind: " + recipe.toString());
        requestManager
                .load(path)
                .into(categoryImage);

        categoryTitle.setText(recipe.getTitle());
    }

    @Override
    public void onClick(View view) {
        listener.onCategoryClick(categoryTitle.getText().toString());
    }
}
