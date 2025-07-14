package org.hanihome.hanihomebe.metro.web.controller;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.metro.application.service.MetroStopService;
import org.hanihome.hanihomebe.metro.web.dto.MetroStopResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/metro/stops")
@RestController
public class MetroStopController {
    private final MetroStopService metroStopService;

    // read
    @GetMapping
    public List<MetroStopResponseDTO> getAll() {
        return metroStopService.getAllStops();
    }

}
