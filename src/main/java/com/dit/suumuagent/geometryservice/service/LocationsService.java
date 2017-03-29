package com.dit.suumuagent.geometryservice.service;

import com.dit.suumuagent.geometryservice.model.Location;
import com.dit.suumuagent.geometryservice.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/locations")
public class LocationsService {

    @Autowired
    private LocationRepository locationRepository;

    @Transactional
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public Location create(@RequestBody Location location) {

        logger.debug("{}.create({}) method called with parameters: {}", this.getClass(), location.getClass(), location);

        //Assert device id  not null
        if (location.getDeviceId() == null)
            throw new IllegalArgumentException("Для местоположения не указан идентификатор устройства.");
        //Assert that coordinates is not null
        if (location.getPoint() == null || location.getPoint().isEmpty())
            throw new IllegalArgumentException("Местоположение не указано.");

        //Set location date
        location.setDate(new Date());

        return locationRepository.save(location);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    @Transactional
    @RequestMapping(
            path = "/delete/by/device/{deviceId}",
            method = RequestMethod.DELETE,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public void deleteByDeviceId(@PathVariable(name = "deviceId") Long deviceId) {
        locationRepository.deleteByDeviceId(deviceId);
    }

    @Transactional
    @RequestMapping(
            path = "/find/by/device/{deviceId}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public Set<Location> findByDeviceId(
            @PathVariable(name = "deviceId") Long deviceId,
            @RequestParam(name = "start_date") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX") Date startDate,
            @RequestParam(name = "end_date") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX") Date endDate
    ) {
        logger.debug("Start date: {}", startDate);
        logger.debug("End date: {}", endDate);
        logger.debug("Device id: {}", deviceId);

        return locationRepository.findAllByDeviceIdAndDateBetween(deviceId, startDate, endDate);
    }

    @Transactional
    @RequestMapping(
            path = "/find/latest/for/devices",
            method = RequestMethod.POST, //не красиво так делать, но оставлять кучу id  в URL это проблема
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public Set<Location> findLatestLocationsForDevices(
            @RequestBody Set<Long> devicesIds
    ) {
        if (devicesIds.isEmpty()) return new HashSet<>(0);
        return locationRepository.findLatestForDevices(devicesIds);
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
}
