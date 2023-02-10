package br.com.alura.forum.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;

public interface CursoRepository extends JpaRepository<Curso, Long> {

	Curso findByNome(String nome);
	
	@Query("SELECT c FROM Curso c")
	Set<Curso> listarCurso();

}
