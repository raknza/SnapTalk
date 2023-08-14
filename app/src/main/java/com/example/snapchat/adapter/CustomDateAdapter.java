package com.example.snapchat.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomDateAdapter extends TypeAdapter<Date> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.TAIWAN);

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        // Not needed for serialization
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        String dateStr = in.nextString();
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new IOException("Error parsing date: " + dateStr, e);
        }
    }
}