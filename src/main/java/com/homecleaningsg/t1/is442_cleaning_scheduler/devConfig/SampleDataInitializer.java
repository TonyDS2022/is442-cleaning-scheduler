package com.homecleaningsg.t1.is442_cleaning_scheduler.devConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homecleaningsg.t1.is442_cleaning_scheduler.admin.Admin;
import com.homecleaningsg.t1.is442_cleaning_scheduler.admin.AdminRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.Client;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.ClientRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.ClientService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSite;
import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSiteRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplication;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveType;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.medicalrecord.MedicalRecord;
import com.homecleaningsg.t1.is442_cleaning_scheduler.medicalrecord.MedicalRecordRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.medicalrecord.MedicalRecordService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.subzone.Subzone;
import com.homecleaningsg.t1.is442_cleaning_scheduler.subzone.SubzoneRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.TripService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Profile("dev")
public class SampleDataInitializer implements ApplicationRunner {

    private final AdminRepository adminRepository;
    private final ContractRepository contractRepository;
    private final CleaningSessionRepository cleaningSessionRepository;
    private final ClientSiteRepository clientSiteRepository;
    private final ClientRepository clientRepository;
    private final ShiftRepository shiftRepository;
    private final SubzoneRepository subzoneRepository;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final LocationRepository locationRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final WorkerRepository workerRepository;
    private final ClientService clientService;
    private final LocationService locationService;
    private final MedicalRecordService medicalRecordService;
    private final TripService tripService;

    public SampleDataInitializer(
            AdminRepository adminRepository,
            ContractRepository contractRepository,
            CleaningSessionRepository cleaningSessionRepository,
            ClientSiteRepository clientSiteRepository,
            ClientRepository clientRepository,
            ShiftRepository shiftRepository,
            SubzoneRepository subzoneRepository,
            LeaveApplicationRepository leaveApplicationRepository,
            LocationRepository locationRepository,
            MedicalRecordRepository medicalRecordRepository,
            WorkerRepository workerRepository,
            ClientService clientService,
            LocationService locationService,
            MedicalRecordService medicalRecordService,
            TripService tripService
    ) {
        this.adminRepository = adminRepository;
        this.contractRepository = contractRepository;
        this.clientSiteRepository = clientSiteRepository;
        this.cleaningSessionRepository = cleaningSessionRepository;
        this.clientRepository = clientRepository;
        this.shiftRepository = shiftRepository;
        this.subzoneRepository = subzoneRepository;
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.locationRepository = locationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.workerRepository = workerRepository;
        this.clientService = clientService;
        this.locationService = locationService;
        this.medicalRecordService = medicalRecordService;
        this.tripService = tripService;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        // Add sample data here
        initializeSubzones();
        initializeLocation();
        initializeTrips();
        initializeWorkers();
        initializeClients();
        initializeContracts();
        initializeCleaningSessions();
        initializeShifts();
        initializeAdmins();
        initializeMedicalRecords();
        initializeLeaveApplications();
    }

