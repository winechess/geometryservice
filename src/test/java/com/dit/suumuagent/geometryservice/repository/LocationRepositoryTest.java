package com.dit.suumuagent.geometryservice.repository;

import com.dit.suumuagent.geometryservice.model.Location;
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
public class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;

    private final static Location c = new Location();

    @BeforeClass
    public static void setUp() throws Exception {
        c.setDate(new Date());
        c.setDeviceId(123L);
    }

    @Test
    public void create() throws Exception {
        Location location = locationRepository.save(c);
        assertThat(location.getDate()).isEqualTo(c.getDate());
        assertThat(location.getDeviceId()).isEqualTo(c.getDeviceId());
    }

}