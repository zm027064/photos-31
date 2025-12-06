package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Photo;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private List<Photo> photos;
    private Context context;
    private OnPhotoClickListener listener;

    public interface OnPhotoClickListener {
        void onPhotoClick(Photo photo);
    }

    public PhotoAdapter(Context context, List<Photo> photos, OnPhotoClickListener listener) {
        this.context = context;
        this.photos = photos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = photos.get(position);

        try {
            holder.thumbnail.setImageBitmap(BitmapFactory.decodeFile(photo.getImagePath()));
        } catch (Exception e) {
            holder.thumbnail.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        holder.filename.setText(photo.getFilename());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPhotoClick(photo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void updatePhotos(List<Photo> newPhotos) {
        this.photos = newPhotos;
        notifyDataSetChanged();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView filename;

        PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.photo_thumbnail);
            filename = itemView.findViewById(R.id.photo_filename);
        }
    }
}
