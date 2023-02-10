package br.com.alura.forum.controller.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import br.com.alura.forum.modelo.Curso;

public class CursoDto implements Serializable {

	private Long id;
	@NotBlank()
	private String nome;
	@NotBlank()//Não deixa valores brancos ou nulos
	private String categoria;
	
	
	
	
	public CursoDto() {
		super();
	}
	
	public Curso  DtoToCurso() {
		
		Curso curso = new Curso(id, nome, categoria);
		return curso;
	}

	public CursoDto(Long id,  String nome, String categoria) {
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

