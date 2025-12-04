package com.example.myapplication.util;

import android.content.Context;

import com.example.myapplication.model.Album;
import com.example.myapplication.model.Photo;
import com.example.myapplication.model.Tag;
import com.example.myapplication.model.TagType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StorageManager {
    private static final String ALBUMS_FILENAME = "albums.dat";

    /**
     * Save all albums to file
     */
    public static void saveAlbums(Context context, List<Album> albums) {
        try {
            File filesDir = context.getFilesDir();
            File file = new File(filesDir, ALBUMS_FILENAME);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ArrayList<>(albums));
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load all albums from file
     */
    @SuppressWarnings("unchecked")
    public static List<Album> loadAlbums(Context context) {
        try {
            File filesDir = context.getFilesDir();
            File file = new File(filesDir, ALBUMS_FILENAME);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            List<Album> albums = (List<Album>) ois.readObject();
            ois.close();
            fis.close();
            return albums != null ? albums : new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Get all albums' names for autocomplete suggestions
     */
    public static List<String> getAllAlbumNames(List<Album> albums) {
        List<String> names = new ArrayList<>();
        for (Album album : albums) {
            names.add(album.getName());
        }
        return names;
    }
}
