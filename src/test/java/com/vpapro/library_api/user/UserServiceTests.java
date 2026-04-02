package com.vpapro.library_api.user;

import com.vpapro.library_api.book.BookEntity;
import com.vpapro.library_api.book.dto.requests.UpdateBookRequestDTO;
import com.vpapro.library_api.exceptions.AvailableCopiesExceedTotalException;
import com.vpapro.library_api.exceptions.UserAlreadyExistException;
import com.vpapro.library_api.exceptions.UserNotFoundException;
import com.vpapro.library_api.user.dto.requests.CreateUserRequestDTO;
import com.vpapro.library_api.user.dto.requests.UpdateUserRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper mapper;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private UserService userService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .email("test@test.com")
                .passwordHash("password")
                .build();
    }

    @Test
    void createUser_whenUserNotExist_returnUser() {
        CreateUserRequestDTO dto = new CreateUserRequestDTO("firstName", "lastName", "test@test.com", "password");
        when(mapper.mapFromCreateUserRequestDTOToUserEntity(dto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        UserEntity result = userService.createUser(dto);

        assertThat(result.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    void createUser_whenUserExist_returnUser() {
        CreateUserRequestDTO dto = new CreateUserRequestDTO("firstName", "lastName", "test@test.com", "password");
        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(dto))
                .isInstanceOf(UserAlreadyExistException.class);
    }

    @Test
    void findCurrentUser_whenUserFound_returnUser() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@test.com");
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.ofNullable(user));

        UserEntity result = userService.findCurrentUser(authentication);

        assertThat(result.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    void findCurrentUser_whenUserNotFound_throwsException() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@test.com");

        assertThatThrownBy(() -> userService.findCurrentUser(authentication))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void findUser_whenUserFound_returnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        UserEntity result = userService.findUser(1L);

        assertThat(result.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    void findUser_whenNotUserFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findUser(1L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void updateUser_whenAllFieldsProvided_updatesAllFields() {
        UpdateUserRequestDTO dto = new UpdateUserRequestDTO("New firstName", "New lastName", "newtest@test.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserEntity result = userService.updateUser(1L, dto);

        assertThat(result.getFirstName()).isEqualTo("New firstName");
        assertThat(result.getLastName()).isEqualTo("New lastName");
        assertThat(result.getEmail()).isEqualTo("newtest@test.com");
    }

    @Test
    void updateUser_whenFirstNameFieldProvided_updatesFirstNameOnly() {
        UpdateUserRequestDTO dto = new UpdateUserRequestDTO("New firstName", null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserEntity result = userService.updateUser(1L, dto);

        assertThat(result.getFirstName()).isEqualTo("New firstName");
        assertThat(result.getLastName()).isEqualTo("lastName");
        assertThat(result.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    void updateUser_whenLastNameFieldProvided_updatesLastNameOnly() {
        UpdateUserRequestDTO dto = new UpdateUserRequestDTO(null, "New lastName", null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserEntity result = userService.updateUser(1L, dto);

        assertThat(result.getFirstName()).isEqualTo("firstName");
        assertThat(result.getLastName()).isEqualTo("New lastName");
        assertThat(result.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    void updateUser_whenEmailFieldProvided_updatesEmailOnly() {
        UpdateUserRequestDTO dto = new UpdateUserRequestDTO(null, null, "newtest@test.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserEntity result = userService.updateUser(1L, dto);

        assertThat(result.getFirstName()).isEqualTo("firstName");
        assertThat(result.getLastName()).isEqualTo("lastName");
        assertThat(result.getEmail()).isEqualTo("newtest@test.com");
    }

    @Test
    void updateUser_whenNoFieldsProvided_updatesNothing() {
        UpdateUserRequestDTO dto = new UpdateUserRequestDTO(null, null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserEntity result = userService.updateUser(1L, dto);

        assertThat(result.getFirstName()).isEqualTo("firstName");
        assertThat(result.getLastName()).isEqualTo("lastName");
        assertThat(result.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    void updateUser_whenEmailAlreadyExists_throwsException() {
        UpdateUserRequestDTO dto = new UpdateUserRequestDTO(null, null, "existing@test.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(1L, dto))
                .isInstanceOf(UserAlreadyExistException.class);
    }
}
