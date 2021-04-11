package com.app.myspeedometer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LocationListener {

    final int update_interval = 500; // milliseconds

    // Data shown to user
    float speed = 0.0f;
    float speed_max = 0.0f;

    int num_updates = 0; // GPS update counter
    int no_loc = 0; // Number of null GPS updates
    int no_speed = 0; // Number of GPS updates which don't have speed

    LocationManager loc_mgr;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.speed_text);
        update_speed(0.0f);

        loc_mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        loc_mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, update_interval, 0.0f, this);

    }

    void update_speed( float x )
    {
        speed = x;

        if ( x > speed_max )
            speed_max = x;

        String s = String.format( "\n"
                        + "Speed:\n"
                        + "%.2f m/s\n"
                        + "%.0f km/h\n"
                        + "\n"
                        + "Max speed:\n"
                        + "%.2f m/s\n"
                        + "%.0f km/h\n"
                        + "\n"
                        + "Updates: %d\n"
                        + "Noloc: %d\n"
                        + "Nospeed: %d\n",
                speed, speed * 3.6f,
                speed_max, speed_max * 3.6f,
                num_updates,
                no_loc,
                no_speed
        );

        text.setText( s );
    }

    @Override
    public void onLocationChanged(@NonNull Location loc )
    {
        num_updates++;

        if ( loc == null )
        {
            no_loc++;
            return;
        }

        if ( !loc.hasSpeed() )
        {
            no_speed++;
            return;
        }

        update_speed( loc.getSpeed() );
    }
    
}