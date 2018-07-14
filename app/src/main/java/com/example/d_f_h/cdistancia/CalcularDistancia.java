package com.example.d_f_h.cdistancia;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.gms.maps.GoogleMap;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class CalcularDistancia extends FragmentActivity  {

    private GoogleMap mMap;

    double oLatitude, oLongitude, dLatitude, dLongitude;

    Button calcDistance;
    TextView distanceLabel;

    String consultUrl ="";
    String distance = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcular_distancia);


        distanceLabel = findViewById(R.id.distanceLabel);

        calcDistance= (Button) findViewById(R.id.B_CDistance);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */



    public void onClick(View v){

        GetDistanceMatrix getDistanceMatrix= new GetDistanceMatrix();

        switch (v.getId()){
            case R.id.B_CDistance:
//                distance = "5 m";
                EditText oLocation= (EditText) findViewById(R.id.Origin);
                EditText dLocation= (EditText) findViewById(R.id.Destination);

                String origin= oLocation.getText().toString();
                String destination= dLocation.getText().toString();

                List<Address> oListAddress=null;
                List<Address> dListAddress=null;

//                distance = origin+ ", "+ destination;
                if(!origin.equals("") && !destination.equals("")){
                    Geocoder geocoder = new Geocoder(this);

                    try {
                        oListAddress= geocoder.getFromLocationName(origin,1);
                        dListAddress= geocoder.getFromLocationName(destination,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(oListAddress != null && dListAddress !=null){
                        oLatitude= oListAddress.get(0).getLatitude();
                        oLongitude= oListAddress.get(0).getLongitude();
                        dLatitude= dListAddress.get(0).getLatitude();
                        dLongitude= dListAddress.get(0).getLongitude();

                        consultUrl= getDistanceMatrixUrl();
                    }

                }

                Log.d("origin  = ", oLatitude+","+oLongitude );
                Log.d("origin  = ", dLatitude+","+dLongitude );


                if(!consultUrl.equals("")){

                    getDistanceMatrix.execute(consultUrl);

                }
                distanceLabel.setText("La distancia es "+ distance);


                Log.d("location = ", distance);

                break;
        }
    }

    private String getDistanceMatrixUrl(){
        StringBuilder googleDistanceMatrixUrl= new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json?");
        googleDistanceMatrixUrl.append("origins="+oLatitude+","+oLongitude);
        googleDistanceMatrixUrl.append("&destinations="+dLatitude+","+dLongitude);
        googleDistanceMatrixUrl.append("&key="+"AIzaSyDw4joWwij0VWhAIpglTGQWfOGAYLCa2Mg");

        return googleDistanceMatrixUrl.toString();
    }

    private class GetDistanceMatrix extends AsyncTask<String,String, String > {

        String url;
        String googleDistanceMatrixData;

        @Override
        protected String doInBackground(String... params){
            url = (String) params[0];

            ConsultURL consult = new ConsultURL();
            try{
                googleDistanceMatrixData= consult.getUrlData(url);
            }
            catch (IOException e){
                e.printStackTrace();
            }
            distance=parseDistanceMatrix(googleDistanceMatrixData);

            return googleDistanceMatrixData;
        }


        public String parseDistanceMatrix(String json){
            String distance = "";
            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(json);
                distance = jsonObject.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getString("text");
            }catch (JSONException e){
                e.printStackTrace();
            }
            Log.d("distance", distance);
            return distance;
        }

    }

}
