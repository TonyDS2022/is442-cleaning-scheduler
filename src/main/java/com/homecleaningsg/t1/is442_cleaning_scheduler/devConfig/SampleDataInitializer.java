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
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerService;
import com.opencsv.CSVReader;
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
import java.io.FileReader;
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
    private final WorkerService workerService;

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
            TripService tripService,
            WorkerService workerService) {
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
        this.workerService = workerService;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        // Add sample data here
        initializeSubzones();
//        initializeLocation();
//        initializeTrips();
        initializeWorkers();
//        initializeClients();
//        initializeContracts();
//        initializeCleaningSessions();
//        initializeShifts();
//        initializeAdmins();
//        initializeMedicalRecords();
//        initializeLeaveApplications();
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
        Location loc1 = new Location("88 Corporation Road", "649823");
        Location loc2 = new Location("61 Kampong Arang Road", "438181");
        Location loc3 = new Location("20 Orchard Road", "238830");

        this.locationRepository.saveAll(List.of(loc1, loc2, loc3));

        this.locationService.updateLocationLatLong().subscribe();
    }

    public void initializeTrips() {
        tripService.buildTrips();
        tripService.updateTripDistanceDurationAsync().subscribe();
    }

    public void initializeWorkers() {
        // read from resource/workers.csv
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/workers.csv"))) {

            String[] values;
            reader.readNext();

            int lineCount = 0;

            while ((values = reader.readNext()) != null && lineCount < 5) {
                String name = values[0].trim();
                String username = values[1].trim();
                String password = values[2].trim();
                String email = values[3].trim();
                String phone = values[4].trim();
                String bio = values[5].trim();
                LocalTime startWorkingHours = LocalTime.parse(values[6].trim());
                LocalTime endWorkingHours = LocalTime.parse(values[7].trim());
                String streetAddress = values[8].trim();
                String postalCode = values[9].trim();
                String unitNumber = values[10].trim();
                Worker worker = new Worker(name, username, password, email, phone, bio, startWorkingHours, endWorkingHours);
                workerService.addResidentialAddressToWorker(worker, streetAddress, postalCode, unitNumber);

                lineCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        contract1.setSessionStartTime(LocalTime.of(9,0));
        contract1.setSessionEndTime(LocalTime.of(12,0));
        contract1.setCreationDate(LocalDate.of(2024, 11, 3));

        Contract contract2 = new Contract();
        contract2.setContractStart(LocalDate.of(2024, 9, 3));
        contract2.setContractEnd(LocalDate.of(2024, 10, 3));
        contract2.setContractComment("Contract 2");
        contract2.setPrice(250.0f);
        contract2.setWorkersBudgeted(3);
        contract2.setRooms(2);
        contract2.setFrequency(Contract.Frequency.BIWEEKLY);
        contract2.setSessionStartTime(LocalTime.of(9,0));
        contract2.setSessionEndTime(LocalTime.of(12,0));
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
                2
        );
        session1.setSessionStatus(CleaningSession.SessionStatus.WORKING);
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
                1
        );
        session2.setSessionStatus(CleaningSession.SessionStatus.NOT_STARTED);
        session2.setSessionRating(CleaningSession.Rating.GOOD);
        session2.setSessionFeedback("Feedback 2");

        CleaningSession session3 = new CleaningSession(
                contract,
                LocalDate.of(2024,11,3),
                LocalTime.of(9,0),
                LocalDate.of(2024,11,3),
                LocalTime.of(12,0),
                "Session 3",
                3
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
}
