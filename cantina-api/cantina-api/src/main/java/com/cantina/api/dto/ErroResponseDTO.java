package com.cantina.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ErroResponseDTO {

    private int status;
    private String erro;
    private List<String> mensagens;
    private LocalDateTime timestamp;

    public ErroResponseDTO(int status, String erro, String mensagem) {
        this.status = status;
        this.erro = erro;
        this.mensagens = List.of(mensagem);
        this.timestamp = LocalDateTime.now();
    }
}
