package edu.upvictoria.fpoo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ModArbol<T extends Comparable<T>> extends Arbol<T> {
    private String pathGuardar;
    public ModArbol(){}
    public ModArbol(String path){
        this.pathGuardar = path;
    }

    public void guardarDatos(T[] t, String nombre, ArrayList<Alumno> listaAlumnos){
        Nodo<T> n1 = new Nodo<>(t[0]);
        for (int i = 1; i < t.length; i++) {
            guardarNodo(n1, new Nodo<>(t[i]));
        }

        heapSortCSV(n1, nombre, listaAlumnos);
    }

    public void heapSortCSV(Nodo<T> nodo, String nombre, ArrayList<Alumno> listaAlumnos) {
        Object[] datosOrdenados = new Object[super.pesoArbol(nodo)];

        if(nombre.equals("OrdenCarga")) heapSortMenor(nodo, datosOrdenados, 0);
        else super.heapSortRecursivo(nodo, datosOrdenados, datosOrdenados.length-1);

        listaAlumnos = buscarDatosAlumno(datosOrdenados, listaAlumnos, nombre);
        crearCSV(nombre, listaAlumnos);
    }

    public int heapSortMenor(Nodo<T> nodo, Object[] datosOrdenados, int i) {
        if (nodo != null) {
            i = heapSortMenor(nodo.getIzq(), datosOrdenados, i);
            datosOrdenados[i++] = nodo.getT();
            i = heapSortMenor(nodo.getDer(), datosOrdenados, i);
        }
        return i;
    }

    public void crearCSV(String nombre, ArrayList<Alumno> listaOrdenada){
        File archivo = new File(pathGuardar+nombre+".csv");

        if(!archivo.exists()) {
            try {
                if (!archivo.createNewFile()) System.out.println("Ocurrio un error al crear el archivo");;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            bw.write("Matricula,Promedio,Materias Cursadas,Materias Reprobadas");
            bw.newLine();
            for (Alumno alumno : listaOrdenada) {
                String datosCSV = alumno.getMatricula();
                datosCSV += "," + alumno.getPromedio();
                datosCSV += "," + alumno.getCantidadMaterias();
                datosCSV += "," + alumno.getCantidadMateriasReprobadas();

                bw.write(datosCSV);
                bw.newLine();
            }
        } catch (IOException e){
            System.out.println("Hubo un error al guardar "+archivo.getName());
            System.out.println("--- Error:");
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("El orden por "+nombre+" se guardó en: "+archivo);
    }

    public ArrayList<Alumno> buscarDatosAlumno(Object[] datosOrdenados, ArrayList<Alumno> listaAlumnos, String nombre){
        ArrayList<Alumno> tmpAlumno = new ArrayList<>();

        for (Object datosOrdenado : datosOrdenados) {
            for (Alumno a : listaAlumnos) {
                if(nombre.equals("Matriculas") && datosOrdenado.equals(a.getMatricula())){
                    tmpAlumno.add(a);
                    continue;
                }

                // Buscar si alumno ya ha sido agregado y no se repita
                if (nombre.equals("OrdenCarga") && datosOrdenado.equals(a.getCantidadMateriasReprobadas()) ||
                        nombre.equals("Promedios") && datosOrdenado.equals(a.getPromedio())) {

                    if (tmpAlumno.isEmpty()) {
                        tmpAlumno.add(a);
                        continue;
                    }
                    boolean yaAgregado = false;
                    for (Alumno evaluar : tmpAlumno) {
                        if (evaluar.getMatricula().equals(a.getMatricula())) {
                            yaAgregado = true;
                            break;
                        }
                    }

                    if (!yaAgregado) {
                        tmpAlumno.add(a);
                    }
                }
            }
        }

        // Ordenar de mayor a menor los promedios de la materia
        if(nombre.equals("OrdenCarga")){
            ArrayList<Integer> limites = new ArrayList<>();

            for (int j = 0; j < tmpAlumno.size() - 1; j++) {
                if(tmpAlumno.get(j).getCantidadMateriasReprobadas() < tmpAlumno.get(j+1).getCantidadMateriasReprobadas()){
                    limites.add(j+1);
                }
            }
            int i;
            this.ordenarAlumno(0, limites.get(0), tmpAlumno);
            for (i = 0; i < limites.size() - 1; i++) {
                this.ordenarAlumno(limites.get(i), limites.get(i+1), tmpAlumno);
            }
            this.ordenarAlumno(limites.get(i), tmpAlumno.size(), tmpAlumno);
        }

        return tmpAlumno;
    }

    public void ordenarAlumno(int desde, int hasta, ArrayList<Alumno> listaAlumnos){
        for (int i = desde; i < hasta - 1; i++) {
            for (int j = desde; j < hasta - 1; j++) {
                if (listaAlumnos.get(j).getPromedio() < listaAlumnos.get(j+1).getPromedio()) {
                    intercambio(j, listaAlumnos);
                }
            }
        }
    }

    public void intercambio(int j, ArrayList<Alumno> listaAlumnos){
        Alumno a = new Alumno();
        a.setPromedio(listaAlumnos.get(j).getPromedio());
        a.setMatricula(listaAlumnos.get(j).getMatricula());
        a.setReemplazarMateriasReprobadas(listaAlumnos.get(j).getCantidadMateriasReprobadas());
        a.setReemplazarMaterias(listaAlumnos.get(j).getCantidadMaterias());

        listaAlumnos.set(j, listaAlumnos.get(j + 1));
        listaAlumnos.set(j + 1, a);
    }
}
