package service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import model.Ambiente;
import model.BradSignature;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import util.Assinador;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Builder
@AllArgsConstructor
public class ProfissaoService {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final String timeStamp = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    private final Long nonce = System.currentTimeMillis();

    private String endpoint;
    private Ambiente ambiente;
    private String authorization;
    private String xBradAuth;
    private String natureza;

    private String payload;
    private BradSignature bradSignature;

    public String getDados() {
        setPayload();
        String signPayload = Assinador.Sign(ambiente.isProducao(), payload);
//        System.out.println ("********** Sign Payload: " + signPayload);

        var logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + authorization)
                .addHeader("X-Brad-Signature", signPayload)
                .addHeader("X-Brad-Nonce", String.valueOf(nonce))
                .addHeader("X-Brad-timestamp", timeStamp)
                .addHeader("X-Brad-Algorithm", ambiente.getAlgoritmo())
                .addHeader("access-token", ambiente.getClientId())
                .addHeader("X-Brad-Auth", "Bearer " + xBradAuth)
                .url(ambiente.getBaseURL() + endpoint + "?natureza=" + natureza)
                .get()
                .build();

        String retorno = "";
        try {
            Response response = client.newCall(request).execute();
            retorno = response.body().string();
//            System.out.println ("********** Response: " + response.toString ( ));
//            System.out.println ("********** Response Body: \n" + retorno);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retorno;
    }

    private void setPayload() {
        StringBuilder payload = new StringBuilder()
                .append("GET").append("\n")
                .append(endpoint).append("\n")
                .append("natureza=" + natureza).append("\n")
                .append("\n")
                .append(authorization).append("\n")
                .append(nonce).append("\n")
                .append(timeStamp).append("\n")
                .append(ambiente.getAlgoritmo());
//        System.out.println ("********** Payload");
//        System.out.println (payload);
        this.payload = String.valueOf(payload);
    }

}
