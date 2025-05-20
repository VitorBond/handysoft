package ru.bondar.handysoft.rest;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.bondar.handysoft.services.CustomService;

@RestController
public class CustomRestController {

    final CustomService customService;

    public CustomRestController(CustomService customService) {
        this.customService = customService;
    }

    @GetMapping("/getValue")
    public ResponseEntity<?> getValue(@Parameter(description = "Путь к локальном файлу xlsx")
                                            @RequestParam("pathToFile") String pathToFile,
                                            @Parameter(description = "Номер минимального значения в файле")
                                            @RequestParam("N") Integer value) {
        return customService.extractValue(pathToFile, value);
    }
}
