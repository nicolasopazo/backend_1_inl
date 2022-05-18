package com.example.backend_1_inl_1.beans;

import com.example.backend_1_inl_1.model.Customer;
import com.example.backend_1_inl_1.model.Item;
import com.example.backend_1_inl_1.model.ItemOrder;
import com.example.backend_1_inl_1.repositories.CustomerRepository;
import com.example.backend_1_inl_1.repositories.ItemRepository;
import com.example.backend_1_inl_1.repositories.OrderRepository;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDate;

public class Beans {

    //@Bean
    public CommandLineRunner addBeans(CustomerRepository customerRepository,
                                      ItemRepository itemRepository,
                                      OrderRepository orderRepository) {
        return args -> {

            Customer customer1 = new Customer("Eva","evaave@gmail.com","Lilla sällskapets väg 57", LocalDate.of(1965,3,15));
            Customer customer2 = new Customer("Olle","ollis@gmail.com","Kungsgatan 34", LocalDate.of(1948,8,22));

            Item item1 = new Item("Born in the USA","Bruce Springsteen", LocalDate.of(1984,6,4),"Rock",46);
            Item item2 = new Item("Tunnel of Love","Bruce Springsteen", LocalDate.of(1987,10,9),"Rock",46);
            Item item3 = new Item("The Kick Inside","Kate Bush", LocalDate.of(1978,2,17),"Rock",43);
            Item item4 = new Item("Hounds of Love","Kate Bush", LocalDate.of(1985,7,16),"Rock",47);

            ItemOrder oder1 = new ItemOrder(LocalDate.of(2022,3,2), item1);
            customer1.addOrder(oder1);

            ItemOrder order2 = new ItemOrder(LocalDate.of(2022,4,12), item3);
            customer1.addOrder(order2);

            itemRepository.save(item1);
            itemRepository.save(item2);
            itemRepository.save(item3);
            itemRepository.save(item4);

            orderRepository.save(oder1);
            orderRepository.save(order2);

            customerRepository.save(customer1);
            customerRepository.save(customer2);

        };
    }

}
