package com.cantina.api;

import com.cantina.api.dto.LancheRequestDTO;
import com.cantina.api.dto.LancheResponseDTO;
import com.cantina.api.dto.LancheResumoDTO;
import com.cantina.api.exception.LancheNotFoundException;
import com.cantina.api.model.Lanche;
import com.cantina.api.repository.LancheRepository;
import com.cantina.api.service.LancheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LancheServiceTest {

    @Mock
    private LancheRepository lancheRepository;

    @InjectMocks
    private LancheService lancheService;

    private Lanche lanche;
    private LancheRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        lanche = new Lanche();
        lanche.setId(1L);
        lanche.setNome("X-Burguer");
        lanche.setDescricao("Delicioso hambúrguer artesanal");
        lanche.setPreco(new BigDecimal("12.50"));
        lanche.setCategoria("Lanche");
        lanche.setDisponivel(true);
        lanche.setCriadoEm(LocalDateTime.now());
        lanche.setAtualizadoEm(LocalDateTime.now());

        requestDTO = new LancheRequestDTO();
        requestDTO.setNome("X-Burguer");
        requestDTO.setDescricao("Delicioso hambúrguer artesanal");
        requestDTO.setPreco(new BigDecimal("12.50"));
        requestDTO.setCategoria("Lanche");
        requestDTO.setDisponivel(true);
    }

    @Test
    @DisplayName("Deve cadastrar um lanche com sucesso")
    void deveCadastrarLancheComSucesso() {
        when(lancheRepository.save(any(Lanche.class))).thenReturn(lanche);

        LancheResponseDTO resultado = lancheService.cadastrar(requestDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("X-Burguer");
        assertThat(resultado.getPreco()).isEqualByComparingTo("12.50");
        verify(lancheRepository, times(1)).save(any(Lanche.class));
    }

    @Test
    @DisplayName("Deve listar todos os lanches como resumo")
    void deveListarTodosLanches() {
        when(lancheRepository.findAll()).thenReturn(List.of(lanche));

        List<LancheResumoDTO> resultado = lancheService.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("X-Burguer");
        assertThat(resultado.get(0).getPreco()).isEqualByComparingTo("12.50");
    }

    @Test
    @DisplayName("Deve buscar lanche por ID com sucesso")
    void deveBuscarLanchePorId() {
        when(lancheRepository.findById(1L)).thenReturn(Optional.of(lanche));

        LancheResponseDTO resultado = lancheService.buscarPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("X-Burguer");
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar lanche com ID inexistente")
    void deveLancarExcecaoQuandoLancheNaoEncontrado() {
        when(lancheRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lancheService.buscarPorId(99L))
                .isInstanceOf(LancheNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Deve atualizar um lanche com sucesso")
    void deveAtualizarLancheComSucesso() {
        requestDTO.setNome("X-Burguer Duplo");
        requestDTO.setPreco(new BigDecimal("18.00"));

        lanche.setNome("X-Burguer Duplo");
        lanche.setPreco(new BigDecimal("18.00"));

        when(lancheRepository.findById(1L)).thenReturn(Optional.of(lanche));
        when(lancheRepository.save(any(Lanche.class))).thenReturn(lanche);

        LancheResponseDTO resultado = lancheService.atualizar(1L, requestDTO);

        assertThat(resultado.getNome()).isEqualTo("X-Burguer Duplo");
        assertThat(resultado.getPreco()).isEqualByComparingTo("18.00");
    }

    @Test
    @DisplayName("Deve remover lanche com sucesso")
    void deveRemoverLancheComSucesso() {
        when(lancheRepository.existsById(1L)).thenReturn(true);
        doNothing().when(lancheRepository).deleteById(1L);

        assertThatCode(() -> lancheService.remover(1L)).doesNotThrowAnyException();
        verify(lancheRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao remover lanche inexistente")
    void deveLancarExcecaoAoRemoverLancheInexistente() {
        when(lancheRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> lancheService.remover(99L))
                .isInstanceOf(LancheNotFoundException.class)
                .hasMessageContaining("99");
    }
}
