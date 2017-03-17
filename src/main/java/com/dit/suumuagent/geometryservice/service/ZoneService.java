package com.dit.suumuagent.geometryservice.service;

import com.dit.suumuagent.geometryservice.helpers.SortUtils;
import com.dit.suumuagent.geometryservice.helpers.exceptions.AccessDeniedException;
import com.dit.suumuagent.geometryservice.model.ConfigurationZone;
import com.dit.suumuagent.geometryservice.model.QZone;
import com.dit.suumuagent.geometryservice.model.Zone;
import com.dit.suumuagent.geometryservice.repository.ZoneRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.*;


@RestController
@RequestMapping(path = "/zones")
public class ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;
    @PersistenceContext
    private EntityManager em;

    @Transactional
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public Zone create(
            @RequestHeader(value = "User-Id") Long userId,
            @RequestHeader(value = "Organization-Id", required = false) Long organizationId,
            @Validated @RequestBody Zone z
    ) {

        System.out.println(z);
        if (!z.getOrganizationId().equals(organizationId) && !isAdmin(organizationId)) {
            throw new AccessDeniedException("You can not create zone in this organization.");
        }

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

    @Transactional
    @RequestMapping(
            path = "/{id}",
            method = RequestMethod.PATCH,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public Zone edit(
            @RequestHeader(value = "User-Id") Long userId,
            @RequestHeader(value = "Organization-Id", required = false) Long organizationId,
            @PathVariable(name = "id") Long id,
            @Validated @RequestBody Zone z
    ) {
        Zone zoneRef = zoneRepository.getOne(id);
        Boolean updatedFlag = false;

        if (!zoneRef.getOrganizationId().equals(organizationId) && !isAdmin(organizationId))
            throw new AccessDeniedException("You can not edit zones in this organization.");

        if (!z.getCoordinates().equals(zoneRef.getCoordinates())) {
            zoneRef.setCoordinates(z.getCoordinates());
            updatedFlag = true;
        }

        if (!z.getTitle().equals(zoneRef.getTitle())) {
            zoneRef.setTitle(z.getTitle());
            updatedFlag = true;
        }

        if (updatedFlag) zoneRef =  zoneRepository.saveAndFlush(zoneRef);

        return zoneRef;
    }

    @Transactional
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
        if (isAdmin(organizationId)) {
            zoneRepository.delete(id);
        } else {
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

        if (!isAdmin(organizationIdHeader) && !organizationIdHeader.equals(organizationId))
            throw new AccessDeniedException("You can not fetch zones of another organization.");

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
        //TODO: сделать проверку является ли пользователем администратором организации, к конфигурации которой прикрепляются зоны необходимо в API GATEWAY

        if (!isAdmin(organizationId)) {
            //Проверяем не использовал ли пользователь (администратор организации) зоны из другой организации
            zones.forEach(zone -> zoneOrganizationEqualsUserOrganization(zone, organizationId));
        }

        zones.forEach(zone -> em.persist(new ConfigurationZone(configId, zone.getId())));
        em.flush();
        em.clear();
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
        //TODO: сделать проверку на то, является ли пользователем администратором организации, конфигурация которой удаляется необходимо в API GATEWAY
        //noinspection JpaQueryApiInspection
        em.createNamedQuery("ConfigurationZone.deleteByConfigId").setParameter("configId", configId).executeUpdate();
    }

    private Boolean isAdmin(Long organizationId) {
        //TODO: !IMPORTANT Replace organizationId != null with userIsAdmin()
        return organizationId == null;
    }

    private void zoneOrganizationEqualsUserOrganization(Zone zone, Long userOrganizationId){
        if (!zone.getOrganizationId().equals(userOrganizationId)) throw new AccessDeniedException("You can not use zones of another organization.");
    }
}
