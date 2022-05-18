package com.example.backend_1_inl_1.controllers;

import com.example.backend_1_inl_1.dto.Purchase;
import com.example.backend_1_inl_1.dto.ResponsMessage;
import com.example.backend_1_inl_1.dto.Response;
import com.example.backend_1_inl_1.model.*;
import com.example.backend_1_inl_1.repositories.CustomerRepository;
import com.example.backend_1_inl_1.repositories.ItemRepository;
import com.example.backend_1_inl_1.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/items")
@CrossOrigin(origins = "https://n-sankell.github.io")
public class ItemController {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public ItemController(ItemRepository itemRepository, OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping()
    public Response<Iterable<Item>> getAllItems() {
        return new Response<>(itemRepository.findAll());
    }

    @GetMapping("{id}")
    public Response<?> getItemById(@PathVariable String id) {
        try {
            long parsedId = Long.parseLong(id);
            return itemRepository.findById(parsedId).isPresent()
                    ? new Response<>(itemRepository.findById(parsedId).get())
                    : new Response<>(ResponsMessage.PRODUCT_NOT_FOUND);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new Response<>(ResponsMessage.NOT_A_NUMBER);
        }
    }

    @PostMapping()
    public Response<String> addItem(@RequestBody Item item) {
        itemRepository.save(item);
        return new Response<>(ResponsMessage.productAdded(item));
    }

    @PostMapping("/buy")
    public Response<String>buyItem(@RequestBody Purchase purchase) {
        if (customerRepository.findById(purchase.customerId()).isPresent() && itemRepository.findById(purchase.itemId()).isPresent()) {

            ItemOrder newOrder = new ItemOrder(LocalDate.now(), itemRepository.findById(purchase.itemId()).get());
            Customer customer = customerRepository.findById(purchase.customerId()).get();

            orderRepository.save(newOrder);
            customer.addOrder(newOrder);
            customerRepository.save(customer);

            return new Response<>(ResponsMessage.ORDER_COMPLETE);

        } else if (customerRepository.findById(purchase.customerId()).isPresent()) {
            return new Response<>(ResponsMessage.PRODUCT_NOT_FOUND);

        } else if (itemRepository.findById(purchase.itemId()).isPresent()) {
            return new Response<>(ResponsMessage.CUSTOMER_NOT_FOUND);

        } else {
            return new Response<>(ResponsMessage.NOTHING_FOUND);
        }
    }

    @GetMapping("/delete/{id}")
    public Response<String> deleteItemById(@PathVariable String id) {
        try {
            long parsedId = Long.parseLong(id);
            if (itemRepository.findById(parsedId).isPresent()) {
                Item foundItem = itemRepository.findById(parsedId).get();
                itemRepository.delete(foundItem);
                return new Response<>(ResponsMessage.productDeleted(foundItem));
            } else {
                return new Response<>(ResponsMessage.PRODUCT_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new Response<>(ResponsMessage.NOT_A_NUMBER);
        }
    }

}


