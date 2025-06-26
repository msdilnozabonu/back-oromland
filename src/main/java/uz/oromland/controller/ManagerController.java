package uz.oromland.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class ManagerController {
    /// --------------------------------------------------------Camp/Sanatorium-------------------------------------------------------------------

    @GetMapping("/camps")
    public String getCamps() {
        return "/camps";
    }


    /// --------------------------------------------------------Documents-------------------------------------------------------------------

    ///  Accept or reject documents for bookings

}
