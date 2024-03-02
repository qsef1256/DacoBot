package net.qsef1256.dacobot.game.explosion.v2.itemtype;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemTypeMapper {

    ItemTypeMapper INSTANCE = Mappers.getMapper(ItemTypeMapper.class);

    ItemTypeDto mapItemTypeDto(ItemTypeEntity itemType);

}