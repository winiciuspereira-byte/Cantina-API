package com.cantina.api.dto;

import com.cantina.api.model.Lanche;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class LancheResumoDTO {

    private Long id;
    private String nome;
    private BigDecimal preco;

    public LancheResumoDTO(Lanche lanche) {
        this.id = lanche.getId();
        this.nome = lanche.getNome();
        this.preco = lanche.getPreco();
    }
}
