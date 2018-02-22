package developers.are.we.valentineschallenge;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.cocoahero.android.geojson.FeatureCollection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 1111;
    private static final double LATITUDE = 46.601363;
    private static final double LONGITUDE = 15.674555;
    private static final int RADIUS_IN_METERS = 500;

    private GoogleMap mMap;
    private JSONObject geoJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        findViewById(R.id.save).setOnClickListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        showOnScreen(LATITUDE, LONGITUDE, RADIUS_IN_METERS);
    }

    @Override
    public void onClick(View view) {
        if (geoJSON != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                WriteToFileHelper.saveToFile(this, geoJSON);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    WriteToFileHelper.saveToFile(this, geoJSON);
                }
                break;
        }
    }

    private void showOnScreen(double latitude, double longitude, int radiusInMeters) {
        FeatureCollection geoJson = GeneratePointsHelper.generateHeart(latitude, longitude, radiusInMeters);
        try {
            geoJSON = geoJson.toJSON();
            GeoJsonLayer layer = new GeoJsonLayer(mMap, geoJSON);
            setColorsForGoogleMap(layer);
            layer.addLayerToMap();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
    }

    private void setColorsForGoogleMap(GeoJsonLayer layer) {
        for (GeoJsonFeature feature : layer.getFeatures()) {
            GeoJsonPolygonStyle geoJsonPolygonStyle = new GeoJsonPolygonStyle();
            geoJsonPolygonStyle.setFillColor(Color.parseColor("#bbfe0101"));
            feature.setPolygonStyle(geoJsonPolygonStyle);
        }
    }


}
