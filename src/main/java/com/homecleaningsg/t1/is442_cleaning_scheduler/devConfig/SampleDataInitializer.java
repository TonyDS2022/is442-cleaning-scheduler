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
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplication;
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
        initializeCleaningSessions();
        initializeShifts();
//        initializeMedicalRecords();
        initializeLeaveApplications();
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
                LocalTime startWorkingHours = LocalTime.parse("08:00");
                LocalTime endWorkingHours = LocalTime.parse("22:00");
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
                Client client = new Client(name, phone);
                client.setActive(isActive);
                client.setJoinDate(joinDate);
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
    // read from resource/cleaningSession.csv
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/cleaningSessions_table.csv"))) {

            String[] values;
            reader.readNext();  // Skip header row
            while ((values = reader.readNext()) != null) {
                Long contractId = Long.parseLong(values[0].trim());
                LocalDate sessionDate = LocalDate.parse(values[1].trim());
                LocalTime startTime = LocalTime.parse(values[2].trim());
                LocalDate endDate = LocalDate.parse(values[3].trim());
                LocalTime endTime = LocalTime.parse(values[4].trim());
                String sessionName = values[5].trim();
                int numberOfCleaners = Integer.parseInt(values[6].trim());
                CleaningSession.SessionStatus sessionStatus = CleaningSession.SessionStatus.valueOf(values[7].trim());
                LocalDate cancelledAt = values[8].trim().isEmpty() ? null : LocalDate.parse(values[8].trim());

                Contract contract = contractRepository.findById(contractId)
                        .orElseThrow(() -> new IllegalStateException("Contract with ID " + contractId + " not found"));

                // Create and configure CleaningSession
                CleaningSession cleaningSession = new CleaningSession(
                        contract,
                        sessionDate,
                        startTime,
                        endDate,
                        endTime,
                        sessionName,
                        numberOfCleaners
                );

                cleaningSession.setSessionStatus(sessionStatus);

                if (cancelledAt != null) {
                    cleaningSession.setCancelledAt(cancelledAt);
                }

                // Save the CleaningSession instance
                cleaningSessionRepository.save(cleaningSession);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void initializeShifts() {
        // read from resource/shifts.csv
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/shifts_table.csv"))) {
            String[] values;
            reader.readNext();  // Skip header row
            while ((values = reader.readNext()) != null) {
                Long sessionId = Long.parseLong(values[0].trim());
                Long workerId = Long.parseLong(values[1].trim());
                LocalDate actualStartDate = LocalDate.parse(values[2].trim());
                LocalTime actualStartTime = LocalTime.parse(values[3].trim());
                LocalDate actualEndDate = LocalDate.parse(values[4].trim());
                LocalTime actualEndTime = LocalTime.parse(values[5].trim());
                Long shiftDurationHours = Long.parseLong(values[6].trim());

                // Fetch the Worker and Session based on workerId and sessionId
                Worker worker = workerRepository.findById(workerId)
                        .orElseThrow(() -> new IllegalStateException("Worker with ID " + workerId + " not found"));

                CleaningSession cleaningSession = cleaningSessionRepository.findById(sessionId)
                        .orElseThrow(() -> new IllegalStateException("Session with ID " + sessionId + " not found"));

                // Create and configure Shift
                Shift shift = new Shift(cleaningSession);
                shift.setWorker(worker);
                shift.setActualStartDate(actualStartDate);
                shift.setActualStartTime(actualStartTime);
                shift.setActualEndDate(actualEndDate);
                shift.setActualEndTime(actualEndTime);
                shift.setShiftDurationHours(shiftDurationHours);

                // Save the Shift instance
                shiftRepository.save(shift);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Transactional
    public void initializeLeaveApplications() {
        // read from resource/leaveApplications_table.csv
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/leaveApplications_table.csv"))) {
            String[] values;
            reader.readNext();  // Skip header row
            while ((values = reader.readNext()) != null) {
                Long workerId = Long.parseLong(values[0].trim());
                Long adminId = Long.parseLong(values[1].trim());
                LeaveApplication.LeaveType leaveType = LeaveApplication.LeaveType.valueOf(values[2].trim());
                LocalDate leaveStartDate = LocalDate.parse(values[3].trim());
                LocalDate leaveEndDate = LocalDate.parse(values[4].trim());
                LocalDate leaveSubmittedDate = LocalDate.parse(values[5].trim());
                LocalTime leaveSubmittedTime = LocalTime.parse(values[6].trim());
                LeaveApplication.ApplicationStatus applicationStatus = LeaveApplication.ApplicationStatus.valueOf(values[7].trim());

                // Fetch the Worker and Admin based on workerId and adminId
                Worker worker = workerRepository.findById(workerId)
                        .orElseThrow(() -> new IllegalStateException("Worker with ID " + workerId + " not found"));

                Admin admin = adminRepository.findById(adminId)
                        .orElseThrow(() -> new IllegalStateException("Admin with ID " + adminId + " not found"));

                // Create and configure LeaveApplications
                LeaveApplication leaveApplication = new LeaveApplication(worker, leaveType, leaveStartDate, leaveEndDate);
                leaveApplication.setAdmin(admin);
                leaveApplication.setLeaveSubmittedDate(leaveSubmittedDate);
                leaveApplication.setLeaveSubmittedTime(leaveSubmittedTime);
                leaveApplication.setApplicationStatus(applicationStatus);

                // Save the Shift instance
                leaveApplicationRepository.save(leaveApplication);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
