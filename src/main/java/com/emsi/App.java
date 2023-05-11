package com.emsi;

import com.emsi.entities.Emission;
import com.emsi.entities.Producer;
import com.emsi.service.ProducerService;
import com.google.gson.Gson;
import org.apache.poi.ss.usermodel.Cell;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void readFromTextFile(ArrayList<Emission> list) {

        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/InputData.txt"));
            Emission c = null;
            String readLine = br.readLine();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            while(readLine != null){
                String [] emission  = readLine.split(",");
                c = new Emission();
                c.setTitre(emission[0].trim());
                c.setDateEmission(dateFormat.parse(emission[1].trim()));
                c.setDureeEmission(dateFormat.parse(emission[2].trim()));
                c.setGenre(emission[4].trim());
                list.add(c);
                readLine = br.readLine();
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static XSSFRow row;
    public static void writeInOutputFile(ArrayList<Emission> list){
        try( FileOutputStream fout = new FileOutputStream("src/main/resources/outputData.txt"))
        {

            for(Emission emission : list){
                fout.write(emission.toString().getBytes());
                fout.write('\n');

                System.out.println("emission :"+emission.toString());
            }
        }
        catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }

    public static void CreateAndWriteInStyleSheet() throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Création d'un objet de type feuille Excel
        XSSFSheet spreadsheet = workbook.createSheet(" emission-info ");

        //Création d'un objet row (ligne)
        XSSFRow row;

        //Les données à inserer;
        Map< String, Object[] > emissioninfo =
                new TreeMap< String, Object[] >();
        emissioninfo.put( "1", new Object[] { "Titre", "Date Emission", "Duree Emission","Genre"});
        emissioninfo.put( "2", new Object[] { "Honda", "Civic", "AB-123-CD","2020-10-31","Red","Gasoline" });
        emissioninfo.put( "3", new Object[] { "Ford", "Mustang", "EF-489-EZ","2016-10-31","Blue","Electric" });


        //parcourir les données pour les écrire dans le fichier Excel
        Set< String > keyid = emissioninfo.keySet();
        int rowid = 0;

        for (String key : keyid) {
            row = spreadsheet.createRow(rowid++);
            Object [] objectArr = emissioninfo.get(key);
            int cellid = 0;

            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String)obj);
            }
        }

        //Ecrire les données dans un FileOutputStream
        FileOutputStream out = new FileOutputStream(new File("src/main/resources/inputDataX.xlsx"));
        workbook.write(out);
        out.close();
        System.out.println("Travail bien fait!!!");
    }

    public static void readJSONFromTxtFile() throws FileNotFoundException, IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("src/main/resources/inputDataJson.txt"));
        JSONObject jsonObject = (JSONObject) obj;

        String jsonObjectList = (String)jsonObject.get("emissionList").toString();
        System.out.println(jsonObjectList);
        Gson gson = new Gson();
        Emission[] emissionArray = gson.fromJson(jsonObjectList, Emission[].class);


        for(Emission obj2 : emissionArray) {
            System.out.println(obj2.toString());
        }
    }

    public static void readFromStyleSheet(){
        try(FileInputStream fis = new FileInputStream(new File("src/main/resources/emissionInfo.xlsx")))
        {
            XSSFWorkbook workbook1 = new XSSFWorkbook(fis);
            XSSFSheet spreadsheet1 = workbook1.getSheetAt(0);
            Iterator <Row>  rowIterator = spreadsheet1.iterator();

            while (rowIterator.hasNext()) {
                row = (XSSFRow) rowIterator.next();
                Iterator < Cell >  cellIterator = row.cellIterator();

                while ( cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    System.out.print(cell.toString()+"\t\t");

                }
                System.out.println();
            }
        }
        catch (FileNotFoundException e) {
            // TODO: handle exception
        }
        catch (IOException e) {
            // TODO: handle exception
        }
    }

    public static void main( String[] args ) throws Exception {
        ArrayList<Emission> list = new ArrayList<Emission>();
        System.out.println(list);
        System.out.println("----text----");
        readFromTextFile(list);
        System.out.println(list);
        writeInOutputFile(list);
        System.out.println("----xlsx----");
        CreateAndWriteInStyleSheet();
        readFromStyleSheet();
        System.out.println("----json----");
        readJSONFromTxtFile();
        System.out.println("----JDBC----");

        ProducerService ownerService = new ProducerService();
        for( Producer owner :ownerService.findAll())
            System.out.println(owner);

        Producer owner1 = new Producer(6,"amine","allaf","Tangier",0674);
        ownerService.save(owner1);
        System.out.println("added");
    }
}
