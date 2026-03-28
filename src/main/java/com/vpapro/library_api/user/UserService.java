package com.vpapro.library_api.user;

import com.vpapro.library_api.exceptions.UserAlreadyExistException;
import com.vpapro.library_api.user.dto.requests.CreateUserRequestDTO;
import com.vpapro.library_api.exceptions.UserNotFoundException;
import com.vpapro.library_api.user.dto.requests.UpdateUserRequestDTO;
import com.vpapro.library_api.user.dto.responses.UserResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private UserMapper mapper;

    public UserEntity createUser(final CreateUserRequestDTO createUserRequestDTO) {
        if (userRepository.existsByEmail((createUserRequestDTO.email()))) {
            throw new UserAlreadyExistException();
        }

        return userRepository.save(mapper.mapFromCreateUserRequestDTOToUserEntity(createUserRequestDTO));
    }

    public UserEntity findCurrentUser(final Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);
    }

    public UserEntity updateCurrentUser(final Authentication authentication, final UpdateUserRequestDTO updateUserRequestDTO) {
        UserEntity userFromDatabase = findCurrentUser(authentication);

        return updateUser(userFromDatabase.getId(), updateUserRequestDTO);
    }

    public void deleteCurrentUser(final Authentication authentication) {
        UserEntity user = findCurrentUser(authentication);
        userRepository.delete(user);
    }

    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity findUser(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public UserEntity updateUser(final Long id, final UpdateUserRequestDTO updateUserRequestDTO) {
        UserEntity userFromDatabase = findUser(id);

        if (updateUserRequestDTO.email() != null) {
            if (userRepository.existsByEmail((updateUserRequestDTO.email()))) {
                throw new UserAlreadyExistException();
            }
            userFromDatabase.setEmail(updateUserRequestDTO.email());
        }

        if (updateUserRequestDTO.firstName() != null) {
            userFromDatabase.setFirstName(updateUserRequestDTO.firstName());
        }

        if (updateUserRequestDTO.lastName() != null) {
            userFromDatabase.setLastName(updateUserRequestDTO.lastName());
        }

        return userRepository.save(userFromDatabase);
    }

    public void deleteUser(final Long id) {
        UserEntity user = findUser(id);
        userRepository.delete(user);
    }
}

