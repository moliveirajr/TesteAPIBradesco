package util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import model.Ambiente;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Builder
public class XBradSignature {

    private String method;
    private String endPoint;
    private String parameters = "\n";
    private String body;
    private String authorization;
    private final String timeStamp = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    private final Long nonce = System.currentTimeMillis();
    private String algorithm = "SHA256";
    private Ambiente ambiente;


    private String getXBradSignature() {

        StringBuilder payload = new StringBuilder()
                .append("POST").append("\n")
                .append(endPoint).append("\n")
                .append(parameters)
                .append(body).append("\n")
                .append(authorization).append("\n")
                .append(nonce).append("\n")
                .append(timeStamp).append("\n")
                .append(ambiente.getAlgoritmo());
        System.out.println("********** Payload");
        System.out.println(payload);

        String signPayload = Assinador.Sign(ambiente.isProducao(), String.valueOf(payload));
        System.out.println("********** Sign Payload: " + signPayload);

        return signPayload;
    }
}