    public void initializeSubzones() throws Exception {
        File subzoneGeojson = new File("MasterPlan2019SubzoneBoundaryNoSeaGEOJSON.geojson");
        ObjectMapper objectMapper = new ObjectMapper();
        FeatureCollection featureCollection = objectMapper.readValue(subzoneGeojson, FeatureCollection.class);

        GeometryFactory geometryFactory = new GeometryFactory();

        for (Feature feature : featureCollection.getFeatures()) {
            Map<String, Object> properties = feature.getProperties();

            // Extract HTML String from Description property
            String descriptionHtml = (String) properties.get("Description");

            // Parse HTML String
            Document doc = Jsoup.parse(descriptionHtml);

            // Find all rows in the table
            Elements rows = doc.select("tr");

            String subzoneName = null;
            String planningAreaName = null;
            String regionName = null;

            // Search for target rows
            for (Element row : rows) {
                Elements columns = row.select("th, td");

                if (columns.size() == 2) {
                    String key = columns.get(0).text().trim();
                    String value = columns.get(1).text().trim();

                    // Check for specific keys
                    switch (key) {
                        case "SUBZONE_N":
                            subzoneName = value;
                            break;
                        case "PLN_AREA_N":
                            planningAreaName = value;
                            break;
                        case "REGION_N":
                            regionName = value;
                            break;
                    }
                }
            }

            // cache geometry to geojson polygon
            org.geojson.GeoJsonObject subzoneGeometryDto = feature.getGeometry();
            org.locationtech.jts.geom.Geometry subzoneGeometry = null;

            // Check if the geometry is a Polygon or MultiPolygon
            if (subzoneGeometryDto instanceof org.geojson.Polygon geoJsonPolygon) {
                // Convert GeoJSON Polygon to JTS Polygon
                List<LngLatAlt> coordinates = geoJsonPolygon.getExteriorRing();

                Coordinate[] jtsCoordinates = coordinates.stream()
                        .map(c -> new Coordinate(c.getLongitude(), c.getLatitude()))
                        .toArray(Coordinate[]::new);

                LinearRing linearRing = geometryFactory.createLinearRing(jtsCoordinates);
                subzoneGeometry = geometryFactory.createPolygon(linearRing);
                subzoneGeometry.setSRID(4326);

            } else if (subzoneGeometryDto instanceof org.geojson.MultiPolygon geoJsonMultiPolygon) {
                // Convert GeoJSON MultiPolygon to JTS MultiPolygon

                List<org.locationtech.jts.geom.Polygon> jtsPolygons = geoJsonMultiPolygon.getCoordinates().stream()
                        .map(polygonCoordinates -> {
                            List<Coordinate> coordinates = polygonCoordinates.get(0).stream()
                                    .map(c -> new Coordinate(c.getLongitude(), c.getLatitude()))
                                    .toList();

                            LinearRing linearRing = geometryFactory.createLinearRing(coordinates.toArray(new Coordinate[0]));
                            return geometryFactory.createPolygon(linearRing);
                        })
                        .toList();

                // Convert the list of JTS Polygons to a JTS MultiPolygon
                subzoneGeometry = geometryFactory.createMultiPolygon(jtsPolygons.toArray(new org.locationtech.jts.geom.Polygon[0]));
                subzoneGeometry.setSRID(4326);
            }

            if (subzoneName != null && planningAreaName != null && regionName != null && subzoneGeometry != null) {
                Subzone subzone = new Subzone(subzoneName, planningAreaName, regionName, subzoneGeometry);
                subzoneRepository.save(subzone);
            }
        }
    }

    public void initializeLocation() {
        List<Location> locations = new ArrayList<>();
        locations.add(new Location("88 Corporation Road", "649823"));
        locations.add(new Location("61 Kampong Arang Road", "438181"));
        locations.add(new Location("20 Orchard Road", "238830"));
        locations.add(new Location("1 Fullerton Road", "049213"));
        locations.add(new Location("10 Bayfront Avenue", "018956"));
        locations.add(new Location("30 Raffles Avenue", "039803"));
        locations.add(new Location("80 Mandai Lake Road", "729826"));
        locations.add(new Location("18 Marina Gardens Drive", "018953"));
        locations.add(new Location("6 Raffles Boulevard", "039594"));
        locations.add(new Location("8 Sentosa Gateway", "098269"));
        locations.add(new Location("1 Cluny Road", "259569"));
        locations.add(new Location("2 Orchard Turn", "238801"));
        locations.add(new Location("1 Beach Road", "189673"));
        locations.add(new Location("1 Esplanade Drive", "038981"));
        locations.add(new Location("10 Bayfront Avenue", "018956"));
        locations.add(new Location("1 Harbourfront Walk", "098585"));
        locations.add(new Location("1 Kim Seng Promenade", "237994"));
        locations.add(new Location("1 Pasir Ris Close", "519599"));
        locations.add(new Location("1 Stadium Place", "397628"));
        locations.add(new Location("1 Empress Place", "179555"));
        locations.add(new Location("1 Saint Andrew’s Road", "178957"));
        locations.add(new Location("1 North Bridge Road", "179094"));
        locations.add(new Location("1 Raffles Place", "048616"));

        this.locationRepository.saveAll(locations);

        this.locationService.updateLocationLatLong().subscribe();
    }

