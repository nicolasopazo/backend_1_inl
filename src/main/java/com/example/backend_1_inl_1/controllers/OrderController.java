package com.example.backend_1_inl_1.controllers;

import com.example.backend_1_inl_1.dto.ResponsMessage;
import com.example.backend_1_inl_1.model.ItemOrder;
import com.example.backend_1_inl_1.dto.Response;
import com.example.backend_1_inl_1.repositories.CustomerRepository;
import com.example.backend_1_inl_1.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "https://n-sankell.github.io")
public class OrderController {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping()
    public Response<Iterable<ItemOrder>> getAllOrders() {
        return new Response<>(orderRepository.findAll());
    }

    @GetMapping("{id}")
    public Response<?> getOrdersByCustomerId(@PathVariable String id) {
        try {
            long parsedId = Long.parseLong(id);
            return customerRepository.findById(parsedId).isPresent()
                    ? new Response<>(customerRepository.findById(parsedId).get().getItemOrders())
                    : new Response<>(ResponsMessage.CUSTOMER_NOT_FOUND);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new Response<>(ResponsMessage.NOT_A_NUMBER);
        }
    }

    @GetMapping("/delete/{id}")
    public Response<String> deleteOrderById(@PathVariable String id) {
        try {
            long parsedId = Long.parseLong(id);
            if (orderRepository.findById(parsedId).isPresent()) {
                ItemOrder foundOrder = orderRepository.findById(parsedId).get();
                orderRepository.delete(foundOrder);
                return new Response<>(ResponsMessage.ORDER_DELETED);
            } else {
                return new Response<>(ResponsMessage.ORDER_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new Response<>(ResponsMessage.NOT_A_NUMBER);
        }
    }

}
