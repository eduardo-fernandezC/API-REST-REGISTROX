package com.example.demo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.model.Compra;
import com.example.demo.model.CompraEntrada;
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
            System.out.println("Esperando a que Hibernate inicialice la base de datos...");
            Thread.sleep(12000);

            if (usuarioRepository.count() > 0) {
                System.out.println("Base de datos ya inicializada. Omitiendo DataLoader.");
                return;
            }

            Faker faker = new Faker();
            Random random = new Random();

            // ----- ROLES -----
            Rol trabajador = new Rol();
            trabajador.setNombre("TRABAJADOR");
            rolRepository.save(trabajador);

            Rol usuarioRol = new Rol();
            usuarioRol.setNombre("USUARIO");
            rolRepository.save(usuarioRol);

            List<Rol> roles = rolRepository.findAll();
            System.out.println("Roles creados: " + roles.size());

            // ----- USUARIOS -----
            List<Usuario> usuarios = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Usuario usuario = new Usuario();
                // Los TRABAJADORES tendrán correos con dominio @registroX.cl
                boolean esTrabajador = random.nextBoolean();
                if (esTrabajador) {
                    usuario.setEmail("trabajador" + i + "@registroX.cl");
                    usuario.setRol(trabajador);
                } else {
                    usuario.setEmail(faker.internet().emailAddress());
                    usuario.setRol(usuarioRol);
                }
                usuario.setPassword("123456");
                usuarios.add(usuarioRepository.save(usuario));
            }

            // Trabajador fijo
            Usuario trabajadorFijo = new Usuario();
            trabajadorFijo.setEmail("trabajador@registroX.cl");
            trabajadorFijo.setPassword("trabajador123");
            trabajadorFijo.setRol(trabajador);
            usuarioRepository.save(trabajadorFijo);

            System.out.println("Usuarios creados correctamente.");

            // ----- IMÁGENES DE PERFIL -----
            for (Usuario u : usuarios) {
                ImagenPerfil img = new ImagenPerfil();
                img.setImageUrl(faker.internet().image());
                img.setUsuario(u);
                imagenPerfilRepository.save(img);
            }

            System.out.println("Imágenes de perfil generadas correctamente.");

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
                entradas.add(entradaRepository.save(entrada));
            }

            System.out.println("Entradas generadas correctamente.");

            // ----- COMPRAS -----
            for (int i = 0; i < 5; i++) {
                Compra compra = new Compra();
                compra.setUsuario(usuarios.get(random.nextInt(usuarios.size())));
                compra.setFechaCompra(LocalDateTime.now().minusDays(random.nextInt(10)));

                List<CompraEntrada> compraEntradas = new ArrayList<>();
                int endIndex = random.nextInt(entradas.size());
                if (endIndex == 0)
                    endIndex = 1;
                for (Entrada entrada : entradas.subList(0, endIndex)) {
                    CompraEntrada ce = new CompraEntrada();
                    ce.setEntrada(entrada);
                    ce.setCompra(compra);
                    ce.setCodigoQR("QR-" + faker.number().digits(8));
                    ce.setEstado("disponible");
                    compraEntradas.add(ce);
                }

                compra.setCompraEntradas(compraEntradas);
                compraRepository.save(compra);
            }

            System.out.println("Compras generadas correctamente.");
            System.out.println("Datos de prueba insertados exitosamente.");

        } catch (Exception e) {
            System.err.println("Error al ejecutar DataLoader: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
