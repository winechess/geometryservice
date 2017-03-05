package com.dit.suumuagent.geometryservice.repository;

import com.dit.suumuagent.geometryservice.model.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {

}