package com.socialnetwork.weconnect.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DobValidator implements ConstraintValidator<DobConstraint, String> {

	private int min;
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		List<String> errors = new ArrayList<>();
		if (Objects.isNull(value))
			return true;
		try {
			LocalDate dob = LocalDate.parse(value, DATE_TIME_FORMATTER);
			long years = ChronoUnit.YEARS.between(dob, LocalDate.now());
			if (years < min) {
				errors.add("BIRTHDAY_INVALID");
			}
		} catch (DateTimeParseException e) {
			errors.add("DATE_FORMAT_INVALID");
		}

		if (!errors.isEmpty()) {
			context.disableDefaultConstraintViolation();
			errors.forEach(error -> context.buildConstraintViolationWithTemplate(error).addConstraintViolation());
			return false;
		}
		return true;
	}

	@Override
	public void initialize(DobConstraint constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
		min = constraintAnnotation.min();
	}
}
