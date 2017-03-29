--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.2
-- Dumped by pg_dump version 9.6.2

-- Started on 2017-03-29 03:58:00 MSK

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = geometryservice, pg_catalog;

--
-- TOC entry 3764 (class 0 OID 51184)
-- Dependencies: 207
-- Data for Name: configurations_zones; Type: TABLE DATA; Schema: geometryservice; Owner: geometryservice
--

INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (1, 33);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (1, 34);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (2, 34);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (5, 33);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (5, 34);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (5, 36);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (23, 34);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (31, 34);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (31, 36);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (32, 36);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (32, 34);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (35, 36);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (35, 34);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (36, 34);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (37, 34);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (38, 34);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (39, 34);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (39, 36);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (46, 38);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (47, 39);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (48, 37);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (49, 40);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (54, 41);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (54, 36);
INSERT INTO configurations_zones (configuration_id, zone_id) VALUES (54, 34);


--
-- TOC entry 3761 (class 0 OID 36829)
-- Dependencies: 204
-- Data for Name: locations; Type: TABLE DATA; Schema: geometryservice; Owner: geometryservice
--

INSERT INTO locations (id, date, device_id, point) VALUES (6, '2017-03-24 15:55:06.802', 20862, '0101000020E6100000A3737E8AE3382640A3737E8AE3382640');
INSERT INTO locations (id, date, device_id, point) VALUES (4, '2017-03-27 16:28:45.081', 20862, '0101000020E6100000A3737E8AE3382640A3737E8AE3382640');
INSERT INTO locations (id, date, device_id, point) VALUES (7, '2017-03-28 19:04:07.38', 20864, '0101000020E6100000310BED9C66392640A3737E8AE3382640');
INSERT INTO locations (id, date, device_id, point) VALUES (8, '2017-03-28 19:04:29.218', 20864, '0101000020E6100000310BED9C66392640310BED9C66392640');
INSERT INTO locations (id, date, device_id, point) VALUES (9, '2017-03-28 19:04:47.953', 20864, '0101000020E6100000A3737E8AE3382640310BED9C66392640');
INSERT INTO locations (id, date, device_id, point) VALUES (10, '2017-03-28 19:04:57.49', 20864, '0101000020E6100000A3737E8AE3382640A3737E8AE3382640');


--
-- TOC entry 3769 (class 0 OID 0)
-- Dependencies: 203
-- Name: locations_id_seq; Type: SEQUENCE SET; Schema: geometryservice; Owner: geometryservice
--

SELECT pg_catalog.setval('locations_id_seq', 10, true);


--
-- TOC entry 3623 (class 0 OID 35097)
-- Dependencies: 189
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: geometryservice; Owner: suumu
--



--
-- TOC entry 3763 (class 0 OID 36844)
-- Dependencies: 206
-- Data for Name: zones; Type: TABLE DATA; Schema: geometryservice; Owner: geometryservice
--

