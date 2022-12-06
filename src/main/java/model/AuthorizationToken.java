package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationToken {

    @Expose
    private final UUID uuid = UUID.randomUUID();
    @Expose
    private final LocalDateTime created = LocalDateTime.now();
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("expires_in")
    private Long expiresIn;
    @SerializedName("scope")
    private String scope;
}
