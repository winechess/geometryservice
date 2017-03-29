package com.dit.suumuagent.geometryservice.service;

import com.dit.suumuagent.geometryservice.helpers.ExtendedCoordinate;
import com.dit.suumuagent.geometryservice.model.Location;
import com.dit.suumuagent.geometryservice.repository.LocationRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LocationServiceTest {


    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private LocationRepository locationRepository;
    private Location location;
    private Random r = new Random();
    HttpEntity req;


    @Before
    public void setUp() throws Exception {

        req = new HttpEntity(null, null);
        location = new Location();
        location.setCoordinates(new ExtendedCoordinate(0.0, 0.0));
        location.setDeviceId(r.longs(1, Long.MAX_VALUE).findFirst().getAsLong());
        location.setDate(new Date());

        locationRepository.save(location);
    }

    @After
    public void tearDown() throws Exception {
        locationRepository.deleteAll();
        location = null;
        req = null;
    }

    @Test
    public void create() throws Exception {

        location.setId(null);
        location.setDate(null);

        ResponseEntity<Location> response = restTemplate.postForEntity("/locations", location, Location.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualToIgnoringNullFields(location);
    }

    @Test
    public void tryToCreateWithEmptyDeviceId() throws Exception {

        location.setId(null);
        location.setDate(null);
        location.setDeviceId(null);

        ResponseEntity<Location> response = restTemplate.postForEntity("/locations", location, Location.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }


    @Test
    public void tryToCreateWithEmptyCoordinates() throws Exception {

        location.setId(null);
        location.setDate(null);
        location.setPoint(null);

        ResponseEntity<Location> response = restTemplate.postForEntity("/locations", location, Location.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void get() throws Exception {

        ResponseEntity<Location[]> response = restTemplate.getForEntity("/locations", Location[].class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().length).isNotEqualTo(0);
        assertThat(response.getBody()[0]).isEqualToIgnoringNullFields(location);
    }

    @Test
    public void deleteLocationsByDeviceId() throws Exception {

        for (int i = 0; i < 5; i++) {
            location.setId(null);
            locationRepository.save(location);
        }

        ResponseEntity<Void> response = restTemplate.exchange("/locations/delete/by/device/{deviceId}", HttpMethod.DELETE, req, Void.class, location.getDeviceId());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        Set<Location> res = locationRepository.findAllByDeviceIdAndDateBetween(
                location.getDeviceId(), location.getDate(), new Date()
        );
        assertThat(res.size()).isEqualTo(0);
    }

}