package pt.diegoramiro.todolist.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ITaskRepository extends JpaRepository<TaskDefault, UUID> {
    List<TaskDefault> findByIdUser(UUID idUser);
}
