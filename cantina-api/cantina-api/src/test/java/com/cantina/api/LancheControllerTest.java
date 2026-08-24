package com.cantina.api;

import com.cantina.api.dto.LancheRequestDTO;
import com.cantina.api.dto.LancheResponseDTO;
import com.cantina.api.dto.LancheResumoDTO;
import com.cantina.api.exception.LancheNotFoundException;
import com.cantina.api.service.LancheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class LancheControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LancheService lancheService;

    @Autowired
    private ObjectMapper objectMapper;

    private LancheRequestDTO requestDTO;
    private LancheResponseDTO responseDTO;
    private LancheResumoDTO resumoDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new LancheRequestDTO();
        requestDTO.setNome("X-Burguer");
        requestDTO.setDescricao("Hambúrguer artesanal");
        requestDTO.setPreco(new BigDecimal("12.50"));
        requestDTO.setCategoria("Lanche");
        requestDTO.setDisponivel(true);

        responseDTO = new LancheResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNome("X-Burguer");
        responseDTO.setDescricao("Hambúrguer artesanal");
        responseDTO.setPreco(new BigDecimal("12.50"));
        responseDTO.setCategoria("Lanche");
        responseDTO.setDisponivel(true);
        responseDTO.setCriadoEm(LocalDateTime.now());
        responseDTO.setAtualizadoEm(LocalDateTime.now());

        resumoDTO = new LancheResumoDTO();
        resumoDTO.setId(1L);
        resumoDTO.setNome("X-Burguer");
        resumoDTO.setPreco(new BigDecimal("12.50"));
    }

    @Test
    @DisplayName("POST /api/lanches → deve retornar 201 ao cadastrar lanche válido")
    void deveCadastrarLancheERetornar201() throws Exception {
        when(lancheService.cadastrar(any(LancheRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/lanches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("X-Burguer"))
                .andExpect(jsonPath("$.preco").value(12.50));
    }

    @Test
    @DisplayName("POST /api/lanches → deve retornar 400 com nome vazio")
    void deveRetornar400ComNomeVazio() throws Exception {
        requestDTO.setNome("");

        mockMvc.perform(post("/api/lanches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("POST /api/lanches → deve retornar 400 com preço negativo")
    void deveRetornar400ComPrecoNegativo() throws Exception {
        requestDTO.setPreco(new BigDecimal("-5.00"));

        mockMvc.perform(post("/api/lanches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/lanches → deve retornar lista com nome e preço apenas")
    void deveListarTodosLanchesCom200() throws Exception {
        when(lancheService.listarTodos()).thenReturn(List.of(resumoDTO));

        mockMvc.perform(get("/api/lanches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("X-Burguer"))
                .andExpect(jsonPath("$[0].preco").value(12.50))
                .andExpect(jsonPath("$[0].descricao").doesNotExist());
    }

    @Test
    @DisplayName("GET /api/lanches/{id} → deve retornar 200 com lanche completo")
    void deveBuscarPorIdERetornar200() throws Exception {
        when(lancheService.buscarPorId(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/lanches/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("X-Burguer"))
                .andExpect(jsonPath("$.descricao").value("Hambúrguer artesanal"));
    }

    @Test
    @DisplayName("GET /api/lanches/{id} → deve retornar 404 quando não encontrado")
    void deveRetornar404QuandoLancheNaoExiste() throws Exception {
        when(lancheService.buscarPorId(99L)).thenThrow(new LancheNotFoundException(99L));

        mockMvc.perform(get("/api/lanches/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("PUT /api/lanches/{id} → deve retornar 200 ao atualizar")
    void deveAtualizarLancheERetornar200() throws Exception {
        when(lancheService.atualizar(eq(1L), any(LancheRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/lanches/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("X-Burguer"));
    }

    @Test
    @DisplayName("DELETE /api/lanches/{id} → deve retornar 204 ao remover")
    void deveRemoverLancheERetornar204() throws Exception {
        doNothing().when(lancheService).remover(1L);

        mockMvc.perform(delete("/api/lanches/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/lanches/{id} → deve retornar 404 quando não existe")
    void deveRetornar404AoRemoverLancheInexistente() throws Exception {
        doThrow(new LancheNotFoundException(99L)).when(lancheService).remover(99L);

        mockMvc.perform(delete("/api/lanches/99"))
                .andExpect(status().isNotFound());
    }
}
