package com.example.servicenewuser.repositories;

import com.example.servicenewuser.domain.User;
import com.example.servicenewuser.exceptions.EtAuthException;


public interface UserRepository {
    Integer create(String firstName, String lastName, String email, String password) throws EtAuthException;

    User findByEmailAndPassword(String email, String password) throws EtAuthException;

    Integer getCountByEmail(String email);

    User findById(Integer userId);
}
