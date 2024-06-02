package com.summitsync.api.status;

import com.summitsync.api.keycloak.KeycloakRestService;
import com.summitsync.api.status.dto.StatusGetDto;
import com.summitsync.api.status.dto.StatusPostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/status")
@RequiredArgsConstructor
public class StatusController {
    private final StatusMapper statusMapper;
    private final StatusService statusService;
    private final KeycloakRestService keycloakRestService;

    @PostMapping
    public StatusGetDto newStatus(@RequestBody StatusPostDto statusPostDto) {
        var status = this.statusMapper.mapStatusPostDtoToStatus(statusPostDto);
        var savedStatus = this.statusService.saveStatus(status);

        return this.statusMapper.mapStatusToStatusGetDto(savedStatus);
    }

    @GetMapping
    public List<StatusGetDto> getAllStatus() {
        return this.statusService
                .findAll()
                .stream()
                .map(this.statusMapper::mapStatusToStatusGetDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public StatusGetDto getStatusById(@PathVariable long id) {
        var status = this.statusService.findById(id);

        return this.statusMapper.mapStatusToStatusGetDto(status);
    }

    @DeleteMapping("/{id}")
    public void deleteStatusById(@PathVariable long id) {
        this.statusService.deleteById(id);
    }

    @PutMapping("/{id}")
    public StatusGetDto putStatusById(@PathVariable long id, @RequestBody StatusPostDto statusPostDto) {
        var status = this.statusService.updateStatus(id, statusPostDto);
        return this.statusMapper.mapStatusToStatusGetDto(status);
    }
}
