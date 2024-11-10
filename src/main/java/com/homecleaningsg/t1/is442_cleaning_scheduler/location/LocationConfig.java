package com.homecleaningsg.t1.is442_cleaning_scheduler.location;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationConfig implements CommandLineRunner {

    private final LocationRepository locationRepository;
    private final LocationService locationService;

    public LocationConfig(LocationRepository locationRepository, LocationService locationService) {
        this.locationRepository = locationRepository;
        this.locationService = locationService;
        Location loc1 = new Location("88 Corporation Road", "649823");
        loc1.setUnitNumber("01-01");
        Location loc2 = new Location("61 Kampong Arang Road", "438181");
        loc2.setUnitNumber("02-02");
        Location loc3 = new Location("20 Orchard Road", "238830");
        loc3.setUnitNumber("03-03");

        Location loc4 = new Location("1 Fullerton Road", "049213");
        loc4.setUnitNumber("04-04");
        Location loc5 = new Location("10 Bayfront Avenue", "018956");
        loc5.setUnitNumber("05-05");
        Location loc6 = new Location("30 Raffles Avenue", "039803");
        loc6.setUnitNumber("06-06");
        Location loc7 = new Location("80 Mandai Lake Road", "729826");
        loc7.setUnitNumber("07-07");
        Location loc8 = new Location("18 Marina Gardens Drive", "018953");
        loc8.setUnitNumber("08-08");
        Location loc9 = new Location("6 Raffles Boulevard", "039594");
        loc9.setUnitNumber("09-09");
        Location loc10 = new Location("8 Sentosa Gateway", "098269");
        loc10.setUnitNumber("10-10");
        Location loc11 = new Location("1 Cluny Road", "259569");
        loc11.setUnitNumber("11-11");
        Location loc12 = new Location("2 Orchard Turn", "238801");
        loc12.setUnitNumber("12-12");
        Location loc13 = new Location("1 Beach Road", "189673");
        loc13.setUnitNumber("13-13");
        Location loc14 = new Location("1 Esplanade Drive", "038981");
        loc14.setUnitNumber("14-14");
        Location loc15 = new Location("10 Bayfront Avenue", "018956");
        loc15.setUnitNumber("15-15");
        Location loc16 = new Location("1 Harbourfront Walk", "098585");
        loc16.setUnitNumber("16-16");
        Location loc17 = new Location("1 Kim Seng Promenade", "237994");
        loc17.setUnitNumber("17-17");
        Location loc18 = new Location("1 Pasir Ris Close", "519599");
        loc18.setUnitNumber("18-18");
        Location loc19 = new Location("1 Stadium Place", "397628");
        loc19.setUnitNumber("19-19");
        Location loc20 = new Location("1 Empress Place", "179555");
        loc20.setUnitNumber("20-20");
        Location loc21 = new Location("1 Saint Andrew’s Road", "178957");
        loc21.setUnitNumber("21-21");
        Location loc22 = new Location("1 North Bridge Road", "179094");
        loc22.setUnitNumber("22-22");
        Location loc23 = new Location("1 Raffles Place", "048616");
        loc23.setUnitNumber("23-23");

        // this.locationRepository.saveAll(List.of(loc1, loc2, loc3));
        this.locationRepository.saveAll(List.of(loc1, loc2, loc3, loc4, loc5, loc6, loc7, loc8, loc9, loc10, loc11, loc12, loc13, loc14, loc15, loc16, loc17, loc18, loc19, loc20, loc21, loc22, loc23));
        this.locationService.updateLocationLatLong().subscribe();
    }

    @Override
    public void run(String... args) throws Exception {

    }
}