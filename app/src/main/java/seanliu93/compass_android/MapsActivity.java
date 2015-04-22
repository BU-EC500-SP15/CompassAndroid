package seanliu93.compass_android;

import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;

import java.net.URL;

import HttpClient.*;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationSource.OnLocationChangedListener mListener;
    private LocationManager locationManager;

    private SearchView searchView;

    private LatLng latlng; // fOR TESTING

    private MarkerOptions marker;

    private static String userID; // the userID being searched

    private static String location; // the location where the userID exists

    // Access to String userID
    public static String GetUserID()
    {
        return userID;
    }

    public static void SetUserID(String s)
    {
        userID = s;
    }

    // Access to String location
    public static String GetLocation()
    {
        return location;
    }

    public static void SetLocation(String s)
    {
        location = s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        new initializeContainersTask().execute();

        latlng = new LatLng(0.0, 0.0);

        searchView = (SearchView)findViewById(R.id.search_view);

        // About Search Parts
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String arg0)
            {
                // TODO Auto-generated method stub to handle Search Requests

                // Only for testing
                Toast.makeText(MapsActivity.this, "________" + arg0, Toast.LENGTH_SHORT).show();

                MapsActivity.SetUserID("NewUserID");
                MapsActivity.SetUserID("NewLocation");

                // Do Search for the userID (arg0)

                // Put a Marker onto the map
                marker = new MarkerOptions().position(latlng);

                mMap.addMarker(marker.title("Marker"));

                return true;
            }

            @Override
            public boolean onQueryTextChange(String arg0)
            {
                // TODO Auto-generated method stub

                return true;

            }
        });

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.addMarker(marker.title("Marker")); // This shall be the username you search for

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker)
            {
                // Create Get Requests and goto the indoor map
                Toast.makeText(MapsActivity.this,  "Marker " + marker.getTitle() + " has been clicked", Toast.LENGTH_SHORT).show();

                // Go to Indoor map interface
                Intent intent = new Intent(MapsActivity.this, IndoorMap.class);

                Bundle bundle = new Bundle();

                bundle.putString("userID", MapsActivity.GetUserID());
                bundle.putString("Location", MapsActivity.GetLocation());
                intent.putExtras(bundle);

                startActivity(intent); //Two parameters：The First is the object of the intent，The second is the requestCode

                return true;
            }
        });

        mMap.addMarker(new MarkerOptions().position(latlng).title(userID));
    }

    private class initializeContainersTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            String response = HttpClient.createNewUserContainer();
            response = HttpClient.createNewUUIDContainer(getApplicationContext(),true);
            response = HttpClient.createNewUUIDContainer(getApplicationContext(),false);
            response = HttpClient.initializeLocContainers(getApplicationContext());
            return response;
        }

        protected void onPostExecute(String response) {

            //System.out.print(response);
        }

    }


}
