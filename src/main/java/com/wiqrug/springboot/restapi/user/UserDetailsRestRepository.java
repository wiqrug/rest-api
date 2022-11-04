package com.wiqrug.springboot.restapi.user;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


//Learn more about Spring Data Rest
public interface UserDetailsRestRepository extends PagingAndSortingRepository<UserDetails, Long> {

    List<UserDetails> findByRole(String role);

}
