package com.example.backend_1_inl_1.controllers;

import com.example.backend_1_inl_1.dto.ResponsMessage;
import com.example.backend_1_inl_1.model.Customer;
import com.example.backend_1_inl_1.dto.Response;
import com.example.backend_1_inl_1.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "https://n-sankell.github.io")
public class CustomerController {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerController (CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping()
    public Response<Iterable<Customer>> getAllCustomers() {
        return new Response<>(customerRepository.findAll());
    }

    @GetMapping("{id}")
    public Response<?> getCustomerById(@PathVariable String id) {
        try {
            long parsedId = Long.parseLong(id);
            return customerRepository.findById(parsedId).isPresent()
                    ? new Response<>(customerRepository.findById(parsedId).get())
                    : new Response<>(ResponsMessage.CUSTOMER_NOT_FOUND);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new Response<>(ResponsMessage.NOT_A_NUMBER);
        }
    }

    @PostMapping()
    public Response<String> addCustomer(@RequestBody Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            return new Response<>(ResponsMessage.EMAIL_IN_USE);
        } else {
            customerRepository.save(customer);
            return new Response<>(ResponsMessage.customerAdded(customer));
        }
    }

    @GetMapping("/delete/{id}")
    public Response<String> deleteCustomerById(@PathVariable String id) {
        try {
            long parsedId = Long.parseLong(id);
            if (customerRepository.findById(parsedId).isPresent()) {
                Customer foundCustomer = customerRepository.findById(parsedId).get();
                customerRepository.delete(foundCustomer);
                return new Response<>(ResponsMessage.customerDeleted(foundCustomer));
            } else {
                return new Response<>(ResponsMessage.CUSTOMER_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new Response<>(ResponsMessage.NOT_A_NUMBER);
        }
    }

}