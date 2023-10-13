package pt.diegoramiro.todolist.task;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.diegoramiro.todolist.utils.Utils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskDefault taskDefault, HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        taskDefault.setIdUser((UUID) idUser);

        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskDefault.getStartAt()) || currentDate.isAfter(taskDefault.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data inicial/data final deve ser maior que a data atual!");
        }
        if (taskDefault.getStartAt().isAfter(taskDefault.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data inicial deve ser menor que a data final!");
        }

        var task = this.taskRepository.save(taskDefault);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/")
    public List<TaskDefault> list(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskDefault taskDefault, @PathVariable UUID id, HttpServletRequest request) {
        var task = this.taskRepository.findById(id).orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada!");
        }

        var idUser = request.getAttribute("idUser");

        if (!task.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão para alterar esta tarefa!");
        }

        Utils.copyNonNullProperties(taskDefault, task);

        var taskUpdated = this.taskRepository.save(task);
        return ResponseEntity.ok().body(taskUpdated);
    }
}
