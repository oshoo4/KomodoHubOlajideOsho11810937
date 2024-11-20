package com.olajideosho.komodohub.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.olajideosho.komodohub.data.local.DBHelper;
import com.olajideosho.komodohub.data.model.Species;

import java.util.ArrayList;
import java.util.List;

public class SpeciesRepository {
    private DBHelper dbHelper;

    public SpeciesRepository(Context context) {
        dbHelper = new DBHelper(context);
    }

    public long insertSpecies(Species species) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("common_name", species.getCommonName());
        values.put("scientific_name", species.getScientificName());
        values.put("description", species.getDescription());
        values.put("conservation_status", species.getConservationStatus());
        values.put("image_url", species.getImageUrl());
        long speciesId = db.insert("Species", null, values);
        db.close();
        return speciesId;
    }

    public Species getSpeciesById(int speciesId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {
                "species_id",
                "common_name",
                "scientific_name",
                "description",
                "conservation_status",
                "image_url"
        };
        String selection = "species_id = ?";
        String[] selectionArgs = {String.valueOf(speciesId)};
        Cursor cursor = db.query("Species", columns, selection, selectionArgs, null, null, null);
        Species species = null;
        if (cursor != null && cursor.moveToFirst()) {
            species = new Species(
                    cursor.getInt(cursor.getColumnIndexOrThrow("species_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("common_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("scientific_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getString(cursor.getColumnIndexOrThrow("conservation_status")),
                    cursor.getString(cursor.getColumnIndexOrThrow("image_url"))
            );
            cursor.close();
        }
        db.close();
        return species;
    }

    public List<Species> getAllSpecies() {
        List<Species> speciesList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {
                "species_id",
                "common_name",
                "scientific_name",
                "description",
                "conservation_status",
                "image_url"
        };
        Cursor cursor = db.query("Species", columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Species species = new Species(
                        cursor.getInt(cursor.getColumnIndexOrThrow("species_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("common_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("scientific_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getString(cursor.getColumnIndexOrThrow("conservation_status")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image_url"))
                );
                speciesList.add(species);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return speciesList;
    }

    public Species getSpeciesByCommonName(String commonName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {
                "species_id",
                "common_name",
                "scientific_name",
                "description",
                "conservation_status",
                "image_url"
        };
        String selection = "common_name = ?";
        String[] selectionArgs = {commonName};
        Cursor cursor = db.query("Species", columns, selection, selectionArgs, null, null, null);
        Species species = null;
        if (cursor != null && cursor.moveToFirst()) {
            species = new Species(
                    cursor.getInt(cursor.getColumnIndexOrThrow("species_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("common_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("scientific_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getString(cursor.getColumnIndexOrThrow("conservation_status")),
                    cursor.getString(cursor.getColumnIndexOrThrow("image_url"))
            );
            cursor.close();
        }
        db.close();
        return species;
    }

    public int updateSpecies(Species species) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("common_name", species.getCommonName());
        values.put("scientific_name", species.getScientificName());
        values.put("description", species.getDescription());
        values.put("conservation_status", species.getConservationStatus());
        values.put("image_url", species.getImageUrl());
        String selection = "species_id = ?";
        String[] selectionArgs = {String.valueOf(species.getSpeciesId())};
        int count = db.update("Species", values, selection, selectionArgs);
        db.close();
        return count;
    }
}
