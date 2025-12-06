package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.PhotoAdapter;
import com.example.myapplication.model.Album;
import com.example.myapplication.model.Photo;
import com.example.myapplication.model.TagType;
import com.example.myapplication.util.DataStore;
import com.example.myapplication.util.SearchManager;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements PhotoAdapter.OnPhotoClickListener {

    private List<Album> albums;
    private Spinner tagType1Spinner, tagType2Spinner, operatorSpinner;
    private AutoCompleteTextView tagValue1Input, tagValue2Input;
    private RecyclerView resultsGrid;
    private PhotoAdapter resultsAdapter;
    private TextView noResultsMessage;
    private TextView resultsTitle;
    private List<Photo> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        albums = DataStore.getAlbums(this);
        if (albums == null) albums = new ArrayList<>();

        searchResults = new ArrayList<>();

        tagType1Spinner = findViewById(R.id.tag_type_spinner);
        tagType2Spinner = findViewById(R.id.tag_type_spinner2);
        operatorSpinner = findViewById(R.id.operator_spinner);

        setupSpinner(tagType1Spinner, new String[]{"Person", "Location"});
        setupSpinner(tagType2Spinner, new String[]{"Person", "Location"});
        setupSpinner(operatorSpinner, new String[]{"AND", "OR"});

        tagValue1Input = findViewById(R.id.tag_value_input);
        tagValue2Input = findViewById(R.id.tag_value_input2);

        updateAutocompleteSuggestions();

        resultsGrid = findViewById(R.id.search_results_grid);
        noResultsMessage = findViewById(R.id.no_results_message);
        resultsTitle = findViewById(R.id.results_title);
        resultsAdapter = new PhotoAdapter(this, searchResults, this);
        resultsGrid.setLayoutManager(new GridLayoutManager(this, 3));
        resultsGrid.setAdapter(resultsAdapter);

        findViewById(R.id.back_button).setOnClickListener(v -> finish());
        findViewById(R.id.search_button).setOnClickListener(v -> performSearch());

        tagType1Spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                updateTagValue1Suggestions();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        tagType2Spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                updateTagValue2Suggestions();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void setupSpinner(Spinner spinner, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void updateAutocompleteSuggestions() {
        updateTagValue1Suggestions();
        updateTagValue2Suggestions();
    }

    private void updateTagValue1Suggestions() {
        TagType type = getSelectedTagType(tagType1Spinner);
        List<String> suggestions = SearchManager.getTagValueSuggestions(albums, type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, suggestions);
        tagValue1Input.setAdapter(adapter);
    }

    private void updateTagValue2Suggestions() {
        TagType type = getSelectedTagType(tagType2Spinner);
        List<String> suggestions = SearchManager.getTagValueSuggestions(albums, type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, suggestions);
        tagValue2Input.setAdapter(adapter);
    }

    private TagType getSelectedTagType(Spinner spinner) {
        String selected = spinner.getSelectedItem().toString();
        return TagType.fromString(selected);
    }

    private void performSearch() {
        String tagValue1 = tagValue1Input.getText().toString().trim();
        if (tagValue1.isEmpty()) {
            Toast.makeText(this, "Enter at least one tag value", Toast.LENGTH_SHORT).show();
            return;
        }

        TagType type1 = getSelectedTagType(tagType1Spinner);
        String tagValue2 = tagValue2Input.getText().toString().trim();

        if (tagValue2.isEmpty()) {
            searchResults = SearchManager.searchByTag(albums, type1, tagValue1);
        } else {
            TagType type2 = getSelectedTagType(tagType2Spinner);
            String operatorStr = operatorSpinner.getSelectedItem().toString();
            SearchManager.SearchOperator operator = operatorStr.equals("AND") ?
                    SearchManager.SearchOperator.AND : SearchManager.SearchOperator.OR;

            searchResults = SearchManager.searchByTags(albums, type1, tagValue1, type2, tagValue2, operator);
        }

        resultsAdapter.updatePhotos(searchResults);
        updateResultsDisplay();
    }

    private void updateResultsDisplay() {
        if (searchResults.isEmpty()) {
            resultsGrid.setVisibility(RecyclerView.GONE);
            noResultsMessage.setVisibility(TextView.VISIBLE);
            resultsTitle.setVisibility(TextView.GONE);
        } else {
            resultsGrid.setVisibility(RecyclerView.VISIBLE);
            noResultsMessage.setVisibility(TextView.GONE);
            resultsTitle.setVisibility(TextView.VISIBLE);
            resultsTitle.setText("Found " + searchResults.size() + " photo(s)");
        }
    }

    @Override
    public void onPhotoClick(Photo photo) {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("photoId", photo.getId());
        startActivity(intent);
    }
}
