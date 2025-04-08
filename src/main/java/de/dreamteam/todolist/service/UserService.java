package de.dreamteam.todolist.service;

import de.dreamteam.todolist.controller.payload.NewUserPayload;
import de.dreamteam.todolist.controller.payload.UpdateUserPayload;
import de.dreamteam.todolist.entity.User;
import de.dreamteam.todolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

//    private final PasswordEncoder passwordEncoder;

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    public User getUserById(Long id) {
//        return userRepository.findById(id).orElse(null);
//    }

    public void saveUser(NewUserPayload payload) {
        User user = User.builder()
                .firstName(payload.firstName())
                .lastName(payload.lastName())
                .username(payload.username())
                .email(payload.email())
                .password(passwordEncoder().encode(payload.password()))
                .build();

        userRepository.save(user);
    }

    public void updateUser(UpdateUserPayload payload) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(currentUsername);
        User existingUser = userRepository.findUserByUsername(currentUsername);
//        if (existingUser == null) {
//            return;
//        }

        existingUser.setFirstName(payload.firstName());
        existingUser.setLastName(payload.lastName());
        existingUser.setEmail(payload.email());
        existingUser.setUsername(payload.username());

        if (payload.password() != null && !payload.password().isEmpty()) {
            existingUser.setPassword(passwordEncoder().encode(payload.password()));
        }

        userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

//    public User findUserByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }
}
