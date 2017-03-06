package com.dit.suumuagent.geometryservice.repository;

import com.dit.suumuagent.geometryservice.model.Coordinate;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class CoordinateRepositoryTest {

    @Autowired
    private CoordinateRepository coordinateRepository;

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
        Coordinate coordinate = coordinateRepository.save(c);
        assertThat(coordinate.getDate()).isEqualTo(c.getDate());
        assertThat(coordinate.getDeviceId()).isEqualTo(c.getDeviceId());
        assertThat(coordinate.getLat()).isEqualTo(c.getLat());
        assertThat(coordinate.getLng()).isEqualTo(c.getLng());
    }

}