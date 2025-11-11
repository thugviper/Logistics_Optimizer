package com.major_project.Logistics_Optimiser.Model;

//public class Location {
//    private double lat;
//    private double lng;
//    private String address;
//    private String label;
//
//    public Location(double lat, double lng, String address, String label) {
//        this.lat = lat;
//        this.lng = lng;
//        this.address = address;
//        this.label = label;
//    }
//
//    public double getLat() { return lat; }
//    public void setLat(double lat) { this.lat = lat; }
//    public double getLng() { return lng; }
//    public void setLng(double lng) { this.lng = lng; }
//    public String getAddress() { return address; }
//    public void setAddress(String address) { this.address = address; }
//    public String getLabel() { return label; }
//    public void setLabel(String label) { this.label = label; }
//}

public class Location {
    private String id;
    private String address;
    private String label;
    private double lat;
    private double lng;
    private int demand;

    public Location() {}

    public Location(String id, String address, String label, double lat, double lng) {
        this.id = id;
        this.address = address;
        this.label = label;
        this.lat = lat;
        this.lng = lng;
        this.demand = 1;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }
    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }
    public int getDemand() { return demand; }
    public void setDemand(int demand) { this.demand = demand; }
}
