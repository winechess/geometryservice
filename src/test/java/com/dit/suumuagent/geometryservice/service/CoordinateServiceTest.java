package com.dit.suumuagent.geometryservice.service;

import com.dit.suumuagent.geometryservice.model.Coordinate;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class CoordinateServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;
    private final static Coordinate c = new Coordinate();

    @BeforeClass
    public static void setUp() throws Exception {
        c.setDate(new Date());
        c.setLat(89.01234567);
        c.setLng(123.4567890);
        c.setDeviceId(123L);
    }

    @Test
    public void create() throws Exception {

        ResponseEntity<Coordinate> response = restTemplate.postForEntity("/coordinates", c, Coordinate.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualToComparingFieldByField(c);
    }

    @Test
    public void get() throws Exception {
        create();
        ResponseEntity<Coordinate[]> response = restTemplate.getForEntity("/coordinates", Coordinate[].class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().length).isNotEqualTo(0);
        assertThat(response.getBody()[0]).isEqualToComparingFieldByField(c);
    }

}