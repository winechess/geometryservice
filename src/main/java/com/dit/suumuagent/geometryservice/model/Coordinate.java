package com.dit.suumuagent.geometryservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vividsolutions.jts.geom.Point;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by vinichenkosa on 05/03/2017.
 */
@Entity
@Table(name = "coordinates", schema = "geometryservice",
        indexes = {
            @Index(name = "coordinates_location_idx", columnList = "location"),
            @Index(name = "coordinates_device_id_idx", columnList = "device_id")
        },
        uniqueConstraints = @UniqueConstraint(name = "coordinates_pkey", columnNames = "id"))
public class Coordinate {

    @Id
    @GeneratedValue(generator = "coordinateIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "coordinateIdGenerator", allocationSize = 1, initialValue = 1, sequenceName = "coordinates_id_seq", schema = "geometryservice")
    @Column(name = "id", nullable = false, updatable = false, insertable = false)
    private Long id;

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "lng", nullable = false)
    private Double lng;
    @Column(name = "lat", nullable = false)
    private Double lat;
    @Column(name = "location", nullable = false)
    private Point location;
    @Column(name = "device_id", nullable = false, updatable = false)
    private Long deviceId;

    public Double getLng() {
        return lng;
    }

    public Double getLat() {
        return lat;
    }

    public Date getDate() {
        return date;
    }

    @JsonProperty("device_id")
    public Long getDeviceId() {
        return deviceId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "id=" + id +
                ", date=" + date +
                ", lng=" + lng +
                ", lat=" + lat +
                ", location=" + location +
                ", deviceId=" + deviceId +
                '}';
    }
}
