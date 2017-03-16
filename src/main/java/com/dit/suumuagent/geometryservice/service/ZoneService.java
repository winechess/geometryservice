package com.dit.suumuagent.geometryservice.service;

import com.dit.suumuagent.geometryservice.helpers.SortUtils;
import com.dit.suumuagent.geometryservice.helpers.exceptions.AccessDeniedException;
import com.dit.suumuagent.geometryservice.model.ConfigurationZone;
import com.dit.suumuagent.geometryservice.model.QZone;
import com.dit.suumuagent.geometryservice.model.Zone;
import com.dit.suumuagent.geometryservice.repository.ZoneRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping(path = "/zones")
public class ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;
    @PersistenceContext
    private EntityManager em;

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public Zone create(
            @RequestHeader(value = "User-Id") Long userId,
            @RequestHeader(value = "Organization-Id", required = false) Long organizationId,
            @RequestBody Zone z
    ) {

        if (z.getOrganizationId() == null)
            throw new IllegalArgumentException("Invalid zone object: \"organization_id\" field required.");

        if (!z.getOrganizationId().equals(organizationId) && !isAdmin(organizationId)) {
            throw new AccessDeniedException("You can not create zone in this organization. (if you are root admin contact technical support)");
        }

        if (z.getPolygon() == null)
            throw new IllegalArgumentException("Invalid zone object: \"coordinates\" field required.");

        if (StringUtils.isBlank(z.getTitle()))
            throw new IllegalArgumentException("Invalid zone object: \"title\" field required.");

        z.setCreated(new Date());
        z.setCreatedBy(userId);
        z.setEdited(z.getCreated());
        z.setEditedBy(z.getCreatedBy());

        return zoneRepository.save(z);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public Page<Zone> findAll(
            @RequestHeader(value = "User-Id") Long userId,
            @RequestHeader(value = "Organization-Id", required = false) Long organizationId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "page", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size,
            @RequestParam(value = "sort", required = false) List<String> sortOrder
    ) {

        QZone qZone = QZone.zone;
        BooleanBuilder predicate = new BooleanBuilder();

        if (!isAdmin(organizationId)) predicate.and(QZone.zone.organizationId.eq(organizationId));
        if (title != null && !title.isEmpty()) predicate.and(qZone.title.like("%" + title + "%"));

        return zoneRepository.findAll(predicate, new PageRequest(pageNumber, size, SortUtils.getSort(sortOrder)));
    }

    @RequestMapping(
            path = "/{id}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public Zone find(
            @RequestHeader(value = "User-Id") Long userId,
            @RequestHeader(value = "Organization-Id", required = false) Long organizationId,
            @PathVariable(name = "id") Long id
    ) {

        BooleanExpression predicate = QZone.zone.id.eq(id);

        if (!isAdmin(organizationId)) predicate.and(QZone.zone.organizationId.eq(organizationId));

        Zone zone = zoneRepository.findOne(predicate);

        if (zone == null)
            throw new EntityNotFoundException("Zone with id: " + id + " was not found.");

        return zone;
    }

    @RequestMapping(
            path = "/{id}",
            method = RequestMethod.PATCH,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public Zone edit(
            @RequestHeader(value = "User-Id") Long userId,
            @RequestHeader(value = "Organization-Id", required = false) Long organizationId,
            @PathVariable(name = "id") Long id, @RequestBody Zone z
    ) throws NoSuchFieldException, IllegalAccessException {

        Zone zoneRef = zoneRepository.getOne(id);
        Boolean updatedFlag = false;

        if (!zoneRef.getOrganizationId().equals(organizationId) && !isAdmin(organizationId))
            throw new AccessDeniedException("You can not edit zones in this organization. (if you are root admin contact technical support)");

        if (z.getPolygon() != null) {
            if (!z.getPolygonCoordinates().equals(zoneRef.getPolygonCoordinates())) {
                zoneRef.setPolygon(z.getPolygon());
                updatedFlag = true;
            }
        }

//        if (z.getOrganizationId() != null && !z.getOrganizationId().equals(zoneRef.getOrganizationId())) {
//            zoneRef.setOrganizationId(z.getOrganizationId());
//            updatedFlag = true;
//        }

        if (z.getTitle() != null && !z.getTitle().equals(zoneRef.getTitle())) {
            zoneRef.setTitle(z.getTitle());
            updatedFlag = true;
        }

        if (updatedFlag) return zoneRepository.saveAndFlush(zoneRef);

        return zoneRef;
    }

    @RequestMapping(
            path = "/{id}",
            method = RequestMethod.DELETE,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public void delete(
            @RequestHeader(value = "User-Id") Long userId,
            @RequestHeader(value = "Organization-Id", required = false) Long organizationId,
            @PathVariable(name = "id") Long id
    ) {
        System.out.println("deleting zone with id "+id);
        if (isAdmin(organizationId)) {
            System.out.println("admin deleting zone with id "+id);
            zoneRepository.delete(id);
        } else {
            System.out.println("user deleting zone with id "+id);
            zoneRepository.deleteByIdAndOrganizationId(id, organizationId);
        }
    }

    @RequestMapping(
            path = "/find/by/organization/{id}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public Iterable<Zone> findByOrganization(
            @RequestHeader(value = "User-Id") Long userId,
            @RequestHeader(value = "Organization-Id", required = false) Long organizationIdHeader,
            @PathVariable(name = "id") Long organizationId
    ) {

        if (!isAdmin(organizationId) && !organizationIdHeader.equals(organizationId))
            throw new AccessDeniedException("You can not fetch zones of another organization. (if you are root admin contact technical support)");

        return zoneRepository.findAll(QZone.zone.organizationId.eq(organizationId));
    }

    @RequestMapping(
            path = "/find/by/configuration/{id}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public Collection<Zone> findByConfigurationId(
            @RequestHeader(value = "User-Id") Long userId,
            @RequestHeader(value = "Organization-Id", required = false) Long organizationId,
            @PathVariable(name = "id") Long configId
    ) {

        if (!isAdmin(organizationId))
            return zoneRepository.findByConfigurationIdAndOrganizationId(configId, organizationId);
        else
            return zoneRepository.findByConfigurationId(configId);
    }


    @Transactional
    @RequestMapping(
            path = "/enable/for/configuration/{id}",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public void enableForConfiguration(
            @RequestHeader(value = "User-Id") Long userId,
            @RequestHeader(value = "Organization-Id", required = false) Long organizationId,
            @PathVariable(name = "id") Long configId, @RequestBody Set<Zone> zones
    ) {

        if (!isAdmin(organizationId)) {
            zones.forEach(zone -> {
                if (!zone.getOrganizationId().equals(organizationId))
                    throw new AccessDeniedException("You can not use zones of another organization.");
            });
        }

        zones.forEach(zone -> em.persist(new ConfigurationZone(configId, zone.getId())));
        em.flush();
        em.clear();
    }

    @Transactional
    @RequestMapping(
            path = "/update/for/configuration/{id}",
            method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public void updateForConfiguration(
            @RequestHeader(value = "User-Id") Long userId,
            @RequestHeader(value = "Organization-Id", required = false) Long organizationId,
            @PathVariable(name = "id") Long configId, @RequestBody Set<Zone> zones
    ) {
        //Мы не удаляем старые зоны, так как при обновлении конфигурации, мы на самом деле создаем новую конфигурацию,
        //а старую скрываем, поэтому переданный id новый и не может содержаться в связке конфигураций и зон
        //disableForConfiguration(configId);
        enableForConfiguration(organizationId, userId, configId, zones);
    }


    @Transactional
    @RequestMapping(
            path = "/disable/for/configuration/{id}",
            method = RequestMethod.DELETE,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public void disableForConfiguration(
            @RequestHeader(value = "User-Id") Long userId,
            @RequestHeader(value = "Organization-Id", required = false) Long organizationId,
            @PathVariable(name = "id") Long configId
    ) {

        //noinspection JpaQueryApiInspection
        em.createNamedQuery("ConfigurationZone.deleteByConfigId").setParameter("configId", configId).executeUpdate();
    }

    private Boolean isAdmin(Long organizationId) {
        //TODO: !IMPORTANT Replace organizationId != null with userIsAdmin()
        return organizationId == null;
    }
}
