package br.com.beatrizcarmo.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.beatrizcarmo.dto.UserDto;
import br.com.beatrizcarmo.models.User;

@Service
public class UserMapper {

	public UserDto toDto(User user) {
		final UserDto dto = new UserDto();
		
		dto.id = user.getId().toString();
		dto.name = user.getName();
		dto.username = user.getUsername();
		dto.password = user.getPassword();
		dto.isPunished = user.getIsPunished();
		
		return dto;
	}
	
	public List<UserDto> toDto(List<User> users){
		final ArrayList<UserDto> usersDto = new ArrayList<>();
		for (User user : users) {
			usersDto.add(toDto(user));
		}
		return usersDto;
	}
	
	public User toEntity(UserDto dto) {
		final User user = new User();
		
		user.setId(UUID.fromString(dto.id));
		user.setName(dto.name);
		user.setUsername(dto.username);
		user.setPassword(dto.password);
		user.setIsPunished(dto.isPunished);
		
		return user;
	}
}
