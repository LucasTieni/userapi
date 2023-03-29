package com.payconomy.userapi.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.payconomy.userapi.domain.exception.BusinessException;
import com.payconomy.userapi.domain.exception.EntityNotFoundException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{
	
	@Autowired
	private MessageSource messageSource;
	
	private static final String GENERIC_ERROR_MSG_END_USER = "An unexpected internal error has occurred in the system. "
			+ "Try again and if the problem persists, contact your system administrator.";
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
	        HttpHeaders headers, HttpStatus status, WebRequest request) {
		
	    String detail = "One or more field are invalid. Fill them up correctly and try again.";
		
	    BindingResult bidingResult = ex.getBindingResult();
	    
	    List<Problem.Object> problemObjects = bidingResult.getAllErrors().stream()
	    		.map(objectError -> {	    		
	    		String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
	    		
	    		String name = objectError.getObjectName();
	    		
	    		if (objectError instanceof FieldError) {
	    			name = ((FieldError) objectError).getField(); 
	    		}
	    		
	    		return Problem.Object.builder()
		    		.name(name)
		    		.userMessage(message)
		    		.build();
		    		})
	    		.collect(Collectors.toList());
	    
		Problem problem = createProblemBuilder(HttpStatus.UNPROCESSABLE_ENTITY, ProblemType.DATA_INVALID, detail)
				.userMessage(detail)
				.objects(problemObjects)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, 
			WebRequest request) {
		
	    HttpStatus status = HttpStatus.CONFLICT;
	    ProblemType problemType = ProblemType.DATA_CONFLICT;
	    String detail = ex.getMessage();

	    Problem problem = createProblemBuilder(status, problemType, detail)
	            .userMessage(detail)
	            .userMessage("Data conflict.")
	            .build();

	    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		Throwable rootCause = ExceptionUtils.getRootCause(ex);
		
		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
		}
		
		ProblemType problemType = ProblemType.MESSAGE_INCOMPREHENSIBLE;
		String detail = "Request body invalid. Verify it and try again";	
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage( GENERIC_ERROR_MSG_END_USER)			
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, 
			HttpHeaders headers, HttpStatus status, WebRequest request){
		
		String path = joinPath(ex.getPath());
		
		ProblemType problemType = ProblemType.MESSAGE_INCOMPREHENSIBLE;
		String detail = String.format("The propertie '%s' received the value '%s', " 
				+ "which has an invalid type.  Fix it and enter a value compatible with the type %s", 
				path, ex.getValue(), ex.getTargetType().getSimpleName());
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage( GENERIC_ERROR_MSG_END_USER)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, 
			HttpHeaders headers, HttpStatus status, WebRequest request){
		
		String path = joinPath(ex.getPath());
		
		ProblemType problemType = ProblemType.MESSAGE_INCOMPREHENSIBLE;
		
	    String detail = String.format("The propertie '%s' does not exist. "
	            + "Fix it or remove this propertie and try again.", path);
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage( GENERIC_ERROR_MSG_END_USER)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private String joinPath(List<Reference> references) {
		return references.stream()
				.map(ref -> ref.getFieldName())
				.collect(Collectors.joining("."));
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<?> handleFigureNaoEncontrado(EntityNotFoundException e,
			WebRequest request){
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.RESOURCE_NOT_FOUND;
		String detail = e.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(GENERIC_ERROR_MSG_END_USER)
				.build();
		
		return handleExceptionInternal(e, problem, new HttpHeaders(), 
				status, request);
	}
	
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handleNegocioException(BusinessException e,
			WebRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.BUSINESS_ERROR;
		String detail = e.getMessage();	
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(GENERIC_ERROR_MSG_END_USER)
				.build();
		
		return handleExceptionInternal(e, problem, new HttpHeaders(), 
				status, request);	
	}
	
	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status,
			ProblemType problemType, String deital) {
		
		return Problem.builder()
				.timestamp(OffsetDateTime.now())
				.status(status.value())
				.type(problemType.getUri())
				.title(problemType.getTitle())
				.detail(deital);
	}
}

