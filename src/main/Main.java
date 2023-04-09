package main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import controlador.Conexion;
import controlador.ObjectIdSerializer;

public class Main {

	public static void main(String[] args) {
		// ejercicio8();
		 ejercicio9();
		// ejercicio10();
		//ejercicio11();
		// ejercicio12();
		// ejercicio13();
		// ejercicio14();
	}

	public static void ejercicio8() {
		// Crear la colección ParticipantesJava que registre los mismos documentos que en el punto 1.

		try {
			// Creo los objetos para los documentos.
			Document doc1 = new Document();
			Document doc2 = new Document();
			Document doc3 = new Document();
			Document doc4 = new Document();
			Document doc5 = new Document();
			Document doc6 = new Document();
			// Seteo los datos del documento 1.
			doc1.append("Nombre", "Sara");
			doc1.append("Apellido", "Pérez");
			doc1.append("ModalidadParticipación", "Asistente");
			doc1.append("Localidad", "Bella Unión");
			doc1.append("FechaNacimiento", formatoFecha("19/07/2004"));
			// Seteo los datos del documento 2.
			doc2.append("Nombre", "Agustin");
			doc2.append("Apellido", "Rodriguez");
			doc2.append("ModalidadParticipación", "Jurado");
			doc2.append("FechaNacimiento", formatoFecha("10/01/2010"));
			doc2.append("Teléfono", "299 9999");
			doc2.append("Mail", "arodriguez@gmail.com");
			doc2.append("Horario", 1);
			// Seteo los datos del documento 3.
			doc3.append("Nombre", "Samuel");
			doc3.append("Apellido", "Gómez");
			doc3.append("ModalidadParticipación", "Jurado");
			doc3.append("FechaNacimiento", formatoFecha("25/09/1999"));
			doc3.append("Teléfono", "2111 1111");
			doc3.append("Mail", "facundo.gomez@utec.edu.uy");
			doc3.append("Horario", 2);
			// Seteo los datos del documento 4.
			doc4.append("Nombre", "Sofía");
			doc4.append("Apellido", "Gutierrez");
			doc4.append("ModalidadParticipación", "Voluntario");
			doc4.append("FechaNacimiento", formatoFecha("08/03/1990"));
			doc4.append("Mail", "sofia.gutierrez@utec.edu.uy");
			doc4.append("Localidad", "Durazno");
			doc4.append("Horario", 1);
			// Seteo los datos del documento 5.
			doc5.append("Nombre", "Sebastián");
			doc5.append("Apellido", "Pérez");
			doc5.append("ModalidadParticipación", "Asistente");
			doc5.append("Localidad", "Bella Unión");
			doc5.append("Horario", 3);
			// Seteo los datos del documento 6.
			doc6.append("Nombre", "Diego");
			doc6.append("Apellido", "López");
			doc6.append("ModalidadParticipación", "NoSeSabe");
			doc6.append("Horario", 2);
			// Creo la coleccion de Documentos.
			ArrayList<Document> docList = new ArrayList<Document>();
			// Agrego cada uno.
			docList.add(doc1);
			docList.add(doc2);
			docList.add(doc3);
			docList.add(doc4);
			docList.add(doc5);
			docList.add(doc6);
			// Inserto la coleccion de documentos a la BD.
			Conexion.getInstance().coleccionDB().insertMany(docList);
			System.out.println("Ejercicio 8 - Creación de Documentos - Se ha completado satisfactoriamente.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al crear Documentos.");
		}
	}

	public static void ejercicio9() {
		// Generar una sentencia que permita recuperar los documentos cuya Localidad sea Bella Unión.

		// Creo el filtro para la consulta.
		Document filter = new Document("Localidad", "Bella Unión");
		// Ejecuta la consulta y obtiene los documentos.
		FindIterable<Document> docs = Conexion.getInstance().coleccionDB().find(filter);
		// Muestra los resultados.
		for (Document doc : docs) {
			System.out.println(jsonConsola(doc));
		}
	}

