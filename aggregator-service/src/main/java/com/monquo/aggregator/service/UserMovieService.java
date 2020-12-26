package com.monquo.aggregator.service;

import com.monquo.aggregator.dto.RecommendedMovie;
import com.monquo.aggregator.dto.UserGenre;
import com.monquo.proto.models.common.Genre;
import com.monquo.proto.models.movie.MovieSearchRequest;
import com.monquo.proto.models.movie.MovieSearchResponse;
import com.monquo.proto.models.movie.MovieServiceGrpc;
import com.monquo.proto.models.user.UserGenreUpdateRequest;
import com.monquo.proto.models.user.UserResponse;
import com.monquo.proto.models.user.UserSearchRequest;
import com.monquo.proto.models.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMovieService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @GrpcClient("movie-service")
    private MovieServiceGrpc.MovieServiceBlockingStub movieStub;

    public List<RecommendedMovie> getUserMovieSuggestions(String longinId){
        // Build requests
        UserSearchRequest userSearchRequest = UserSearchRequest.newBuilder().setLoginId(longinId).build();
        // Response
        UserResponse userResponse = this.userStub.getUserGenre(userSearchRequest);

        MovieSearchRequest movieSearchRequest = MovieSearchRequest.newBuilder().setGenre(userResponse.getGenre()).build();
        MovieSearchResponse movieSearchResponse = this.movieStub.getMovies(movieSearchRequest);

        return movieSearchResponse.getMovieList()
                .stream()
                .map(movieDto -> new RecommendedMovie(movieDto.getTitle(), movieDto.getYear(), movieDto.getRating()))
                .collect(Collectors.toList());
    }


    public void setUserGenre(UserGenre userGenre){
        UserGenreUpdateRequest userGenreUpdateRequest = UserGenreUpdateRequest.newBuilder()
                .setLoginId(userGenre.getLoginId())
                .setGenre(Genre.valueOf(userGenre.getGenre().toUpperCase()))
                .build();

        UserResponse userResponse = this.userStub.updateUserGenre(userGenreUpdateRequest);
    }
}
