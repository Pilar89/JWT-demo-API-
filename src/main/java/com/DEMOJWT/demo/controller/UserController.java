package com.DEMOJWT.demo.controller;

import com.DEMOJWT.demo.dto.User;
import com.DEMOJWT.demo.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

  @Autowired
  private UserRepository userRepository;

  //get lista de usuarios almacenados en mongoDb
  @GetMapping("users")
  public List<User>getAll(){
    return userRepository.findAll();
  }

    @PostMapping("user")
    public User login(@RequestParam("user") String username, @RequestParam("password") String pwd) {
    List<User>usuarios = getAll();
      System.out.println("usuarios "+ usuarios);
   var  validador = usuarios.stream()
      .filter(usuario ->
        usuario.getUser().equals(username) && usuario.getPwd().equals(pwd)).collect(Collectors.toList());
      if (validador.isEmpty()){
        User user = new User();
        user.setUser(username);
        user.setToken("No se puede generar el token usuario y/o contraseña no se encuentran registrados");
        return user;

      }

        String token = getJWTToken(username);
        User user = new User();
        user.setUser(username);
        user.setToken(token);
        return user;

    }

    private String getJWTToken(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("sofkaJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Valido " + token;
    }
}
