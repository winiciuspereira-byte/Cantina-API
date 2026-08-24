package com.cantina.api.repository;

import com.cantina.api.model.Lanche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LancheRepository extends JpaRepository<Lanche, Long> {

    List<Lanche> findByDisponivel(Boolean disponivel);

    List<Lanche> findByCategoriaIgnoreCase(String categoria);

    boolean existsByNomeIgnoreCase(String nome);
}
