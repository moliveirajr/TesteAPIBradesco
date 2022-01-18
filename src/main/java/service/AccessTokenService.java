package service;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.BradSignature;
import model.Ambiente;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import util.Assinador;
import util.XBradSignature;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AccessTokenService {

    public static final MediaType JSON = MediaType.get ("application/json; charset=utf-8");
    private final String ENDPOINT = "/cartoes/aquisicao/parceiros/v1/login/origem";
    private final String timeStamp = ZonedDateTime.now ( ).truncatedTo (ChronoUnit.SECONDS).format (DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    private final Long nonce = System.currentTimeMillis ( );
    private Ambiente ambiente;
    private BradSignature bradSignature;
    private String authorization;
    private String body;
    private String payload;


    public void setBradSignature() {
        setPayload ( );
        String signPayload = Assinador.Sign (ambiente.isProducao ( ), payload);
        System.out.println ("********** Sign Payload: " + signPayload);

        var logging = new HttpLoggingInterceptor ( );
        logging.level (HttpLoggingInterceptor.Level.NONE);
        OkHttpClient client = new OkHttpClient ( ).newBuilder ( )
                .addInterceptor (logging)
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .build ( );

        RequestBody requestBody = RequestBody.create (body, JSON);

        Request request = new Request.Builder ( )
                .addHeader ("Content-Type", "application/json")
                .addHeader ("Authorization", "Bearer " + authorization)
                .addHeader ("X-Brad-Signature", signPayload)
                .addHeader ("X-Brad-Nonce", String.valueOf (nonce))
                .addHeader ("X-Brad-timestamp", timeStamp)
                .addHeader ("X-Brad-Algorithm", ambiente.getAlgoritmo ( ))
                .addHeader ("access-token", ambiente.getClientId ( ))
                .url (ambiente.getBaseURL ( ) + ENDPOINT)
                .post (requestBody)
                .build ( );

        try {
            Response response = client.newCall (request).execute ( );
            String retorno = response.body ( ).string ( );
            System.out.println ("********** Response: " + response);
            System.out.println ("********** Response Body: \n" + retorno);
            Gson gson = new Gson ( );
            this.bradSignature = gson.fromJson (retorno, BradSignature.class);
        } catch (IOException e) {
            e.printStackTrace ( );
        }
    }

    private void setPayload() {
        StringBuilder payload = new StringBuilder ( )
                .append ("POST").append ("\n")
                .append (ENDPOINT).append ("\n")
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
