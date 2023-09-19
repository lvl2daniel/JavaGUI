import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.math.BigInteger;
import java.util.Scanner;

import javax.swing.*;


public class App{

    // Define instance variables
    int WINDOW_WIDTH = 1500;
    int WINDOW_HEIGHT = 700;
    int itemNumber = 1;
    JFrame frame = new JFrame("Nile.com");

    boolean itemFound = false;

    public class Item {
        public String itemID;
        public String itemTitle;
        public float itemPrice;
        public int itemQuantity;
        public int discountPercent;
        public float totalItemPrice;

        public Item(String itemID, String itemTitle, float itemPrice, int itemQuantity, int discountPercent, float totalItemPrice) {
            this.itemID = itemID;
            this.itemTitle = itemTitle;
            this.itemPrice = itemPrice;
            this.itemQuantity = itemQuantity;
            this.discountPercent = discountPercent;
            this.totalItemPrice = totalItemPrice;
        }
    }

    public class Cart {
        public Item[] items = new Item[100];

        public String displayCart(){
            String cart = "";
            int itemAmt = 0;
            for(Item item : items){
                if(item != null){
                    itemAmt++;
                    cart += itemAmt + ". " + item.itemID + " " + item.itemTitle + " $" + String.format("%.2f",item.itemPrice) + " " + item.itemQuantity + " " + item.discountPercent + "% $" + String.format("%.2f",item.totalItemPrice) + "\n";
                }
            }
            System.out.println(cart);
            return cart;
            //make a jOptionPane that displays the cart
            //for each item in the cart, display the itemID, itemTitle, itemPrice, itemQuantity, discountPercent, totalItemPrice
           

        }
    }

    float totalPrice = 0;
    // Create a new cart object
    Cart cart = new Cart();
    Item currItem = new Item("", "", 0, 0, 0, 0);
   
    // Set grid layout
    private static final GridLayout LAYOUT = new GridLayout(0, 2);

    // Initialize arrays for labels, text fields, text areas, and buttons
    JLabel[] labels = new JLabel[4];
    JTextField[] textFields = new JTextField[2];
    JTextArea[] textAreas = new JTextArea[2];
    JButton findItemButton = new JButton("Find Item # " + itemNumber);
    JButton addItemButton = new JButton("Add Item # " + itemNumber + " To Cart");
    JButton viewCart = new JButton("View Cart");
    JButton checkOutButton = new JButton("Check Out");
    JButton emptyCartButton = new JButton("Empty Cart - Start A New Order");
    JButton exitButton = new JButton("Exit (Close App)");

