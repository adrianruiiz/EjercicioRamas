package edu.upvictoria.fpoo;

import java.util.Scanner;

public class ModOrdenCarga extends OrdenCarga implements IOrden {
    static Scanner reader = new Scanner(System.in);
    private String pathGuardar;
    ModOrdenCarga(){}
    ModOrdenCarga(String path, String pathGuardar){
        super(path);
        this.pathGuardar = pathGuardar;

        System.out.print("El directorio definido por el usuario para leer los datos de los alumnos es: ");
        System.out.println(super.getDirectorioRuta());
        System.out.print("El directorio definido por el usuario para guardar los datos de los alumnos es: ");
        System.out.println(this.pathGuardar);
    }

    @Override
    public void obtenerPromedios() {
        super.promediarMaterias();

        this.menu();
    }

    public void menu(){
        String respuesta;
        do {
            System.out.println("-------- MENU --------");
            System.out.println("[1] Ordenar alumnos por Toma de Carga");
            System.out.println("[2] Ordenar alumnos por Promedios");
            System.out.println("[3] Ordenar alumnos por Matrículas");
            System.out.println("[4] Ordenar alumnos por todos los ordenes");
            System.out.println("[5] Terminar programa");
            System.out.print("> ");
            respuesta = reader.nextLine();
            switch (respuesta) {
                case "1" -> this.ordenCarga();
                case "2" -> this.promedio();
                case "3" -> this.matricula();
                case "4" -> {
                    this.ordenCarga();
                    this.promedio();
                    this.matricula();
                }
                case "5" -> System.out.println("Gracias por usar el programa!!!");
                default -> {
                    System.out.println("Valor inválido, ingrese de nuevo");
                    respuesta = "s";
                }
            }
        } while(!respuesta.equals("5"));
    }

    @Override
    public void ordenCarga() {
        Integer[] materiasReprobadas = new Integer[getDatosAlumno().size()];
        for (int i = 0; i < getDatosAlumno().size(); i++) {
            materiasReprobadas[i] = getDatosAlumno().get(i).getCantidadMateriasReprobadas();
        }

        ModArbol<Integer> modArbol = new ModArbol<>(pathGuardar);
        modArbol.guardarDatos(materiasReprobadas, "OrdenCarga", getDatosAlumno());
    }

    @Override
    public void promedio() {
        Float[] datos = new Float[getDatosAlumno().size()];
        for (int i = 0; i < getDatosAlumno().size(); i++) {
            datos[i] = getDatosAlumno().get(i).getPromedio();
        }

        ModArbol<Float> modArbol = new ModArbol<>(pathGuardar);
        modArbol.guardarDatos(datos, "Promedios", getDatosAlumno());
    }

    @Override
    public void matricula(){
        String[] datos = new String[getDatosAlumno().size()];
        for (int i = 0; i < getDatosAlumno().size(); i++) {
            datos[i] = getDatosAlumno().get(i).getMatricula();
        }

        ModArbol<String> modArbol = new ModArbol<>(pathGuardar);
        modArbol.guardarDatos(datos, "Matriculas", getDatosAlumno());
    }
}
