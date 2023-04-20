package ml.altkom.client.fluxeduclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/flux-client")
public class FluxClientController {

    private static final RestTemplate restTemplate = new RestTemplate();
    static {
        restTemplate.setUriTemplateHandler(
                new DefaultUriBuilderFactory("http://localhost:8080")
        );
    }
    private static final WebClient client = WebClient.create("http://localhost:8080");

    @GetMapping("/all")
    public List<String> all(){
        List<String> names = new ArrayList<>();
        log.info("all started");
        long t = System.currentTimeMillis();
        getAllIds().forEach(studentId -> {
            StudentDto student = restTemplate.getForObject("/flux/one/" + studentId.getId(), StudentDto.class);
            assert student != null;
            names.add(student.name);
        });
        log.info("done in {} ms", System.currentTimeMillis() - t);
        return names;
    }

    @GetMapping(value = "/all-reacted")
    public Mono<List<String>> allReacted(){
        log.info("all-reacted started");
        long t = System.currentTimeMillis();
        return Flux.fromIterable(getAllIds()).flatMap(this::getOneStudent)
                .log()
                .map(StudentDto::getName)
//                .flatMap( s -> Mono.just(s.getName()))
                .collectList().doOnSuccess(x -> {
                    log.info("reacted {} done in {} ms", x.size(), System.currentTimeMillis() - t);
                    log.info("reacted {} ", x);
                });
    }

    private Mono<StudentDto> getOneStudent(StudentId studentId) {
        return client.get().uri("/flux/one/{id}", studentId.getId())
                .retrieve()
                .bodyToMono(StudentDto.class);
    }



    @GetMapping("/all-reacted2")
    public List<String> allReacted2(){
        List<String> names = new ArrayList<>();
        log.info("all-reacted started2");
        for (StudentId studentId : getAllIds()) {
            client.get().uri("/flux/one/" + studentId.getId())
                    .retrieve()
                    .bodyToMono(StudentDto.class)
//                    .log()
                    .subscribe();
//            names.add(studentDto.name);
        }
        return names;
    }

    private static List<StudentId> getAllIds() {
        ResponseEntity<StudentId[]> response = restTemplate.getForEntity("/flux/ids", StudentId[].class);
        return List.of(Objects.requireNonNull(response.getBody()));
    }
}
