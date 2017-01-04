package br.org.knob.followme.model;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import br.org.knob.android.framework.model.GenericModel;
import br.org.knob.android.framework.util.Util;

public class Location implements GenericModel, Serializable, Comparable<Location> {
    public static final String TAG = "Location";

    private static final String DATE_FORMAT = "d/M/y H:m:s.S";

    private Long id;
    private Date date;
    private String latitude;
    private String longitude;
    private transient Bitmap snapshot;

    public Location() {
    }

    public Location(Date date, String latitude, String longitude, Bitmap snapshot) {
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.snapshot = snapshot;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) { this.id = id; }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public ContentValues getValues() {
        ContentValues values = new ContentValues();
        values.put("date", (new SimpleDateFormat(DATE_FORMAT)).format(date));
        values.put("latitude", this.latitude);
        values.put("longitude", this.longitude);

        // Do some heavy lifting for the snapshot bitmap
        if(snapshot != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            snapshot.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            values.put("snapshot", byteArray);
        } else {
            values.putNull("snapshot");
        }

        return values;
    }

    @Override
    public void setValues(ContentValues values) {
        this.id = values.getAsLong("_id");
        try {
            this.date = (new SimpleDateFormat(DATE_FORMAT)).parse(values.getAsString("date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.latitude = values.getAsString("latitude");
        this.longitude = values.getAsString("longitude");

        byte[] snapshotByteArray = values.getAsByteArray("snapshot");
        if(snapshotByteArray != null) {
            this.snapshot = BitmapFactory.decodeByteArray(snapshotByteArray, 0, snapshotByteArray.length);
        }
    };

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Bitmap getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(Bitmap snapshot) {
        this.snapshot = snapshot;
    }

    @Override
    public int compareTo(Location o) {
        if(o == null) {
            return 0;
        }

        return this.getDate().compareTo(o.getDate());
    }

    // Compare by date, desc
    public static class DateAscComparator implements Comparator<Location> {
        public int compare(Location location1, Location location2) {
            if(location1 == null || location2 == null) {
                return 0;
            }

            return location1.getDate().compareTo(location2.getDate());
        }
    }

    // Compare by date, desc
    public static class DateDescComparator implements Comparator<Location> {
        public int compare(Location location1, Location location2) {
            if(location1 == null || location2 == null) {
                return 0;
            }

            if(location1.getDate().compareTo(location2.getDate()) < 0) {
                return 1; // Invert
            } else if(location1.getDate().compareTo(location2.getDate()) > 0) {
                return -1; // Invert
            } else {
                return 0;
            }
        }
    }
}
