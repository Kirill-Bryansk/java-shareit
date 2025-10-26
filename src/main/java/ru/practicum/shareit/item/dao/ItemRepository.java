package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i" +
           " from Item as i" +
           " where i.available = true and " +
           "(lower(i.name) like lower(concat('%', ?1, '%') ) or" +
           " lower(i.description) like lower(concat('%', ?1, '%') ))")
    List<Item> search(String text);

    List<Item> findAllByOwnerId(Long ownerId);

    @Query("SELECT i FROM Item i WHERE i.id = :itemId AND i.owner.id = :ownerId")
    void checkOwnerOfItem(@Param("itemId") Long itemId, @Param("ownerId") Long ownerId);
}
