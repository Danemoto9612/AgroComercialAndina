package com.comercializadora.seguridad;

import com.comercializadora.modelo.repositorios.UsuarioRepositorio;
import com.comercializadora.modelo.entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UsuarioRepositorio usuarioRepositorio;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Crear admin por defecto si no existe
        if (usuarioRepositorio.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol("ROLE_ADMIN");
            admin.setActivo(true);
            usuarioRepositorio.save(admin);
            System.out.println("✅ Usuario admin creado → usuario: admin | contraseña: admin123");
        }

        // Crear usuario básico por defecto si no existe
        if (usuarioRepositorio.findByUsername("usuario").isEmpty()) {
            Usuario user = new Usuario();
            user.setUsername("usuario");
            user.setPassword(passwordEncoder.encode("usuario123"));
            user.setRol("ROLE_USER");
            user.setActivo(true);
            usuarioRepositorio.save(user);
            System.out.println("✅ Usuario básico creado → usuario: usuario | contraseña: usuario123");
        }
    }
}
