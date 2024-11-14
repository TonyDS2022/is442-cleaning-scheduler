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
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.medicalrecord.MedicalRecordRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.medicalrecord.MedicalRecordService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.subzone.Subzone;
import com.homecleaningsg.t1.is442_cleaning_scheduler.subzone.SubzoneRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.Trip;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.TripRepository;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    private final TripRepository tripRepository;
    private final ContractService contractService;

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
            WorkerService workerService, TripRepository tripRepository, ContractService contractService) {
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
        this.tripRepository = tripRepository;
        this.contractService = contractService;
    }

    @Override
    @Transactional
    public synchronized void run(ApplicationArguments args) throws Exception {
        // Add sample data here
        initializeSubzones();
        initializeLocation();
        initializeTrips();
        initializeAdmins();
        initializeWorkers();
        initializeClients();
        initializeContracts();
//        initializeCleaningSessions();
//        initializeShifts();
//        initializeMedicalRecords();
//        initializeLeaveApplications();
    }

    @Transactional
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

    @Transactional
    public void initializeLocation() {
        try (CSVReader reader = new CSVReader(new FileReader( "src/main/resources/locations_table.csv"))) {

            String[] values;
            reader.readNext();

            while ((values = reader.readNext()) != null) {
                Double latitude = Double.parseDouble(values[0].trim());
                Double longitude = Double.parseDouble(values[1].trim());
                Long subzoneId = Long.parseLong(values[3].trim());
                String address = values[4].trim();
                String postalCode = values[5].trim();
                Location location = new Location(address, postalCode);
                location.setLatitude(latitude);
                location.setLongitude(longitude);
                Subzone subzone = subzoneRepository.findById(subzoneId).orElseThrow(() -> new IllegalStateException("Subzone with ID " + subzoneId + " not found"));
                location.setSubzone(subzone);
                locationRepository.save(location);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void initializeTrips() {
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/trips_table.csv"))) {

            String[] values;
            reader.readNext();

            while ((values = reader.readNext()) != null) {
                Double tripDistanceMeters = Double.parseDouble(values[1].trim());
                Double tripDurationSeconds = Double.parseDouble(values[2].trim());
                Long destinationId = Long.parseLong(values[3].trim());
                Long originId = Long.parseLong(values[4].trim());
                Location origin = locationRepository.findById(originId).orElseThrow(() -> new IllegalStateException("Location with ID " + originId + " not found"));
                Location destination = locationRepository.findById(destinationId).orElseThrow(() -> new IllegalStateException("Location with ID " + destinationId + " not found"));
                Trip trip = new Trip(origin, destination);
                trip.setTripDurationSeconds(tripDurationSeconds);
                trip.setTripDistanceMeters(tripDistanceMeters);
                tripRepository.save(trip);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void initializeAdmins() {
        Admin rootAdmin = new Admin("Ray Holt", "Peralta$T3stiM0ny", true);
        Admin admin1 = new Admin("Jake Peralta", "NoiceToit!", false);
        Admin admin2 = new Admin("Amy Santiago", "GinaLovesLinManuel!", false);
        adminRepository.saveAll(List.of(rootAdmin, admin1, admin2));
    }

    @Transactional
    public void initializeWorkers() {
        // read from resource/workers.csv
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/workers.csv"))) {

            String[] values;
            reader.readNext();
            while ((values = reader.readNext()) != null) {
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
                Long adminId = Long.parseLong(values[11].trim());
                LocalDate joinDate = LocalDate.parse(values[12].trim());
                LocalDate deactivatedAt = values[13].trim().isEmpty() ? null : LocalDate.parse(values[13].trim());
                Worker worker = new Worker(name, username, password, email, phone, bio, startWorkingHours, endWorkingHours);
                worker.setJoinDate(joinDate);
                if(deactivatedAt != null){
                    worker.setDeactivatedAt(deactivatedAt);
                }
                Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new IllegalStateException("Admin with ID " + adminId + " not found"));
                worker.setSupervisor(admin);
                workerService.addResidentialAddressToWorker(worker, streetAddress, postalCode, unitNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void initializeClients() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/clients_table.csv"))) {

            String[] values;
            reader.readNext();
            while ((values = reader.readNext()) != null) {
                String name = values[0].trim();
                String phone = values[1].trim();
                LocalDate joinDate = LocalDate.parse(values[2].trim(), formatter);
                boolean isActive = values[3].trim().equals("t");
                LocalDate deactivatedAt = values[4].trim().isEmpty() ? null : LocalDate.parse(values[4].trim(), formatter);
                Client client = new Client(name, phone, isActive, joinDate);
                clientRepository.save(client);
                if (!isActive) {
                    client.setDeactivatedAt(deactivatedAt);
                }
                String streetAddress = values[5].trim();
                String postalCode = values[6].trim();
                String unitNumber = values[7].trim();
                Long numberOfRooms = Long.parseLong(values[8].trim());
                ClientSite.PropertyType propertyType = ClientSite.PropertyType.valueOf(values[9].trim());
                clientService.addClientSiteToClient(client, streetAddress, postalCode, unitNumber, numberOfRooms, propertyType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void initializeContracts() {
        List<Integer> validHour = List.of(8, 13, 18);
        Random random = new Random(24601);
        List<Client> clients = clientRepository.findAll();
        for (Client client: clients) {
            if (!client.isActive()) {
                continue;
            }
            if (client.getClientSites().get(0).getPropertyType() == ClientSite.PropertyType.LANDED) {
                continue;
            }
            Long numberOfRooms = client.getClientSites().get(0).getNumberOfRooms();
            ClientSite.PropertyType propertyType = client.getClientSites().get(0).getPropertyType();
            LocalTime startTime = LocalTime.of(validHour.get(random.nextInt(validHour.size())), 0);
            LocalTime endTime = startTime.plusHours(contractService.getNumberOfHours(numberOfRooms, propertyType));
            Contract contract = new Contract(
                    client.getClientSites().get(0),
                    client,
                    client.getJoinDate(),
                    client.getJoinDate().plusMonths(3),
                    startTime,
                    endTime,
                    "This is a sample contract",
                    276.0f,
                    1,
                    numberOfRooms.intValue(),
                    "WEEKLY"
            );
            contractService.addContract(contract);
        }
    }

    @Transactional
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

    @Transactional
    public void initializeShifts() {
        CleaningSession session1 = cleaningSessionRepository.findById(1L).orElseThrow(() -> new IllegalStateException("CleaningSession 1 not found"));
        CleaningSession session2 = cleaningSessionRepository.findById(2L).orElseThrow(() -> new IllegalStateException("CleaningSession 2 not found"));
        CleaningSession session3 = cleaningSessionRepository.findById(3L).orElseThrow(() -> new IllegalStateException("CleaningSession 3 not found"));

        Worker worker1 = workerRepository.findById(4L).orElseThrow(() -> new IllegalStateException("Worker 1 not found"));
        Worker worker2 = workerRepository.findById(5L).orElseThrow(() -> new IllegalStateException("Worker 6 not found"));
        Worker worker3 = workerRepository.findById(6L).orElseThrow(() -> new IllegalStateException("Worker 7 not found"));

        // These two shifts are part of the same cleaning session,
        // meaning they share the same start and end times (auto assigned in Shift.java)
        // A shift represents a worker's involvement in the cleaning session,
        // and is used to denote the cleaning session for each worker.

        // for testing shift start and shift end
        Shift shift1 = new Shift(session1);
        shift1.setWorker(worker1);
        // this is the actual time that the worker arrive to the shift
        shift1.setActualStartDate(LocalDate.of(2024, 10, 5));
        shift1.setActualStartTime(LocalTime.of(9, 0));
        shift1.setActualEndDate(LocalDate.of(2024, 10, 5));
        shift1.setActualEndTime(LocalTime.of(12, 0));

        Shift shift2 = new Shift(session1);
        shift2.setWorker(worker2);
        // this is the actual time that the worker arrive to the shift - worker6 is one hour late
        shift2.setActualStartDate(LocalDate.of(2024, 10, 5));
        shift2.setActualStartTime(LocalTime.of(10, 0));
        shift2.setActualEndDate(LocalDate.of(2024, 10, 5));
        shift2.setActualEndTime(LocalTime.of(12, 0));


        Shift shift3 = new Shift(session2);
        shift3.setWorker(worker3);
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


        Shift shift6 = new Shift(session2);
        shift6.setWorker(worker2);
        // this is the actual time that the worker arrive to the shift
        shift6.setActualStartDate(LocalDate.of(2024, 10, 12));
        shift6.setActualStartTime(LocalTime.of(9, 0));
        shift6.setActualEndDate(LocalDate.of(2024, 10, 12));
        shift6.setActualEndTime(LocalTime.of(12, 0));

        shiftRepository.saveAll(new ArrayList<>(List.of(shift1, shift2, shift3, shift4, shift5, shift6)));

        session1.setShifts(new ArrayList<>(List.of(shift1, shift2)));
        session2.setShifts(new ArrayList<>(List.of(shift3, shift4)));
        session3.setShifts(new ArrayList<>(List.of(shift5)));
        cleaningSessionRepository.saveAll(new ArrayList<>(List.of(session1, session2, session3)));
    }

}
