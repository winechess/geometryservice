package com.dit.suumuagent.geometryservice.model;

import com.dit.suumuagent.geometryservice.helpers.ExtendedCoordinate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "locations", schema = "geometryservice",
        indexes = {
                @Index(name = "locations_point_idx", columnList = "point"),
                @Index(name = "locations_device_id_idx", columnList = "device_id")
        },
        uniqueConstraints = @UniqueConstraint(name = "locations_pkey", columnNames = "id"))
public class Location {

    @Id
    @GeneratedValue(generator = "locationIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "locationIdGenerator", allocationSize = 1, initialValue = 1, sequenceName = "locations_id_seq", schema = "geometryservice")
    @Column(name = "id", nullable = false, updatable = false, insertable = false)
    private Long id;
    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date date;
    @Column(name = "point", nullable = false)
    @NotNull
    private Point point;
    @Column(name = "device_id", nullable = false, updatable = false)
    @NotNull
    private Long deviceId;

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("date")
    public Date getDate() {
        return date;
    }

    @JsonProperty("device_id")
    public Long getDeviceId() {
        return deviceId;
    }

    @JsonProperty("point")
    public ExtendedCoordinate getCoordinates() {
        if (this.point == null) return null;
        return new ExtendedCoordinate(this.point.getX(), this.point.getY());
    }

    @JsonProperty("point")
    public void setCoordinates(ExtendedCoordinate coordinate) {
        if(coordinate==null) this.point = null;
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        this.point = geometryFactory.createPoint(coordinate);
    }

    @JsonProperty("device_id")
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    @JsonIgnore
    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public void setDate(Date date) {
        this.date = date;
    }

    @JsonIgnore
    public Point getPoint() {
        return this.point;
    }

    @JsonIgnore
    public void setPoint(Point point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", date=" + date +
                ", point=" + point +
                ", deviceId=" + deviceId +
                '}';
    }
}
