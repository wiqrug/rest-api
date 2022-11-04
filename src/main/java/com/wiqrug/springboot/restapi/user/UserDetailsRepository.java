package com.wiqrug.springboot.restapi.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDetailsRepository extends JpaRepository <UserDetails, Long> {



    //This is fking magic!!!!!!!!!!!!!!!!!!!!
    List<UserDetails> findByRole(String role);

}
