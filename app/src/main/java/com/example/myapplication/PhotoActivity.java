
package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.TagAdapter;
import com.example.myapplication.model.Album;
import com.example.myapplication.model.Photo;
import com.example.myapplication.model.Tag;
import com.example.myapplication.model.TagType;
import com.example.myapplication.util.SearchManager;

import java.util.List;

public class PhotoActivity extends AppCompatActivity implements TagAdapter.OnTagClickListener {

    private Album album;
    private int photoIndex;
    private Photo currentPhoto;
    private List<Photo> albumPhotos;

    private ImageView photoView;
    private TextView photoFilename;
    private RecyclerView tagsList;
    private TagAdapter tagAdapter;
    private TextView noTagsMessage;
    private boolean slideshowMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        album = (Album) getIntent().getSerializableExtra("album");
        photoIndex = getIntent().getIntExtra("photoIndex", 0);

        if (album == null) {
            finish();
            return;
        }

        albumPhotos = album.getPhotos();
        currentPhoto = albumPhotos.get(photoIndex);

        photoView = findViewById(R.id.photo_view);
        photoFilename = findViewById(R.id.photo_filename);
        noTagsMessage = findViewById(R.id.no_tags_message);

        // Setup tags RecyclerView
        tagsList = findViewById(R.id.tags_list);
        tagAdapter = new TagAdapter(this, currentPhoto.getTags(), this);
        tagsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tagsList.setAdapter(tagAdapter);

        updatePhoto();

        // Setup buttons
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        Button prevButton = findViewById(R.id.prev_button);
        prevButton.setOnClickListener(v -> previousPhoto());

        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(v -> nextPhoto());

        Button addTagButton = findViewById(R.id.add_tag_button);
        addTagButton.setOnClickListener(v -> showAddTagDialog());

        Button deleteTagButton = findViewById(R.id.delete_tag_button);
        deleteTagButton.setOnClickListener(v -> showDeleteTagDialog());

        Button slideshowButton = findViewById(R.id.slideshow_button);
        slideshowButton.setOnClickListener(v -> toggleSlideshow());
    }

    private void updatePhoto() {
        if (currentPhoto == null || photoIndex < 0 || photoIndex >= albumPhotos.size()) {
            return;
        }

        currentPhoto = albumPhotos.get(photoIndex);

        // Display photo
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhoto.getImagePath());
        if (bitmap != null) {
            photoView.setImageBitmap(bitmap);
        }

        // Display filename
        photoFilename.setText(currentPhoto.getFilename());

        // Update tags
        tagAdapter.updateTags(currentPhoto.getTags());
        updateTagsVisibility();
    }

    private void updateTagsVisibility() {
        if (currentPhoto.getTags().isEmpty()) {
            noTagsMessage.setVisibility(View.VISIBLE);
        } else {
            noTagsMessage.setVisibility(View.GONE);
        }
    }

    private void previousPhoto() {
        if (photoIndex > 0) {
            photoIndex--;
            updatePhoto();
        } else {
            Toast.makeText(this, "First photo", Toast.LENGTH_SHORT).show();
        }
    }

    private void nextPhoto() {
        if (photoIndex < albumPhotos.size() - 1) {
            photoIndex++;
            updatePhoto();
        } else {
            Toast.makeText(this, "Last photo", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleSlideshow() {
        slideshowMode = !slideshowMode;
        if (slideshowMode) {
            Toast.makeText(this, "Slideshow on - use arrows to navigate", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Slideshow off", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddTagDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Tag");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_tag, null);
        Spinner tagTypeSpinner = view.findViewById(R.id.tag_type_spinner);
        AutoCompleteTextView tagValueInput = view.findViewById(R.id.tag_value_input);

        // Setup tag type spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Person", "Location"});
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagTypeSpinner.setAdapter(spinnerAdapter);

        // Setup autocomplete for tag values
        List<String> suggestions = SearchManager.getTagValueSuggestions(
                List.of(album),
                TagType.PERSON);
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, suggestions);
        tagValueInput.setAdapter(autoCompleteAdapter);
        tagValueInput.setThreshold(1);

        builder.setView(view);
        builder.setPositiveButton("OK", (dialog, which) -> {
            String tagTypeStr = tagTypeSpinner.getSelectedItem().toString();
            String tagValue = tagValueInput.getText().toString().trim();

            if (!tagValue.isEmpty()) {
                TagType tagType = TagType.fromString(tagTypeStr);
                Tag newTag = new Tag(tagType, tagValue);
                currentPhoto.addTag(newTag);
                tagAdapter.updateTags(currentPhoto.getTags());
                updateTagsVisibility();
                Toast.makeText(PhotoActivity.this, "Tag added", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDeleteTagDialog() {
        if (currentPhoto.getTags().isEmpty()) {
            Toast.makeText(this, "No tags to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Tags to Delete");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_delete_tag, null);
        RecyclerView tagsToDeleteList = view.findViewById(R.id.tags_to_delete_list);
        TextView noTagsMsg = view.findViewById(R.id.no_tags_message);

        if (currentPhoto.getTags().isEmpty()) {
            noTagsMsg.setVisibility(View.VISIBLE);
            tagsToDeleteList.setVisibility(View.GONE);
        } else {
            TagAdapter deleteAdapter = new TagAdapter(this, currentPhoto.getTags());
            deleteAdapter.setSelectionMode(true, true);
            tagsToDeleteList.setLayoutManager(new LinearLayoutManager(this));
            tagsToDeleteList.setAdapter(deleteAdapter);

            builder.setView(view);
            builder.setPositiveButton("Delete", (dialog, which) -> {
                List<Tag> toDelete = deleteAdapter.getSelectedTags();
                for (Tag tag : toDelete) {
                    currentPhoto.removeTag(tag);
                }
                tagAdapter.updateTags(currentPhoto.getTags());
                updateTagsVisibility();
                Toast.makeText(PhotoActivity.this, toDelete.size() + " tag(s) deleted", Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        }
    }

    @Override
    public void onTagClick(Tag tag) {
        // Can be used for viewing tag details in future
    }

    @Override
    public void onTagDelete(Tag tag) {
        currentPhoto.removeTag(tag);
        tagAdapter.updateTags(currentPhoto.getTags());
        updateTagsVisibility();
        Toast.makeText(this, "Tag deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent();
        intent.putExtra("album", album);
        setResult(RESULT_OK, intent);
    }
}
