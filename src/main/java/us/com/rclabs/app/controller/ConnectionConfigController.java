package us.com.rclabs.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ConnectionConfigController {

    @GetMapping("/connection-config")
    public String showConnectionConfigForm(Model model) {
        // Add any necessary attributes to the model
        return "connection-config";
    }

    @PostMapping("/connection-config")
    public String handleConnectionConfigForm(@RequestParam("connectionName") String connectionName,
                                             @RequestParam("userID") String userID,
                                             @RequestParam("password") String password,
                                             @RequestParam("url") String url,
                                             Model model) {
        // Handle the form submission and save the connection details
        // Add any necessary attributes to the model
        return "connection-config";
    }
}