    public void initializeTrips() {
        tripService.buildTrips();
        tripService.updateTripDistanceDurationAsync().subscribe();
    }

    public void initializeWorkers() {
        Worker worker1 = new Worker(
                "Karthiga",
                "karthiga",
                "kar1243",
                "karthigamagesh17@gmail.com",
                "99999999",
                "Hello, I am a sincere cleaner specialised in wooden floors",
                LocalTime.of(8,0),
                LocalTime.of(17,0));
        worker1.setJoinDate(LocalDate.of(2024,5,3));

        Worker worker2 = new Worker(
                "John",
                "john",
                "john1243",
                "john@gmail.com",
                "99999999",
                "Cleaner with 20 years experience in cleaning bungalows/landed properties",
                LocalTime.of(13,0),
                LocalTime.of(22, 0));
        worker2.setJoinDate(LocalDate.of(2024,10,3));

        Worker worker3 = new Worker(
                "Alice Tan",
                "alice",
                "alice1234",
                "alice.tan@example.com",
                "91234567",
                "Experienced cleaner specializing in office spaces and carpet cleaning.",
                LocalTime.of(7, 0),
                LocalTime.of(16, 0)
        );
        worker3.setJoinDate(LocalDate.of(2024,1,3));

        Worker worker4 = new Worker(
                "Michael Lee",
                "michael",
                "mike5678",
                "michael.lee@example.com",
                "92345678",
                "Detail-oriented cleaner with expertise in high-rise window cleaning and disinfection.",
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
        );
        worker4.setJoinDate(LocalDate.of(2024,10,3));

        Worker worker5 = new Worker(
                "Sarah Lim",
                "sarah",
                "sarahlim2023",
                "sarah.lim@example.com",
                "93456789",
                "5 years experience in residential cleaning, with a focus on green cleaning methods.",
                LocalTime.of(10, 0),
                LocalTime.of(19, 0)
        );
        worker5.setJoinDate(LocalDate.of(2024,3,3));

        Location loc5 = locationRepository.findByPostalCode("438181")
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        worker5.setHomeLocation(loc5);

        Worker worker6 = new Worker(
                "Ravi Kumar",
                "ravi",
                "ravi@999",
                "ravi.kumar@example.com",
                "94567890",
                "Specializes in cleaning kitchens and commercial food preparation areas.",
                LocalTime.of(9, 0),
                LocalTime.of(15, 0)
        );
        worker6.setJoinDate(LocalDate.of(2024,1,4));
        worker6.setDeactivatedAt(LocalDate.of(2024,2,4));
        worker6.setActive(false);

        Worker worker7 = new Worker(
                "Maria Garcia",
                "maria",
                "maria8765",
                "maria.garcia@example.com",
                "95678901",
                "Friendly and reliable cleaner with 10 years of experience in hotel housekeeping.",
                LocalTime.of(12, 0),
                LocalTime.of(21, 0)
        );
        worker7.setJoinDate(LocalDate.of(2024,8,3));

        Worker worker8 = new Worker(
                "Tommy Wu",
                "tommy",
                "tommy0987",
                "tommy.wu@example.com",
                "96789012",
                "Focused on deep-cleaning services for industrial environments.",
                LocalTime.of(14, 0),
                LocalTime.of(22, 0)
        );
        worker8.setJoinDate(LocalDate.of(2024,9,2));

        Location loc8 = locationRepository.findByPostalCode("238830")
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        worker8.setHomeLocation(loc8);

        Worker worker9 = new Worker(
                "Lucy Wang",
                "lucy",
                "lucy5432",
                "lucy.wang@example.com",
                "97890123",
                "Residential cleaner with a specialty in eco-friendly products and methods.",
                LocalTime.of(8, 0),
                LocalTime.of(17, 0)
        );
        worker9.setJoinDate(LocalDate.of(2024,9,3));

        Worker worker10 = new Worker(
                "David Ong",
                "david",
                "david3210",
                "david.ong@example.com",
                "98901234",
                "Experienced in managing cleaning teams and ensuring high-quality standards in large commercial facilities.",
                LocalTime.of(8, 0),
                LocalTime.of(14, 0)
        );
        worker10.setJoinDate(LocalDate.of(2024,10,3));
        worker10.setDeactivatedAt(LocalDate.now());
        worker10.setActive(false);

        workerRepository.saveAll(List.of(worker1, worker2, worker3, worker4, worker5, worker6, worker7, worker8, worker9, worker10));
        // save loc instances
        locationRepository.saveAll(List.of(
                loc8, loc5
        ));
    }

