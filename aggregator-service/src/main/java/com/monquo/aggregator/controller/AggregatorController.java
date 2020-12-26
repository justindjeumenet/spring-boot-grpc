package com.monquo.aggregator.controller;

import com.monquo.aggregator.dto.RecommendedMovie;
import com.monquo.aggregator.dto.UserGenre;
import com.monquo.aggregator.service.UserMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AggregatorController {

    @Autowired
    private UserMovieService userMovieService;

    @GetMapping("/users/{loginId}")
    public List<RecommendedMovie> getMovies(@PathVariable String loginId){
        return this.userMovieService.getUserMovieSuggestions(loginId);
    }

    @PutMapping("/user")
    public void setUserGenre(@RequestBody UserGenre userGenre){
        this.userMovieService.setUserGenre(userGenre);
    }
}
