package com.monquo.movie.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class Movie {

    @Id
    private int id;

    private String title;
    private int year;
    private double rating;
    private String genre;
}
