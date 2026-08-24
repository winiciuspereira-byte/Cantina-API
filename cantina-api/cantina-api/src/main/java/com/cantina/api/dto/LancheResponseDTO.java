package com.cantina.api.dto;

import com.cantina.api.model.Lanche;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class LancheResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String categoria;
    private Boolean disponivel;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public LancheResponseDTO(Lanche lanche) {
        this.id = lanche.getId();
        this.nome = lanche.getNome();
        this.descricao = lanche.getDescricao();
        this.preco = lanche.getPreco();
        this.categoria = lanche.getCategoria();
        this.disponivel = lanche.getDisponivel();
        this.criadoEm = lanche.getCriadoEm();
        this.atualizadoEm = lanche.getAtualizadoEm();
    }
}
