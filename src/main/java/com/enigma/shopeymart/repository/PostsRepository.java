package com.enigma.shopeymart.repository;

import com.enigma.shopeymart.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts,Integer> {
}
