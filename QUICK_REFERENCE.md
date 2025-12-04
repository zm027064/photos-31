# Quick Reference - Activity & Layout Mapping

## Activity → Layout Mapping

### MainActivity
**Layout**: `activity_main.xml`
- **Header**: Title "My Albums" + "New Album" button
- **Search Bar**: EditText + Search button
- **Content**: RecyclerView displaying items from `item_album.xml`
- **ID References**:
  - `album_list` (RecyclerView)
  - `create_album_button` (Button)
  - `search_button` (Button)
  - `search_query` (EditText)

### AlbumActivity
**Layout**: `activity_album.xml`
**Receives**: Album name via Intent extras
- **Header**: Back button + Album name + Menu
- **Content**: RecyclerView grid displaying items from `item_photo.xml`
- **Buttons**: Add Photo, Remove Photo, Move Photo
- **ID References**:
  - `back_button`
  - `album_name_title`
  - `photo_grid` (RecyclerView)
  - `add_photo_button`
  - `remove_photo_button`
  - `move_photo_button`

### PhotoActivity
**Layout**: `activity_photo.xml`
**Receives**: Photo file path, current album, photo index
- **Header**: Back button + Filename + Slideshow button
- **Main**: ImageView for photo display
- **Tags**: RecyclerView displaying items from `item_tag.xml`
- **Controls**: Previous, Add Tag, Delete Tag, Next buttons
- **ID References**:
  - `photo_view` (ImageView)
  - `tags_list` (RecyclerView)
  - `prev_button`, `next_button`
  - `add_tag_button`, `delete_tag_button`
  - `slideshow_button`

### SearchActivity
**Layout**: `activity_search.xml`
- **Criteria 1**: Tag type spinner + AutoCompleteTextView
- **Operator**: AND/OR spinner
- **Criteria 2**: Optional second tag type + value
- **Results**: RecyclerView grid with `item_photo.xml`
- **ID References**:
  - `tag_type_spinner`, `tag_type_spinner2`
  - `tag_value_input`, `tag_value_input2`
  - `operator_spinner`
  - `search_button`
  - `search_results_grid`

---

## Dialog Layouts

### Create Album
```java
// Show dialog
DialogFragment dialog = CreateAlbumDialog.newInstance();
dialog.show(getSupportFragmentManager(), "create_album");
```
**Layout**: `dialog_create_album.xml`
**IDs**: `album_name_input`, `dialog_ok_button`, `dialog_cancel_button`

### Add Tag
```java
// Spinner values: Person, Location
// AutoComplete suggestions from existing tags
```
**Layout**: `dialog_add_tag.xml`
**IDs**: `tag_type_spinner`, `tag_value_input`

### Move Photo
```java
// Spinner populated with all albums except current
```
**Layout**: `dialog_move_photo.xml`
**IDs**: `target_album_spinner`

### Delete Tag
```java
// RecyclerView with checkboxes for each tag
```
**Layout**: `dialog_delete_tag.xml`
**IDs**: `tags_to_delete_list`

---

## RecyclerView Adapters Needed

| RecyclerView | Item Layout | Adapter Class |
|--------------|-------------|---------------|
| `album_list` (MainActivity) | `item_album.xml` | `AlbumAdapter` |
| `photo_grid` (AlbumActivity) | `item_photo.xml` | `PhotoAdapter` |
| `tags_list` (PhotoActivity) | `item_tag.xml` | `TagAdapter` |
| `search_results_grid` (SearchActivity) | `item_photo.xml` | `PhotoAdapter` |
| `tags_to_delete_list` (DeleteTagDialog) | Check item layout | `TagCheckboxAdapter` |

---

## Key View IDs Reference

### Buttons
- `create_album_button`, `back_button`
- `search_button`, `add_photo_button`, `remove_photo_button`, `move_photo_button`
- `prev_button`, `next_button`
- `add_tag_button`, `delete_tag_button`
- `album_options_menu`, `slideshow_button`

### Input Fields
- `search_query` (EditText)
- `album_name_input` (EditText in dialog)
- `tag_value_input` (AutoCompleteTextView)
- `tag_value_input2` (AutoCompleteTextView)

### Spinners
- `tag_type_spinner` (Person/Location)
- `tag_type_spinner2` (Person/Location)
- `operator_spinner` (AND/OR)
- `target_album_spinner` (Album names)

### Lists & Grids
- `album_list` (RecyclerView - albums)
- `photo_grid` (RecyclerView - photos in album)
- `tags_list` (RecyclerView - tags on photo)
- `search_results_grid` (RecyclerView - search results)

### Text Views
- `album_name_title` (Album name header)
- `photo_filename` (Photo filename display)
- `no_albums_message`, `empty_photos_message`, `no_tags_message`
- `no_search_results`, `no_results_message`

### Image Views
- `photo_view` (Main photo display)
- `album_thumbnail` (In `item_album.xml`)
- `photo_thumbnail` (In `item_photo.xml`)

---

## String Resource Categories

### Navigation
- `back`, `search`, `ok`, `cancel`, `move`, `delete`

### Screens
- `my_albums`, `new_album`, `search_photos`
- `add_photo`, `remove_photo`, `move_photo`
- `add_tag`, `delete_tag`, `slideshow`

### Messages
- `no_albums_message`, `no_photos_in_album`, `no_tags`
- `no_search_results`, `tag_value`, `album_name_hint`

### Tags
- `person`, `location`, `tags_label`, `tag_type`

---

## Layout Inflation Pattern

```java
// In Adapter
@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_album, parent, false);
    return new ViewHolder(view);
}

// In Activity
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    // Now findViewById() will work
}
```

---

## Fragment Compatibility

For dialogs, use DialogFragment:
```java
public class CreateAlbumDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
            .inflate(R.layout.dialog_create_album, null);
        return new AlertDialog.Builder(getActivity())
            .setView(view)
            .create();
    }
}
```
