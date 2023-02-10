package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.CursoDto;
import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/cursos")
public class cursoController {
	
	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	@GetMapping
	@Cacheable("listaCurso")
	public List<Curso> lista() {
			System.err.println("Carregou lista do banco");
			List<Curso> cursos = cursoRepository.findAll();
			return cursos;
	}
	
	@GetMapping("/cancel")
	@CacheEvict("listaCurso")
	public String cancel() {
			System.err.println("Cancelou cache de listar cursos");
			
			return "Cache cancelado";
	}
	
	@GetMapping("/listar-cursos")
	public Set<Curso> listarSet(){
		
			Set<Curso> listarTodos = cursoRepository.listarCurso();
			
			 return listarTodos;
		
		
	}
	
	
	
	@PostMapping("/cadastrar")
	@Transactional
	@CacheEvict(value = "listaCurso",allEntries = true)
	public ResponseEntity<Curso> cadastrar(@RequestBody @Valid CursoDto dto, UriComponentsBuilder uriBuilder) {
		
		Curso cursoEntity = dto.DtoToCurso();
		cursoEntity = cursoRepository.save(cursoEntity);
		URI uri = uriBuilder.path("/cursos/cadastrar/{id}").buildAndExpand(cursoEntity.getId()).toUri();
		return ResponseEntity.created(uri).body(cursoEntity);
		//return new ResponseEntity<Curso>(cursoM, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);
		if (topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}

}







