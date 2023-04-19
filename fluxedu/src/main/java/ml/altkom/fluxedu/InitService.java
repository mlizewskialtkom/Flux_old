package ml.altkom.fluxedu;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@AllArgsConstructor
public class InitService {
    private final StudentRepo studentRepo;

    @EventListener(ApplicationReadyEvent.class)
    public void onReadyApp(){
        log.info("onReadyApp");
        AtomicReference<Integer> no = new AtomicReference<>(1);
        studentRepo.deleteAll()
                .thenMany(
                        Flux.just("Joeann", "Trudie", "Brandie", "Sofie", "Valaree", "Kennie", "Arabella", "Nisse", "Glory", "Robinett", "Elsie", "Kelbee", "Lianne", "Johan")
                ).map(name -> new Student(name, no.getAndSet(no.get() + 1)))
                .flatMap(studentRepo::save)
                .thenMany(studentRepo.findAll())
                .subscribe(el -> log.info(el.toString()));
        //code line to log string "hello world"
        log.info("hello world");
    }

    //create deployment.Jenkinsfile file

}
