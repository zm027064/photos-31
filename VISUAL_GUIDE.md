# Android Photos App - Visual Navigation Map

## App Flow Diagram

```
┌─────────────────────────────────────────────────────────┐
│                      MAIN ACTIVITY                      │
│                    (Home Screen)                        │
│                                                         │
│  [My Albums] ┌────────────────────────────────┐ [New]   │
│              │ Album 1 (5 photos)             │         │
│              │ Album 2 (3 photos)             │         │
│              │ Album 3 (12 photos)            │         │
│              │ (Long-press: Rename/Delete)    │         │
│              └────────────────────────────────┘         │
│                                                         │
│  [Search Photo] [Search Button]                         │
└─────────────────────────────────────────────────────────┘
    ↓                           ↓
  (click)                    (click)
    ↓                           ↓
┌─────────────────┐      ┌──────────────────────┐
│ ALBUM ACTIVITY  │      │ SEARCH ACTIVITY      │
│                 │      │                      │
│ Album: Photos   │      │ Search by Tags       │
│ ┌─────────────┐ │      │                      │
│ │ [Thumb] [×] │ │      │ Type: [Person ▼]    │
│ │ [Thumb] [×] │ │      │ Value: [________]    │
│ │ [Thumb] [×] │ │      │                      │
│ │ [Thumb] [×] │ │      │ AND [▼]              │
│ │ (3x grid)   │ │      │                      │
│ └─────────────┘ │      │ Type: [Loc... ▼]    │
│                 │      │ Value: [________]    │
│ [Add] [Remove]  │      │                      │
│ [Move]          │      │ [Search]             │
│ [Back] [⋮]      │      │                      │
│                 │      │ Results: (3x grid)   │
│                 │      │ ┌─────────────────┐  │
│                 │      │ │ [Photo Thumb]   │  │
│                 │      │ │ [Photo Thumb]   │  │
│                 │      │ │ [Photo Thumb]   │  │
│                 │      │ │ (From all albums)   │
│                 │      │ └─────────────────┘  │
└─────────────────┘      │ [Back]               │
    ↓                    └──────────────────────┘
  (click)
    ↓
┌─────────────────────────────────────────────────────────┐
│               PHOTO ACTIVITY                            │
│          (Full Screen Photo Viewer)                     │
│                                                         │
│ [Back] Photo.jpg           [Slideshow ⏮️]               │
│ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━          │
│                                                         │
│                   ┌─────────────────┐                   │
│                   │                 │                   │
│                   │   BIG PHOTO     │                   │
│                   │   DISPLAYED     │                   │
│                   │                 │                   │
│                   └─────────────────┘                   │
│                                                         │
│ ┌───────────────────────────────────────────────────┐  │
│ │ Tags:                                             │  │
│ │ ┌──────────────┐  ┌──────────────┐               │  │
│ │ │ John   [×]   │  │ NYC    [×]   │               │  │
│ │ └──────────────┘  └──────────────┘               │  │
│ │ (Tag badges with delete buttons)                 │  │
│ └───────────────────────────────────────────────────┘  │
│                                                         │
│ [⬅️] [Add Tag] [Delete Tag] [➡️]                       │
│                                                         │
│  (Prev/Next for slideshow manual navigation)          │
└─────────────────────────────────────────────────────────┘
    ↓              ↓              ↓
  (click)      (click)        (click)
    ↓              ↓              ↓
    │    ┌─────────────────────┐   │
    │    │ ADD TAG DIALOG      │   │
    └───→│                     │←──┘
         │ Type: [Person ▼]    │
         │ Value: [______]     │ ← autocomplete suggestions
         │                     │
         │ [Cancel] [OK]       │
         │                     │
         └─────────────────────┘


               ┌──────────────────────────┐
               │ DELETE TAG DIALOG        │
               │                          │
               │ ☐ John (Person)         │
               │ ☑ NYC (Location)        │
               │ ☐ Jane (Person)         │
               │                          │
               │ [Cancel] [Delete]       │
               │                          │
               └──────────────────────────┘
```

---

## Screen Components Reference

### Main Activity (activity_main.xml)
```
┌────────────────────────────────────────────┐
│ My Albums                         [New Album] │
├────────────────────────────────────────────┤
│ [Search Photos]              [Search Button]│
├────────────────────────────────────────────┤
│ ┌──────────────────────────────────────────┤
│ │ Album 1                                   │
│ │ [Thumbnail] (5 photos)                    │
│ ├──────────────────────────────────────────┤
│ │ Album 2                                   │
│ │ [Thumbnail] (0 photos)                    │
│ ├──────────────────────────────────────────┤
│ │ Album 3                                   │
│ │ [Thumbnail] (12 photos)                   │
│ └──────────────────────────────────────────┤
└────────────────────────────────────────────┘

KEY IDs:
- album_list (RecyclerView)
- create_album_button
- search_button
- search_query
```

### Album Activity (activity_album.xml)
```
┌────────────────────────────────────────────┐
│ [Back] Album Name                [Options ⋮]│
├────────────────────────────────────────────┤
│ ┌─────┐  ┌─────┐  ┌─────┐                  │
│ │[IMG]│  │[IMG]│  │[IMG]│                  │
│ │[×]  │  │[×]  │  │[×]  │                  │
│ └─────┘  └─────┘  └─────┘                  │
│ ┌─────┐  ┌─────┐  ┌─────┐                  │
│ │[IMG]│  │[IMG]│  │[IMG]│                  │
│ │[×]  │  │[×]  │  │[×]  │                  │
│ └─────┘  └─────┘  └─────┘                  │
│                                             │
├─────────────────────────────────────────────┤
│ [Add Photo] [Remove Photo] [Move Photo]    │
└────────────────────────────────────────────┘

KEY IDs:
- photo_grid (RecyclerView)
- back_button
- album_options_menu
- add_photo_button
- remove_photo_button
- move_photo_button
```

