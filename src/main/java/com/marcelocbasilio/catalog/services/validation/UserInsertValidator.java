package com.marcelocbasilio.catalog.services.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.marcelocbasilio.catalog.controllers.exceptions.FieldMessage;
import com.marcelocbasilio.catalog.dtos.UserInsertDto;
import com.marcelocbasilio.catalog.entities.User;
import com.marcelocbasilio.catalog.repositories.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDto> {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void initialize(UserInsertValid ann) {
	}

	@Override
	public boolean isValid(UserInsertDto dto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		User user = userRepository.findByEmail(dto.getEmail());
		if (user != null) {
			list.add(new FieldMessage("email", "E-mail já existe!"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}

}
