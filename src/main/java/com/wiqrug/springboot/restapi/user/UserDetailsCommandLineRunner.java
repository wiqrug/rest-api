package com.wiqrug.springboot.restapi.user;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
public class UserDetailsCommandLineRunner  implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserDetailsRepository repository;


    @Override
    public void run(String... args) throws Exception {
        logger.info(Arrays.toString(args));

        repository.save(new UserDetails("Alekos", "Admin"));
        repository.save(new UserDetails("likos","pikos"));
        repository.save(new UserDetails("asdf","asdf"));


        //return the list of users
       // List<UserDetails> users = repository.findAll();

        //find by role

        List<UserDetails> users = repository.findByRole("Admin");

        users.forEach(user -> logger.info(user.toString()));

    }
}
