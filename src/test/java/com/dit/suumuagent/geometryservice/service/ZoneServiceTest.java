package com.dit.suumuagent.geometryservice.service;

import com.dit.suumuagent.geometryservice.helpers.ExtendedCoordinate;
import com.dit.suumuagent.geometryservice.model.Zone;
import com.dit.suumuagent.geometryservice.repository.ZoneRepository;
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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ZoneServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ZoneRepository zoneRepository;
    private Zone zone;
    private HttpHeaders headers;
    private Random r = new Random();


    @Before
    public void setUp() throws Exception {

        ArrayList<ExtendedCoordinate> points = new ArrayList<>();
        points.add(new ExtendedCoordinate(0.0, 0.0));
        points.add(new ExtendedCoordinate(1.0, 0.0));
        points.add(new ExtendedCoordinate(1.0, 1.0));
        points.add(new ExtendedCoordinate(0.0, 1.0));
        points.add(new ExtendedCoordinate(0.0, 0.0));

        zone = new Zone();
        zone.setCoordinates(points);
        zone.setOrganizationId(1L);
        zone.setTitle("Test");

        headers = new HttpHeaders();
        headers.set("User-Id", "1");
        //headers.set("Organization-Id", zone.getOrganizationId().toString());

        zone = createZone(zone, headers);

//
//        ArrayList<ClientHttpRequestInterceptor> i = new ArrayList<>();
//        i.add(new LogRequestResponseFilter());
//        restTemplate.getRestTemplate().setInterceptors(i);

    }

    @After
    public void tearDown() throws Exception {
        zoneRepository.deleteAll();
        zone = null;
        headers = null;
    }

    @Test
    public void create() throws Exception {

        System.out.println("Testing create method ...");

        System.out.println("Saving entity to server...");
        zone.setId(null);
        HttpEntity<Zone> req = new HttpEntity<>(zone, headers);
        ResponseEntity<Zone> response = restTemplate.postForEntity("/zones", req, Zone.class);

        System.out.println("Assert that server respond with status 200");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        System.out.println("Assert that entity from server is equals to sended object");
        zone.setId(response.getBody().getId());
        assertThat(response.getBody()).isEqualToComparingFieldByFieldRecursively(zone);
    }

    @Test
    public void tryToCreateZoneAsUserInAnotherOrganization() throws Exception {

        System.out.println("tryToCreateZoneAsUserInAnotherOrganization");

        System.out.println("Saving entity to server...");
        headers.set("Organization-Id", "33542");
        zone.setId(null);
        HttpEntity<Zone> req = new HttpEntity<>(zone, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/zones", req, String.class);

        System.out.println("Assert that server respond with status 403");
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
        System.out.println("assert that message equals \"You can not create zone in this organization\"");
        assertThat(response.getBody()).contains("You can not create zone in this organization");
    }

    @Test
    public void findAll() throws Exception {

        System.out.println("Testing findAll method ...");

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

        System.out.println("Getting entity by id from server ...");
        ResponseEntity<Zone> response = getZoneById(zone.getId());
        Zone foundedZone = response.getBody();

        System.out.println("Assert that server respond with status 200");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        System.out.println("Assert that founded entity is equal to sended zone");
        assertThat(foundedZone).isEqualToComparingFieldByFieldRecursively(this.zone);

    }

    @Test
    public void edit() throws Exception {

        System.out.println("Testing edit method started.");

        //Change zone object by adding new coordinate to polygon
        List<ExtendedCoordinate> coords = zone.getCoordinates();
        coords.add(1, new ExtendedCoordinate(2.0, 2.0));
        //Rectreate polygon with new coordinates
        zone.setCoordinates(coords);
        zone.setTitle("New title");

        System.out.println("Patching entity by id ...");
        HttpEntity<Zone> req = new HttpEntity<>(zone, headers);
        ResponseEntity<Zone> response = restTemplate.exchange("/zones/{id}", HttpMethod.PATCH, req, Zone.class, zone.getId());

        System.out.println("Assert that server respond with status 200");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        System.out.println("Assert that zone updated");
        assertThat(response.getBody()).isEqualToComparingFieldByFieldRecursively(zone);

    }

    @Test
    public void tryToEditZoneAsUserInAnotherOrganization() throws Exception {

        System.out.println("tryToEditZoneAsUserInAnotherOrganization");

        //Change zone object by adding new coordinate to polygon
        List<ExtendedCoordinate> coords = zone.getCoordinates();
        coords.add(1, new ExtendedCoordinate(2.0, 2.0));
        //Recreate polygon with new coordinates
        zone.setCoordinates(coords);
        zone.setTitle("New title");
        headers.set("Organization-Id", "33542");
        HttpEntity<Zone> req = new HttpEntity<>(zone, headers);

        System.out.println("Patching entity by id ...");
        ResponseEntity<Map> response = restTemplate.exchange("/zones/{id}", HttpMethod.PATCH, req, Map.class, zone.getId());

        System.out.println("Assert that server respond with status 403");
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
        System.out.println("assert that message equals \"You can not create zone in this organization\"");
        assertThat((String) response.getBody().get("message")).isEqualTo("You can not edit zones in this organization.");

    }

    @Test
    public void updateEntityWithEmptyFields() throws Exception {

        System.out.println("Testing updateEntityWithEmptyFields ...");

        zone.setPolygon(null);
        zone.getCoordinates().clear();
        HttpEntity<Zone> req = new HttpEntity<>(zone, headers);

        System.out.println("Sending entity with coordinates property equal NULL ...");
        ResponseEntity<Zone> response = restTemplate.postForEntity("/zones", req, Zone.class);

        System.out.println("Assert that server respond with status 400");
        assertThat(response.getStatusCodeValue()).isEqualTo(400);

    }

    @Test
    public void delete() throws Exception {

        System.out.println("Testing delete method started.");

        HttpEntity<Zone> req = new HttpEntity<>(headers);

        System.out.println("Calling service delete method ...");
        restTemplate.exchange("/zones/{id}", HttpMethod.DELETE, req, Object.class, zone.getId());

        System.out.println("Calling service find method...");
        ResponseEntity<Zone> response = getZoneById(zone.getId());

        System.out.println("Assert that entity was not found");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    public void deleteZoneAsUser() throws Exception {

        System.out.println("deleteZoneAsUser");

        headers.set("Organization-Id", "1");
        HttpEntity<Zone> req = new HttpEntity<>(headers);

        System.out.println("Calling service delete method ...");
        restTemplate.exchange("/zones/{id}", HttpMethod.DELETE, req, Object.class, zone.getId());

        System.out.println("Calling service find method...");
        ResponseEntity<Zone> response = getZoneById(zone.getId());

        System.out.println("Assert that entity was not found");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    public void findZoneByOrganizationId() throws Exception {

        System.out.println("findZoneByOrganizationId");

        Long organizationId = 1L;
        HttpEntity<Zone> req = new HttpEntity<>(headers);

        System.out.println("Sending request to server: {url: '/zones/find/by/organization/{id}', method: GET, id: " + organizationId + "} ...");
        ResponseEntity<Set> response = restTemplate.exchange("/zones/find/by/organization/{id}", HttpMethod.GET, req, Set.class, organizationId);

        System.out.println("Assert that response code is 200");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        System.out.println("Assert that 1 entity was found");
        assertThat(response.getBody().size()).isEqualTo(1);

    }

    @Test
    public void tryToFindZoneByOrganizationIdAsUserInAnotherOrganization() throws Exception {

        System.out.println("tryToFindZoneByOrganizationIdAsUserInAnotherOrganization");

        Long organizationId = 2L;
        headers.set("Organization-Id", "1");
        HttpEntity<Zone> req = new HttpEntity<>(headers);

        System.out.println("Sending request to server: {url: '/zones/find/by/organization/{id}', method: GET, id: " + organizationId + "} ...");
        ResponseEntity<Map> response = restTemplate.exchange("/zones/find/by/organization/{id}", HttpMethod.GET, req, Map.class, organizationId);

        System.out.println("Assert that response code is 403");
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
        System.out.println("Assert that message equals \"You can not fetch zones of another organization.\"");
        assertThat((String) response.getBody().get("message")).isEqualTo("You can not fetch zones of another organization.");

    }

    @Test
    public void enableZonesForConfiguration() throws Exception {

        System.out.println("enableZonesForConfiguration");

        Set<Zone> zones = createSeveralZones(2);
        Long configurationId = r.longs(1, Long.MAX_VALUE).findFirst().getAsLong();

        ResponseEntity<Object> response = enableZonesForConfiguration(configurationId, headers, zones);

        System.out.println("Assert that response code is 200");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

    }

    @Test
    public void enableZonesForConfigurationAsUser() throws Exception {

        System.out.println("enableZonesForConfigurationAsUser");

        Set<Zone> zones = createSeveralZones(2);
        Long configurationId = r.longs(1, Long.MAX_VALUE).findFirst().getAsLong();
        headers.set("Organization-Id", "1");
        ResponseEntity<Object> response = enableZonesForConfiguration(configurationId, headers, zones);

        System.out.println("Assert that response code is 200");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

    }

    @Test
    public void tryToEnableZonesOfAnotherOrganizationForConfigurationAsUser() throws Exception {

        System.out.println("tryToEnableZonesOfAnotherOrganizationForConfigurationAsUser");

        Set<Zone> zones = createSeveralZones(2, r.longs(1, Long.MAX_VALUE).findFirst().getAsLong());
        Long configurationId = r.longs(1, Long.MAX_VALUE).findFirst().getAsLong();

        headers.set("Organization-Id", "1");
        ResponseEntity<Object> response = enableZonesForConfiguration(configurationId, headers, zones);

        System.out.println("Assert that server respond with status 403");
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
        System.out.println("assert that message equals \"You can not create zone in this organization\"");
        assertThat((String) ((Map) response.getBody()).get("message")).isEqualTo("You can not use zones of another organization.");

    }


    @Test
    public void disableZonesForConfiguration() throws Exception {

        System.out.println("disableZonesForConfiguration");

        Set<Zone> zones = createSeveralZones(2);
        Long configurationId = r.longs(1, Long.MAX_VALUE).findFirst().getAsLong();

        enableZonesForConfiguration(configurationId, headers, zones);

        HttpEntity<Iterable> req = new HttpEntity<>(zones, headers);
        System.out.println("Sending request to server: {url: '/zones/disable/for/configuration/{id}', method: DELETE, id: " + configurationId + "} ...");
        ResponseEntity<Object> response = restTemplate.exchange("/zones/disable/for/configuration/{id}", HttpMethod.DELETE, req, Object.class, configurationId);

        System.out.println("Assert that response code is 200");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        System.out.println("Assert that there are no zones enabled for organization");
        assertThat(findZonesByConfigurationId(configurationId, headers).getBody().size()).isEqualTo(0);

    }

    @Test
    public void findZoneByConfigurationId() throws Exception {

        System.out.println("findZoneByConfigurationId");
        //Because when we edit config
        Long configurationId = r.longs(1, Long.MAX_VALUE).findFirst().getAsLong();

        enableZonesForConfiguration(configurationId, headers, createSeveralZones(2));

        //Act
        ResponseEntity<Set> response = findZonesByConfigurationId(configurationId, headers);

        //Assert
        System.out.println("Assert that response code is 200");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        System.out.println("Assert that 2 zones was found for configuration " + configurationId);
        assertThat(response.getBody().size()).isEqualTo(2);

    }

    @Test
    public void findZoneByConfigurationIdAsUser() throws Exception {

        System.out.println("findZoneByConfigurationIdAsUser");

        Long configurationId = r.longs(1, Long.MAX_VALUE).findFirst().getAsLong();
        enableZonesForConfiguration(configurationId, headers, createSeveralZones(2));
        headers.set("Organization-Id", "1");
        //Act
        ResponseEntity<Set> response = findZonesByConfigurationId(configurationId, headers);

        //Assert
        System.out.println("Assert that response code is 200");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        System.out.println("Assert that 2 zones was found for configuration " + configurationId);
        assertThat(response.getBody().size()).isEqualTo(2);

    }

    private ResponseEntity<Zone> getZoneById(Long id) {
        HttpEntity<Zone> req = new HttpEntity<>(headers);
        return restTemplate.exchange("/zones/{id}", HttpMethod.GET, req, Zone.class, id);
    }

    private ResponseEntity<Set> findZonesByConfigurationId(Long configurationId, HttpHeaders headers) {
        HttpEntity<Object> req = new HttpEntity<>(headers);
        System.out.println("Sending request to server: {url: '/zones/find/by/configuration/{id}', method: GET, id: " + configurationId + "} ...");
        return restTemplate.exchange("/zones/find/by/configuration/{id}", HttpMethod.GET, req, Set.class, configurationId);
    }

    private ResponseEntity<Object> enableZonesForConfiguration(Long configurationId, HttpHeaders headers, Set<Zone> zones) {
        HttpEntity<Iterable> req = new HttpEntity<>(zones, headers);
        System.out.println("Sending request to server: {url: '/zones/enable/for/configuration/{id}', method: POST, id: " + configurationId + "} ...");
        return restTemplate.exchange("/zones/enable/for/configuration/{id}", HttpMethod.POST, req, Object.class, configurationId);
    }

    private Zone createZone(Zone zone, HttpHeaders headers) {
        HttpEntity<Zone> req = new HttpEntity<>(zone, headers);
        return restTemplate.postForEntity("/zones", req, Zone.class).getBody();
    }

    private Set<Zone> createSeveralZones(int zoneAmount, Long organizationId) {
        zone.setOrganizationId(organizationId);
        return createSeveralZones(zoneAmount);
    }

    private Set<Zone> createSeveralZones(int zoneAmount) {

        Set<Zone> createdZones = new HashSet<>(zoneAmount);
        zone.setId(null);
        HttpEntity<Zone> req = new HttpEntity<>(zone, headers);

        for (int i = 0; i < zoneAmount; i++) {
            createdZones.add(restTemplate.postForEntity("/zones", req, Zone.class).getBody());
        }
        return createdZones;
    }

    class SpringPage extends PageImpl<Zone> {
        public SpringPage() {
            super(Collections.emptyList());
        }

        public SpringPage(List<Zone> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }
}