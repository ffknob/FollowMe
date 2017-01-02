package br.org.knob.followme.model;

import android.content.ContentValues;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import br.org.knob.android.framework.model.GenericModel;

public class Location implements GenericModel, Serializable, Comparable<Location> {
    public static final String TAG = "Location";

    private static final String DATE_FORMAT = "d/M/y H:m:s.S";

    private Long id;
    private Date date;
    private String latitude;
    private String longitude;

    public Location() {

    }

    public Location(Date date, String latitude, String longitude) {
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
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

        return values;
    }

    @Override
    public void setValues(ContentValues values) {
        this.id = values.getAsLong("_id");
        try {
            this.date =  (new SimpleDateFormat(DATE_FORMAT)).parse(values.getAsString("date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.latitude = values.getAsString("latitude");
        this.longitude = values.getAsString("longitude");
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
