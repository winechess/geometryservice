package com.dit.suumuagent.geometryservice.helpers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vividsolutions.jts.geom.Coordinate;

public class ExtendedCoordinate extends Coordinate {

    public ExtendedCoordinate(double x, double y) {
        super(x, y);
    }

    public ExtendedCoordinate(Coordinate c) {
        super(c);
    }

    public ExtendedCoordinate() {
        super();
    }

    @JsonProperty("lat")
    Double getX() {
        return this.x;
    }

    @JsonProperty("lat")
    void setX(Double x) {
        this.x = x;
    }

    @JsonProperty("lng")
    Double getY() {
        return this.y;
    }

    @JsonProperty("lng")
    void setY(Double y) {
        this.y = y;
    }

    @JsonIgnore
    Double getZ() {
        return this.z;
    }

    @JsonIgnore
    void setZ(Double z) {
        this.z = z;
    }

}
