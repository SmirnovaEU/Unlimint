package unlimint.orders.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private BigDecimal amount;

    @Getter
    @Setter
    @JsonIgnore
    private String currency;

    @Getter
    @Setter
    private String comment;

    @Getter
    @Setter
    private String filename;

    @Getter
    @Setter
    private int line;

    @Getter
    @Setter
    private String result;

}


