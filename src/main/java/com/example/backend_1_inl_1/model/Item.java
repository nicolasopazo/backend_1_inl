package com.example.backend_1_inl_1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String albumName;
    private String artist;
    private LocalDate releaseDate;
    private String genre;
    private int albumLength;

    public Item(String albumName, String artist, LocalDate releaseDate, String genre, int albumLength) {
        this.albumName = albumName;
        this.artist = artist;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.albumLength = albumLength;
    }

    @Override
    public String toString() {
        return artist + " - " + albumName;
    }

}
