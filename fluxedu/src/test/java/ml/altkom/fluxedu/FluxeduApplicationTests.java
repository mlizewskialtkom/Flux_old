package ml.altkom.fluxedu;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
class FluxeduApplicationTests {

	@Test
	void test1() {
		Flux.just("Joeann", "Trudie", "Brandie", "Sofie", "Valaree", "Kennie", "Arabella", "Nisse", "Glory", "Robinett", "Elsie", "Kelbee", "Lianne", "Johan")
				.filter(name -> name.startsWith("J"))
				.subscribe(System.out::println);
	}

}
