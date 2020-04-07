package lab.excel;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;



public class xls {

    private static final String FILENAME = "C:/Users/Nik/Desktop/Test.json";
    //private static final String FILE_NAME = "./src/main/resources/Test.xlsx";
    private static final String FILE_NAME = "Test.xlsx";


    public static void main(String[] args) throws IOException {
        System.out.println("Server Started");
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        //HttpContext context = server.createContext("/img");
        //server.createContext("/img", new ImageHandler());
        server.createContext("/test", new LinkTest());
        server.createContext("/xlsx", new JsonLink());
        //server.createContext("/test", new StaticHandler("/test", "C:/Users/Nikita/IdeaProjects/look-it-app-image-server_v2/web"));
        server.setExecutor(null);

        //context.setHandler(BasicHttpServerExample::handleRequest);
        server.start();
    }



    static class JsonLink implements HttpHandler {


        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = "";
            httpExchange.sendResponseHeaders(200, response.length());
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String query = br.readLine();
            System.out.println(query);

            min(query);

            response = x();
            //httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            System.out.println(response);
            os.close();
        }

        private static String x() throws IOException {
            //String filePath = "./src/main/resources/Test.xlsx";
            String filePath = "Test.xlsx";
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            String base64 = new sun.misc.BASE64Encoder().encode(bytes);


            fis.close();
            return base64;
        }