INSERT INTO zones (id, polygon, organization_id, title, created, created_by, edited, edited_by) VALUES (36, '0103000020E61000000100000005000000B31B47A4A4DF4B40010000007FC842403AAEBB555BDE4B40010000000DE04240A620A9AF6CD74B400000000040DA4240EFC6B0363CD64B40010000000CC34240B31B47A4A4DF4B40010000007FC84240', 1147, 'Zone 2', '2017-03-13 18:27:38.959', 1, '2017-03-13 18:27:38.959', 1);
INSERT INTO zones (id, polygon, organization_id, title, created, created_by, edited, edited_by) VALUES (34, '0103000020E610000001000000050000004F1E9642DFDC4B4001000000C8CA4240CAB92E93ACDC4B4001000000E8E6424006A62754BDD54B40010000008EE64240A9E30732DFD14B4001000000F8C742404F1E9642DFDC4B4001000000C8CA4240', 1147, 'Zone 1', '2017-03-13 11:45:35.32', 1, '2017-03-13 11:45:35.32', 1);
INSERT INTO zones (id, polygon, organization_id, title, created, created_by, edited, edited_by) VALUES (37, '0103000020E61000000100000004000000C81E93F182E24B4001000000FBC44240DEE10F7609D64B400100000097DD4240EAA8C89277D24B4001000000D1C14240C81E93F182E24B4001000000FBC44240', 3, 'Zone 3', '2017-03-14 19:15:21.111', 1, '2017-03-14 19:15:21.111', 1);
INSERT INTO zones (id, polygon, organization_id, title, created, created_by, edited, edited_by) VALUES (38, '0103000020E61000000100000005000000E8A6F6EC55E04B4001000000ADB842409666279320D74B4001000000D6CB4240CB3D7DE7D8D44B4001000000E1A242405F0B3A8101E34B4001000000F59D4240E8A6F6EC55E04B4001000000ADB84240', 19018, 'Zone 4', '2017-03-14 19:16:21.274', 1, '2017-03-14 19:16:21.274', 1);
INSERT INTO zones (id, polygon, organization_id, title, created, created_by, edited, edited_by) VALUES (39, '0103000020E6100000010000000C0000004F5E00AF4EF24B40010000008DC942408B72C79B06F14B40010000007ABE4240C61A482067EB4B400100000058C2424021AF6A51E4DA4B40010000003CC04240CBD6F3C08CD44B40010000005CAF4240A142C19F9ACD4B4001000000B8BC4240A2CA4076CDCD4B4000000000D8D84240F6C970D622D64B4001000000F6E742406D69720517DB4B4001000000DED24240817CCDEDB2EB4B400100000049D14240AB3923296FF04B4001000000A0D442404F5E00AF4EF24B40010000008DC94240', 9393, 'Zone 6', '2017-03-14 19:21:34.455', 1, '2017-03-14 19:21:34.455', 1);
INSERT INTO zones (id, polygon, organization_id, title, created, created_by, edited, edited_by) VALUES (40, '0103000020E61000000100000006000000516AB0B29AD94B4001000000BFA642409DB8CDE28ED34B400100000021DB4240C6D3B41764CF4B400100000022CB424086D11F35B2CE4B4001000000EA9942408BE38DC5E6D94B4001000000C0964240516AB0B29AD94B4001000000BFA64240', 20605, 'Zone 5', '2017-03-14 19:22:53.627', 1, '2017-03-14 19:22:53.627', 1);
INSERT INTO zones (id, polygon, organization_id, title, created, created_by, edited, edited_by) VALUES (41, '0103000020E61000000100000005000000578D30F8BDDF4B4001000000E7C942405A31D53247DC4B4000000000EEE04240A4E8EAFF92D14B40010000005FD94240632FF7652DD14B4001000000CFB44240578D30F8BDDF4B4001000000E7C94240', 1147, 'Test', '2017-03-20 12:21:54.955', 2, '2017-03-20 12:21:54.955', 2);
INSERT INTO zones (id, polygon, organization_id, title, created, created_by, edited, edited_by) VALUES (45, '0103000020E610000001000000040000006F74E5A7DCDD4B400100000068D04240BEAF493B93DC4B40010000002EEC4240AEB14F52B0CF4B400100000067E042406F74E5A7DCDD4B400100000068D04240', 9394, 'sdfg', '2017-03-20 12:26:32.505', 2, '2017-03-20 12:26:32.505', 2);
INSERT INTO zones (id, polygon, organization_id, title, created, created_by, edited, edited_by) VALUES (46, '0103000020E610000001000000040000009E1E7A6F4DE34B40010000007ABE424085F01756A7DE4B4001000000DDE242408BE00B26DDD24B400100000024D842409E1E7A6F4DE34B40010000007ABE4240', 1147, 'dfghasf', '2017-03-20 12:26:42.592', 2, '2017-03-20 12:26:42.592', 2);
INSERT INTO zones (id, polygon, organization_id, title, created, created_by, edited, edited_by) VALUES (33, '0103000020E6100000010000000600000003442EC7E8EA4B4001000000E0B2424031C9AA9E4ED94B400100000088EC424031C9AA9E4ED94B40010000002CB242403C229B511BEB4B4001000000AAE8424019597652D4C94B400100000068D0424003442EC7E8EA4B4001000000E0B24240', 9394, 'Devil zone1', '2017-03-13 11:23:24.245', 1, '2017-03-13 11:23:24.245', 1);
INSERT INTO zones (id, polygon, organization_id, title, created, created_by, edited, edited_by) VALUES (50, '0103000020E61000000100000004000000F278189AF8DC4B400100000061B942400F72CF5681D94B400100000049D14240793740B742D34B4001000000CEC44240F278189AF8DC4B400100000061B94240', 1147, 'Zone 10', '2017-03-21 13:30:37.399', 2, '2017-03-21 13:30:37.399', 2);


--
-- TOC entry 3770 (class 0 OID 0)
-- Dependencies: 205
-- Name: zones_id_seq; Type: SEQUENCE SET; Schema: geometryservice; Owner: geometryservice
--

SELECT pg_catalog.setval('zones_id_seq', 51, true);


-- Completed on 2017-03-29 03:58:02 MSK

--
-- PostgreSQL database dump complete
--

