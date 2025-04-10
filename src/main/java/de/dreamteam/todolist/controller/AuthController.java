package de.dreamteam.todolist.controller;

import de.dreamteam.todolist.controller.payload.LoginPayload;
import de.dreamteam.todolist.entity.User;
import de.dreamteam.todolist.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("todo-list-api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final SecurityContextRepository securityContextRepository;
    private final MessageSource messageSource;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginPayload payload, Locale locale,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(payload.username(), payload.password());

        Authentication authentication = authenticationManager.authenticate(authRequest);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        securityContextRepository.saveContext(context, request, response);

        User user = userService.findUserByUsername(payload.username());

        Map<String, Object> resp = new HashMap<>();
        resp.put("username", user.getUsername());
        resp.put("message", messageSource.getMessage("auth.login.info.login_is_successful",
                null, locale));

        return ResponseEntity.ok(resp);
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser(Locale locale) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getName())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", messageSource.getMessage("auth.current.errors.user_not_authenticated",
                    null, locale));
            return ResponseEntity.status(401).body(errorResponse);
        }

        User user = userService.findUserByUsername(authentication.getName());

        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("error", messageSource.getMessage(
                    "auth.current.errors.user_not_found", null, locale)));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("firstName", user.getFirstName());
        response.put("lastName", user.getLastName());
        response.put("email", user.getEmail());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, Locale locale) {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(null);
        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", messageSource.getMessage(
                "auth.logout.info.logout_is_successful", null, locale));

        return ResponseEntity.ok(responseBody);
    }
}
