package com.rodolfoafonso.nobile.mapper;

import com.rodolfoafonso.nobile.domain.entity.Watch;
import com.rodolfoafonso.nobile.dto.WatchDTO;
import com.rodolfoafonso.nobile.dto.WatchResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface WatchMapper {

    Watch toEntity(WatchDTO dto);

    @Mapping(source = "seller.id", target = "sellerId")
    WatchResponseDTO toDto(Watch watch);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(WatchDTO dto, @MappingTarget Watch entity);
}
