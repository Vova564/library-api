package com.vpapro.library_api.security;

import com.vpapro.library_api.exceptions.UserNotFoundException;
import com.vpapro.library_api.user.UserEntity;
import com.vpapro.library_api.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .email("test@test.com")
                .build();
    }

    @Test
    void loadUserByUsername_whenUserExist_returnCorrectUser() {
        String email = "test@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.ofNullable(user));

        UserDetails result = customUserDetailsService.loadUserByUsername(email);
        assertThat(result.getUsername()).isEqualTo(email);
    }

    @Test
    void loadUserByUsername_whenUserNotExist_throwsException() {
        String email = "test@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(email))
                .isInstanceOf(UserNotFoundException.class);
    }
}
