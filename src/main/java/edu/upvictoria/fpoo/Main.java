package edu.upvictoria.fpoo;

public class Main {
    public static void main( String[] args ) {
        //Directorio donde se guardan los datos de los alumnos en archivos CSV
        ModOrdenCarga modOrdenCarga = new ModOrdenCarga("C:\\Users\\torre\\Downloads\\Alumnos\\", "C:\\Users\\torre\\OneDrive\\Documentos\\HeapSort_Resources\\");
        modOrdenCarga.obtener();
    }
}
