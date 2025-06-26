package uz.oromland.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.oromland.entity.Camp;
import uz.oromland.entity.City;
import uz.oromland.entity.Sanatorium;
import uz.oromland.repository.CampRepository;
import uz.oromland.repository.CityRepository;
import uz.oromland.repository.SanatoriumRepository;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestPagesController {

    private final CampRepository campRepository;
    private final SanatoriumRepository sanatoriumRepository;
    private final CityRepository cityRepository;

    @GetMapping
    public String testPagesIndex() {
        return "test-pages";
    }

    @GetMapping("/camp-details")
    public String campDetails(Model model) {
        // Get the first camp from the database
        List<Camp> camps = campRepository.findAll();
        if (!camps.isEmpty()) {
            model.addAttribute("camp", camps.get(0));
        }
        return "camp-details";
    }

    @GetMapping("/camp-details/{id}")
    public String campDetailsById(@PathVariable Long id, Model model) {
        Optional<Camp> campOptional = campRepository.findById(id);
        if (campOptional.isPresent()) {
            model.addAttribute("camp", campOptional.get());
        }
        return "camp-details";
    }

    @GetMapping("/sanatorium-list")
    public String sanatoriumList(Model model) {
        List<Sanatorium> sanatoriums = sanatoriumRepository.findAll();
        model.addAttribute("sanatoriums", sanatoriums);
        
        List<City> cities = cityRepository.findAll();
        model.addAttribute("cities", cities);
        
        return "sanatorium-list";
    }

    @GetMapping("/camp-booking")
    public String campBooking(Model model) {
        // Get the first camp from the database for the booking form
        List<Camp> camps = campRepository.findAll();
        if (!camps.isEmpty()) {
            model.addAttribute("camp", camps.get(0));
        }
        return "camp-booking";
    }
}