package com.summitsync.api.location;

import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import com.summitsync.api.location.dto.PostLocationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository repository;

    public Location getLocationById(Long id){
        Optional<Location>data=repository.findById(id);
        if(data.isEmpty()){
            throw new ResourceNotFoundException("Location on id: "+id+" does not exist");
        }
        return data.get();
    }

    public List<Location>getAllLocations(){
        return repository.findAll();
    }

    public Location updateLocation(Location locationToUpdate, PostLocationDto updatedLocation){
        locationToUpdate.setCountry(updatedLocation.getCountry());
        locationToUpdate.setPhone(updatedLocation.getPhone());
        locationToUpdate.setEmail(updatedLocation.getEmail());
        locationToUpdate.setStreet(updatedLocation.getStreet());
        locationToUpdate.setMapsUrl(updatedLocation.getMapsUrl());
        locationToUpdate.setPostCode(updatedLocation.getPostCode());
        locationToUpdate.setTitle(updatedLocation.getTitle());
        locationToUpdate.setCity(updatedLocation.getCity());
        return repository.save(locationToUpdate);
    }

    public Location createLocation(Location location){
        return repository.save(location);
    }

    public void deleteLocation(Long id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        } else{
            throw new ResourceNotFoundException("Location on id: "+id+" does not exist");
        }

    }
}
