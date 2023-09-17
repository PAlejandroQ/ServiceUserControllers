package com.example.servicenewuser.services;

import com.example.servicenewuser.domain.User;
import com.example.servicenewuser.exceptions.EtAuthException;

public interface UserService {
    User validateUser(String email, String password) throws EtAuthException;
    User registerUser(String firsName, String lastName, String email, String password) throws EtAuthException;

}
