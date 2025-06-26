package uz.oromland.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.oromland.entity.Camp;
import uz.oromland.entity.City;
import uz.oromland.entity.Sanatorium;
import uz.oromland.repository.CampRepository;
import uz.oromland.repository.CityRepository;
import uz.oromland.repository.SanatoriumRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/database")
@RequiredArgsConstructor
public class DatabaseTestController {

    private final CampRepository campRepository;
    private final SanatoriumRepository sanatoriumRepository;
    private final CityRepository cityRepository;

    @GetMapping
    public String databaseTest(Model model) {
        // Get all cities
        List<City> cities = cityRepository.findAll();
        model.addAttribute("cities", cities);

        // Get all camps
        List<Camp> camps = campRepository.findAll();
        model.addAttribute("camps", camps);

        // Get all sanatoriums
        List<Sanatorium> sanatoriums = sanatoriumRepository.findAll();
        model.addAttribute("sanatoriums", sanatoriums);

        return "database-test";
    }

    @GetMapping("/seed")
    public String seedDatabase() {
        // Only seed if the database is empty
        if (cityRepository.count() == 0) {
            // Create cities
            City tashkent = new City();
            tashkent.setName("Tashkent");
            cityRepository.save(tashkent);

            City samarkand = new City();
            samarkand.setName("Samarkand");
            cityRepository.save(samarkand);

            City bukhara = new City();
            bukhara.setName("Bukhara");
            cityRepository.save(bukhara);

            City namangan = new City();
            namangan.setName("Namangan");
            cityRepository.save(namangan);

            // Create camps
            Camp camp1 = new Camp();
            camp1.setName("Pine Valley Summer Camp");
            camp1.setDescription("This is a wonderful summer camp located in a beautiful natural setting. The camp offers a variety of activities for children of all ages, including swimming, hiking, arts and crafts, sports, and more.");
            camp1.setLocation("Mountain View, Tashkent Region");
            camp1.setAgeFrom(7);
            camp1.setAgeTo(14);
            camp1.setNumberOfExchanges(5);
            camp1.setDuration(14);
            camp1.setAvailableSeasons("Summer (June - August)");
            camp1.setContactPhone("+998 90 123 4567");
            camp1.setStartDate(Timestamp.valueOf(LocalDateTime.now().plusDays(30)));
            camp1.setEndDate(Timestamp.valueOf(LocalDateTime.now().plusDays(44)));
            camp1.setMaxParticipants(100);
            camp1.setCurrentParticipants(45);
            camp1.setIsActive(true);
            camp1.setPrice(new BigDecimal("1200.00"));
            camp1.setCity(tashkent);
            campRepository.save(camp1);

            Camp camp2 = new Camp();
            camp2.setName("Lakeside Adventure Camp");
            camp2.setDescription("Experience the thrill of outdoor adventures at our lakeside camp. Activities include canoeing, fishing, rock climbing, and team-building exercises.");
            camp2.setLocation("Lake District, Samarkand Region");
            camp2.setAgeFrom(10);
            camp2.setAgeTo(16);
            camp2.setNumberOfExchanges(4);
            camp2.setDuration(10);
            camp2.setAvailableSeasons("Summer, Spring");
            camp2.setContactPhone("+998 90 987 6543");
            camp2.setStartDate(Timestamp.valueOf(LocalDateTime.now().plusDays(15)));
            camp2.setEndDate(Timestamp.valueOf(LocalDateTime.now().plusDays(25)));
            camp2.setMaxParticipants(80);
            camp2.setCurrentParticipants(35);
            camp2.setIsActive(true);
            camp2.setPrice(new BigDecimal("950.00"));
            camp2.setCity(samarkand);
            campRepository.save(camp2);

            // Create sanatoriums
            Sanatorium sanatorium1 = new Sanatorium();
            sanatorium1.setName("Mountain View Sanatorium");
            sanatorium1.setDescription("A peaceful retreat in the mountains with thermal springs and healing treatments.");
            sanatorium1.setAvailableSeasons("All Year");
            sanatorium1.setCreatedBy(1L);
            sanatorium1.setTotalCapacity(200);
            sanatorium1.setCurrentCapacity(120);
            sanatorium1.setIsActive(true);
            sanatorium1.setPhoneNumber("+998 90 111 2222");
            sanatorium1.setLocation("Mountain Region, Tashkent");
            sanatorium1.setOneDayPrice(new BigDecimal("150.00"));
            sanatorium1.setTotalPrice(new BigDecimal("1050.00"));
            sanatorium1.setCity(tashkent);
            sanatoriumRepository.save(sanatorium1);

            Sanatorium sanatorium2 = new Sanatorium();
            sanatorium2.setName("Riverside Wellness Center");
            sanatorium2.setDescription("Located by a serene river, our wellness center offers a range of therapeutic treatments and relaxation options.");
            sanatorium2.setAvailableSeasons("Spring, Summer, Fall");
            sanatorium2.setCreatedBy(1L);
            sanatorium2.setTotalCapacity(150);
            sanatorium2.setCurrentCapacity(90);
            sanatorium2.setIsActive(true);
            sanatorium2.setPhoneNumber("+998 90 333 4444");
            sanatorium2.setLocation("Riverside Area, Bukhara");
            sanatorium2.setOneDayPrice(new BigDecimal("120.00"));
            sanatorium2.setTotalPrice(new BigDecimal("840.00"));
            sanatorium2.setCity(bukhara);
            sanatoriumRepository.save(sanatorium2);
        }

        return "redirect:/database";
    }
}