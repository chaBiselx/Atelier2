package org.api.boundary;

import org.control.KeyManagement;
import org.control.PasswordManagement;
import org.api.entity.Utilisateur;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import org.mindrot.jbcrypt.BCrypt;

@Path("/authentification")
public class AuthentificationBoundary {

    @Inject
    private KeyManagement keyManagement;

    @Context
    private UriInfo uriInfo;

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Response authentifieUtilisateur(Utilisateur user) {
        try {
            String mail = user.getMail();
            String motDePasse = user.getPassword();
            // On authentifie l'utilisateur en utilisant les crédentails fournis
            authentifie(mail, motDePasse);
            // On fournit un token
            String token = issueToken(mail);
            return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private void authentifie(String mail, String motDePasse) throws Exception {
        System.out.println("hash : "+PasswordManagement.digestPassword(motDePasse));
        // On authentifie l'utilisateur en utilisant la BD, LDAP,...
        // On lève une exception si les crédentials sont invalides
        String motDePasseUser = "$2a$10$hptny9c6DZW25O5v8hy1Oe0IjsLy89Ho6rHzutlDlj.Ts5L090Jii";
        if (mail.equals("mail") && BCrypt.checkpw(motDePasse, motDePasseUser)) {  // A changer pour qu'il puisse check si l'username/password est bon dans la table utilisateur 
        } else {
            throw new NotAuthorizedException("Problème d'authentification");
        }
    }

    private String issueToken(String login) {
        Key key = keyManagement.generateKey();
        String jwtToken = Jwts.builder()
                .setSubject(login)
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(5L)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        System.out.println(">>>> token/key : " + jwtToken + " -- " + key);
        return jwtToken;
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
