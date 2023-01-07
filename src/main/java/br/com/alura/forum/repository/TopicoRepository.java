package br.com.alura.forum.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.alura.forum.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

	List<Topico> findByCursoNome(String nomeCurso);
	
	@Query("SELECT c FROM Topico c WHERE c.curso.nome = :nomeCurso")
	Set<Topico> listarTopicosPorCurso(@Param("nomeCurso") String nomeCurso);
	
	@Query("SELECT c FROM Topico c WHERE c.curso.nome = :nomeCurso")
	Page<Topico> PageTopicosPorCurso(@Param("nomeCurso") String nomeCurso, Pageable pageable);

}
