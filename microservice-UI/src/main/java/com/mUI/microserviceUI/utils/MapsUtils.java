package com.mUI.microserviceUI.utils;

import com.mUI.microserviceUI.beans.MerchantBean;
import com.mUI.microserviceUI.beans.Place;
import com.mUI.microserviceUI.beans.UserBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.mUI.microserviceUI.utils.ConfigUtils.getConfigProprety;

public class MapsUtils {

    /**
     * Enoodes an address for an url search
     * @param address
     * @return url encoded address
     */
    public static String setUrlAddressForMapsAPI(String address){
        String mCity = getConfigProprety("api.maps.city");

        String apiMapsAddress = getConfigProprety("api.maps.url");
        String mAddressForMaps = address.replaceAll(" ", "+");
        String addressForMaps = getConfigProprety("api.maps.center")+mAddressForMaps+mCity;

        String paramsForMaps = getConfigProprety("api.maps.zoom")+getConfigProprety("api.maps.size");
        String markersForMaps = getConfigProprety("api.maps.markers")+mAddressForMaps+mCity;
        String paramApiKey = getConfigProprety("api.maps.key");
        String url = apiMapsAddress+addressForMaps+paramsForMaps+markersForMaps+paramApiKey;
        return url;
    }

    /**
     * Searches address in google api maps from a String address
     * @param text
     * @return place
     */
    public static Place searchPlaceFromText(String text) {
        Place place = new Place();
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(getConfigProprety("api.placesearch.fromtext.url"));
            String cityUrlEncoded = getConfigProprety("api.maps.city").replace("+", "%25");
            sb.append(text+cityUrlEncoded);
            sb.append(getConfigProprety("api.placesearch.params"));
            sb.append(getConfigProprety("api.maps.key"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            e.getMessage();
            System.out.println("catched MalformedURLException");
        } catch (IOException e) {
            e.getMessage();
            System.out.println("catched IOException"+"\n"+e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("candidates");

            // Extract the Place descriptions from the results
            for (int i = 0; i < predsJsonArray.length(); i++) {
                place.setPlace_id(predsJsonArray.getJSONObject(i).getString("place_id"));
                place.setName(predsJsonArray.getJSONObject(i).getString("name"));
                place.setFormatted_address(predsJsonArray.getJSONObject(i).getString("formatted_address"));
                place.setIcon(predsJsonArray.getJSONObject(i).getString("icon"));
            }
        } catch (JSONException e) {
            e.getMessage();
        }
        return place;
    }


    /**
     * sets up parameters to produce ggogle map from a given address
     * @param m
     */
    public static void setUpForGMaps(MerchantBean m){
        m.setMapsAddress(MapsUtils.setUrlAddressForMapsAPI(m.getAddress()));
        try{
            String search = URLEncoder.encode(m.getMerchantName()+" "+m.getAddress(), "UTF-8");
            Place place =  MapsUtils.searchPlaceFromText(search);
            m.setIconLink(place.getIcon());
        }catch (UnsupportedEncodingException e){
            e.getMessage();
        }
    }

    /**
     * Finds nearest location
     */
    public static void nearestOption(UserBean user){
        user.setMapsAddress(setUrlAddressForMapsAPI(user.getAddress()));
        try{
            String search = URLEncoder.encode(user.getAddress(), "UTF-8");
            Place place = searchPlaceFromText(search);
        }catch (UnsupportedEncodingException e){
            e.getMessage();
        }
    }

    public static ArrayList<String> geocodeFromString(String address){
        ArrayList<String> longLat = new ArrayList<>();
        try{
            HttpURLConnection conn = null;
            StringBuilder sb = new StringBuilder(getConfigProprety("api.maps.geocode"));
            String addressUrlEncoded = address.replace(" ", "%25");
            sb.append(addressUrlEncoded);
            sb.append(getConfigProprety("api.maps.key"));
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            StringBuffer sbLocation = new StringBuffer();

            for (int i=0; i != -1; i = isr.read()){
                sbLocation.append((char)i);
            }
            String getContent = sbLocation.toString().trim();
            if(getContent.contains("results")){
                String temp = getContent.substring(getContent.indexOf("["));
                JSONArray JSONArrayForAll = new JSONArray(temp);
                String lng = JSONArrayForAll.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng").toString();
                String lat = JSONArrayForAll.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat").toString();
                longLat.add(lng);
                longLat.add(lat);
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return longLat;
    }

}