    public App() {
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textAreas[0] = new JTextArea();
        textAreas[1] = new JTextArea();

        textAreas[0].setEditable(false);
        textAreas[1].setEditable(false);

        Container c = frame.getContentPane();
        c.setLayout(LAYOUT);

        // Initialize and add components to arrays
        labels[0] = new JLabel("Enter item ID for Item # " + itemNumber + ":  ", SwingConstants. RIGHT);
        labels[1] = new JLabel("Enter quantity for Item # " + itemNumber + ":  ", SwingConstants. RIGHT);
        labels[2] = new JLabel("Details for Item # " + itemNumber + ":  ", SwingConstants. RIGHT);
        labels[3] = new JLabel("Order subtotal for " + 0 + " item(s)" + ":  " , SwingConstants. RIGHT);

        textFields[0] = new JTextField();
        textFields[1] = new JTextField();

        //change color of font in label 0 and 1 to yellow
        labels[0].setForeground(Color.YELLOW);
        labels[1].setForeground(Color.YELLOW);
        //change color of font in label 2 and 3 to red
        labels[2].setForeground(Color.RED);
        labels[3].setForeground(Color.RED);
        //set background of canvas to dark gray
        c.setBackground(Color.DARK_GRAY);
        //make text areas shorter
        textAreas[0].setPreferredSize(new Dimension(60, 20));
        textAreas[1].setPreferredSize(new Dimension(60, 20));




        for(JLabel label : labels) {
            label.setFont(new Font("Arial", Font.BOLD, 15));
            label.setSize(200, 70);
   
        findItemButton.setEnabled(true);
        emptyCartButton.setEnabled(true);
        checkOutButton.setEnabled(false);
        exitButton.setEnabled(true);
        addItemButton.setEnabled(itemFound);
        viewCart.setEnabled(false);

        }

        findItemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findItem(textFields[0].getText(), Integer.parseInt(textFields[1].getText()));
            }
        } );
        addItemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addToCart(textFields[0].getText(), Integer.parseInt(textFields[1].getText()));
            }
        } );
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        } );

        viewCart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                JOptionPane.showMessageDialog(frame, cart.displayCart(), "Nile Dot Com - Current Shopping Cart Status", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        emptyCartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                newOrder();
            }
        });
        checkOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                checkOut();
            }
        });



        // Add components to the container
        c.add(labels[0]);
        c.add(textFields[0]);
        c.add(labels[1]);
        c.add(textFields[1]);
        c.add(labels[2]);
        c.add(textAreas[0]);
        c.add(labels[3]);
        c.add(textAreas[1]);
        c.add(findItemButton);
        c.add(addItemButton);
        c.add(viewCart);
        c.add(checkOutButton);
        c.add(emptyCartButton);
        c.add(exitButton);

        frame.setVisible(true);
    }




    // Define methods
    public void findItem(String id, int quantity) {
        //read in inventoryCSV
        boolean found = false;
        try (Scanner scanner = new Scanner(new File("inventory.csv"))){
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] values = line.split(",");
                //remove whitespace from values
                for(int i = 0; i < values.length; i++){
                    values[i] = values[i].trim();
                }
                System.out.println("before parseint");
                if(values[0].equals(id)){
                    found = true;
                    if(values[2].equals("false")){
                        //display error message saying not in stock
                        JOptionPane.showMessageDialog(frame, "Sorry.. that item is out of stock. Please try another item", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    else if(quantity > Integer.parseInt(values[3]))
                    {
                        JOptionPane.showMessageDialog(frame, "Insufficient Stock. Only "+values[3] + " on hand. Please reduce the quantity.",  "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
                        //clear quantity text field
                        textFields[1].setText("");
                        break;
                    }
                    else {
                    System.out.println("after parseint");
                    
                    itemFound = true;
                    int discountPercent = 0;
                    addItemButton.setEnabled(itemFound);
                    if(quantity > 14){
                        discountPercent = 20;
                    }
                    else if(quantity > 9){
                        discountPercent = 15;
                    }
                    else if(quantity > 4){
                        discountPercent = 10;
                    }
                    else{
                        discountPercent = 0;
                    }
                    

                        //discount of 10% of quantity greater than 4
                        //discount of 15% if quantity greater than 9
                        //discount of 20% if quantity greater than 14
                    float totalItemPrice = (float) (Float.parseFloat(values[4]) * quantity * (1 - (discountPercent / 100)));
                    currItem = new Item(values[0], values[1], Float.parseFloat(values[4]), quantity, discountPercent, totalItemPrice);

                    textAreas[0].setText(values[0] +" " + values[1] + " $" + values[4] + " " + quantity + " " + discountPercent + "% $" + String.format("%.2f",totalItemPrice));
                    //disable findItemButton
                    labels[2].setText("Details for Item # " + (itemNumber) + ":  ");

                    findItemButton.setEnabled(false);
                    //enable addItemButton
                    addItemButton.setEnabled(true);
                    //disable text fields
                    textFields[0].setEnabled(false);
                    textFields[1].setEnabled(false);
                    //update order subtotal to reflect quantity
                    totalPrice += totalItemPrice;
                    break;
                    }
                }
            }
                //display java swing popup saying item not found
            if(!found)
                JOptionPane.showMessageDialog(frame, "Item ID " + id + " not found", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
             
        }
        catch(Exception e){
            System.out.println("Error: " + e);
        }
        //search for itemID
        //if found, populate textArea with items details
        //if not found display error message
    }

    // Main method
    public void addToCart(String itemID, int quantity){
        //add item to cart
        cart.items[itemNumber-1] = currItem;
        //create a pop up window that says item added to cart
        JOptionPane.showMessageDialog(frame, "Item " + itemID + " accepted. Added to cart", "Nile Dot Com - Item Confirmed", JOptionPane.INFORMATION_MESSAGE);
        //clear text fields
        textFields[0].setText("");
        textFields[1].setText("");
        //increment itemNumber
        itemNumber++;
        //update labels with itemsnumber
        labels[0].setText("Enter item ID for Item # " + itemNumber + ":  ");
        labels[1].setText("Enter quantity for Item # " + itemNumber + ":  ");
        labels[3].setText("Order subtotal for " + (itemNumber - 1) + " item(s)");

        //update order subtotal
        textAreas[1].setText("$" + String.format("%.2f",totalPrice));

        //change text on buttons
        findItemButton.setText("Find Item # " + itemNumber);
        addItemButton.setText("Add Item # " + itemNumber + " To Cart");
        //enable findItemButton
        findItemButton.setEnabled(true);
        //disable addItemButton
        addItemButton.setEnabled(false);
        //enable checkOutButton
        checkOutButton.setEnabled(true);
        //enable viewCartButton
        viewCart.setEnabled(true);
        //enable text fields
        textFields[0].setEnabled(true);
        textFields[1].setEnabled(true);
        
    }

    public void checkOut(){
         java.util.Date date = new java.util.Date();
        System.out.println(date);
        String[] dateAndTime = date.toString().split(" ");
        String[] time = dateAndTime[3].split(":");
        System.out.println(time);
        String dateTime = dateAndTime[2] + " " + dateAndTime[1] + ", " + dateAndTime[5] + ", " + time[0] + ":" + time[1] + ":" + time[2] + " PM EDT";
        System.out.println(dateTime);
        JOptionPane.showMessageDialog(frame,
            "Date: " + dateTime + "\n\nNumber of line items: " + (itemNumber-1) + "\n\nItem# / ID / Title / Price / Qty / Disc % / Subtotal:\n\n" + cart.displayCart() + "\n\n" + "Order subtotal: " + String.format("%.2f", totalPrice) +
            "\n\n" + "Tax rate:     6%\n\nTax amount:    $" + String.format("%.2f",(totalPrice*.06)) + "\n\nORDER TOTAL:     $" + String.format("%.2f",(totalPrice + (totalPrice * .06)))+"\n\nThanks for shopping at Nile Dot Com!", "Nile Dot Com - FINAL INVOICE", JOptionPane.INFORMATION_MESSAGE);
        dateTime = dateAndTime[1] + " " + dateAndTime[2] + ", " + dateAndTime[5] + ", " + time[0] + ":" + time[1] + ":" + time[2] + " PM EDT";
        //add to transactions.csv
        //ID, Title, Price, Quantity, Discount, Total, Date, Time
        //get date and time
       
        //write to transactions.csv
        //create a unique id using permuatation DDMMYYYYHHMMSS
        //change dateAndTime[1] from its abbreviation to its number
        dateAndTime[1] = dateAndTime[1].replace("Jan", "01");
        dateAndTime[1] = dateAndTime[1].replace("Feb", "02");
        dateAndTime[1] = dateAndTime[1].replace("Mar", "03");
        dateAndTime[1] = dateAndTime[1].replace("Apr", "04");
        dateAndTime[1] = dateAndTime[1].replace("May", "05");
        dateAndTime[1] = dateAndTime[1].replace("Jun", "06");
        dateAndTime[1] = dateAndTime[1].replace("Jul", "07");
        dateAndTime[1] = dateAndTime[1].replace("Aug", "08");
        dateAndTime[1] = dateAndTime[1].replace("Sep", "09");
        dateAndTime[1] = dateAndTime[1].replace("Oct", "10");
        dateAndTime[1] = dateAndTime[1].replace("Nov", "11");
        dateAndTime[1] = dateAndTime[1].replace("Dec", "12");
        String uniqueID = dateAndTime[2] + dateAndTime[1] + dateAndTime[5] + time[0] + time[1] + time[2];
        System.out.println(uniqueID+ " <- uniqueID");
        try{
            java.io.FileWriter fw = new java.io.FileWriter("transactions.csv", true);
            for(Item item : cart.items){
                if(item != null){
                    fw.write(uniqueID + ", " + item.itemID + ", " + item.itemTitle + ", " + item.itemPrice + ", " + item.itemQuantity + ", " + item.discountPercent + ".0, $" + item.totalItemPrice + ", " + dateTime + "\n");
                    System.out.println(uniqueID + ", " + item.itemID + ", " + item.itemTitle + "," + item.itemPrice + "," + item.itemQuantity + "," + item.discountPercent + "," + item.totalItemPrice + "," + dateTime);
                }
            }
            fw.write("\n");
            fw.close();
        }
        catch(Exception e){
            System.out.println("Error: " + e);
        }

        //make fields no longer editable
        textFields[0].setEnabled(false);
        textFields[1].setEnabled(false);
        //disable findItemButton
        findItemButton.setEnabled(false);
        //disable addItemButton
        addItemButton.setEnabled(false);
        //disable checkOutButton
        checkOutButton.setEnabled(false);
          
    }

    public void newOrder(){
          //reset gui and variables
        
        itemNumber = 1;
        labels[0].setText("Enter item ID for Item # " + itemNumber + ":  ");
        labels[1].setText("Enter quantity for Item # " + itemNumber + ":  ");
        labels[2].setText("Details for Item # " + itemNumber + ":  ");
        labels[3].setText("Order subtotal for " + 0 + " item(s)" + ":  " );
        totalPrice = 0;
        cart = new Cart();
        currItem = new Item("", "", 0, 0, 0, 0);
        textAreas[0].setText("");
        textAreas[1].setText("");
        textFields[0].setText("");
        textFields[1].setText("");
        findItemButton.setText("Find Item # " + itemNumber);
        addItemButton.setText("Add Item # " + itemNumber + " To Cart");
        findItemButton.setEnabled(true);
        addItemButton.setEnabled(false);
        viewCart.setEnabled(false);
        checkOutButton.setEnabled(false);
        textFields[0].setEnabled(true);
        textFields[1].setEnabled(true);
    }




    public static void main(String[] args) {
        // Create an instance of the App class to launch the GUI
        SwingUtilities.invokeLater(() -> new App());
    }
}
