package com.easyittech.appointmentservice;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
@EnableHystrix
@EnableHystrixDashboard
public class AppointmentServiceApplication {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(groupKey = "easyittech", commandKey = "easyittech", fallbackMethod = "errorFallBack")
    @GetMapping("/appointment")
    public String makeAppointment() {
        String pay = restTemplate.getForObject("http://localhost:8081/payment", String.class);
        String email = restTemplate.getForObject("http://localhost:8082/email", String.class);
        return pay + " - " + email;
    }

    public String errorFallBack() {
        return "operation is failed";
    }

    public static void main(String[] args) {
        SpringApplication.run(AppointmentServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
