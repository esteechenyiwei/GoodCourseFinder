# GoodCourseFinder

This java mini program helps Penn students find the best CIS course to take. 

It takes in a bunch of courses and relevant information in JSON format from an input file path and writes out the required number of "good" courses to a specified file. "Good" means having high quality-to-difficulty ratio (having high quality and low difficulty as specified on the course review website.

It uses JSONArray and JSONObject to store and process relevant information regarding the course, including "enrollment", "course quality", "course instructor" and "course difficutlty". 

To use the program, one needs to key in three information as the argument: 

 * 1. input file name (must be in JSON format)
 * 2. output file name
 * 3. number of courses output required
 
 The developer is still working on:
 1. Making a nice user interface for this app
 2. Implementing real-time course info extraction from Penn's course rating website.
 3. Adding in other major options such as finding the best Finance course and so on. 
