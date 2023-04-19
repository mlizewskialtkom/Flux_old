package ml.altkom.client.fluxeduclient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudentDto {
    public String id;
    @Getter
    public String name;
    public Integer order;
}
