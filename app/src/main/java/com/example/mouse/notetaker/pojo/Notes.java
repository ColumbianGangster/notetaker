package com.example.mouse.notetaker.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notes {
    public static List<Note> ITEMS = new ArrayList<Note>();
    public static Map<String, Note> ITEM_MAP = new HashMap<String, Note>();
    private static int COUNT = 0;

//    static {
//         Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createNote(i));
//        }
//    }

    private static void addItem(Note item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static Note createNote(int position) {
        return new Note(String.valueOf(position), "Item " + position, "Content "+position,makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static void addNote(Note newNote){
        newNote.id = Integer.toString(COUNT);
        COUNT++;
        addItem(newNote);
    }

}
