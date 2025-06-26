package uz.oromland.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    @GetMapping
    public String getDashboard() {
        // This method can be expanded to return actual dashboard data
        return "/dashboard";
    }


}
