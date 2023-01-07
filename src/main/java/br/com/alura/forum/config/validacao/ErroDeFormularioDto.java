package br.com.alura.forum.config.validacao;

import org.springframework.http.HttpStatus;

public class ErroDeFormularioDto {
	
	private String campo;
	private String erro;
	private HttpStatus status;
	
	public ErroDeFormularioDto(String campo, String erro) {
		this.campo = campo;
		this.erro = erro;
	}
	
	public ErroDeFormularioDto(HttpStatus httpStatus ,String campo, String erro) {
		this.campo = campo;
		this.erro = erro;
		this.status = httpStatus;
	}
	
	public ErroDeFormularioDto() {
		// TODO Auto-generated constructor stub
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getCampo() {
		return campo;
	}

	public String getErro() {
		return erro;
	}
	
	

}
