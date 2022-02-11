import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Consulta {

    private String cpf;
    private String proposta;

    public void consultaProposta() {
        System.out.println(cpf);
        System.out.println("Consulta de Proposta");
    }
}