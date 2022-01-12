package service;

import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Ambiente;
import model.AuthorizationToken;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import util.Assinador;

import java.io.IOException;
import java.util.Map;

@NoArgsConstructor
@Data
public class AuthorizationTokenService {

    private static final String ENDPOINT = "/auth/server/v1.1/token";
    private Ambiente ambiente;
    private AuthorizationToken authorizationToken;

    public AuthorizationTokenService(Ambiente ambiente) {
        this.ambiente = ambiente;
    }

    public void setAuthorizationToken() throws IOException {
        var logging = new HttpLoggingInterceptor();
        logging.level (HttpLoggingInterceptor.Level.NONE);
        OkHttpClient client = new OkHttpClient ( ).newBuilder ( )
                .addInterceptor (logging)
                .build ( );
        RequestBody requestBody = new FormBody.Builder ( )
                .add ("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
                .add ("assertion", getAssertion ( ))
                .build ( );
        Request request = new Request.Builder ( )
                .addHeader ("alg", ambiente.getAlgoritmo ( ))
                .addHeader ("typ", "JWT")
                .url (ambiente.getBaseURL ( ) + ENDPOINT)
                .post (requestBody)
                .build ( );
        Response response = client.newCall (request).execute ( );
        String json = response.body ().string ();
        System.out.println (json);
        Gson gson = new Gson ();
        authorizationToken = gson.fromJson (json,AuthorizationToken.class);
    }

    private String getAssertion() {
        String aud = ambiente.getBaseURL ( ) + ENDPOINT;
        Long iat = System.currentTimeMillis ( ) / 1_000;
        Long exp = (System.currentTimeMillis ( ) / 1_000) + 2_592_000;
        Long jti = System.currentTimeMillis ( );

        Map<String, Object> mapHeader = Map.of (
                "alg", ambiente.getAlgoritmo ( ),
                "typ", "JWT");

        Map<String, String> mapClaims = Map.of (
                "aud", aud,
                "sub", ambiente.getClientId ( ),
                "iat", String.valueOf (iat),
                "exp", String.valueOf (exp),
                "jti", String.valueOf (jti),
                "ver", "1.1");
        String assertion = Jwts.builder ( )
                .setHeader (mapHeader)
                .setClaims (mapClaims)
                .signWith (Assinador.getPrivateKey (ambiente.isProducao ( ))).compact ( );

        System.out.println ("**** Assertion: "+assertion );
        return assertion;
    }
}
