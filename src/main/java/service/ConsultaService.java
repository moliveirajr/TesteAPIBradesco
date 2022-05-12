package service;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import model.Ambiente;
import model.BradSignature;
import okhttp3.MediaType;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import util.Assinador;
import util.HttpGetWithEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
public class ConsultaService {
    public static final MediaType JSON = MediaType.get ("application/json; charset=utf-8");
    private final String timeStamp = ZonedDateTime.now ( ).truncatedTo (ChronoUnit.SECONDS).format (DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    private final Long nonce = System.currentTimeMillis ( );

    private String endpoint;
    private Ambiente ambiente;
    private String authorization;
    private String xBradAuth;
    private String body;

    private String payload;
    private BradSignature bradSignature;

    public String consulta() {
        var gson = new Gson();
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Consulta de Propostas -  "+ambiente.getAmbiente());
        System.out.println("--------------------------------------------------------------------");
        setPayload ( );
        String signPayload = Assinador.Sign (ambiente.isProducao ( ), payload);
//        System.out.println ("********** Sign Payload: " + signPayload);

        try (var httpClient = HttpClientBuilder.create().build()) {

            HttpGetWithEntity httpGetWithEntity = new HttpGetWithEntity(ambiente.getBaseURL() + endpoint);
            httpGetWithEntity.addHeader("Content-Type", "application/json");
            httpGetWithEntity.addHeader("Authorization", "Bearer " + authorization);
            httpGetWithEntity.addHeader("X-Brad-Signature", signPayload);
            httpGetWithEntity.addHeader("X-Brad-Nonce", String.valueOf(nonce));
            httpGetWithEntity.addHeader("X-Brad-timestamp", timeStamp);
            httpGetWithEntity.addHeader("X-Brad-Algorithm", ambiente.getAlgoritmo());
            httpGetWithEntity.addHeader("access-token", ambiente.getClientId());
            httpGetWithEntity.addHeader("X-Brad-Auth", "Bearer " + xBradAuth);
            httpGetWithEntity.setEntity(new StringEntity(body));

            var response = httpClient.execute(httpGetWithEntity);
            HttpEntity httpEntity = response.getEntity();
            String responseBody = "";
            if (httpEntity!=null) {
                var bufferedReader = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
                responseBody = bufferedReader.lines().collect(Collectors.joining());
                bufferedReader.close();
            }


            System.out.println ("/n********** Response Body");
            System.out.println(responseBody);

            return responseBody;

        } catch (IOException e) {
            System.out.println("Não foi possível repassar a chamada ao bradesco pelo motivo: " + e.getMessage());
        }
        return "";
    }

    private void setPayload() {
        StringBuilder payload = new StringBuilder ( )
                .append ("GET").append ("\n")
                .append (endpoint).append ("\n")
                .append ("\n")
                .append (body).append ("\n")
                .append (authorization).append ("\n")
                .append (nonce).append ("\n")
                .append (timeStamp).append ("\n")
                .append (ambiente.getAlgoritmo ( ));
        System.out.println ("********** Payload");
        System.out.println (payload);
        this.payload = String.valueOf (payload);
    }
}
