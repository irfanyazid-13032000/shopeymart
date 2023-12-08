package com.enigma.shopeymart.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeloController {

    @GetMapping("hello")
    public String hello(){
        return "<h1 style='color:red';>waw</h1>";
    }

    @GetMapping("hello/newjeans")
    public String[] newjeans(){
        return new String[] {"hanni","haerin","hyein","minji","danielle"};
    }

    @GetMapping("/hello/{id}/req")
    public String getRequestParam(@PathVariable("id") String id,@RequestParam("key") String key){
//        return key + " bajingannnnn!!!!!";
        return "<h1 style='color:red';>"+key + id+ " bajingannnn!!!!!!!!!</h1>";
    }

    @GetMapping("berita/{id}/{sifat}")
    public String getById(@PathVariable String id, @PathVariable String sifat){
//        return "data " + id + " " + sifat;
        return "<h1 style='color:red';>"+id +" "+ sifat + "</h1>";
    }




}
