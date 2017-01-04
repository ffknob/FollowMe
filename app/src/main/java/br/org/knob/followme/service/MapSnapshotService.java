package br.org.knob.followme.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;

import com.google.android.gms.maps.GoogleMap;

import br.org.knob.followme.model.Location;

public class MapSnapshotService {
    private static final String TAG = "MapSnapshotService";

    private static final int MAP_SNAPSHOT_WIDTH = 320;
    private static final int MAP_SNAPSHOT_HEIGHT = 240;

    private Context context;
    private GoogleMap map;
    private Location location;
    private boolean saveIfPersised;

    public MapSnapshotService(Context context, GoogleMap map, Location location) {
        this.context = context;
        this.map = map;
        this.location = location;
    }

    public void takeSnapshot(boolean _saveIfPersised) {
        this.saveIfPersised = _saveIfPersised;

        if(map != null) {
            map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                public void onMapLoaded() {
                    map.snapshot(new GoogleMap.SnapshotReadyCallback() {
                        public void onSnapshotReady(Bitmap bitmap) {
                            if (location != null) {
                                // Resize the snapshot to thumbnail size
                                Bitmap resizedBitmap = scaleCenterCrop(bitmap, MAP_SNAPSHOT_WIDTH, MAP_SNAPSHOT_HEIGHT);
                                //bitmap.recycle();

                                // Set snapshot to the location and save it (if asked for)
                                location.setSnapshot(resizedBitmap);

                                LocationService locationService = new LocationService(context);
                                if (saveIfPersised && locationService.isPersisted(location)) {
                                    locationService.save(location);
                                }

                            }
                        }
                    });
                }
            });
        }
    }

    public Bitmap scaleCenterCrop(Bitmap sourceBitmap, int newHeight, int newWidth) {
        int sourceWidth = sourceBitmap.getWidth();
        int sourceHeight = sourceBitmap.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap croppedBitmap = Bitmap.createBitmap(newWidth, newHeight, sourceBitmap.getConfig());
        Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(sourceBitmap, null, targetRect, null);

        return croppedBitmap;
    }
}
