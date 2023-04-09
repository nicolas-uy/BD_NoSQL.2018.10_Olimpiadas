package controlador;

import java.lang.reflect.Type;

import org.bson.types.ObjectId;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/*Esta clase simplemente convierte el objeto ObjectId a una cadena de texto
 * para que se pueda incluir en la representación JSON.*/

public class ObjectIdSerializer implements JsonSerializer<ObjectId> {

	@Override
	public JsonElement serialize(ObjectId objectId, Type arg1, JsonSerializationContext arg2) {
		return new JsonPrimitive(objectId.toString());
	}

}
