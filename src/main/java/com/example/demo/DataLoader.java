package com.example.demo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.model.Compra;
import com.example.demo.model.Entrada;
import com.example.demo.model.ImagenPerfil;
import com.example.demo.model.Rol;
import com.example.demo.model.Usuario;
import com.example.demo.repository.CompraRepository;
import com.example.demo.repository.EntradaRepository;
import com.example.demo.repository.ImagenPerfilRepository;
import com.example.demo.repository.RolRepository;
import com.example.demo.repository.UsuarioRepository;

import net.datafaker.Faker;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EntradaRepository entradaRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ImagenPerfilRepository imagenPerfilRepository;

    @Override
    public void run(String... args) {
        try {
            // Evitar duplicar datos si ya hay usuarios creados
            if (usuarioRepository.count() > 0) {
                System.out.println("Base de datos ya inicializada. Omitiendo DataLoader.");
                return;
            }

            Faker faker = new Faker();
            Random random = new Random();

            // ----- ROL -----
            Rol admin = new Rol();
            admin.setNombre("ADMIN");
            rolRepository.save(admin);

            Rol user = new Rol();
            user.setNombre("USER");
            rolRepository.save(user);

            List<Rol> roles = rolRepository.findAll();

            // ----- USUARIOS -----
            List<Usuario> usuarios = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Usuario usuario = new Usuario();
                usuario.setEmail(faker.internet().emailAddress());
                usuario.setPassword("123456");
                usuario.setRol(roles.get(random.nextInt(roles.size())));
                usuarios.add(usuarioRepository.save(usuario));
            }

            // ----- IMÁGENES DE PERFIL -----
            for (Usuario u : usuarios) {
                ImagenPerfil img = new ImagenPerfil();
                img.setImageUrl(faker.internet().image());
                img.setUsuario(u);
                imagenPerfilRepository.save(img);
            }

            // ----- ENTRADAS -----
            List<Entrada> entradas = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Entrada entrada = new Entrada();
                entrada.setTitulo(faker.book().title());
                entrada.setLugar(faker.address().cityName());
                entrada.setPrecio(faker.number().randomDouble(2, 5000, 30000));
                entrada.setEstado("disponible");
                entrada.setCodigoQR("QR-" + faker.number().digits(6));
                entrada.setCantidad(faker.number().numberBetween(1, 5));
                entrada.setUsuario(usuarios.get(random.nextInt(usuarios.size())));
                entradas.add(entradaRepository.save(entrada));
            }

            // ----- COMPRAS -----
            for (int i = 0; i < 5; i++) {
                Compra compra = new Compra();
                compra.setUsuario(usuarios.get(random.nextInt(usuarios.size())));
                compra.setFechaCompra(LocalDateTime.now().minusDays(random.nextInt(10)));

                // seleccionar entradas ya persistidas
                int endIndex = random.nextInt(entradas.size());
                if (endIndex == 0) endIndex = 1;
                List<Entrada> seleccionadas = new ArrayList<>(entradas.subList(0, endIndex));
                compra.setEntradas(seleccionadas);

                compraRepository.save(compra);
            }

            System.out.println("Datos de prueba insertados correctamente.");

        } catch (Exception e) {
            System.err.println("Error al ejecutar DataLoader: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
