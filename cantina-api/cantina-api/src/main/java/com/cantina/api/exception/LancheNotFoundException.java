package com.cantina.api.exception;

public class LancheNotFoundException extends RuntimeException {

    public LancheNotFoundException(Long id) {
        super("Lanche não encontrado com o ID: " + id);
    }
}
