package it.uniroma3.siw.moviefestival.dto;

public class LoginResponseDTO {

    private String token;
    private String username;
    private String ruolo;

    public LoginResponseDTO(String token, String username, String ruolo) {
        this.token = token;
        this.username = username;
        this.ruolo = ruolo;
    }

    public String getToken() { return token; }
    public String getUsername() { return username; }
    public String getRuolo() { return ruolo; }
}
