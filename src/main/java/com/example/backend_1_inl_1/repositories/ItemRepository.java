package com.example.backend_1_inl_1.repositories;

import com.example.backend_1_inl_1.model.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {
}
