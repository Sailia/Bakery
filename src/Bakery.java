import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Bakery {
    static Connection conn;
    boolean isQuestionnaireComplete;
    public static void main(String[] args) {

        try {
            String url = "jdbc:mysql://cs.neiu.edu:3306/CS3152SP21_sailia?serverTimezone=UTC&";
            url += "user=CS3152SP21_sailia&password=sailia523434";

            Scanner nameObj = new Scanner(System.in);
            System.out.println(welcome());
            String name = nameObj.nextLine();

            conn = DriverManager.getConnection(url);

            ResultSet rs = statementQuery("SELECT * FROM Customers WHERE FirstName = '" + name +"'");

            int customerId;

            if(!rs.next()) {
                String lName = register(name);
                String insertNewCustomer = "INSERT INTO Customers (FirstName, LastName) ";
                insertNewCustomer += "VALUES('" + name + "', '" + lName + "')";
                int customer = statementUpdate(insertNewCustomer);
                System.out.println(registered(name));

                String getCustomerIDQuery = "SELECT CustomerID FROM Customers WHERE FirstName= '" + name + "' AND LastName= '" + lName + "'";
                ResultSet customerIDQuery = statementQuery(getCustomerIDQuery);
                customerIDQuery.next();
                customerId = customerIDQuery.getInt("CustomerID");

                ArrayList<String> restrictions =  getDietaryRestrictions();

                if(!restrictions.isEmpty()) {
                    String insert = "INSERT INTO CustomersToRestrictions (CustomerID, DietaryRestrictionsID)";
                    insert += "VALUES";
                    for(int j = 0; j < restrictions.size(); j++) {
                        // insert into CustomersToRestrictions table where restrictions ID maps to customerID
                        insert += "(" + customerId + ", " + restrictions.get(j) + ")";
                        if (j < restrictions.size() - 1) {
                            insert += ", ";
                        }
                    }
                    int insertQuery = statementUpdate(insert);
                }
            } else {
                customerId = rs.getInt("CustomerID");
                String firstName = rs.getString("FirstName");
                System.out.println("Welcome back, " + firstName + "!");
            }
            ArrayList<String> foods = getRestrictedFoods(customerId);
            printRestrictedFoods(foods);
        } catch(SQLException ex) {
            ex.printStackTrace();

        }
    }

    public static String welcome() {
        return "Hi there, can I get your first name?";
    }

    public static String register(String name) {
        System.out.println(name + ", we will need to register you. Please provide your last name.");
        Scanner lastNameObj = new Scanner(System.in);
        return lastNameObj.nextLine();
    }

    public static ResultSet statementQuery(String query) {
        ResultSet rs = null;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    public static int statementUpdate(String query) {
        int rs = -1;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeUpdate(query);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    public static String registered(String name) {
        return "Thank you " + name + ", you are now registered.";
    }

    public static ArrayList<String> getDietaryRestrictions() {
        boolean notDone = true;
        ArrayList<String> restrictionsList = new ArrayList<String>();
        while (notDone) {
            System.out.println("Do you have any dietary restrictions you'd like to add? Please enter y/n");
            Scanner restrictionObj = new Scanner(System.in);
            String restriction = restrictionObj.nextLine();
            if(restriction.equals("y")) {
                System.out.println("Please enter a number pertaining to your dietary restrictions");
                System.out.println("[1] Vegan \n[2] Keto \n[3] Gluten free \n[4] Lactose free \n[5] Nut free");
                restriction = restrictionObj.nextLine();
                restrictionsList.add(restriction);
            } else {
                notDone = false;
            }
        }
        return restrictionsList;
    }

    public static ArrayList<String> getRestrictedFoods(int customerId) {
        ArrayList<String> results = new ArrayList<String>();
        try {
            ResultSet options = statementQuery("SELECT DietaryRestrictionsID FROM CustomersToRestrictions WHERE CustomerID =" + customerId);
            ArrayList<Integer> restrictionIds = new ArrayList<Integer>();
            while (options.next()) {
                restrictionIds.add(options.getInt("DietaryRestrictionsID"));
            }

            String queryBase = "";
            if (restrictionIds.size() > 0) {
                System.out.println("These are what we have in store for you today");
                queryBase = "SELECT Name From Items WHERE DietaryRestrictionsID IN (";
                for (int i = 0; i < restrictionIds.size(); i++) {
                    queryBase += restrictionIds.get(i).toString();
                    if (i < restrictionIds.size() - 1) {
                        queryBase += ", ";
                    }
                }
                queryBase += ")";
            } else {
                queryBase = "Select Name from UnrestrictedFoods";
            }
            ResultSet availableFoods = statementQuery(queryBase);
            while(availableFoods.next()) {
                results.add(availableFoods.getString("Name"));
            }

        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return results;
    }
    public static void printRestrictedFoods(ArrayList<String> foods) {
        System.out.println("Here's what you can order: ");
        for(int i = 0; i < foods.size(); i++) {
            System.out.println(foods.get(i));
        }
    }
}