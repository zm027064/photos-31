
package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adapter.AlbumAdapter;
import com.example.myapplication.model.Album;
import com.example.myapplication.util.StorageManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AlbumAdapter.OnAlbumClickListener {

    private static final int ALBUM_REQUEST_CODE = 1;
    private List<Album> albums;
    private RecyclerView albumRecyclerView;
    private AlbumAdapter albumAdapter;
    private Album selectedAlbum = null;
    private TextView emptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load albums
        albums = StorageManager.loadAlbums(this);
        if (albums == null) {
            albums = new ArrayList<>();
        }

        // Setup RecyclerView
        albumRecyclerView = findViewById(R.id.album_list);
        emptyMessage = findViewById(R.id.empty_albums_message);
        albumAdapter = new AlbumAdapter(this, albums, this);
        albumRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        albumRecyclerView.setAdapter(albumAdapter);

        updateEmptyState();

        // Setup buttons
        Button createButton = findViewById(R.id.create_album_button);
        createButton.setOnClickListener(v -> createAlbum());

        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> openSearch());
    }

    private void createAlbum() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create New Album");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_create_album, null);
        EditText input = view.findViewById(R.id.album_name_input);
        builder.setView(view);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String albumName = input.getText().toString().trim();
            if (!albumName.isEmpty()) {
                if (albumExists(albumName)) {
                    Toast.makeText(MainActivity.this, "Album already exists", Toast.LENGTH_SHORT).show();
                } else {
                    Album newAlbum = new Album(albumName);
                    albums.add(newAlbum);
                    albumAdapter.notifyDataSetChanged();
                    StorageManager.saveAlbums(this, albums);
                    updateEmptyState();
                }
            } else {
                Toast.makeText(MainActivity.this, "Enter album name", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void renameAlbum(Album album) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rename Album");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_create_album, null);
        EditText input = view.findViewById(R.id.album_name_input);
        input.setText(album.getName());
        builder.setView(view);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (!newName.isEmpty()) {
                if (!newName.equals(album.getName()) && albumExists(newName)) {
                    Toast.makeText(MainActivity.this, "Album already exists", Toast.LENGTH_SHORT).show();
                } else {
                    album.setName(newName);
                    albumAdapter.notifyDataSetChanged();
                    StorageManager.saveAlbums(this, albums);
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteAlbum(Album album) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Album?");
        builder.setMessage("Are you sure you want to delete \"" + album.getName() + "\"?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            albums.remove(album);
            albumAdapter.notifyDataSetChanged();
            StorageManager.saveAlbums(this, albums);
            updateEmptyState();
            Toast.makeText(MainActivity.this, "Album deleted", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void openSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("albums", new ArrayList<>(albums));
        startActivity(intent);
    }

    private void updateEmptyState() {
        if (albums.isEmpty()) {
            albumRecyclerView.setVisibility(RecyclerView.GONE);
            emptyMessage.setVisibility(TextView.VISIBLE);
        } else {
            albumRecyclerView.setVisibility(RecyclerView.VISIBLE);
            emptyMessage.setVisibility(TextView.GONE);
        }
    }

    private boolean albumExists(String name) {
        for (Album album : albums) {
            if (album.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onAlbumClick(Album album) {
        Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
        intent.putExtra("album", album);
        startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }

    @Override
    public void onAlbumLongClick(Album album) {
        String[] options = {"Rename", "Delete", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(album.getName());
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                renameAlbum(album);
            } else if (which == 1) {
                deleteAlbum(album);
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ALBUM_REQUEST_CODE && resultCode == RESULT_OK) {
            albums = StorageManager.loadAlbums(this);
            albumAdapter.updateAlbums(albums);
            updateEmptyState();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StorageManager.saveAlbums(this, albums);
    }
}
