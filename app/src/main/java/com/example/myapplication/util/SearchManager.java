package com.example.myapplication.util;

import com.example.myapplication.model.Album;
import com.example.myapplication.model.Photo;
import com.example.myapplication.model.Tag;
import com.example.myapplication.model.TagType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchManager {

    public enum SearchOperator {
        AND, OR
    }

    /**
     * Search photos by a single tag criterion
     */
    public static List<Photo> searchByTag(List<Album> albums, TagType tagType, String tagValue) {
        List<Photo> results = new ArrayList<>();
        String searchValue = tagValue.toLowerCase();

        for (Album album : albums) {
            for (Photo photo : album.getPhotos()) {
                for (Tag tag : photo.getTags()) {
                    if (tag.getTagType() == tagType &&
                        tag.getTagValue().toLowerCase().startsWith(searchValue)) {
                        if (!results.contains(photo)) {
                            results.add(photo);
                        }
                        break;
                    }
                }
            }
        }
        return results;
    }

    /**
     * Search photos by two tag criteria with AND/OR logic
     */
    public static List<Photo> searchByTags(List<Album> albums, 
                                          TagType tagType1, String tagValue1,
                                          TagType tagType2, String tagValue2,
                                          SearchOperator operator) {
        List<Photo> results1 = searchByTag(albums, tagType1, tagValue1);
        List<Photo> results2 = searchByTag(albums, tagType2, tagValue2);

        if (operator == SearchOperator.AND) {
            results1.retainAll(results2);
            return results1;
        } else { // OR
            Set<Photo> combined = new HashSet<>(results1);
            combined.addAll(results2);
            return new ArrayList<>(combined);
        }
    }

    /**
     * Get all tag values for a specific tag type (for autocomplete)
     */
    public static List<String> getTagValueSuggestions(List<Album> albums, TagType tagType) {
        Set<String> suggestions = new HashSet<>();
        for (Album album : albums) {
            for (Photo photo : album.getPhotos()) {
                for (Tag tag : photo.getTags()) {
                    if (tag.getTagType() == tagType) {
                        suggestions.add(tag.getTagValue());
                    }
                }
            }
        }
        List<String> sorted = new ArrayList<>(suggestions);
        java.util.Collections.sort(sorted, String.CASE_INSENSITIVE_ORDER);
        return sorted;
    }

    /**
     * Get autocomplete suggestions starting with prefix
     */
    public static List<String> getAutocompleteSuggestions(List<Album> albums, 
                                                         TagType tagType, String prefix) {
        List<String> allSuggestions = getTagValueSuggestions(albums, tagType);
        List<String> filtered = new ArrayList<>();
        String lowerPrefix = prefix.toLowerCase();

        for (String suggestion : allSuggestions) {
            if (suggestion.toLowerCase().startsWith(lowerPrefix)) {
                filtered.add(suggestion);
            }
        }
        return filtered;
    }
}
