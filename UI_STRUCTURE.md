# Android Photos App - UI Structure

## Overview
This document describes the XML UI layout structure for the Android Photos application. All UI elements have been built using Android XML layouts as required.

## Layout Files

### Main Activities

#### `activity_main.xml` (Home Screen)
**Purpose**: Displays all albums and provides navigation

**Components**:
- Header with app title and "New Album" button
- Search bar with EditText and Search button
- RecyclerView for displaying albums
- Empty state message

**Features**:
- Create new albums
- Search photos across all albums
- View all available albums

---

#### `activity_album.xml` (Album View)
**Purpose**: Shows photos within a selected album

**Components**:
- Header with back button, album name, and options menu
- RecyclerView grid for photo thumbnails
- Action buttons: Add Photo, Remove Photo, Move Photo
- Empty state message

**Features**:
- Add photos from device storage
- Remove selected photos
- Move photos to other albums
- Long-press for context menu (rename, delete album)

---

#### `activity_photo.xml` (Photo Display)
**Purpose**: Full-screen photo viewer with tagging and slideshow

**Components**:
- Header bar with back button, filename, and slideshow toggle
- Large ImageView for photo display
- Tags container showing all tags
- Navigation buttons (Previous, Next)
- Tag management buttons (Add Tag, Delete Tag)

**Features**:
- Display photo with proper scaling
- View all tags applied to photo
- Add new tags (Person or Location)
- Delete existing tags
- Manual slideshow navigation
- Displays filename

---

#### `activity_search.xml` (Search Screen)
**Purpose**: Search photos by tag criteria

**Components**:
- Header with back button
- First search criteria: Tag type spinner + AutoCompleteTextView
- Operator selector (AND/OR)
- Second search criteria: Optional tag type + AutoCompleteTextView
- Search button
- RecyclerView for results
- Empty state message

**Features**:
- Single or dual tag search
- Autocomplete for tag values
- AND/OR conjunction logic
- Case-insensitive matching
- Autocomplete suggestions from existing tags
- Results from all albums

---

### Item Templates

#### `item_album.xml`
**Purpose**: Individual album card in the albums list

**Components**:
- Album thumbnail (ImageView)
- Album name (TextView)
- Photo count (TextView)

**Used by**: RecyclerView in MainActivity

---

#### `item_photo.xml`
**Purpose**: Individual photo thumbnail in album/search results

**Components**:
- Photo thumbnail (ImageView)
- Photo filename (TextView)
- Selection checkbox (CheckBox)

**Used by**: RecyclerView in AlbumActivity and SearchActivity

---

#### `item_tag.xml`
**Purpose**: Individual tag badge displayed with photo

**Components**:
- Tag text (TextView)
- Delete button (ImageButton with close icon)

**Used by**: RecyclerView in PhotoActivity

---

### Dialog Layouts

#### `dialog_create_album.xml`
**Purpose**: Dialog for creating a new album

**Components**:
- Title
- Album name input field
- Cancel and OK buttons

**Triggered by**: "New Album" button on main screen

---

#### `dialog_add_tag.xml`
**Purpose**: Dialog for adding a tag to a photo

**Components**:
- Title
- Tag type spinner (Person/Location)
- Tag value input field with autocomplete
- Cancel and OK buttons

**Triggered by**: "Add Tag" button in photo view

---

#### `dialog_move_photo.xml`
**Purpose**: Dialog for moving a photo to another album

**Components**:
- Title
- Target album spinner
- Cancel and Move buttons

**Triggered by**: "Move Photo" button in album view

---

#### `dialog_delete_tag.xml`
**Purpose**: Dialog for selecting tags to delete

**Components**:
- Title
- List of checkable tags
- Cancel and Delete buttons

**Triggered by**: "Delete Tag" button in photo view

---

## Drawable Resources

### Background Shapes

| File | Purpose |
|------|---------|
| `edit_text_background.xml` | Rounded background for EditText/AutoCompleteTextView |
| `spinner_background.xml` | Rounded background for Spinner dropdowns |
| `tag_background.xml` | Light blue background for tag badges |
| `ic_close.xml` | Close icon vector drawable (24x24) |
| `ic_album_placeholder.xml` | Placeholder for album thumbnails |
| `ic_photo_placeholder.xml` | Placeholder for photo thumbnails |

---

## String Resources (`strings.xml`)

All UI text is externalized for internationalization support. Categories include:

- **Main Screen**: Albums, New Album, Search, No Albums message
- **Album Screen**: Photos, Add/Remove/Move Photo, Empty album message
- **Photo Screen**: Tags, Slideshow, Add/Delete Tag
- **Search Screen**: Results, No results message, Tag criteria
- **Dialogs**: Titles, labels, button text
- **Tag Types**: Person, Location

---

## Design Principles

### Responsive Layout
- Uses `match_parent` and `wrap_content` appropriately
- Weight-based sizing for flexible layouts
- Proper padding and margins for spacing

### User Experience
- Clear hierarchies with header bars
- Back buttons for navigation
- Empty state messages guide users
- Grouped related actions
- Consistent button placement

### Accessibility
- All text uses readable font sizes
- Sufficient color contrast
- Proper labeling for all fields
- EditText and Spinner backgrounds differentiated

### Modern Android Design
- RecyclerView instead of ListView for efficiency
- Proper spacing and padding (8dp, 12dp, 16dp, 24dp grid)
- Themed colors and rounded corners
- Gesture-friendly button sizes (48dp minimum)

---

## XML Namespaces Used

```xml
xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:app="http://schemas.android.com/apk/res-auto"
xmlns:tools="http://schemas.android.com/tools"
```

- **android**: Standard Android attributes
- **app**: AppCompat and Material Design attributes
- **tools**: Preview and design-time attributes

---

## Layout Structure Summary

```
res/layout/
├── activity_main.xml              (Home screen - Albums list)
├── activity_album.xml             (Album view - Photos grid)
├── activity_photo.xml             (Photo display - Fullscreen viewer)
├── activity_search.xml            (Search - Tag-based photo search)
├── item_album.xml                 (Album card in list)
├── item_photo.xml                 (Photo thumbnail in grid)
├── item_tag.xml                   (Tag badge with delete)
├── dialog_create_album.xml        (Create album dialog)
├── dialog_add_tag.xml             (Add tag dialog)
├── dialog_move_photo.xml          (Move photo dialog)
└── dialog_delete_tag.xml          (Delete tag dialog)

res/drawable/
├── edit_text_background.xml       (EditText styling)
├── spinner_background.xml         (Spinner styling)
├── tag_background.xml             (Tag badge styling)
├── ic_close.xml                   (Close icon)
├── ic_album_placeholder.xml       (Album placeholder)
└── ic_photo_placeholder.xml       (Photo placeholder)

res/values/
└── strings.xml                    (All UI text strings)
```

---

## Implementation Notes

1. **RecyclerView**: Used instead of GridView for better performance and flexibility
2. **AutoCompleteTextView**: Provides tag value suggestions with autocomplete
3. **Spinners**: Handle tag type selection and search operators
4. **RelativeLayout**: Used for complex photo display layout with overlays
5. **LinearLayout**: Primary layout for most screens with vertical/horizontal orientation

All layouts follow Android best practices and are compatible with API 36 and above.
