import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
/**
 * Calculates optimal escape course for spaceship escape.
 * Reads charts from input file chart.json.
 * Writes course to output file course.json.
 */
public class SpaceshipEscape {
	public static void main(String[] args) {
		try {
			// READ INPUT DATA
			// open chart.json and read contents into jsonMap
			FileReader reader = new FileReader("chart.json");
			Gson gson = new Gson();
			Type T = new TypeToken<Map<String, JsonElement>>(){}.getType();
			Map<String, JsonElement> jsonMap = gson.fromJson(reader, T);
			// read timePerBlastMove from jsonMap
			int timePerBlastMove = jsonMap.get("t_per_blast_move").getAsInt();
			// read asteroids from jsonMap
			JsonArray jsonAsteroids = (JsonArray) jsonMap.get("asteroids");
			ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
			for(int i = 0; i < jsonAsteroids.size(); ++i) {
				JsonObject jsonAsteroid = (JsonObject) jsonAsteroids.get(i);
				int offset = jsonAsteroid.getAsJsonPrimitive("offset").getAsInt();
				int timePerAsteroidCycle = jsonAsteroid.getAsJsonPrimitive("t_per_asteroid_cycle")
						.getAsInt();
				asteroids.add(new Asteroid(offset, timePerAsteroidCycle));
			}
			reader.close();
			
			// CALCULATE OPTIMAL ESCAPE COURSE
			CourseCalculator calculator = new CourseCalculator(timePerBlastMove, asteroids);
			LinkedList<Integer> course = calculator.calculateEscapeCourse();
			
			// WRITE OUTPUT TO FILE
			// convert course to json string
			String jsonCourse = gson.toJson(course);
			// write json string to course.json
			FileWriter writer = new FileWriter("course.json");
			writer.write(jsonCourse);
			writer.close();
			System.out.println(jsonCourse);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}