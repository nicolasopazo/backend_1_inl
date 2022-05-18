package com.example.backend_1_inl_1.controllers;

import com.example.backend_1_inl_1.dto.Purchase;
import com.example.backend_1_inl_1.dto.ResponsMessage;
import com.example.backend_1_inl_1.model.Customer;
import com.example.backend_1_inl_1.model.Item;
import com.example.backend_1_inl_1.repositories.CustomerRepository;
import com.example.backend_1_inl_1.repositories.ItemRepository;

import com.example.backend_1_inl_1.repositories.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    @Autowired
    private ItemRepository mockItemRepository;

    @MockBean
    @Autowired
    private CustomerRepository mockCustomerRepository;

    @MockBean
    @Autowired
    private OrderRepository mockOrderRepository;

    @BeforeEach
    public void setMockMvc() {
        Item item1 = new Item(1,"AlbumOne","ArtistOne", LocalDate.of(1981,6,4),"Visor",46);
        Item item2 = new Item(2,"AlbumTwo","ArtistTwo", LocalDate.of(1982,6,4),"Visor",46);
        Item item3 = new Item(3,"AlbumThree","ArtistThree", LocalDate.of(1983,6,4),"Visor",46);
        Item item4 = new Item(4,"AlbumFour","ArtistFour", LocalDate.of(1984,6,4),"Visor",46);

        Customer customer1 = new Customer(1,"Eva","evaave@gmail.com","Lilla sällskapets väg 57", LocalDate.of(1965,3,15));
        Customer customer2 = new Customer(2,"Olle","ollis@gmail.com","Kungsgatan 34", LocalDate.of(1948,8,22));
        Customer customer3 = new Customer(3,"Adam","adams@gmail.com","Johannesvägen 23", LocalDate.of(1975,5,23));

        when(mockCustomerRepository.findById(1L)).thenReturn(Optional.of(customer1));
        when(mockCustomerRepository.findById(2L)).thenReturn(Optional.of(customer2));
        when(mockCustomerRepository.findById(3L)).thenReturn(Optional.of(customer3));
        when(mockCustomerRepository.findAll()).thenReturn(Arrays.asList(customer1,customer2,customer3));

        when(mockItemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(mockItemRepository.findById(2L)).thenReturn(Optional.of(item2));
        when(mockItemRepository.findById(3L)).thenReturn(Optional.of(item3));
        when(mockItemRepository.findById(4L)).thenReturn(Optional.of(item4));
        when(mockItemRepository.findAll()).thenReturn(Arrays.asList(item1,item2,item3));

    }

    @Test
    void getAllItems() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/items")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", hasSize(3)))
                .andExpect(content().json(
                "{response:[" +
                          "{\"id\": 1, \"albumName\": \"AlbumOne\", \"artist\": \"ArtistOne\", \"releaseDate\": \"1981-06-04\", \"genre\": \"Visor\", \"albumLength\": 46}, " +
                          "{\"id\": 2, \"albumName\": \"AlbumTwo\", \"artist\": \"ArtistTwo\", \"releaseDate\": \"1982-06-04\", \"genre\": \"Visor\", \"albumLength\": 46}, " +
                          "{\"id\": 3, \"albumName\": \"AlbumThree\", \"artist\": \"ArtistThree\", \"releaseDate\": \"1983-06-04\", \"genre\": \"Visor\", \"albumLength\": 46}" +
                          "]}"));
    }

    @Test
    void getItemByIdSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/items/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                "{response:" +
                          "{\"id\": 2, \"albumName\": \"AlbumTwo\", \"artist\": \"ArtistTwo\", \"releaseDate\": \"1982-06-04\", \"genre\": \"Visor\", \"albumLength\": 46}" +
                          "}"));
    }

    @Test
    void getItemByIdFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/items/9")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", is(ResponsMessage.PRODUCT_NOT_FOUND)));
    }

    @Test
    void getItemByIdParseFail() throws Exception {
        System.out.println("Test - getItemByIdParseFail - This test prints a stacktrace for NumberFormatException");

        mockMvc.perform(MockMvcRequestBuilders.get("/items/WRONG")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", is(ResponsMessage.NOT_A_NUMBER)));
    }

    @Test
    void addItem() throws Exception {
        Item item4 = new Item(4,"AlbumFour","ArtistFour", LocalDate.of(1984,6,4),"Visor",46);

        mockMvc.perform(MockMvcRequestBuilders.post("/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item4)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", notNullValue()))
                .andExpect(jsonPath("$.response", is(ResponsMessage.productAdded(item4))));

        System.out.println("Test - addItem return: " + ResponsMessage.productAdded(item4));
    }

    @Test
    void buyItemSuccess() throws Exception {
        Purchase purchase = new Purchase(1L,1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/items/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchase)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", notNullValue()))
                .andExpect(jsonPath("$.response", is(ResponsMessage.ORDER_COMPLETE)));
    }

    @Test
    void buyItemWrongProduct() throws Exception {
        Purchase purchase = new Purchase(2L,9L);

        mockMvc.perform(MockMvcRequestBuilders.post("/items/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchase)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", notNullValue()))
                .andExpect(jsonPath("$.response", is(ResponsMessage.PRODUCT_NOT_FOUND)));
    }

    @Test
    void buyItemWrongCustomer() throws Exception {
        Purchase purchase = new Purchase(9L,1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/items/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchase)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", notNullValue()))
                .andExpect(jsonPath("$.response", is(ResponsMessage.CUSTOMER_NOT_FOUND)));
    }

    @Test
    void buyItemWrongCustomerAndProduct() throws Exception {
        Purchase purchase = new Purchase(9L,9L);

        mockMvc.perform(MockMvcRequestBuilders.post("/items/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchase)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", notNullValue()))
                .andExpect(jsonPath("$.response", is(ResponsMessage.NOTHING_FOUND)));
    }

    @Test
    void deleteItemById() throws Exception {
        Item item4 = new Item(4,"AlbumFour","ArtistFour", LocalDate.of(1984,6,4),"Visor",46);

        mockMvc.perform(MockMvcRequestBuilders.get("/items/delete/4")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", is(ResponsMessage.productDeleted(item4))));

        System.out.println("Test - deleteItemById return: " + ResponsMessage.productDeleted(item4));
    }

    @Test
    void deleteItemByIdFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/items/delete/9")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", is(ResponsMessage.PRODUCT_NOT_FOUND)));
    }

    @Test
    void deleteItemByIdParseFail() throws Exception {
        System.out.println("Test - deleteItemByIdParseFail - This test prints a stacktrace for NumberFormatException");

        mockMvc.perform(MockMvcRequestBuilders.get("/items/delete/WRONG")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", is(ResponsMessage.NOT_A_NUMBER)));
    }

}