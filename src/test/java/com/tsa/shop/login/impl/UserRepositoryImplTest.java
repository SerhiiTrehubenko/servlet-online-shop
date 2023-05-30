package com.tsa.shop.login.impl;

import com.tsa.shop.domain.User;
import com.tsa.shop.login.repoimpl.UserRepositoryImpl;
import com.tsa.shop.login.repo.UserRepository;
import org.eclipse.jetty.util.security.Credential;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryImplTest {
    private final String email = "tsa@gmail.com";

    @Test
    void shouldReturnUserWhenIncomeEmailCoincides() {
        User userInDb = createUser();

        UserRepository userRepository = new UserRepositoryImpl();

        userRepository.addUser(userInDb);

        User retrievedUser = userRepository.getUser(email);
        assertNotNull(retrievedUser);
        assertEquals(userInDb, userRepository.getUser(email));
    }

    @Test
    void shouldReturnNullWhenIncomeEmailDoesNotCoincides() {
        String emailDoesNotCoincideWithEnyUser = "@gmail.com";

        User userInDb = createUser();

        UserRepository userRepository = new UserRepositoryImpl();

        userRepository.addUser(userInDb);

        User retrievedUser = userRepository.getUser(emailDoesNotCoincideWithEnyUser);
        assertNull(retrievedUser);
    }

    private User createUser() {
        UUID sole = UUID.randomUUID();
        String password = "HelloWorld!!!";
        String md5 = Credential.MD5.digest(password + sole);
        return new User(email, md5, sole.toString());
    }
}