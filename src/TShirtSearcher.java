import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.*;

public class TShirtSearcher {

    private static final String filePath = "./inventory.txt";
    private static Inventory allTShirts;
    private static final String appName = "Geeks Tees";

    public static void main(String[] args) {
        allTShirts = loadInventory(filePath);
        TShirtSpecs usersDesiredTShirt = getFilters();
        processSearchResults(usersDesiredTShirt);
        System.exit(0);
    }

    public static TShirtSpecs getFilters(){
        Size size = (Size) JOptionPane.showInputDialog(null,"Welcome to the Greek Geek's t-shirt searcher!\n\nPlease select your preferred size (XS - 4XL):",appName, JOptionPane.QUESTION_MESSAGE,null,Size.values(),Size.M);
        if(size==null)System.exit(0);

        String brand = (String) JOptionPane.showInputDialog(null,"Please select your preferred brand:",appName, JOptionPane.QUESTION_MESSAGE,null,allTShirts.getAllBrands().toArray(), "");
        if(brand==null)System.exit(0);

        String neckLine = (String) JOptionPane.showInputDialog(null,"Please select your preferred brand:",appName, JOptionPane.QUESTION_MESSAGE,null,allTShirts.getAllNeckLines().toArray(new String[0]), "");
        if(neckLine==null)System.exit(0);

        int minPrice=-1,maxPrice = -1;
        while(minPrice<0) {
            String userInput = JOptionPane.showInputDialog(null, "Please enter your lowest preferred price", appName, JOptionPane.QUESTION_MESSAGE);
            if(userInput==null)System.exit(0);
            try {
                minPrice = Integer.parseInt(userInput);
                if(minPrice<0) JOptionPane.showMessageDialog(null,"Price must be >= 0.",appName, JOptionPane.ERROR_MESSAGE);
            }
            catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Invalid input. Please try again.", appName, JOptionPane.ERROR_MESSAGE);
            }
        }
        while(maxPrice<minPrice) {
            String userInput = JOptionPane.showInputDialog(null, "Please enter your highest preferred price", appName, JOptionPane.QUESTION_MESSAGE);
            if(userInput==null)System.exit(0);
            try {
                maxPrice = Integer.parseInt(userInput);
                if(maxPrice<minPrice) JOptionPane.showMessageDialog(null,"Price must be >= "+minPrice,appName, JOptionPane.ERROR_MESSAGE);
            }
            catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Invalid input. Please try again.", appName, JOptionPane.ERROR_MESSAGE);
            }
        }
        TShirtSpecs dreamTShirt = new TShirtSpecs(0,0,"", null, "");
        dreamTShirt.setSize(size);
        dreamTShirt.setMaxPrice(maxPrice);
        dreamTShirt.setMinPrice(minPrice);
        dreamTShirt.setBrand(brand);
        dreamTShirt.setNeckLine(neckLine);
        return dreamTShirt;
    }

    public static void processSearchResults(TShirtSpecs dreamTShirt){
        List<TShirt> matchingTShirts = allTShirts.findMatch(dreamTShirt);

        if(matchingTShirts.size()>0) {
            Map<String, TShirt> options = new HashMap<>();
            StringBuilder infoToShow = new StringBuilder("Matches found!! The following t-shirts meet your criteria: \n");
            for (TShirt matchingTShirt : matchingTShirts) {
                infoToShow.append(matchingTShirt.getTShirtInformation());
                options.put(matchingTShirt.getName(), matchingTShirt);
            }
            String choice = (String) JOptionPane.showInputDialog(null, infoToShow + "\n\nPlease select which t-shirt you'd like to order:", appName, JOptionPane.INFORMATION_MESSAGE, null, options.keySet().toArray(), "");
            if(choice==null) System.exit(0);
            TShirt chosenTShirt = options.get(choice);
            submitOrder(getUserContactInfo(),chosenTShirt,dreamTShirt.getSize());
            JOptionPane.showMessageDialog(null,"Thank you! Your order has been submitted. "+
                    "One of our friendly staff will be in touch shortly.",appName, JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(null,"Unfortunately none of our t-shirts meet your criteria :("+
                    "\n\tTo exit, click OK.",appName, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static Geek getUserContactInfo(){
        String name;
        do {
            name = JOptionPane.showInputDialog(null, "Please enter your name.", appName, JOptionPane.QUESTION_MESSAGE);
            if(name==null) System.exit(0);
            if(name.length()==0) JOptionPane.showMessageDialog(null, "Invalid entry. You must enter your name.",appName, JOptionPane.ERROR_MESSAGE);
        }while (name.length()==0);

        long phoneNumber = 0;
        String phoneNumberInput = "";
        while(phoneNumberInput.length()!=10 || phoneNumber<=0){
            phoneNumberInput = JOptionPane.showInputDialog(null,"Please enter your 10-digit phone number:",appName,JOptionPane.QUESTION_MESSAGE);
            if (phoneNumberInput == null) System.exit(0);
            try {
                phoneNumber = Long.parseLong(phoneNumberInput);
            } catch (NumberFormatException n) {
                JOptionPane.showMessageDialog(null, "Invalid entry. Please enter numbers only. No special characters!",appName, JOptionPane.ERROR_MESSAGE);
                phoneNumber=0;
            }
            if(phoneNumber!=0 && phoneNumberInput.length()!=10 || phoneNumber<0)
                JOptionPane.showMessageDialog(null, "Invalid entry. Phone number must be comprised of 10 positive integers.",appName, JOptionPane.ERROR_MESSAGE);
        }
        return new Geek(name,phoneNumber);
    }

    public static void submitOrder(Geek geek, TShirt tShirt, Size size) {
        String filePath = geek.getName().replace(" ","_")+"_"+tShirt.getProductCode()+".txt";
        Path path = Path.of(filePath);
        String lineToWrite = "Order details:\n\t" +
                "Name: "+geek.getName()+
                "\n\tPhone number: 0"+geek.getPhoneNumber()+
                "\n\tItem: "+tShirt.getName()+" ("+tShirt.getProductCode()+")" +
                "\n\tSize: "+size;
        try {
            Files.writeString(path, lineToWrite);
        }catch (IOException io){
            System.out.println("Order could not be placed. \nError message: "+io.getMessage());
            System.exit(0);
        }
    }

    public static Inventory loadInventory(String filePath) {
        Inventory allTShirts = new Inventory();
        Path path = Path.of(filePath);
        List<String> fileContents = null;
        try {
            fileContents = Files.readAllLines(path);
        }catch (IOException io){
            System.out.println("File could not be found");
            System.exit(0);
        }

        for(int i=1;i<fileContents.size();i++){
            String[] info = fileContents.get(i).split("\\[");
            String[] singularInfo = info[0].split(",");
            String sizesRaw = info[1].replace("]","");
            String description = info[2].replace("]","");
            String neckLineInfo = singularInfo[4];

            String name = singularInfo[0];

            long productCode = 0;
            try{
                productCode = Long.parseLong(singularInfo[1]);
            }catch (NumberFormatException n) {
                System.out.println("Error in file. Product code could not be parsed for t-shirt on line "+(i+1)+". Terminating. \nError message: "+n.getMessage());
                System.exit(0);
            }

            double price = 0;
            try{
                price = Double.parseDouble(singularInfo[2]);
            }catch (NumberFormatException n){
                System.out.println("Error in file. Price could not be parsed for t-shirt on line "+(i+1)+". Terminating. \nError message: "+n.getMessage());
                System.exit(0);
            }

            String brand = singularInfo[3];

            Set<Size> sizes = new HashSet<>();
            for(String s: sizesRaw.split(",")){
                Size size = Size.S;
                try {
                    size = Size.valueOf(s);
                }catch (IllegalArgumentException e){
                    System.out.println("Error in file. Size data could not be parsed for t-shirt on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
                    System.exit(0);
                }
                sizes.add(size);
            }

            TShirtSpecs specs = new TShirtSpecs(0, 0, brand, null, neckLineInfo);


            TShirt tShirt = new TShirt(name,productCode,price,description,sizes, specs);
            allTShirts.addTShirt(tShirt);
        }
        return allTShirts;
    }
}
