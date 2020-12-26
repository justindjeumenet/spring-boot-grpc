package com.monquo.user.service;

import com.google.protobuf.StringValue;
import com.monquo.proto.models.common.Genre;
import com.monquo.proto.models.user.UserGenreUpdateRequest;
import com.monquo.proto.models.user.UserResponse;
import com.monquo.proto.models.user.UserSearchRequest;
import com.monquo.proto.models.user.UserServiceGrpc;
import com.monquo.user.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {
    // will add UserService to gRPC Server when start
    @Autowired
    private UserRepository repository;

    @Override
    public void getUserGenre(UserSearchRequest request, StreamObserver<UserResponse> responseObserver) {
        // Prepare builder for response
        UserResponse.Builder builder = UserResponse.newBuilder();

        // find the use genre with the request and build the response
        this.repository.findById(request.getLoginId())
                .ifPresent(user -> {
                    builder.setName(user.getName())
                            .setLoginId(user.getLogin())
                            .setGenre(Genre.valueOf(user.getGenre().toUpperCase()));
                });

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    @Transactional // to update automatically.
    public void updateUserGenre(UserGenreUpdateRequest request, StreamObserver<UserResponse> responseObserver) {
        // Prepare builder for response
        UserResponse.Builder builder = UserResponse.newBuilder();

        this.repository.findById(request.getLoginId())
                .ifPresent(user -> {
                    user.setGenre(request.getGenre().toString());
                    builder.setName(user.getName())
                            .setLoginId(user.getLogin())
                            .setGenre(Genre.valueOf(user.getGenre().toUpperCase()));
                });

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();

    }
}
