package com.koto.web;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

	private static void enrich(final ProblemDetail pd, final HttpServletRequest req) {
		pd.setProperty("timestamp", OffsetDateTime.now());
		if (req != null) {
			pd.setProperty("path", req.getRequestURI());
		}
	}

	// 404 - Not found (ex: service.get(id) qui ne trouve pas)
	@ExceptionHandler(EntityNotFoundException.class)
	public ProblemDetail handleNotFound(final EntityNotFoundException ex, final HttpServletRequest req) {
		final ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		pd.setTitle("Resource not found");
		enrich(pd, req);
		return pd;
	}

	// 400 - Mauvaise requête métier
	@ExceptionHandler(IllegalArgumentException.class)
	public ProblemDetail handleIllegalArgument(final IllegalArgumentException ex, final HttpServletRequest req) {
		final ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
		pd.setTitle("Bad request");
		enrich(pd, req);
		return pd;
	}

	// 409 - Conflit d’unicité / FK (ex: username unique, FK violée)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ProblemDetail handleDataIntegrity(final DataIntegrityViolationException ex, final HttpServletRequest req) {
		final ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Data integrity violation");
		pd.setTitle("Conflict");
		// Astuce: ne pas exposer les détails SQL en prod
		pd.setProperty("reason", ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage());
		enrich(pd, req);
		return pd;
	}

	// 400 - JSON illisible / type incorrect
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ProblemDetail handleNotReadable(final HttpMessageNotReadableException ex, final HttpServletRequest req) {
		final ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Malformed JSON request");
		pd.setTitle("Bad request");
		enrich(pd, req);
		return pd;
	}

	// 400 - @Valid sur DTO (body)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpServletRequest req) {
		final List<Map<String, Object>> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(this::toError)
				.toList();

		final ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
		pd.setTitle("Validation error");
		pd.setProperty("errors", errors);
		enrich(pd, req);
		return pd;
	}

	// 400 - @Validated sur params/query/path (hors body)
	@ExceptionHandler(ConstraintViolationException.class)
	public ProblemDetail handleConstraintViolation(final ConstraintViolationException ex, final HttpServletRequest req) {
		final List<Map<String, Object>> errors = ex.getConstraintViolations().stream()
				.map(v -> Map.of(
						"field", v.getPropertyPath().toString(),
						"message", v.getMessage(),
						"rejectedValue", v.getInvalidValue()
				))
				.toList();

		final ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
		pd.setTitle("Validation error");
		pd.setProperty("errors", errors);
		enrich(pd, req);
		return pd;
	}

	// 4xx/5xx encapsulés dans ErrorResponseException (optionnel)
	@ExceptionHandler(ErrorResponseException.class)
	public ProblemDetail handleErrorResponse(final ErrorResponseException ex, final HttpServletRequest req) {
		final ProblemDetail pd = ex.getBody();
		enrich(pd, req);
		return pd;
	}

	// 500 - catch-all (log + message générique)
	@ExceptionHandler(Exception.class)
	public ProblemDetail handleUnexpected(final Exception ex, final HttpServletRequest req) {
		log.error("Unexpected error", ex);
		final ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
		pd.setTitle("Internal error");
		enrich(pd, req);
		return pd;
	}

	private Map<String, Object> toError(final FieldError fe) {
		return Map.of(
				"field", fe.getField(),
				"message", fe.getDefaultMessage(),
				"rejectedValue", fe.getRejectedValue(),
				"object", fe.getObjectName()
		);
	}
}
