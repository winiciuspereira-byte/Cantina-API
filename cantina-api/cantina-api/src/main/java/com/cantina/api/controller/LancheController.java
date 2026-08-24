package com.cantina.api.controller;

import com.cantina.api.dto.LancheRequestDTO;
import com.cantina.api.dto.LancheResponseDTO;
import com.cantina.api.dto.LancheResumoDTO;
import com.cantina.api.service.LancheService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lanches")
public class LancheController {

    private final LancheService lancheService;

    @Autowired
    public LancheController(LancheService lancheService) {
        this.lancheService = lancheService;
    }

    // POST /api/lanches → Cadastrar lanche (201 Created)
    @PostMapping
    public ResponseEntity<LancheResponseDTO> cadastrar(@Valid @RequestBody LancheRequestDTO requestDTO) {
        LancheResponseDTO lancheCriado = lancheService.cadastrar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(lancheCriado);
    }

    // GET /api/lanches → Listar todos (resumo: nome + preço)
    @GetMapping
    public ResponseEntity<List<LancheResumoDTO>> listarTodos() {
        List<LancheResumoDTO> lanches = lancheService.listarTodos();
        return ResponseEntity.ok(lanches);
    }

    // GET /api/lanches/{id} → Consultar lanche por ID (dados completos)
    @GetMapping("/{id}")
    public ResponseEntity<LancheResponseDTO> buscarPorId(@PathVariable Long id) {
        LancheResponseDTO lanche = lancheService.buscarPorId(id);
        return ResponseEntity.ok(lanche);
    }

    // PUT /api/lanches/{id} → Atualizar lanche
    @PutMapping("/{id}")
    public ResponseEntity<LancheResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LancheRequestDTO requestDTO) {
        LancheResponseDTO lancheAtualizado = lancheService.atualizar(id, requestDTO);
        return ResponseEntity.ok(lancheAtualizado);
    }

    // DELETE /api/lanches/{id} → Remover lanche (204 No Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        lancheService.remover(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/lanches/disponiveis → Listar lanches disponíveis (bônus)
    @GetMapping("/disponiveis")
    public ResponseEntity<List<LancheResponseDTO>> listarDisponiveis() {
        List<LancheResponseDTO> lanches = lancheService.listarDisponiveis();
        return ResponseEntity.ok(lanches);
    }

    // GET /api/lanches/categoria/{categoria} → Listar por categoria (bônus)
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<LancheResponseDTO>> listarPorCategoria(@PathVariable String categoria) {
        List<LancheResponseDTO> lanches = lancheService.listarPorCategoria(categoria);
        return ResponseEntity.ok(lanches);
    }
}
