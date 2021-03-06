package arab_open_university.com.bustracker;

import com.firebase.geofire.GeoLocation;

import java.util.List;

/**
 * Created by akhalaf on 5/1/2017.
 */

public class BusStation {


    private int NumOfBusStations;
    private List<String> busStationsNames;
    private List<Double> busStationsLat;
    private List<Double> busStationsLong;
    private List<String> busesIDs;


    public List<String> getBusStationsNames() {
        return busStationsNames;
    }

    public void setBusStationsNames(List<String> busStationsNames) {
        this.busStationsNames = busStationsNames;
    }

    public List<Double> getBusStationsLat() {
        return busStationsLat;
    }

    public void setBusStationsLat(List<Double> busStationsLat) {
        this.busStationsLat = busStationsLat;
    }

    public List<Double> getBusStationsLong() {
        return busStationsLong;
    }

    public void setBusStationsLong(List<Double> busStationsLong) {
        this.busStationsLong = busStationsLong;
    }

    public int getNumOfBusStations() {
        return NumOfBusStations;
    }

    public void setNumOfBusStations(int numOfBusStations) {
        NumOfBusStations = numOfBusStations;
    }



    public List<String> getBusesIDs() {
        return busesIDs;
    }

    public void setBusesIDs(List<String> busesIDs) {
        this.busesIDs = busesIDs;
    }
}
