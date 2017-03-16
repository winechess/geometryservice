package com.dit.suumuagent.geometryservice.model;

import com.dit.suumuagent.geometryservice.helpers.ExtendedCoordinate;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ZoneTest {

    private ArrayList<ExtendedCoordinate> points;

    @Before
    public void setUp() throws Exception {

        points = new ArrayList<>();
        points.add(new ExtendedCoordinate(0.0, 0.0));
        points.add(new ExtendedCoordinate(1.0, 0.0));
        points.add(new ExtendedCoordinate(1.0, 1.0));
        points.add(new ExtendedCoordinate(0.0, 1.0));
        points.add(new ExtendedCoordinate(0.0, 0.0));
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        System.out.println("Zone test ended");

    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("Zone test started");
    }

    @Test
    public void createPolygon() throws Exception {
        Zone zone = new Zone();
        zone.createPolygon(points);

        //noinspection unchecked
        assertThat(zone.getPolygon()).isNotNull();
        assertThat(points).isEqualTo(zone.getPolygonCoordinates());
    }

}