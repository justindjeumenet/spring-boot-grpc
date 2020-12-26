package com.monquo.movie.service;

import com.monquo.movie.repository.MovieRepository;
import com.monquo.proto.models.movie.MovieDto;
import com.monquo.proto.models.movie.MovieSearchRequest;
import com.monquo.proto.models.movie.MovieSearchResponse;
import com.monquo.proto.models.movie.MovieServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class MovieService extends MovieServiceGrpc.MovieServiceImplBase {
    @Autowired
    private MovieRepository repository;

    @Override
    public void getMovies(MovieSearchRequest request, StreamObserver<MovieSearchResponse> responseObserver) {

        // Assemble all we need for response;
       List<MovieDto> movieDtoList = this.repository.getMovieByGenreOrderByYearDesc(request.getGenre().toString())
               .stream() // because MovieDTO is a repeated field in proto
               .map(movie -> MovieDto.newBuilder()
                                   .setTitle(movie.getTitle())
                                   .setYear(movie.getYear())
                                   .setRating(movie.getRating())
                                   .build())
               .collect(Collectors.toList());
       // Build the response
        MovieSearchResponse movieSearchResponse = MovieSearchResponse.newBuilder().addAllMovie(movieDtoList).build();

        //Complete the process
        responseObserver.onNext(movieSearchResponse);
        responseObserver.onCompleted();

    }
}
