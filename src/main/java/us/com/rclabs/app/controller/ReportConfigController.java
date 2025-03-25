package us.com.rclabs.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReportConfigController {

    @GetMapping("/report-config")
    public String showReportConfigForm(Model model) {
        // Add any necessary attributes to the model
        return "report-config";
    }

    @PostMapping("/report-config")
    public String handleReportConfigForm(@RequestParam("reportName") String reportName,
                                         @RequestParam("reportType") String reportType,
                                         @RequestParam("reportFrequency") String reportFrequency,
                                         @RequestParam("reportFormat") String reportFormat,
                                         Model model) {
        // Handle the form submission and save the report details
        // Add any necessary attributes to the model
        return "report-config";
    }
}
