package br.com.alura.forum.config.validacao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiErroMessageHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
			List<ApiErroMessageJordan> listErros = new ArrayList<>();
			
			
			List<FieldError> erros = ex.getBindingResult().getFieldErrors();					
			erros.forEach(e -> {
				String messagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
				ApiErroMessageJordan apiErroMessageJordan = new ApiErroMessageJordan(status, e.getField(), messagem);
				listErros.add(apiErroMessageJordan);
			});
			
		return new ResponseEntity<>(listErros, status);
	}

//	@Override
//	protected ResponseEntity<Object> handleMethodArgumentNotValid(
//			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//			
//			List<String> erros = ex.getBindingResult().getAllErrors()
//					.stream()
//					.map(e -> e.getDefaultMessage())
//					.collect(Collectors.toList());
//			
//			List<String> campos = ex.getBindingResult().getFieldErrors()
//					.stream()
//					.map(e -> e.getField())
//					.collect(Collectors.toList());
//
//			
//			
//		ApiErroMessageJordan apiErroMessageJordan = new ApiErroMessageJordan(status, campos,  erros);
//		
//		return new ResponseEntity<>(apiErroMessageJordan, status);
//	}
}
