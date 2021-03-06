package org.api.boundary;

import org.control.KeyManagement;
import org.control.PasswordManagement;
import org.api.entity.Utilisateur;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.inject.Inject;
import javax.validation.Valid;
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

@Path("auth")
@Api(value = "API RESTful")
public class AuthentificationBoundary {

    @Inject
    private KeyManagement keyManagement;
    
    @Inject
    private UtilisateurManager um;

    @Context
    private UriInfo uriInfo;

    @POST
    @ApiOperation(value = "Se connecter en tant qu'utilisateur, renvoie un token Bearer dans le header")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK")
        ,
        @ApiResponse(code = 401, message = "Unauthorized")})
    @Produces("application/json")
    @Consumes("application/json")
    public Response authentifieUtilisateur(@Valid Utilisateur user) {
        try {
            String mail = user.getMail();
            String motDePasse = user.getPassword();
            // On authentifie l'utilisateur en utilisant les crédentails fournis
            authentifie(mail, motDePasse);
            // On fournit un token
            String token = issueToken(mail);
            Utilisateur u = um.findByEmail(mail);
            u.setId(""); // mask UUID
            u.setPassword(""); // mask password hash
            return Response.ok().header(AUTHORIZATION, "Bearer " + token).entity(u).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private void authentifie(String mail, String motDePasse) throws Exception {
        if (um.checkCredentials(mail, motDePasse)) { 
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
                .setExpiration(toDate(LocalDateTime.now().plusDays(365L)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        System.out.println(">>>> token/key : " + jwtToken + " -- " + key);
        return jwtToken;
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
