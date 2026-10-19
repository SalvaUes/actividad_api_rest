package edu.sv.ues.dam235.apirestdemo.services;

import edu.sv.ues.dam235.apirestdemo.dtos.LoginDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.TokenDTO;

public interface AuthServices {
    TokenDTO login(LoginDTO request);
    void logout(String token);
}
