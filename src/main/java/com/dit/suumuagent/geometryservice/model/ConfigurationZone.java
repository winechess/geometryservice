package com.dit.suumuagent.geometryservice.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table( name = "configurations_zones", schema = "geometryservice",
        uniqueConstraints = @UniqueConstraint(name = "configurations_zones_unique", columnNames = {"configuration_id", "zone_id"})
)

@NamedNativeQueries(value = {
        @NamedNativeQuery(name = "ConfigurationZone.deleteByConfigId", query = "DELETE FROM geometryservice.configurations_zones WHERE configuration_id=:configId")
})
public class ConfigurationZone {

    public ConfigurationZone(Long configId, Long zoneId) {
        this.id = new ConfigurationZoneId(configId, zoneId);
    }

    @EmbeddedId
    private ConfigurationZoneId id;

    @Embeddable
    class ConfigurationZoneId implements Serializable {

        ConfigurationZoneId(Long configurationId, Long zoneId) {
            this.configurationId = configurationId;
            this.zoneId = zoneId;
        }

        @Column(name = "configuration_id", nullable = false, updatable = false)
        private Long configurationId;
        @Column(name = "zone_id", nullable = false, updatable = false)
        private Long zoneId;

    }
}
