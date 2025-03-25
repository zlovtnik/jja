package us.com.rclabs.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import us.com.rclabs.app.config.ConnectionConfig;

/**
 * Controller responsible for handling connection configuration requests.
 * Manages both the display and processing of connection configuration forms.
 */
@Controller
public class ConnectionConfigController {

    /**
     * Displays the connection configuration form.
     *
     * @param model the Spring MVC model
     * @return view name for the configuration form
     */
    @GetMapping("/connection-config")
    public String showConnectionForm(Model model) {
        model.addAttribute("connectionConfig", new ConnectionConfig());
        return "connection-config";
    }

    /**
     * Processes the submitted connection configuration form.
     *
     * @param connectionConfig the form data bound to ConnectionConfig object
     * @return redirect to success page or back to form on error
     */
    @PostMapping("/connection-config")
    public String saveConnection(@ModelAttribute ConnectionConfig connectionConfig) {
        try {
            // TODO: Add validation and persistence logic
            System.out.println("Connection Name: " + connectionConfig.getConnectionName());
            return "redirect:/connection-config?success=true";
        } catch (Exception e) {
            return "redirect:/connection-config?error=true";
        }
    }
}