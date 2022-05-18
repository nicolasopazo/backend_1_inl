package com.example.backend_1_inl_1.controllers;

import com.example.backend_1_inl_1.dto.ResponsMessage;
import com.example.backend_1_inl_1.model.Customer;
import com.example.backend_1_inl_1.model.Item;
import com.example.backend_1_inl_1.model.ItemOrder;
import com.example.backend_1_inl_1.repositories.CustomerRepository;
import com.example.backend_1_inl_1.repositories.ItemRepository;
import com.example.backend_1_inl_1.repositories.OrderRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Autowired
    private OrderRepository mockOrderRepository;

    @MockBean
    @Autowired
    private ItemRepository mockItemRepository;

    @MockBean
    @Autowired
    private CustomerRepository mockCustomerRepository;


    @BeforeEach
    void setMockMvc() {
        Customer customer1 = new Customer(1,"Eva","evaave@gmail.com","Lilla sällskapets väg 57", LocalDate.of(1965,3,15));
        Customer customer2 = new Customer(2,"Olle","ollis@gmail.com","Kungsgatan 34", LocalDate.of(1948,8,22));

        Item item1 = new Item(1,"AlbumOne","ArtistOne", LocalDate.of(1981,6,4),"Visor",46);
        Item item2 = new Item(2,"AlbumTwo","ArtistTwo", LocalDate.of(1982,6,4),"Visor",46);
        Item item3 = new Item(3,"AlbumThree","ArtistThree", LocalDate.of(1983,6,4),"Visor",46);
        Item item4 = new Item(4,"AlbumFour","ArtistFour", LocalDate.of(1984,6,4),"Visor",46);

        ItemOrder order1 = new ItemOrder(1,LocalDate.of(2021,6,13),item1);
        ItemOrder order2 = new ItemOrder(2,LocalDate.of(2022,3,5),item2);
        ItemOrder order3 = new ItemOrder(3,LocalDate.of(2022,12,7),item3);

        customer1.addOrder(order1);
        customer1.addOrder(order2);
        customer2.addOrder(order3);

        when(mockItemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(mockItemRepository.findById(2L)).thenReturn(Optional.of(item2));
        when(mockItemRepository.findById(3L)).thenReturn(Optional.of(item3));
        when(mockItemRepository.findById(4L)).thenReturn(Optional.of(item4));

        when(mockCustomerRepository.findById(1L)).thenReturn(Optional.of(customer1));
        when(mockCustomerRepository.findById(2L)).thenReturn(Optional.of(customer2));

        when(mockOrderRepository.findById(1L)).thenReturn(Optional.of(order1));
        when(mockOrderRepository.findById(2L)).thenReturn(Optional.of(order2));
        when(mockOrderRepository.findById(3L)).thenReturn(Optional.of(order3));
        when(mockOrderRepository.findAll()).thenReturn(Arrays.asList(order1,order2,order3));
    }

    @Test
    void getAllOrders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", hasSize(3)))
                .andExpect(content().json(
                "{response:[" +
                          "{\"id\":1,\"orderDate\":\"2021-06-13\",\"item\":{\"id\": 1, \"albumName\": \"AlbumOne\", \"artist\": \"ArtistOne\", \"releaseDate\": \"1981-06-04\", \"genre\": \"Visor\", \"albumLength\": 46}}," +
                          "{\"id\":2,\"orderDate\":\"2022-03-05\",\"item\":{\"id\": 2, \"albumName\": \"AlbumTwo\", \"artist\": \"ArtistTwo\", \"releaseDate\": \"1982-06-04\", \"genre\": \"Visor\", \"albumLength\": 46}}," +
                          "{\"id\":3,\"orderDate\":\"2022-12-07\",\"item\":{\"id\": 3, \"albumName\": \"AlbumThree\", \"artist\": \"ArtistThree\", \"releaseDate\": \"1983-06-04\", \"genre\": \"Visor\", \"albumLength\": 46}}" +
                          "]}"));
    }

    @Test
    void getOrdersByCustomerId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                "{response:[" +
                          "{\"id\":1,\"orderDate\":\"2021-06-13\",\"item\":{\"id\": 1, \"albumName\": \"AlbumOne\", \"artist\": \"ArtistOne\", \"releaseDate\": \"1981-06-04\", \"genre\": \"Visor\", \"albumLength\": 46}}," +
                          "{\"id\":2,\"orderDate\":\"2022-03-05\",\"item\":{\"id\": 2, \"albumName\": \"AlbumTwo\", \"artist\": \"ArtistTwo\", \"releaseDate\": \"1982-06-04\", \"genre\": \"Visor\", \"albumLength\": 46}}" +
                          "]}"));
    }

    @Test
    void getOrdersByCustomerIdFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/9")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", is(ResponsMessage.CUSTOMER_NOT_FOUND)));
    }

    @Test
    void getOrdersByCustomerIdParseFail() throws Exception {
        System.out.println("Test - getOrdersByCustomerIdParseFail - This test prints a stacktrace for NumberFormatException");

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/WRONG")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", is(ResponsMessage.NOT_A_NUMBER)));
    }

    @Test
    void deleteOrderById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/delete/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", is(ResponsMessage.ORDER_DELETED)));
    }

    @Test
    void deleteOrderByIdFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/delete/9")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", is(ResponsMessage.ORDER_NOT_FOUND)));
    }

    @Test
    void deleteOrderByIdParseFail() throws Exception {
        System.out.println("Test - deleteOrderByIdParseFail - This test prints a stacktrace for NumberFormatException");

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/delete/WRONG")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", is(ResponsMessage.NOT_A_NUMBER)));
    }

}