package net.qsef1256.dacobot.game.explosion.domain.itemtype;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemTypeMapper {

    ItemTypeMapper INSTANCE = Mappers.getMapper(ItemTypeMapper.class);

    ItemTypeDto mapItemTypeDto(ItemTypeEntity itemType);

}