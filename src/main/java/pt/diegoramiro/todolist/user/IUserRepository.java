package pt.diegoramiro.todolist.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IUserRepository extends JpaRepository<UserDefault, UUID> {
    UserDefault findByUsername(String username);

}
