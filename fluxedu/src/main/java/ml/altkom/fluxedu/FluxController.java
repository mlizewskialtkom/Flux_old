package ml.altkom.fluxedu;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/flux")
public class FluxController {

    private final StudentRepo studentRepo;

    public FluxController(StudentRepo studentRepo) {
        this.studentRepo = studentRepo;
    }

    @PostConstruct
    public void postC(){
      log.info("Initialized controller");
    }

    @GetMapping("/all")
    public Flux<Student> all(){
        return studentRepo.findAll();
    }

    @GetMapping("/ids")
    public Mono<List<String>> ids(){
        return studentRepo.findAll().map(Student::getId).collectList();
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE, path = "/all-json")
    public Flux<Student> allJson(){
        return studentRepo.findAll().delayElements(Duration.ofSeconds(1));
    }

    @GetMapping("/one/{id}")
    public Mono<Student> getStudent(@PathVariable String id) throws InterruptedException {
        long leftLimit = 500L;
        long rightLimit = 3000L;
        long generatedLong = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        Thread.sleep(generatedLong);
        return studentRepo.findById(id).log();
    }

    // zapisz Nowego studenta do bazy
    @GetMapping("/add/{name}")
    public Mono<Student> addStudent(@PathVariable String name) throws InterruptedException {
        Student student = new Student(name, new Random().nextInt(100));
        return studentRepo.save(student).log();
    }

//    //pobierz studenta po polu name
//    @GetMapping("/get/{name}")
//    public Mono<Student> getStudentByName(@PathVariable String name) throws InterruptedException {
//        return studentRepo.findByName(name);
//    }
}
