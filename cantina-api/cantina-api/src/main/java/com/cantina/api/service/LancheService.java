package com.cantina.api.service;

import com.cantina.api.dto.LancheRequestDTO;
import com.cantina.api.dto.LancheResponseDTO;
import com.cantina.api.dto.LancheResumoDTO;
import com.cantina.api.exception.LancheNotFoundException;
import com.cantina.api.model.Lanche;
import com.cantina.api.repository.LancheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LancheService {

    private final LancheRepository lancheRepository;

    @Autowired
    public LancheService(LancheRepository lancheRepository) {
        this.lancheRepository = lancheRepository;
    }

    public LancheResponseDTO cadastrar(LancheRequestDTO requestDTO) {
        Lanche lanche = converterParaEntidade(requestDTO);
        Lanche lancheSalvo = lancheRepository.save(lanche);
        return new LancheResponseDTO(lancheSalvo);
    }

    public List<LancheResumoDTO> listarTodos() {
        return lancheRepository.findAll()
                .stream()
                .map(LancheResumoDTO::new)
                .toList();
    }

    public LancheResponseDTO buscarPorId(Long id) {
        Lanche lanche = lancheRepository.findById(id)
                .orElseThrow(() -> new LancheNotFoundException(id));
        return new LancheResponseDTO(lanche);
    }

    public LancheResponseDTO atualizar(Long id, LancheRequestDTO requestDTO) {
        Lanche lanche = lancheRepository.findById(id)
                .orElseThrow(() -> new LancheNotFoundException(id));

        lanche.setNome(requestDTO.getNome());
        lanche.setDescricao(requestDTO.getDescricao());
        lanche.setPreco(requestDTO.getPreco());
        lanche.setCategoria(requestDTO.getCategoria());
        lanche.setDisponivel(requestDTO.getDisponivel() != null ? requestDTO.getDisponivel() : true);

        Lanche lancheAtualizado = lancheRepository.save(lanche);
        return new LancheResponseDTO(lancheAtualizado);
    }

    public void remover(Long id) {
        if (!lancheRepository.existsById(id)) {
            throw new LancheNotFoundException(id);
        }
        lancheRepository.deleteById(id);
    }

    public List<LancheResponseDTO> listarDisponiveis() {
        return lancheRepository.findByDisponivel(true)
                .stream()
                .map(LancheResponseDTO::new)
                .toList();
    }

    public List<LancheResponseDTO> listarPorCategoria(String categoria) {
        return lancheRepository.findByCategoriaIgnoreCase(categoria)
                .stream()
                .map(LancheResponseDTO::new)
                .toList();
    }

    private Lanche converterParaEntidade(LancheRequestDTO dto) {
        Lanche lanche = new Lanche();
        lanche.setNome(dto.getNome());
        lanche.setDescricao(dto.getDescricao());
        lanche.setPreco(dto.getPreco());
        lanche.setCategoria(dto.getCategoria());
        lanche.setDisponivel(dto.getDisponivel() != null ? dto.getDisponivel() : true);
        return lanche;
    }
}
