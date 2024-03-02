package net.qsef1256.dacobot.game.explosion.v2.item;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemDto mapItemDto(Item item);

    Item mapItem(ItemDto item);

}