package com.learn.web_client_pratice.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class NBAController {
    private final WebClient.Builder webClient;
    public NBAController(WebClient.Builder webClient) {
        this.webClient = webClient;
    }

    public record Users (String name, int age) {
        public Users (String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
    /*
        Mono su dung khi api tra ve 1 du lieu duy nhat
        Flux su dung khi api tra ve nhieu du lieu
    */
    @GetMapping("/searchUser")
    public Mono<Users> getSeason(){
        Duration timeout = Duration.ofMinutes(5);
        String name = "John Doe";
        return webClient.baseUrl("http://localhost:8080")
                .build()
                .get()
                .uri("/search/{name}", name)
                .retrieve()
                .bodyToMono(Users.class)
                .timeout(timeout);
    }

    @PostMapping("/addUser")
    public Mono<Integer> addNewUser(){
        Users user = new Users("Kai Cenat", 24);
        return webClient.baseUrl("http://localhost:8080")
                .build()
                .post()
                .uri("/add")
                .body(Mono.just(user), Users.class)
                .retrieve()
                .bodyToMono(Integer.class);
    }

    @GetMapping("/search/{name}")
    public Mono<Users> searchName(@PathVariable String name){
        List<Users> names = new ArrayList<>();
        names.add(new Users("John Doe", 22));
        names.add(new Users("Africa Freek", 19));
        Optional<Users> findName = names.stream().filter(s -> s.name.equals(name)).findFirst();
        return findName.map(Mono::just).orElseGet(Mono::empty);
    }

    @PostMapping("/add")
    public Mono<Integer> addUser(@RequestBody Users user){
        List<Users> names = new ArrayList<>();
        names.add(user);
        return Mono.just(names.size());
    }
}
