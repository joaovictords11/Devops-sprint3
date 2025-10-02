package com.exemplo.patio;

import com.exemplo.patio.model.Moto;
import com.exemplo.patio.model.Registro;
import com.exemplo.patio.repository.MotoRepository;
import com.exemplo.patio.repository.RegistroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class PatioApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatioApplication.class, args);
    }
}