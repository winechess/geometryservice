package com.dit.suumuagent.geometryservice.model;

import com.dit.suumuagent.geometryservice.helpers.ExtendedCoordinate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "zones", schema = "geometryservice",
        indexes = {
                @Index(name = "zones_polygon_idx", columnList = "polygon"),
                @Index(name = "zones_organization_id_idx", columnList = "organization_id")
        },
        uniqueConstraints = @UniqueConstraint(name = "zones_pkey", columnNames = "id"))
public class Zone {

    @Id
    @GeneratedValue(generator = "zoneIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "zoneIdGenerator", allocationSize = 1, initialValue = 1, sequenceName = "zones_id_seq", schema = "geometryservice")
    @Column(name = "id", nullable = false, updatable = false, insertable = false)
    private Long id;
    @NotBlank(message = "Zone title can not be empty.")
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "polygon", nullable = false)
    @NotNull(message = "Polygon can not be empty!")
    private Polygon polygon;
    @Column(name = "organization_id", nullable = false)
    @NotNull
    private Long organizationId;

    @Column(name = "created", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name = "created_by", nullable = false, updatable = false)
    private Long createdBy;
    @Column(name = "edited", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date edited;
    @Column(name = "edited_by", nullable = false)
    private Long editedBy;
    @Transient
    @NotNull(message = "Zone coordinates can not be empty!")
    @Size(min = 4, message = "Zone required minimum 3 coordinates.")
    private List<ExtendedCoordinate> coordinates;

    @JsonProperty("coordinates")
    public List<ExtendedCoordinate> getCoordinates() {
        if (coordinates != null) return coordinates;
        if (this.polygon == null) return null;
        this.coordinates = Arrays.stream(this.polygon.getCoordinates()).map(ExtendedCoordinate::new).collect(Collectors.toList());
        return  this.coordinates;
    }

    @JsonProperty("coordinates")
    public void setCoordinates(List<ExtendedCoordinate> coordinates) {
        this.coordinates = coordinates;
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        this.polygon = geometryFactory.createPolygon(coordinates.toArray(new ExtendedCoordinate[coordinates.size()]));
    }

    @JsonProperty("organization_id")
    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @JsonProperty("organization_id")
    public Long getOrganizationId() {
        return organizationId;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonIgnore
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonIgnore
    public Date getCreated() {
        return created;
    }

    @JsonIgnore
    public void setCreated(Date created) {
        this.created = created;
    }

    @JsonIgnore
    public Long getCreatedBy() {
        return createdBy;
    }

    @JsonIgnore
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @JsonIgnore
    public Date getEdited() {
        return edited;
    }

    @JsonIgnore
    public void setEdited(Date edited) {
        this.edited = edited;
    }

    @JsonIgnore
    public Long getEditedBy() {
        return editedBy;
    }

    @JsonIgnore
    public void setEditedBy(Long editedBy) {
        this.editedBy = editedBy;
    }

    @JsonIgnore
    public Polygon getPolygon() {
        return this.polygon;
    }

    @JsonIgnore
    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", polygon=" + polygon +
                ", organizationId=" + organizationId +
                ", created=" + created +
                ", createdBy=" + createdBy +
                ", edited=" + edited +
                ", editedBy=" + editedBy +
                '}';
    }
}
