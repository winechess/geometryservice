package com.dit.suumuagent.geometryservice.repository;

import com.dit.suumuagent.geometryservice.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Modifying
    void deleteByDeviceId(Long deviceId);

    Set<Location> findAllByDeviceId(Long deviceId);

    @Query(value = "SELECT DISTINCT ON (device_id) id, date, device_id, point FROM   geometryservice.locations where device_id in (?1) ORDER  BY device_id,date DESC", nativeQuery = true)
    Set<Location> findLatestForDevices(Set<Long> devicesIds);

}