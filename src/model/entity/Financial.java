package model.entity;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Log4j
public class Financial {
    private int id;
    private int amount;
    private FType fType;
    private LocalDateTime dateTime;
    private String description;


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
