package br.org.knob.followme.model;

import android.content.ContentValues;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.org.knob.android.framework.model.GenericModel;

public class Location implements GenericModel {
    public static final String TAG = "Location";

    private static final String DATE_FORMAT = "d/M/y H:m:s.S";

    private Long id;
    private Date date;
    private String latitude;
    private String longitude;

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
}
