package com.summitsync.api.location;

import com.summitsync.api.location.dto.GetLocationDto;
import com.summitsync.api.location.dto.PostLocationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/location")
public class LocationController {

    private final LocationMapper mapper;
    private final LocationService service;

    @PostMapping()
    public ResponseEntity<GetLocationDto>createLocation(@RequestBody PostLocationDto dto){
        Location location=mapper.mapPostLocationDtoToLocation(dto);
        Location createdLocation=service.createLocation(location);
        GetLocationDto response=mapper.mapLocationToGetLocationDto(createdLocation);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetLocationDto>getLocationById(@PathVariable Long id){
        Location location=service.getLocationById(id);
        GetLocationDto response=mapper.mapLocationToGetLocationDto(location);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<GetLocationDto>> getAllLocations(){
        List<Location>allLocations=service.getAllLocations();
        List<GetLocationDto> response=new ArrayList<>();
        for(Location location : allLocations){
            response.add(mapper.mapLocationToGetLocationDto(location));
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetLocationDto>updateLocation(@PathVariable Long id, @RequestBody PostLocationDto dto){
        var dbLocation = this.service.getLocationById(id);
        Location location = service.updateLocation(dbLocation, dto);
        GetLocationDto response = mapper.mapLocationToGetLocationDto(location);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteLocation(@PathVariable Long id){
        service.deleteLocation(id);
    }
}
