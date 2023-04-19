package ml.altkom.fluxedu;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Student {
    private String id;
    private String name;
    private Integer order;

    public Student(String name, Integer order) {
        this.name = name;
        this.order = order;
    }
}
