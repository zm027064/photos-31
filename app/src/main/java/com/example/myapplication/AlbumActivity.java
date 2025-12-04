
package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.PhotoAdapter;
import com.example.myapplication.model.Album;
import com.example.myapplication.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class AlbumActivity extends AppCompatActivity implements PhotoAdapter.OnPhotoClickListener {

    private static final int PICK_IMAGE = 1;
    private static final int PHOTO_REQUEST_CODE = 2;
    private Album album;
    private List<Photo> photos;
    private RecyclerView photoGrid;
    private PhotoAdapter photoAdapter;
    private TextView albumTitle;
    private TextView emptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        album = (Album) getIntent().getSerializableExtra("album");
        if (album != null) {
            photos = new ArrayList<>(album.getPhotos());
        } else {
            photos = new ArrayList<>();
        }

        albumTitle = findViewById(R.id.album_name_title);
        if (album != null) {
            albumTitle.setText(album.getName());
        }

        emptyMessage = findViewById(R.id.empty_photos_message);
        photoGrid = findViewById(R.id.photo_grid);
        photoAdapter = new PhotoAdapter(this, photos, this);
        photoGrid.setLayoutManager(new GridLayoutManager(this, 3));
        photoGrid.setAdapter(photoAdapter);

        updateEmptyState();

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        Button addPhotoButton = findViewById(R.id.add_photo_button);
        addPhotoButton.setOnClickListener(v -> addPhoto());

        Button removePhotoButton = findViewById(R.id.remove_photo_button);
        removePhotoButton.setOnClickListener(v -> removePhoto());

        Button movePhotoButton = findViewById(R.id.move_photo_button);
        movePhotoButton.setOnClickListener(v -> movePhoto());
    }

    private void addPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void removePhoto() {
        List<Photo> selectedPhotos = photoAdapter.getSelectedPhotos();
        if (!selectedPhotos.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Remove Photos?");
            builder.setMessage("Remove " + selectedPhotos.size() + " photo(s)?");
            builder.setPositiveButton("Remove", (dialog, which) -> {
                for (Photo photo : selectedPhotos) {
                    photos.remove(photo);
                    if (album != null) {
                        album.removePhoto(photo);
                    }
                }
                photoAdapter.clearSelection();
                photoAdapter.updatePhotos(photos);
                updateEmptyState();
                Toast.makeText(AlbumActivity.this, "Photos removed", Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        } else {
            Toast.makeText(this, "Select photos to remove", Toast.LENGTH_SHORT).show();
        }
    }

    private void movePhoto() {
        List<Photo> selectedPhotos = photoAdapter.getSelectedPhotos();
        if (!selectedPhotos.isEmpty()) {
            // For now, just show a message
            // Full implementation would require access to all albums
            Toast.makeText(this, "Move feature requires access to all albums", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Select photos to move", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Photo newPhoto = new Photo(picturePath);
                photos.add(newPhoto);
                if (album != null) {
                    album.addPhoto(newPhoto);
                }
                photoAdapter.updatePhotos(photos);
                updateEmptyState();
                Toast.makeText(this, "Photo added", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            photoAdapter.clearSelection();
        }
    }

    private void updateEmptyState() {
        if (photos.isEmpty()) {
            photoGrid.setVisibility(RecyclerView.GONE);
            emptyMessage.setVisibility(TextView.VISIBLE);
        } else {
            photoGrid.setVisibility(RecyclerView.VISIBLE);
            emptyMessage.setVisibility(TextView.GONE);
        }
    }

    @Override
    public void onPhotoClick(Photo photo) {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("album", album);
        intent.putExtra("photo", photo);
        intent.putExtra("photoIndex", photos.indexOf(photo));
        startActivityForResult(intent, PHOTO_REQUEST_CODE);
    }

    @Override
    public void onPhotoLongClick(Photo photo) {
        Toast.makeText(this, "Hold to select multiple photos for deletion", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (album != null) {
            Intent intent = new Intent();
            intent.putExtra("album", album);
            setResult(RESULT_OK, intent);
        }
    }
}
