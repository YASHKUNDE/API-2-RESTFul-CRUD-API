package in.api_2;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class Login_API_Controller {

    private final Login_API_Repository repo;
    private final BCryptPasswordEncoder passwordEncoder;

    // Constructor Injection
    public Login_API_Controller(Login_API_Repository repo) {
        this.repo = repo;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    // ─────────────────────────────────────────────
    // VIEW ALL USERS
    // ─────────────────────────────────────────────
    @GetMapping("/view_users")
    public ResponseEntity<Login_Response> findAllUsers() {
        List<Login_API> users = repo.findAll();

        if (users.isEmpty()) {
            return ResponseEntity.status(404).body(
                new Login_Response("404", "No users found", null)
            );
        }

        return ResponseEntity.ok(
            new Login_Response("200", "Users found successfully", users)
        );
    }


    // ─────────────────────────────────────────────
    // ADD USER
    // ─────────────────────────────────────────────
    @PostMapping("/add_users")
    public ResponseEntity<Login_Response> addUsers(
            @Valid @RequestBody Login_API s,
            BindingResult bindingResult) {

        // Validation errors
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .reduce("", (a, b) -> a + b + "; ");
            return ResponseEntity.status(400).body(
                new Login_Response("400", "Validation failed: " + errorMsg, null)
            );
        }

        try {
            // Duplicate checks
            boolean userNameExists = repo.existsByUserName(s.getUserName());
            boolean emailExists    = repo.existsByEmail(s.getEmail());
            boolean mobileExists   = repo.existsByMobile(s.getMobile());

            if (userNameExists || emailExists || mobileExists) {
                String message = "User already exists. ";
                if (userNameExists && emailExists && mobileExists) {
                    message += "Username, Email and Mobile already exist.";
                } else if (userNameExists && emailExists) {
                    message += "Username and Email already exist.";
                } else if (userNameExists && mobileExists) {
                    message += "Username and Mobile already exist.";
                } else if (emailExists && mobileExists) {
                    message += "Email and Mobile already exist.";
                } else if (userNameExists) {
                    message += "Try another username.";
                } else if (emailExists) {
                    message += "Try another email.";
                } else {
                    message += "Try another mobile number.";
                }
                return ResponseEntity.status(400).body(
                    new Login_Response("400", message, null)
                );
            }

            // Hash password before saving
            s.setPass(passwordEncoder.encode(s.getPass()));

            Login_API savedUser = repo.save(s);
            return ResponseEntity.status(201).body(
                new Login_Response("201", "User saved successfully", List.of(savedUser))
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                new Login_Response("500", "Server error: " + e.getMessage(), null)
            );
        }
    }


    // ─────────────────────────────────────────────
    // UPDATE USER
    // ─────────────────────────────────────────────
    @PutMapping("/update_users")
    public ResponseEntity<Login_Response> updateUser(
            @Valid @RequestBody Login_API s,
            BindingResult bindingResult) {

        // Validation errors
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .reduce("", (a, b) -> a + b + "; ");
            return ResponseEntity.status(400).body(
                new Login_Response("400", "Validation failed: " + errorMsg, null)
            );
        }

        try {
            // ID check
            if (s.getId() == null) {
                return ResponseEntity.badRequest().body(
                    new Login_Response("400", "User ID must not be null for update.", null)
                );
            }

            // Existence check
            Optional<Login_API> existing = repo.findById(s.getId());
            if (existing.isEmpty()) {
                return ResponseEntity.status(404).body(
                    new Login_Response("404", "User with ID " + s.getId() + " not found.", null)
                );
            }

            // Duplicate checks 
            Login_API current = existing.get();

            boolean userNameExists = repo.existsByUserName(s.getUserName())
                && !current.getUserName().equals(s.getUserName());
            boolean emailExists = repo.existsByEmail(s.getEmail())
                && !current.getEmail().equals(s.getEmail());
            boolean mobileExists = repo.existsByMobile(s.getMobile())
                && !current.getMobile().equals(s.getMobile());

            if (userNameExists || emailExists || mobileExists) {
                String message = "Update conflict. ";
                if (userNameExists) message += "Username already taken. ";
                if (emailExists)    message += "Email already taken. ";
                if (mobileExists)   message += "Mobile already taken. ";
                return ResponseEntity.status(400).body(
                    new Login_Response("400", message.trim(), null)
                );
            }

            // Re-hash password only if it was changed
            if (!s.getPass().equals(current.getPass())) {
                s.setPass(passwordEncoder.encode(s.getPass()));
            }

            Login_API updated = repo.save(s);
            return ResponseEntity.ok(
                new Login_Response("200", "User updated successfully", List.of(updated))
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                new Login_Response("500", "User update failed: " + e.getMessage(), null)
            );
        }
    }


    // ─────────────────────────────────────────────
    // DELETE USER
    // ─────────────────────────────────────────────
    @DeleteMapping("/delete_users")
    public ResponseEntity<Login_Response> deleteStudent(
            @RequestBody Map<String, Integer> payload) {

        try {
            if (payload == null || !payload.containsKey("id")) {
                return ResponseEntity.badRequest().body(
                    new Login_Response("400", "Missing 'id' in request body.", null)
                );
            }

            int id = payload.get("id");

            Optional<Login_API> existingUser = repo.findById(id);
            if (existingUser.isEmpty()) {
                return ResponseEntity.status(404).body(
                    new Login_Response("404", "User with ID " + id + " not found.", null)
                );
            }

            repo.deleteById(id);

            return ResponseEntity.ok(
                new Login_Response("200", "User deleted successfully", List.of(existingUser.get()))
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                new Login_Response("500", "User delete failed: " + e.getMessage(), null)
            );
        }
    }


    // ─────────────────────────────────────────────
    // LOGIN (via Email OR Username)
    // ─────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<Login_Response> login(@RequestBody Login_Request request) {

        try {
            Optional<Login_API> foundUser = Optional.empty();
            String loginType = "";

            // Try email first
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                foundUser = repo.findByEmail(request.getEmail().trim());
                loginType = "email";
            }

            // Fallback to username
            if (foundUser.isEmpty()
                    && request.getUserName() != null
                    && !request.getUserName().trim().isEmpty()) {
                foundUser = repo.findByUserName(request.getUserName().trim());
                loginType = "username";
            }

            if (foundUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Login_Response("400", "Invalid email/username", null)
                );
            }

            Login_API user = foundUser.get();

            // BCrypt password check
            if (!passwordEncoder.matches(request.getPass(), user.getPass())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Login_Response("400", "Invalid password", null)
                );
            }

            return ResponseEntity.ok(
                new Login_Response("200", "Login successful via " + loginType, List.of(user))
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new Login_Response("500", "Server error: " + e.getMessage(), null)
            );
        }
    }


    // ─────────────────────────────────────────────
    // GET USER BY MOBILE
    // ─────────────────────────────────────────────
    @PostMapping("/mobileid")
    public ResponseEntity<Login_Response> getUserByMobile(
            @Valid @RequestBody Login_Request request,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .reduce("", (a, b) -> a + b + "; ");
            return ResponseEntity.status(400).body(
                new Login_Response("400", "Validation failed: " + errorMsg, null)
            );
        }

        try {
            // Validate mobile number format
            long mobileNumber = Long.parseLong(request.getId());
            String mobileStr = String.valueOf(mobileNumber);

            List<Login_API> result = repo.findByMobile(mobileStr);

            if (!result.isEmpty()) {
                return ResponseEntity.ok(
                    new Login_Response("200", "User found", result)
                );
            } else {
                
                return ResponseEntity.status(400).body(
                    new Login_Response("400", "No user found with this mobile number", null)
                );
            }

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body(
                new Login_Response("400", "Invalid mobile number format", null)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                new Login_Response("500", "Internal server error: " + e.getMessage(), null)
            );
        }
    }
}