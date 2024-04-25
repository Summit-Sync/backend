package com.summitsync.api.location;

import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Location updateLocation(Location location){
        Location data=getLocationById(location.getLocationId());
        data.setCountry(location.getCountry());
        data.setPhone(location.getPhone());
        data.setEmail(location.getEmail());
        data.setStreet(location.getStreet());
        data.setMapsUrl(location.getMapsUrl());
        data.setPostCode(location.getPostCode());
        data.setTitle(location.getTitle());
        data.setCity(location.getCity());
        return repository.save(data);
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