        private static void min(String query) throws IOException {
            //JSONObject json = Json();
            //System.out.println(json);
            int i = 0;
            int rowNum = 0;
            int colNum = 0;

            FileReader reader = new FileReader(FILENAME);
            JSONParser jsonParser = new JSONParser();
            String str = "Даты событий";

            try {
                //JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
                JSONObject jsonObject = (JSONObject) jsonParser.parse(query);
                JSONArray jsonArray = (JSONArray) jsonObject.get("Сводка");
                JSONArray jsonArray2 = (JSONArray) jsonObject.get("События");

                Iterator<Object> iterator = jsonArray.iterator();
                Iterator<Object> iterator2 = jsonArray2.iterator();
                System.out.println(jsonObject);
                System.out.println(jsonArray);
                System.out.println(jsonArray2);

                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet sheet1 = workbook.createSheet("Сводка");
                XSSFSheet sheet2 = workbook.createSheet("События");
                Row row = sheet1.createRow(rowNum++);


                while (iterator.hasNext()) {
                    jsonObject = (JSONObject) iterator.next();

                    for (Object key : jsonObject.keySet()) {
                        //System.out.println(key.toString());
                        if (key.equals(str)) // работа с датами событий
                        {
                            jsonArray = (JSONArray) jsonObject.get(key);

                            for(Object jb : jsonArray)
                            {
                                if(colNum == 0)
                                {
                                    Cell cell1 = row.createCell(colNum++);
                                    cell1.setCellValue("Фамилия");
                                    Cell cell2 = row.createCell(colNum++);
                                    cell2.setCellValue("Имя");
                                }
                                Cell cell = row.createCell(colNum++);
                                cell.setCellValue((String) jb);
                            }
                            colNum = 0;
                            row = sheet1.createRow(rowNum++);
                        }else  // работа с объектами-людьми
                        {
                            if (key.equals("Участие в событиях")) {
                                jsonArray = (JSONArray) jsonObject.get(key);

                                for (Object jb : jsonArray) {
                                    Cell cell = row.createCell(colNum++);
                                    cell.setCellValue((String) jb);
                                }
                                colNum = 0;
                                row = sheet1.createRow(rowNum++);
                            } else {
                                Cell cell = row.createCell(colNum++);
                                cell.setCellValue((String) jsonObject.get(key));

                            }
                        }

                       // System.out.println(key + ":" + jsonObject.get(key));
                    }

                }
                rowNum = 0;
                colNum = 0;
                Row row2 = sheet2.createRow(rowNum++);
                Cell cell;
                while (iterator2.hasNext()) {
                    jsonObject = (JSONObject) iterator2.next();
                    for (Object key : jsonObject.keySet()) {
                        System.out.println(key + ":" + jsonObject.get(key));
                        cell = row2.createCell(colNum++);
                        cell.setCellValue((String) key);
                        jsonArray2 = (JSONArray) jsonObject.get(key);
                        for(Object jb : jsonArray2)
                        {
                            cell = row2.createCell(colNum++);
                            cell.setCellValue((String) jb);
                        }
                        colNum = 0;
                        row2 = sheet2.createRow(rowNum++);
                    }


                }

                try {
                    FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
                    workbook.write(outputStream);
                    workbook.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Done");
                // } catch (ParseException e) {
                //    e.printStackTrace();
                //}
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


    }

    private static class LinkTest implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = "";
            httpExchange.sendResponseHeaders(200, response.length());

            String query = httpExchange.getRequestURI().getQuery();
            //System.out.println(query);
            response = testLink();
            try {
                testParserJson(response);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            //System.out.println(response);
            os.close();
        }
    }

    private static void testParserJson(String str) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(str);
        JSONArray jsonArray2;
        JSONArray jsonArray3;

        Iterator<Object> iterator = jsonArray.iterator();
        Iterator<Object> iterator2;
        Iterator<Object> iterator3;

        JSONObject jsonObject;
        JSONObject jsonObject2;
        JSONObject jsonObject3;
        JSONObject jsonObject4;
        JSONObject jsonObject5;

        String title, pars, name, surname, role;
        ArrayList<String> beginTime = new ArrayList<String>(25);
        ArrayList<String> endTime = new ArrayList<String>(25);

        while (iterator.hasNext()) {                                                    // title
            jsonObject = (JSONObject) iterator.next();
            title = jsonObject.get("title").toString();
            System.out.println(title);

            jsonArray2 = (JSONArray) jsonObject.get("shifts");
            System.out.println(jsonArray2.toString());
            iterator2 = jsonArray2.iterator();
            while (iterator2.hasNext()) {                                               // shifts
                jsonObject2 = (JSONObject) iterator2.next();

                pars = jsonObject2.get("beginTime").toString();
                pars = pars.substring(5,10);
                pars = pars.replace('-','.');
                beginTime.add(pars);

                pars = jsonObject2.get("endTime").toString();
                pars = pars.substring(5,10);
                pars = pars.replace('-','.');
                endTime.add(pars);

                jsonArray3 = (JSONArray) jsonObject2.get("places");
                jsonObject3 = (JSONObject) jsonArray3.get(0);
                jsonArray3 = (JSONArray) jsonObject3.get("participants");
                iterator3 = jsonArray3.iterator();

                while (iterator3.hasNext()) {                                           // participants
                    jsonObject4 = (JSONObject) iterator3.next();
                    jsonObject5 = (JSONObject) jsonObject4.get("user");

                    name = jsonObject5.get("firstName").toString();
                    surname = jsonObject5.get("lastName").toString();

                    jsonObject5 = (JSONObject) jsonObject4.get("eventRole");
                    role = jsonObject5.get("title").toString();

                    System.out.print(name);
                    System.out.print(" ");
                    System.out.print(surname);
                    System.out.println();
                    System.out.println(role);
                }

            }

            for(String st : beginTime)
            {
                System.out.print(st);
                System.out.print(" ");
            }
            System.out.println();

            for(String st : endTime)
            {
                System.out.print(st);
                System.out.print(" ");
            }
            System.out.println();



            beginTime.clear();
            endTime.clear();
        }





    }

    //static String q = "?end=2020-05-04T00:00:00Z&begin=2020-01-01T00:00:00Z";
    static String q = "";


    private static String testLink() throws IOException {
        String request = "https://dev.rtuitlab.ru/api/event/docsGen" + q;
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "tmWdXwVzGNy94jwMUXYtApadJUFChYuknEUxrkzsyqUBpfKksDNTpRbh7u22EEJx7pE4t4ThjKf");
        connection.connect();

        int code = connection.getResponseCode();
        String answer = "";
        if (code == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf8"));

            String line = null;

            while ((line = reader.readLine()) != null) {
                answer += line;
            }
            reader.close();
        }
        connection.disconnect();
        return answer;
    }
}