    public void initializeClients() {
        Location location1 = this.locationRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Location with ID 1 not found"));
        Location location2 = this.locationRepository.findById(2L).orElseThrow(() -> new IllegalStateException("Location with ID 2 not found"));

        Client client1 = new Client("Amy Santiago", "98472094", true,  LocalDate.of(2024,10,4));
        Client client2 = new Client("Jake Peralta", "92384923", true, LocalDate.of(2024,8,2));
        this.clientRepository.saveAll(List.of(client1, client2));

        ClientSite clientSite1 = new ClientSite(client1, location1.getAddress(), location1.getPostalCode(), "#01-01", location1);
        ClientSite clientSite2 = new ClientSite(client2, location2.getAddress(), location2.getPostalCode(), "#02-02", location2);

        client1.setDeactivatedAt(LocalDate.of(2024, 11, 4));
        client1.setActive(false);
        this.clientSiteRepository.saveAll(List.of(clientSite1, clientSite2));
    }

    public void initializeContracts() {
        Client client1 = this.clientRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("Client with ID 1 not found"));
        Client client2 = this.clientRepository.findById(2L)
                .orElseThrow(() -> new IllegalStateException("Client with ID 2 not found"));

        Contract contract1 = new Contract();
        contract1.setContractStart(LocalDate.of(2024, 11, 3));
        contract1.setContractEnd(LocalDate.of(2024, 12, 3));
        contract1.setContractComment("Contract 1");
        contract1.setPrice(60.0f);
        contract1.setWorkersBudgeted(1);
        contract1.setRooms(1);
        contract1.setFrequency(Contract.Frequency.WEEKLY);
        contract1.setSessionDurationMinutes(60);
        contract1.setCreationDate(LocalDate.of(2024, 11, 3));

        Contract contract2 = new Contract();
        contract2.setContractStart(LocalDate.of(2024, 9, 3));
        contract2.setContractEnd(LocalDate.of(2024, 10, 3));
        contract2.setContractComment("Contract 2");
        contract2.setPrice(250.0f);
        contract2.setWorkersBudgeted(3);
        contract2.setRooms(2);
        contract2.setFrequency(Contract.Frequency.BIWEEKLY);
        contract2.setSessionDurationMinutes(120);
        contract2.setCreationDate(LocalDate.of(2024, 9, 1));

        client1.addContract(contract1);
        contract1.setClientSite(client1.getClientSites().get(0));
        client2.addContract(contract2);
        contract2.setClientSite(client2.getClientSites().get(0));

        System.out.println("Contract 1 Rate: " + contract1.getRate());
        System.out.println("Contract 2 Rate: " + contract2.getRate());

