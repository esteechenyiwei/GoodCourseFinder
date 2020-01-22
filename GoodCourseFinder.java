
import java.io.*;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * Name: Good Course Finder
 * Developed by : Estee (Yiwei) Chen
 * Introduction: This program takes in a bunch of courses and relevant information in JSON format
 * from an input file path and writes out the required number of "good" courses to a specified
 * file. "Good" means having high quality-to-difficulty ratio (having high quality and low
 * difficulty as specified on the course review website.
 *
 * arguments:
 * 1. input file name (must be in JSON format)
 * 2. output file name
 * 3. number of courses output required
 *
 */

public class GoodCourseFinder {

    public static void main(String[] args) throws IOException, ParseException {
        try {
            if (args.length != 3) {
                throw new IllegalArgumentException("Incorrect number of arguments");
            }

            String input = args[0];
            String output = args[1];
            int numberOfCourses = Integer.parseInt(args[2]);

            //handles course difficulty
            if (numberOfCourses <= 0) {
                throw new ArithmeticException();
            }

            // create a parser
            JSONParser parser = new JSONParser();

            // open the file and parse it to get the array of JSON objects
            //the filereader reads the file 'in' if it is valid
            JSONArray courses = (JSONArray) parser.parse(new FileReader(input));

            // use an iterator to iterate over each element of the array
            Map<String, Double> resultMap = findHighestRatingCourses(courses, numberOfCourses);
            List<Entry<String, Double>> resultList = new ArrayList<>(resultMap.entrySet());
            //sort the list of results by their quality-difficulty ratio
            resultList.sort(Entry.comparingByValue());
            Collections.reverse(resultList);

            System.out.println(resultList.toString());

            File out = new File(output);
            boolean b = out.createNewFile();
            System.out.println("reached createNewFile");
            PrintWriter pw = new PrintWriter(out);

            for (int i = 0; i < numberOfCourses; i++) {
                if (i == resultList.size()) {
                    break;
                }
                pw.write(resultList.get(i).getKey() + " " +
                        String.format("%.2f",resultList.get(i).getValue()) + "\n");
            }

            pw.flush();
            pw.close();

        } catch (IllegalArgumentException e) {
            System.out.println("Incorrect number of arguments");
            System.exit(1);
        } catch (ArithmeticException e) {
            System.out.println("Number of classes must be positive");
            System.exit(1);
        } catch (FileNotFoundException e) {
            System.out.println("Invalid input file");
            System.exit(1);
        } catch (ParseException e) {
            System.out.println("Invalid JSON format");
            System.exit(1);
        }
    }

    /*
     * takes in a JSONArray, build a data structure to store the course and its data
     * sorts based on q-to-d ratio
     * outputs/writes the top n courses to a file
     */
    static Map<String, Double> findHighestRatingCourses(JSONArray courses, int number) {
        Iterator iter = courses.iterator();
        Map<String, JSONArray> nameInfoMap = new HashMap<String, JSONArray>();
        Map<String, Double> courseRatioMap = new HashMap<String, Double>();
        // iterate while there are more objects in array
        while (iter.hasNext()) {

            // get the next JSON object
            JSONObject course = (JSONObject) iter.next();


            String name = (String) course.get("course");
            String code = name.substring(0, 7);
            String dept = name.substring(0, 3);
            //if the name is "CIS or CSE":
            if (dept.equals("CIS") || dept.equals("CSE")) {
                //if this code not yet in the map, then add to the map
                if (!nameInfoMap.containsKey(code)) {
                    JSONArray sections = new JSONArray();
                    nameInfoMap.put(code, sections);

                }
                //add the course object to the array of that course code
                nameInfoMap.get(code).add(course);
            }

        }
        //access each item in the map, extract #student number and course quality and difficulty
        for (Entry<String, JSONArray> e : nameInfoMap.entrySet()) {
            double qualSum = 0.0;
            double diffSum = 0.0;

            String c = e.getKey();
            Iterator ite = e.getValue().iterator();
            while (ite.hasNext()) {
                JSONObject jo = (JSONObject) ite.next();
                long stuCount = (java.lang.Long) jo.get("enrollment");
                double courseQuality = (Double) jo.get("courseQuality");
                double courseDifficulty = (Double) jo.get("courseDifficulty");
                qualSum += courseQuality * stuCount;
                diffSum += courseDifficulty * stuCount;


            }
            double qualityToDifficultyRatio = qualSum / diffSum;
            courseRatioMap.put(c, qualityToDifficultyRatio);

        }
        return courseRatioMap;
    }
}
