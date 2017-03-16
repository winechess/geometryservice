package com.dit.suumuagent.geometryservice.service;

import com.dit.suumuagent.geometryservice.helpers.ExtendedCoordinate;
import com.dit.suumuagent.geometryservice.model.Zone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ZoneServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;
    private Zone zone;
    private HttpHeaders headers;



    @Before
    public void setUp() throws Exception {

        ArrayList<ExtendedCoordinate> points = new ArrayList<>();
        points.add(new ExtendedCoordinate(0.0, 0.0));
        points.add(new ExtendedCoordinate(1.0, 0.0));
        points.add(new ExtendedCoordinate(1.0, 1.0));
        points.add(new ExtendedCoordinate(0.0, 1.0));
        points.add(new ExtendedCoordinate(0.0, 0.0));

        zone = new Zone();
        zone.createPolygon(points);
        zone.setOrganizationId(321L);
        zone.setTitle("Test");

        headers = new HttpHeaders();
        headers.set("User-Id", "1");
        //headers.set("Organization-Id", zone.getOrganizationId().toString());
//
//        ArrayList<ClientHttpRequestInterceptor> i = new ArrayList<>();
//        i.add(new LogRequestResponseFilter());
//        restTemplate.getRestTemplate().setInterceptors(i);

    }

    @After
    public void tearDown() throws Exception {
        zone = null;
        headers = null;

    }

    @Test
    public void create() throws Exception {

        System.out.println("Testing create method ...");

        System.out.println("Saving entity to server...");
        HttpEntity<Zone> req = new HttpEntity<>(zone, headers);
        ResponseEntity<Zone> response = restTemplate.postForEntity("/zones", req, Zone.class);

        System.out.println("Assert that server respond with status 200");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        //Set zone object id and date to returning id and date to pass field by filed comaprison and not to create entity further
        zone.setId(response.getBody().getId());
        System.out.println("Assert that entity from server is equals to sended object");
        assertThat(response.getBody()).isEqualToComparingFieldByFieldRecursively(zone);

        System.out.println("Testing create method ended.");
    }

    @Test
    public void createEntityWithEmptyPolygon() throws Exception {

        System.out.println("Testing createEntityWithEmptyPolygon ...");

        Zone z = new Zone();
        z.setOrganizationId(zone.getOrganizationId());

        System.out.println("Saving zone with empty polygon ...");
        HttpEntity<Zone> req = new HttpEntity<>(z, headers);
        ResponseEntity<Zone> response = restTemplate.postForEntity("/zones",  req, Zone.class);

        System.out.println("Assert that server respond with status 400");
        assertThat(response.getStatusCodeValue()).isEqualTo(400);

        System.out.println("Testing createEntityWithEmptyPolygon ended.");
    }

    @Test
    public void createEntityWithEmptyConfigId() throws Exception {

        System.out.println("Testing createEntityWithEmptyConfigId ...");

        Zone z = new Zone();
        z.setPolygon(zone.getPolygon());

        System.out.println("Saving zone with empty configId...");
        HttpEntity<Zone> req = new HttpEntity<>(z, headers);
        ResponseEntity<Zone> response = restTemplate.postForEntity("/zones", req, Zone.class);

        System.out.println("Assert that server respond with status 400");
        assertThat(response.getStatusCodeValue()).isEqualTo(400);

        System.out.println("Testing createEntityWithEmptyConfigId ended.");
    }

    @Test
    public void findAll() throws Exception {
        
        System.out.println("Testing findAll method ...");

        System.out.println("Saving entity to server if not exists ...");
        createZoneIfNotExists();

        System.out.println("Getting all entities from server ...");
        HttpEntity<Zone> req = new HttpEntity<>(headers);
        ResponseEntity<LinkedHashMap> response = restTemplate.exchange("/zones", HttpMethod.GET, req, LinkedHashMap.class);
        LinkedHashMap body = response.getBody();

        System.out.println("Assert that server respond with status 200");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        System.out.println("Assert that amount of found entities is not 0");
        assertThat(body.get("totalElements")).isNotEqualTo(0);
        
        System.out.println("Testing findAll method ended.");
    }

    @Test
    public void find() throws Exception {
        
        System.out.println("Testing find method started.");

        System.out.println("Saving entity to server if not exists ...");
        createZoneIfNotExists();

        System.out.println("Getting entity by id from server ...");
        ResponseEntity<Zone> response = getZoneById(zone.getId());
        Zone testedZone = response.getBody();

        System.out.println("Assert that server respond with status 200");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        System.out.println("Assert that founded entity is not null");
        assertThat(testedZone).isNotNull();

        System.out.println("Testing find method ended.");
    }

    @Test
    public void edit() throws Exception {

        System.out.println("Testing edit method started.");

        System.out.println("Saving entity to server if not exists ...");
        createZoneIfNotExists();

        //Change zone object by adding new coordinate to polygon
        List<ExtendedCoordinate> coords = zone.getPolygonCoordinates();
        coords.add(1, new ExtendedCoordinate(2.0, 2.0));
        //Rectreate polygon with new coordinates
        zone.createPolygon(coords);
        zone.setTitle("New title");
        //zone.setOrganizationId(1L);

        System.out.println("Patching entity by id ...");
        HttpEntity<Zone> req = new HttpEntity<>(zone, headers);
        Zone response = restTemplate.patchForObject("/zones/{id}", req, Zone.class, zone.getId());

        System.out.println("Assert that server return not null entity");
        assertThat(response).isNotNull();
        System.out.println("Assert that server returned entity is equal to sended object");
        assertThat(response).isEqualToComparingFieldByFieldRecursively(zone);

        System.out.println("Testing edit method ended.");
    }

    @Test
    public void updateEntityWithEmptyFields() throws Exception {

        System.out.println("Testing updateEntityWithEmptyFields ...");

        System.out.println("Sending entity with polygon value = NULL to edit service method ...");
        Zone z = new Zone();
        z.setId(zone.getId());
        HttpEntity<Zone> req = new HttpEntity<>(z, headers);
        ResponseEntity<Zone> response = restTemplate.postForEntity("/zones", req, Zone.class);

        System.out.println("Assert that server respond with status 400");
        assertThat(response.getStatusCodeValue()).isEqualTo(400);

        System.out.println("Testing updateEntityWithEmptyFields ended.");
    }

    @Test
    public void delete() throws Exception {

        System.out.println("Testing delete method started.");

        System.out.println("Saving entity to server if not exists ...");
        createZoneIfNotExists();

        System.out.println("Calling service delete method ...");
        HttpEntity<Zone> req = new HttpEntity<>(headers);
        restTemplate.exchange("/zones/{id}", HttpMethod.DELETE, req, Object.class, zone.getId());

        System.out.println("Calling service find method...");
        ResponseEntity<Zone> response = getZoneById(zone.getId());

        System.out.println("Assert that entity was not found");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        //Setting entity id to null to mark it as not persisted
        zone.setId(null);

        System.out.println("Testing delete method ended.");

    }

    private ResponseEntity<Zone> getZoneById(Long id){
        HttpEntity<Zone> req = new HttpEntity<>(headers);
        return restTemplate.exchange("/zones/{id}", HttpMethod.GET, req, Zone.class, id);
    }

    private void createZoneIfNotExists(){
        if(zone.getId() != null) return;
        HttpEntity<Zone> req = new HttpEntity<>(zone, headers);
        ResponseEntity<Zone> response = restTemplate.postForEntity("/zones", req, Zone.class);
        zone.setId(response.getBody().getId());
    }

    class SpringPage extends PageImpl<Zone>{
        public SpringPage() {
            super(Collections.emptyList());
        }

        public SpringPage(List<Zone> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }
}