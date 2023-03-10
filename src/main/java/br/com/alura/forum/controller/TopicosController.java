package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	@Autowired
	CacheManager cacheManager;
	
	@GetMapping("/topicos-curso")
	@Cacheable(value = "listaDeTopicos")
	public List<TopicoDto> lista(String nomeCurso) {
		if (nomeCurso == null) {
			
			List<Topico> topicos = topicoRepository.findAll();
			System.err.println("Chamou do banco");
			return TopicoDto.converter(topicos);
			
		} else {
			List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
			return TopicoDto.converter(topicos);
		}
	}
	
	@GetMapping("/listar-topicos-page")
	public Page<TopicoDto> listarTopicosPage( @RequestParam(required = false) String nomeCurso, 
											  @RequestParam int pagina,
											  @RequestParam int qtd,
											  @RequestParam(required = false) String ordenacao){
		Pageable pageable = PageRequest.of(pagina, qtd, Direction.DESC, ordenacao);
		//Forma mais manual, onde ?? necessario que o cliente, mande todos os parametros para a paginacao
		if (nomeCurso == null) {
			Page<Topico> listarTodos = topicoRepository.findAll(pageable);
			Page<TopicoDto> responseTopicos = TopicoDto.pageToPageDto(listarTodos);
			 return responseTopicos;
		}
		
		Page<Topico> cursosPorTopico = topicoRepository.PageTopicosPorCurso(nomeCurso, pageable);
		return TopicoDto.pageToPageDto(cursosPorTopico);
	}
	
	@GetMapping("/listar-topicos-page-melhor-forma")
	@Cacheable(value = "pageDeTopicos")
	public Page<TopicoDto> listarTopicosPageMelhorForma(@RequestParam(required = false) String nomeCurso,
			@PageableDefault(direction = Direction.DESC,page = 0,size = 5,sort = "id") Pageable pageable){
			//Caso os cliente n??o envie informacoes referentes a paginacao, o pagebleDeafult se encarrega disso.
		if (nomeCurso == null) {
			Page<Topico> listarTodos = topicoRepository.findAll(pageable);
			Page<TopicoDto> responseTopicos = TopicoDto.pageToPageDto(listarTodos);
			 return responseTopicos;
		}
		
		Page<Topico> cursosPorTopico = topicoRepository.PageTopicosPorCurso(nomeCurso, pageable);
		return TopicoDto.pageToPageDto(cursosPorTopico);
	}
	
	@GetMapping("/listar-topicos-curso")
	public Set<TopicoDto> listarSet(@RequestParam String nomeCurso){
		System.out.println(nomeCurso);
		if (nomeCurso == null) {
			List<Topico> listarTodos = topicoRepository.findAll();
			Set<TopicoDto> setTopicos = TopicoDto.converterToSet(listarTodos);
			 return setTopicos;
		}else {
			Set<Topico> cursosPorNome = topicoRepository.listarTopicosPorCurso(nomeCurso);
			Set<TopicoDto> setTopicos = TopicoDto.converter(cursosPorNome);
			return setTopicos;
			
		}
		
	}
	
	
	
	@PostMapping
	@Transactional
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		//apaga o cache atribuido na lista
		List<String> listCache = Arrays.asList("pageDeTopicos","listaDeTopicos");
		listCache.forEach(cache -> cacheManager.getCache(cache).clear());
		
		
		
		
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
		
	}
	
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	private void limpaCacheListaDeTopicos() {}
	@CacheEvict(value = "pageDeTopicos",allEntries = true)
	private void limpaCachePageDeTopicos() {}

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







