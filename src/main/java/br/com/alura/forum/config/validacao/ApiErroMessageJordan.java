package br.com.alura.forum.config.validacao;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;



public class ApiErroMessageJordan {

	
	private HttpStatus httpStatus;
	private List<String> campos;
	private List<String> erros;
	
	
	public ApiErroMessageJordan( ) {
		
	}

	public ApiErroMessageJordan(HttpStatus httpStatus, List<String> campos, List<String> erros) {
		super();
		this.httpStatus = httpStatus;
		this.erros = erros;
		this.campos = campos;
	}
	
	public ApiErroMessageJordan(HttpStatus httpStatus, String campo, String erro) {
		super();
		this.httpStatus = httpStatus;
		this.erros = Arrays.asList(erro);
		this.campos = Arrays.asList(campo);
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public List<String> getErros() {
		return erros;
	}

	public void setErros(List<String> erros) {
		this.erros = erros;
	}
	
	public void setcampos(List<String> campos) {
		this.campos = campos;
	}
	public List<String> getcampos() {
		return campos;
	}

	
	
	
	
	
}