### Photo Activity (activity_photo.xml)
```
┌────────────────────────────────────────────┐
│ [Back] Photo.jpg              [Slideshow ⏮️]│
├────────────────────────────────────────────┤
│                                             │
│                                             │
│         ┌──────────────────────┐            │
│         │                      │            │
│         │    PHOTO DISPLAY     │            │
│         │                      │            │
│         │     Centered Full    │            │
│         │      Screen View     │            │
│         │                      │            │
│         └──────────────────────┘            │
│                                             │
├────────────────────────────────────────────┤
│ Tags:                                       │
│ [John ×]  [NYC ×]  [Downtown ×]           │
├────────────────────────────────────────────┤
│ [⬅️]  [Add Tag] [Delete Tag]  [➡️]         │
└────────────────────────────────────────────┘

KEY IDs:
- photo_view (ImageView)
- tags_list (RecyclerView)
- prev_button, next_button
- add_tag_button, delete_tag_button
- slideshow_button
```

### Search Activity (activity_search.xml)
```
┌────────────────────────────────────────────┐
│ [Back] Search Photos                        │
├────────────────────────────────────────────┤
│ [Person ▼]    [Enter tag value]            │
│                                             │
│ [AND ▼]                                     │
│                                             │
│ [Location ▼]  [Enter tag value] (opt.)     │
│                                             │
│ [Search Button]                             │
├────────────────────────────────────────────┤
│ Search Results:                             │
│ ┌─────┐  ┌─────┐  ┌─────┐                  │
│ │[IMG]│  │[IMG]│  │[IMG]│                  │
│ └─────┘  └─────┘  └─────┘                  │
│ ┌─────┐  ┌─────┐  ┌─────┐                  │
│ │[IMG]│  │[IMG]│  │[IMG]│                  │
│ └─────┘  └─────┘  └─────┘                  │
│                                             │
│ (All matching photos from all albums)       │
└────────────────────────────────────────────┘

KEY IDs:
- tag_type_spinner, tag_type_spinner2
- tag_value_input, tag_value_input2
- operator_spinner
- search_button
- search_results_grid
```

---

## Dialog Components

### Create Album Dialog
```
┌─────────────────────────────────────────┐
│ Create New Album                        │
├─────────────────────────────────────────┤
│                                         │
│ [Album Name Input Field             ]  │
│                                         │
├─────────────────────────────────────────┤
│                      [Cancel] [OK]      │
└─────────────────────────────────────────┘
```

### Add Tag Dialog
```
┌─────────────────────────────────────────┐
│ Add Tag                                 │
├─────────────────────────────────────────┤
│ Tag Type:                               │
│ [Person ▼]                              │
│                                         │
│ Tag Value:                              │
│ [Enter value             ]   ← autocomplete
│                                         │
├─────────────────────────────────────────┤
│                      [Cancel] [OK]      │
└─────────────────────────────────────────┘
```

### Move Photo Dialog
```
┌─────────────────────────────────────────┐
│ Move Photo                              │
├─────────────────────────────────────────┤
│ Select Destination Album:               │
│ [Album 1 ▼]                             │
│ (Shows all albums except current)       │
│                                         │
├─────────────────────────────────────────┤
│                      [Cancel] [Move]    │
└─────────────────────────────────────────┘
```

### Delete Tag Dialog
```
┌─────────────────────────────────────────┐
│ Select Tags to Delete                   │
├─────────────────────────────────────────┤
│ ☐ John (Person)                         │
│ ☑ NYC (Location)                        │
│ ☐ Jane (Person)                         │
│ ☑ Downtown (Location)                   │
│                                         │
├─────────────────────────────────────────┤
│                      [Cancel] [Delete]  │
└─────────────────────────────────────────┘
```

---

## Responsive Design

### Landscape (1080 x 2400)
- All layouts adapt to full width
- Photos displayed larger
- Controls remain accessible
- Grid columns: 3 for photos/search

### Tablet Support
- Spinners have wider touch areas
- Dialogs centered on screen
- Photo display remains fullscreen
- RecyclerView columns auto-scale

---

## View Hierarchy

```
MainActivity
  └─ RecyclerView (album_list)
      └─ AlbumViewHolder
          ├─ ImageView (thumbnail)
          ├─ TextView (album name)
          └─ TextView (photo count)

AlbumActivity
  └─ RecyclerView (photo_grid - GridLayout)
      └─ PhotoViewHolder
          ├─ ImageView (thumbnail)
          ├─ TextView (filename)
          └─ CheckBox (selection)

PhotoActivity
  ├─ ImageView (photo_view)
  └─ RecyclerView (tags_list)
      └─ TagViewHolder
          ├─ TextView (tag text)
          └─ ImageButton (delete)

SearchActivity
  └─ RecyclerView (search_results_grid)
      └─ PhotoViewHolder (same as album)
```

---

## Color Scheme

| Element | Color | Use |
|---------|-------|-----|
| Primary Background | #FFFFFF | Screens |
| Secondary Background | #F5F5F5 | Input fields |
| Header Background | #333333 | Photo view header |
| Accent Color | #E8F4F8 | Tag badges |
| Text Primary | #000000 | Labels |
| Text Secondary | #666666 | Counters |
| Borders | #CCCCCC | Fields |

---

**All layouts complete and ready for implementation!**
