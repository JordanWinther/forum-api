package br.com.alura.forum.controller.dto;

import javax.validation.constraints.NotBlank;

import br.com.alura.forum.modelo.Curso;

public class CursoDto {

	private Long id;
	@NotBlank()//Não deixa valores brancos ou nulos
	private String categoria;
	@NotBlank()
	private String nome;
	
	
	
	public CursoDto() {
		super();
	}
	
	public Curso  DtoToCurso() {
		
		Curso curso = new Curso(id, categoria, nome);
		return curso;
	}

	public CursoDto(Long id, String categoria, String nome) {
		super();
		this.id = id;
		this.categoria = categoria;
		this.nome = nome;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	
}

