package com.dit.suumuagent.geometryservice.repository;

import com.dit.suumuagent.geometryservice.model.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.Set;

public interface ZoneRepository extends JpaRepository<Zone, Long>, QueryDslPredicateExecutor<Zone> {

    Page<Zone> findByTitleContaining(String title, Pageable p);

    Set<Zone> findByOrganizationId(Long organizationId);

    @Query(value = "select z.* from geometryservice.configurations_zones cz inner join geometryservice.zones z on cz.zone_id=z.id and cz.configuration_id=?1", nativeQuery = true)
    Set<Zone> findByConfigurationId(Long configurationId);

    @Query(value = "select z.* from geometryservice.configurations_zones cz inner join geometryservice.zones z on cz.zone_id=z.id and cz.configuration_id=?1 and z.organization_id=?2", nativeQuery = true)
    Set<Zone> findByConfigurationIdAndOrganizationId(Long configurationId, Long organizationId);

    @Modifying
    void deleteByIdAndOrganizationId(Long id, Long organizationId);


}