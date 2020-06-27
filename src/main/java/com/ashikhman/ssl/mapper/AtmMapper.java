package com.ashikhman.ssl.mapper;

import com.ashikhman.ssl.client.alfabank.model.Atm;
import com.ashikhman.ssl.dto.AtmDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AtmMapper {

    @Mapping(target = "latitude", source = "coordinates.latitude")
    @Mapping(target = "longitude", source = "coordinates.longitude")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "location", source = "address.location")
    @Mapping(target = "payments", expression = "java(atm.hasPayments())")
    AtmDto atmToDto(Atm atm);
}
