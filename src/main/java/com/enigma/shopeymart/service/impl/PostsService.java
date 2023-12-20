package com.enigma.shopeymart.service.impl;

import com.enigma.shopeymart.entity.Posts;
import com.enigma.shopeymart.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostsService {
    @Value("${api.endpoint.url.post}")
    private String url;

    private final PostsRepository postsRepository;

    private final RestTemplate restTemplate;

    public ResponseEntity<List<Posts>> getAllPosts(){
        ResponseEntity<Posts[]> apiResponse = restTemplate.getForEntity(url,Posts[].class);

        List<Posts> externalPosts = List.of(Objects.requireNonNull(apiResponse.getBody()));

        List<Posts> dbPosts = postsRepository.findAll();

        dbPosts.addAll(externalPosts);

        return ResponseEntity.ok(dbPosts);
    }

    public ResponseEntity<String> getPostById(Long id){
        return responseMethod(restTemplate.getForEntity(url+id,String.class),"failed");
    }

    public ResponseEntity<String> createPosts(Posts posts){
        postsRepository.save(posts);
//        mengatur header permintaan
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        membugkus header dalam http
        HttpEntity<Posts> requestEntity = new HttpEntity<>(posts,httpHeaders);
        return responseMethod(restTemplate.postForEntity(url,requestEntity,String.class),"failed nih");

    }




    private ResponseEntity<String> responseMethod(ResponseEntity<String> restTemplate,String message){
        ResponseEntity<String> responseEntity = restTemplate;
        if (responseEntity.getStatusCode().is2xxSuccessful()){
            String responseBody = responseEntity.getBody();
            return ResponseEntity.ok(responseBody);
        }
        return ResponseEntity.status(responseEntity.getStatusCode()).body(message);
    }








}