	public static void ejercicio10() {
		// Generar una sentencia que permita recuperar los documentos de los que participan como Voluntarios y cuya asistencia sea en todo el día (valor: 1)

		// Crear el filtro para la consulta
		Document filter = new Document("ModalidadParticipación", "Voluntario").append("Horario", 1);
		// Ejecutar la consulta y obtener los documentos
		FindIterable<Document> docs = Conexion.getInstance().coleccionDB().find(filter);
		// Muestra los resultados.
		for (Document doc : docs) {
			System.out.println(jsonConsola(doc));
		}
	}

	public static void ejercicio11() {
		// Generar una sentencia que permita recuperar aquellos documentos asociados a asistentes que sean menores de edad, ya que se requiere pedirles permiso de menor a sus padres

		// Obtener la fecha actual
		LocalDate currentDate = LocalDate.now();
		// Calcula la fecha límite para ser menor de edad
		LocalDate limitDate = currentDate.minusYears(18);
		// Crear un filtro para obtener los documentos cuya fecha de nacimiento es posterior a la fecha límite
		Bson filter = Filters.gt("FechaNacimiento", limitDate);
		// Ejecutar la consulta
		MongoCursor<Document> cursor = Conexion.getInstance().coleccionDB().find(filter).iterator();
		// Recorrer los resultados

		while (cursor.hasNext()) {
			System.out.println(jsonConsola(cursor.next()));
		}
	}

	public static void ejercicio12() {
		// Agregarle a los documentos la clave PermisoMenor con el valor Si o No, si es requerido solicitar dicho permiso (como se realizó la búsqueda en el punto 4)

		// Obtener la fecha actual
		LocalDate currentDate = LocalDate.now();
		// Calcular la fecha límite para ser mayor de edad
		LocalDate limitDate = currentDate.minusYears(18);
		// Crear un filtro para obtener los documentos cuya fecha de nacimiento es anterior a la fecha límite.
		Bson filter = Filters.lte("FechaNacimiento", limitDate);
		// Actualizar los documentos mayores de edad.
		Conexion.getInstance().coleccionDB().updateMany(filter, Updates.set("PermisoMenor", "No"));

		// Crear un filtro para obtener los documentos cuya fecha de nacimiento es posterior o igual a la fecha límite
		filter = Filters.gt("FechaNacimiento", limitDate);
		// Actualizar los documentos menores de edad
		Conexion.getInstance().coleccionDB().updateMany(filter, Updates.set("PermisoMenor", "Si"));

	}

	public static void ejercicio13() {
		// Agregarle a los documentos la clave Institucion con el valor UTEC para aquellos participantes que tienen cuenta de correo de UTEC

		// Crea un filtro para obtener los documentos cuya cuenta de correo contiene el dominio "utec.edu.uy"
		Bson filter = Filters.regex("Mail", ".*utec\\.edu\\.uy");
		// Actualizar los documentos que cumplen con el filtro
		Conexion.getInstance().coleccionDB().updateMany(filter, Updates.set("Institucion", "UTEC"));

	}

	public static void ejercicio14() {
		// Eliminar los documentos cuyo valor para la clave ModalidadParticipacion es NoSeSabe

		// Crear un filtro para obtener los documentos con ModalidadParticipacion = "NoSeSabe"
		Bson filter = Filters.eq("ModalidadParticipación", "NoSeSabe");
		// Eliminar los documentos que cumplen con el filtro
		Conexion.getInstance().coleccionDB().deleteMany(filter);
	}

	private static LocalDate formatoFecha(String fechaString) {
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		date = LocalDate.parse(fechaString, formatter);
		return date;
	}

	private static String jsonConsola(Document doc) {
		// Crea el objeto Gson para impresión en consola.
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(ObjectId.class, new ObjectIdSerializer())
				.create();
		return gson.toJson(doc);
	}
}
