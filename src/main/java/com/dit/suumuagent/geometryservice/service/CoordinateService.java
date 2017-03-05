package com.dit.suumuagent.geometryservice.service;

import com.dit.suumuagent.geometryservice.model.Coordinate;
import com.dit.suumuagent.geometryservice.repository.CoordinateRepository;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Created by vinichenkosa on 05/03/2017.
 */
@RestController
public class CoordinateService {

    @Autowired
    private CoordinateRepository coordinateRepository;

    @RequestMapping(
            method = RequestMethod.POST,
            path = "/coordinates",
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public Coordinate create(@RequestBody Coordinate c){
        c.setDate(new Date());
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        c.setLocation(geometryFactory.createPoint(new com.vividsolutions.jts.geom.Coordinate(c.getLng(), c.getLat())));
        return coordinateRepository.save(c);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "/coordinates"
    )
    public List<Coordinate> get(){
        return coordinateRepository.findAll();
    }

}
