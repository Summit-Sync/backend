package com.summitsync.api.location;

import com.summitsync.api.location.dto.GetLocationDto;
import com.summitsync.api.location.dto.PostLocationDto;
import org.springframework.stereotype.Service;

@Service
public class LocationMapper {

    public Location mapPostLocationDtoToLocation(PostLocationDto dto){
        return Location.builder()
                .country(dto.getCountry())
                .email(dto.getEmail())
                .mapsUrl(dto.getMapsUrl())
                .postCode(dto.getPostCode())
                .phone(dto.getPhone())
                .street(dto.getStreet())
                .room(dto.getRoom())
                .city(dto.getCity())
                .title(dto.getTitle())
                .build();
    }

    public GetLocationDto mapLocationToGetLocationDto(Location location){
        return GetLocationDto.builder()
                .locationId(location.getLocationId())
                .country(location.getCountry())
                .email(location.getEmail())
                .mapsUrl(location.getMapsUrl())
                .phone(location.getPhone())
                .postCode(location.getPostCode())
                .room(location.getRoom())
                .street(location.getStreet())
                .city(location.getCity())
                .title(location.getTitle())
                .build();
    }

    public Location mapLocationDtoToLocation(GetLocationDto dto){
        return Location.builder()
                .locationId(dto.getLocationId())
                .country(dto.getCountry())
                .email(dto.getEmail())
                .mapsUrl(dto.getMapsUrl())
                .postCode(dto.getPostCode())
                .phone(dto.getPhone())
                .street(dto.getStreet())
                .room(dto.getRoom())
                .title(dto.getTitle())
                .city(dto.getCity())
                .build();
    }
}
