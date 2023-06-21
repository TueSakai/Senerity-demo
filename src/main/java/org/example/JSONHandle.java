package org.example;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.restassured.path.json.JsonPath;

public class JSONHandle {
    public static void main(String[] args) throws Exception {

        List<String> screen = new ArrayList<>();
        screen.add("1920x1080");
        screen.add("2340x1920");
        screen.add("3840x2380");
        Map<String, List<String>> details = new HashMap<>();
        details.put("screen", screen);

        Map<String, String> infomation = new HashMap<>();
        infomation.put("phone", "0123456789");
        infomation.put("address", "Phu Thinh St");
        infomation.put("country", "HN");



        List<Map> info = new ArrayList<>();
        info.add(infomation);
        info.add(details);

        JSONObject object = new JSONObject();
        object.put("color","blue");
        object.put("material","steel");
        object.put("isSquare",true);
        object.put("price",15);
        object.put("infomation",info);

        JSONObject request = new JSONObject();
        request.put("email","Nitin@gmail.com");
        request.put("object",object);

        JsonPath jsonPath = new JsonPath(request.toString());
        jsonPath.prettyPrint();
        FileWriter fos = new FileWriter(new File("data.json"));
        fos.write(jsonPath.prettify());
        fos.flush();
        fos.close();

    }
}
