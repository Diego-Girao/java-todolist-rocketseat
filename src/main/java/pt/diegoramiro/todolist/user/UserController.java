package pt.diegoramiro.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserDefault userDefault) {
        var user = this.userRepository.findByUsername(userDefault.getUsername());
        if (user != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe!");
        }

        var passwordHashed = BCrypt.withDefaults().hashToString(12, userDefault.getPassword().toCharArray());

        userDefault.setPassword(passwordHashed);

        var userCreated = this.userRepository.save(userDefault);
//        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso!");
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}