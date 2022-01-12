package model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Optin {
    private Integer codigoOptin;
    private String valorOptin;
}
