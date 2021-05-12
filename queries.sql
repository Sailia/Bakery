CREATE TABLE `CS3152SP21_sailia`.`Items`
( `id` INT NOT NULL ,
`name` VARCHAR(50) NOT NULL ,
`price``` REAL NOT NULL ,
`items_to_restrictions_id` INT NOT NULL ,
PRIMARY KEY (`id`)
)

CREATE TABLE Items (
                       ItemID int NOT NULL,
                       OrderID int NOT NULL,
                       Name VARCHAR(50),
                       Price REAL
)

ALTER TABLE Items
ADD PRIMARY KEY (ItemID)

ALTER TABLE Items
MODIFY COLUMN ItemID INT AUTO_INCREMENT

ALTER TABLE Items
DROP COLUMN OrderID

INSERT INTO Items (Name, Price)
VALUES ("Chocolate Chip Muffin", 3.50)

CREATE TABLE Orders ( OrderID int NOT NULL, OrderDate DATE, OrderItemsID INT )

ALTER TABLE Orders ADD PRIMARY KEY (OrderID)

ALTER TABLE Orders MODIFY COLUMN OrderID INT AUTO_INCREMENT

CREATE TABLE OrderItems ( OrderItemsID INT AUTO_INCREMENT, OrderID INT, ItemID INT, PRIMARY KEY(OrderItemsID) )

CREATE TABLE Customers ( CustomerID INT NOT NULL AUTO_INCREMENT, FirstName VARCHAR(50), LastName VARCHAR(50), PRIMARY KEY (CustomerID) )

CREATE TABLE DietaryRestrictions ( DietaryRestrictionsID INT NOT NULL AUTO_INCREMENT, Name VARCHAR(50), PRIMARY KEY(DietaryRestrictionsID) )

INSERT INTO DietaryRestrictions (Name) VALUES ("Vegan"), ("Vegetarian"), ("Gluten free"), ("Lactose free"), ("Nut free")

ALTER TABLE Items ADD DietaryRestrictionsID INT

INSERT INTO Items (Name, Price, DietaryRestrictionsID) VALUES ("Gluten free Chocolate Chip Muffin", 3.50, 3), ("Gluten free Crossaint", 2.50, 3), ("Gluten free Cheese Pastry", 5.00, 3), ("Gluten free Biscotti", 1.50, 3), ("Gluten free Scone", 4.50, 3), ("Lactose free Lemon Poppyseed Muffin", 3.50, 4), ("Lactose free Salmon Quiche", 5.00, 4), ("Lactose free Cheese Pastry", 3.50, 4), ("Nut free Biscotti", 1.50, 5), ("Vegan Scone", 4.00, 1), ("Vegan Chocolate Chip Muffin", 3.50, 1), ("Vegan Crossaint", 2.50, 1), ("Vegan Banana Bread", 5.00, 1), ("Vegan Chocolate Chip Cookie", 1.50, 1), ("Vegan Scone", 4.00, 1), ("Nut free Granola", 8.00, 5), ("Nut free Danish", 3.50, 5), ("Nut free Cranberry Orange Muffin", 3.50, 5)

INSERT INTO Items (Name, Price, DietaryRestrictionsID) VALUES ("Gluten free muesli", 2.99, 3)

INSERT INTO Customers (FirstName, LastName) VALUES ("Syema", "Ailia")

INSERT INTO Customers (FirstName, LastName) VALUES("Eric", "Margules")

CREATE TABLE CustomersToRestrictions ( CustomersToRestrictionsID INT NOT NULL AUTO_INCREMENT, CustomerID INT, DietaryRestrictionsID INT, PRIMARY KEY(CustomersToRestrictionsID), FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID), FOREIGN KEY (DietaryRestrictionsID) REFERENCES DietaryRestrictions(DietaryRestrictionsID) )

ALTER TABLE `CustomersToRestrictions` CHANGE `CustomerID` `CustomerID` INT(11) NOT NULL;

ALTER TABLE `CustomersToRestrictions` CHANGE `DietaryRestrictionsID` `DietaryRestrictionsID` INT(11) NOT NULL;

CREATE VIEW getfoods AS SELECT Name, Price FROM Items WHERE DietaryRestrictionsID IN (3,4)

INSERT INTO Items (Name, Price) VALUES ("Crossaint", 3.50), ("Bacon Doughnut", 2.50), ("Cupcake", 3.00), ("Scone", 3.50), ("Salmon Quiche", 5.00)

CREATE VIEW UnrestrictedFoods AS SELECT Name, Price FROM Items WHERE DietaryRestrictionsID IS NULL
