package com.marcelocbasilio.catalog.services;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.marcelocbasilio.catalog.dtos.RoleDto;
import com.marcelocbasilio.catalog.dtos.UserDto;
import com.marcelocbasilio.catalog.dtos.UserInsertDto;
import com.marcelocbasilio.catalog.dtos.UserUpdateDto;
import com.marcelocbasilio.catalog.entities.Role;
import com.marcelocbasilio.catalog.entities.User;
import com.marcelocbasilio.catalog.repositories.RoleRepository;
import com.marcelocbasilio.catalog.repositories.UserRepository;
import com.marcelocbasilio.catalog.services.exceptions.DatabaseException;
import com.marcelocbasilio.catalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, RoleRepository roleRepository,
			BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(readOnly = true)
	public Page<UserDto> findAllPaged(Pageable pageable) {
		Page<User> users = userRepository.findAll(pageable);
		return users.map(UserDto::new);
	}

	@Transactional(readOnly = true)
	public UserDto findById(Long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		User user = optionalUser.orElseThrow(() -> new ResourceNotFoundException("[fbi] Entity not found."));
		return new UserDto(user);
	}

	@Transactional
	public UserDto insert(UserInsertDto userInsertDto) {
		User user = new User();
		copyDtoToEntity(userInsertDto, user);
		user.setPassword(passwordEncoder.encode(userInsertDto.getPassword()));
		user = userRepository.save(user);
		return new UserDto(user);
	}

	@Transactional
	public UserDto update(Long id, UserUpdateDto userDto) {
		try {
			User user = userRepository.getReferenceById(id);
			copyDtoToEntity(userDto, user);
			user = userRepository.save(user);
			return new UserDto(user);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("[upd] Id not found " + id);
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!userRepository.existsById(id)) {
			throw new ResourceNotFoundException("[dlt1] Resource not found " + id);
		}
		try {
			userRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("[dlt2] Referential Integrity Failure");
		}
	}

	private void copyDtoToEntity(UserDto userDto, User user) {
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());

		user.getRoles().clear();
		for (RoleDto roleDto : userDto.getRoles()) {
			Role role = roleRepository.getReferenceById(roleDto.getId());
			user.getRoles().add(role);
		}
	}

}