        this.clientRepository.saveAll(List.of(client1, client2));
        this.contractRepository.saveAll(List.of(contract1, contract2));
    }

    public void initializeCleaningSessions() {
        Contract contract = this.contractRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Contract not found"));

        // Create CleaningSession instances
        CleaningSession session1 = new CleaningSession(
                contract,
                LocalDate.of(2024,10,5),
                LocalTime.of(9,0),
                LocalDate.of(2024,10,5),
                LocalTime.of(12,0),
                "Session 1",
                CleaningSession.SessionStatus.WORKING
        );
        session1.setSessionRating(CleaningSession.Rating.AVERAGE);
        session1.setSessionFeedback("Feedback 1");
        session1.setPlanningStage(CleaningSession.PlanningStage.GREEN);

        CleaningSession session2 = new CleaningSession(
                contract,
                LocalDate.of(2024,10,12),
                LocalTime.of(14,0),
                LocalDate.of(2024,10,12),
                LocalTime.of(17,0),
                "Session 2",
                CleaningSession.SessionStatus.NOT_STARTED
        );
        session2.setSessionRating(CleaningSession.Rating.GOOD);
        session2.setSessionFeedback("Feedback 2");

        CleaningSession session3 = new CleaningSession(
                contract,
                LocalDate.of(2024,11,3),
                LocalTime.of(9,0),
                LocalDate.of(2024,11,3),
                LocalTime.of(12,0),
                "Session 3",
                CleaningSession.SessionStatus.NOT_STARTED
        );
        session3.setSessionStatus(CleaningSession.SessionStatus.CANCELLED);
        session3.setCancelledAt(LocalDate.of(2024,11,1));

        this.cleaningSessionRepository.saveAll(List.of(session1, session2, session3));
    }

    public void initializeShifts() {
        CleaningSession session1 = cleaningSessionRepository.findById(1L).orElseThrow(() -> new IllegalStateException("CleaningSession 1 not found"));
        CleaningSession session2 = cleaningSessionRepository.findById(2L).orElseThrow(() -> new IllegalStateException("CleaningSession 2 not found"));
        CleaningSession session3 = cleaningSessionRepository.findById(3L).orElseThrow(() -> new IllegalStateException("CleaningSession 3 not found"));

        Worker worker1 = workerRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Worker 1 not found"));
        // Worker worker2 = workerRepository.findById(2L).orElseThrow(() -> new IllegalStateException("Worker 2 not found"));
        Worker worker6 = workerRepository.findById(6L).orElseThrow(() -> new IllegalStateException("Worker 6 not found"));
        Worker worker7 = workerRepository.findById(7L).orElseThrow(() -> new IllegalStateException("Worker 7 not found"));

        // These two shifts are part of the same cleaning session,
        // meaning they share the same start and end times (auto assigned in Shift.java)
        // A shift represents a worker's involvement in the cleaning session,
        // and is used to denote the cleaning session for each worker.

        Shift shift1 = new Shift(session1);
        shift1.setWorker(worker1);
        // this is the actual time that the worker arrive to the shift
        shift1.setActualStartDate(LocalDate.of(2024, 10, 5));
        shift1.setActualStartTime(LocalTime.of(9, 0));
        shift1.setActualEndDate(LocalDate.of(2024, 10, 5));
        shift1.setActualEndTime(LocalTime.of(12, 0));

        Shift shift2 = new Shift(session1);
        shift2.setWorker(worker6);
        // this is the actual time that the worker arrive to the shift - worker6 is one hour late
        shift2.setActualStartDate(LocalDate.of(2024, 10, 5));
        shift2.setActualStartTime(LocalTime.of(10, 0));
        shift2.setActualEndDate(LocalDate.of(2024, 10, 5));
        shift2.setActualEndTime(LocalTime.of(12, 0));


        Shift shift3 = new Shift(session2);
        shift3.setWorker(worker7);
        shift3.setActualStartDate(LocalDate.of(2024, 10, 12));
        shift3.setActualStartTime(LocalTime.of(14, 0));
        shift3.setActualEndDate(LocalDate.of(2024, 10, 12));
        shift3.setActualEndTime(LocalTime.of(17, 0));

        Shift shift4 = new Shift(session2);
        shift4.setWorker(worker1);
        shift4.setActualStartDate(LocalDate.of(2024, 10, 12));
        shift4.setActualStartTime(LocalTime.of(14, 5));
        shift4.setActualEndDate(LocalDate.of(2024, 10, 12));
        shift4.setActualEndTime(LocalTime.of(17, 5));

        // assign to session on 11-06-2024 to test leaveApplication clash
        Shift shift5 = new Shift(session3);
        shift5.setWorker(worker1);

        shiftRepository.saveAll(new ArrayList<>(List.of(shift1, shift2, shift3, shift4, shift5)));

        session1.setShifts(new ArrayList<>(List.of(shift1, shift2)));
        session2.setShifts(new ArrayList<>(List.of(shift3, shift4)));
        session3.setShifts(new ArrayList<>(List.of(shift5)));
        cleaningSessionRepository.saveAll(new ArrayList<>(List.of(session1, session2, session3)));
    }

    public void initializeAdmins() {
        Admin ad1 = new Admin("rootadmin", "adminroot1234", true);
        Admin ad2 = new Admin("admin", "admin1234", false);
        adminRepository.saveAll(List.of(ad1, ad2));
    }

    public void initializeMedicalRecords() {
        String mcId1 = medicalRecordService.generateCustomMcId();
        String mcId2 = medicalRecordService.generateCustomMcId();
        String mcId3 = medicalRecordService.generateCustomMcId();

        MedicalRecord medicalRecord1 = new MedicalRecord(mcId1, "fake-blob-123", "user1_image1_timestamp", OffsetDateTime.now(), OffsetDateTime.now().plusDays(7));
        MedicalRecord medicalRecord2 = new MedicalRecord(mcId2, "fake-blob-456", "user1_image2_timestamp", OffsetDateTime.now().minusDays(10), OffsetDateTime.now().minusDays(3));

        medicalRecordRepository.saveAll(List.of(medicalRecord1, medicalRecord2));
    }

    public void initializeLeaveApplications() {
        LeaveApplication leaveApp1 = new LeaveApplication(
                1L,  // workerId
                2L,  // adminId
                LeaveType.MEDICAL,
                "fake-medical-cert-001.pdf",  // fileName
                "hash1",  // imageHash
                LocalDate.of(2024, 11, 1),  // leaveStartDate
                LocalTime.MIDNIGHT,  // leaveStartTime
                LocalDate.of(2024, 11, 1),  // leaveEndDate
                LocalTime.MIDNIGHT,  // leaveEndTime
                LocalDate.of(2024, 11, 6),  // leaveSubmittedDate
                LocalTime.of(8, 30),  // leaveSubmittedTime
                LeaveApplication.ApplicationStatus.APPROVED,  // applicationStatus
                10,  // medicalLeaveBalance
                5  // otherLeaveBalance
        );

        LeaveApplication leaveApp2 = new LeaveApplication(
                1L,
                3L,
                LeaveType.OTHERS,
                null,  // fileName
                "hash2",  // imageHash
                LocalDate.of(2024, 10, 5),  // leaveStartDate
                LocalTime.MIDNIGHT,
                // LocalDate.now().plusDays(7),
                LocalDate.of(2024, 10, 6),
                LocalTime.MIDNIGHT,
                LocalDate.now(),
                LocalTime.now(),
                LeaveApplication.ApplicationStatus.PENDING,
                10,
                5
        );

        LeaveApplication leaveApp3 = new LeaveApplication(
                1L,
                4L,
                LeaveType.OTHERS,
                null,  // fileName
                "hash3",  // imageHash
                LocalDate.of(2024, 11, 6),  // leaveStartDate
                LocalTime.MIDNIGHT,
                LocalDate.of(2024, 11, 13),  // leaveEndDate
                LocalTime.MIDNIGHT,
                LocalDate.now().minusDays(4),
                LocalTime.now().minusHours(4),
                LeaveApplication.ApplicationStatus.REJECTED,
                10,
                5
        );

        leaveApplicationRepository.saveAll(List.of(leaveApp1, leaveApp2, leaveApp3));
    }
}
