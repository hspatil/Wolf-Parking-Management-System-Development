import java.sql.*;

public class InsertDemoData {
	static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/slee82";
	
	private static Connection connection = null;
	private static Statement statement = null;
	private static ResultSet result = null;
	
	
	// Create base relations and insert demo data
	static void initialize() {
		try {
			connectToDatabase();
			
			//Create Base tables;
			statement.executeUpdate("CREATE TABLE Driver (Driver_id VARCHAR(10), "
					+ "Name VARCHAR(25), "+"Status VARCHAR(1), PRIMARY KEY(Driver_id));");
			statement.executeUpdate("CREATE TABLE Vehicles (licenseNo VARCHAR(10), "
					+ "Model VARCHAR(25) NOT NULL, Color VARCHAR(25) NOT NULL, Manf VARCHAR(10), "+
					"Year YEAR, PRIMARY KEY (licenseNo));");
			statement.executeUpdate("CREATE TABLE Citations (Citation_no VARCHAR(5), Fee INT NOT NULL, "+
					"Category VARCHAR(15) NOT NULL, Citation_date DATE NOT NULL, Citation_time TIME NOT NULL, Payment_status BOOL NOT NULL, Appealed BOOL NOT NULL, PRIMARY KEY (Citation_no));");
			statement.executeUpdate("CREATE TABLE Visitors (Driver_id VARCHAR(10), PRIMARY KEY (Driver_id));");
			statement.executeUpdate("CREATE TABLE Student (Driver_id VARCHAR(10), PRIMARY KEY (Driver_id));");
			statement.executeUpdate("CREATE TABLE Employee (Driver_id VARCHAR(10), PRIMARY KEY (Driver_id));");
			statement.executeUpdate("CREATE TABLE Security (sId INT, sName VARCHAR(25) NOT NULL, PRIMARY KEY (sId));");
			statement.executeUpdate("CREATE TABLE Parking_lots (Parking_lots_name VARCHAR(25), Parking_lots_address VARCHAR(50), PRIMARY KEY(Parking_lots_name, Parking_lots_address));");
			statement.executeUpdate("CREATE TABLE Zones (zone_ID VARCHAR(2), Parking_lots_name VARCHAR(25), Parking_lots_address VARCHAR(50), "+
			"PRIMARY KEY(zone_ID, Parking_lots_name, Parking_lots_address), "+ "FOREIGN KEY (Parking_lots_name, Parking_lots_address) REFERENCES Parking_lots (Parking_lots_name, Parking_lots_address) ON UPDATE CASCADE ON DELETE CASCADE);");
			statement.executeUpdate("CREATE TABLE Spaces (Space_number INT AUTO_INCREMENT, Space_type VARCHAR(25) NOT NULL DEFAULT 'regular', Status BOOLEAN NOT NULL, Parking_lots_name VARCHAR(25), "+
			"Parking_lots_address VARCHAR(50), PRIMARY KEY(Space_number, Parking_lots_name, Parking_lots_address), "+
					"FOREIGN KEY(Parking_lots_name, Parking_lots_address) REFERENCES Parking_lots (Parking_lots_name, Parking_lots_address) ON UPDATE CASCADE ON DELETE CASCADE);");
			statement.executeUpdate("CREATE TABLE Permit (Permit_id VARCHAR(25), Permit_type VARCHAR(25) NOT NULL, Start_date DATE NOT NULL, "+
					"Expiration_date DATE NOT NULL, Expiration_time TIME NOT NULL, PRIMARY KEY(Permit_id));");
			statement.executeUpdate("CREATE TABLE Permission_to_Park (Permit_id VARCHAR(25), Parking_lots_name VARCHAR(50), Parking_lots_address VARCHAR(50), zone_id VARCHAR(2), PRIMARY KEY (Permit_id, Parking_lots_name, Parking_lots_address, zone_id), "+
					"FOREIGN KEY (zone_id) REFERENCES Zones (zone_id) ON UPDATE CASCADE ON DELETE CASCADE, "+
					"FOREIGN KEY (Parking_lots_name, Parking_lots_address) REFERENCES Parking_lots(Parking_lots_name, Parking_lots_address) ON UPDATE CASCADE ON DELETE CASCADE);");
			statement.executeUpdate("CREATE TABLE Given_to_Student (Driver_id VARCHAR(10), "+
					"Permit_id VARCHAR(25), PRIMARY KEY (Driver_id, Permit_id), FOREIGN KEY (Permit_id) REFERENCES Permit(Permit_id) ON UPDATE CASCADE ON DELETE CASCADE, FOREIGN KEY (Driver_id) REFERENCES Driver(Driver_id) ON UPDATE CASCADE ON DELETE CASCADE);");
			statement.executeUpdate("CREATE TABLE Given_to_Visitor (Driver_id VARCHAR(10), "+
					"Permit_id VARCHAR(10), PRIMARY KEY (Driver_id, Permit_id), FOREIGN KEY (Permit_id) REFERENCES Permit(Permit_id) ON UPDATE CASCADE ON DELETE CASCADE, FOREIGN KEY (Driver_id) REFERENCES Driver(Driver_id) ON UPDATE CASCADE ON DELETE CASCADE);");
			statement.executeUpdate("CREATE TABLE Given_to_Employee (Driver_id VARCHAR(10), "+
					"Permit_id VARCHAR(10), PRIMARY KEY (Driver_id, Permit_id), FOREIGN KEY (Permit_id) REFERENCES Permit(Permit_id) ON UPDATE CASCADE ON DELETE CASCADE, FOREIGN KEY (Driver_id) REFERENCES Driver(Driver_id) ON UPDATE CASCADE ON DELETE CASCADE);");
			statement.executeUpdate("CREATE TABLE owns (Driver_id VARCHAR(10), "+
					"licenseNo VARCHAR(10), PRIMARY KEY(Driver_id, licenseNo), "+
					"FOREIGN KEY (Driver_id) REFERENCES Driver(Driver_id) ON UPDATE CASCADE ON DELETE CASCADE, "+
					"FOREIGN KEY (licenseNo) REFERENCES Vehicles(licenseNo) ON UPDATE CASCADE ON DELETE CASCADE);"
					);
			statement.executeUpdate("CREATE TABLE issued_to (licenseNo VARCHAR(10), Citation_no VARCHAR(5), PRIMARY KEY(LicenseNo, Citation_no), "+
					"FOREIGN KEY(licenseNo) REFERENCES Vehicles(licenseNo) ON UPDATE CASCADE ON DELETE CASCADE, "+
					"FOREIGN KEY (Citation_no) REFERENCES Citations(Citation_no) ON UPDATE CASCADE ON DELETE CASCADE);");
			statement.executeUpdate("CREATE TABLE makes (sId INT, Citation_no VARCHAR(5), "+
					"PRIMARY KEY(sId, Citation_no), FOREIGN KEY (sId) REFERENCES Security(sId) ON UPDATE CASCADE ON DELETE CASCADE, "+
					"FOREIGN KEY (Citation_no) REFERENCES Citations(Citation_no) ON UPDATE CASCADE ON DELETE CASCADE);");
			statement.executeUpdate("CREATE TABLE of (Citation_no VARCHAR(5), Parking_lots_name VARCHAR(25), Parking_lots_address VARCHAR(50), PRIMARY KEY (Citation_no, Parking_lots_name, Parking_lots_address), "+
					"FOREIGN KEY (Citation_no) REFERENCES Citations(Citation_no) ON UPDATE CASCADE ON DELETE CASCADE, "+
					"FOREIGN KEY (Parking_lots_name, Parking_lots_address) REFERENCES Parking_lots (Parking_lots_name, Parking_lots_address) ON UPDATE CASCADE ON DELETE CASCADE)");
			statement.executeUpdate("CREATE TABLE Given_to_Vehicles (licenseNo VARCHAR(10), "+
					"Permit_id VARCHAR(25), FOREIGN KEY (licenseNo) REFERENCES Vehicles (licenseNo) ON UPDATE CASCADE ON DELETE CASCADE, "+
					"FOREIGN KEY (Permit_id) REFERENCES Permit(Permit_id) ON UPDATE CASCADE ON DELETE CASCADE);");
			
			//insert demo data
			//insert driver demo data
			statement.executeUpdate("INSERT INTO Driver VALUES ('7729119111', 'Sam BankmanFried', 'V')");
			statement.executeUpdate("INSERT INTO Visitors VALUES ('7729119111')");
			statement.executeUpdate("INSERT INTO Driver VALUES ('266399121', 'John Clay', 'E')");
			statement.executeUpdate("INSERT INTO Employee VALUES ('266399121')");
			statement.executeUpdate("INSERT INTO Driver VALUES ('366399121', 'Julia Hicks', 'E')");
			statement.executeUpdate("INSERT INTO Employee VALUES ('366399121')");
			statement.executeUpdate("INSERT INTO Driver VALUES ('466399121', 'Ivan Garcia', 'E')");
			statement.executeUpdate("INSERT INTO Employee VALUES ('466399121')");
			statement.executeUpdate("INSERT INTO Driver VALUES ('122765234', 'Sachin Tendulkar', 'S')");
			statement.executeUpdate("INSERT INTO Student VALUES ('122765234')");
			statement.executeUpdate("INSERT INTO Driver VALUES ('9194789124', 'Charles Xavier', 'V')");
			statement.executeUpdate("INSERT INTO Visitors VALUES ('9194789124')");
			
			//insert parking lot demo data
			statement.executeUpdate("INSERT INTO Parking_lots VALUES ('Poulton Deck', '1021 Main Campus Dr, Raleigh, NC, 27606')");
			statement.executeUpdate("INSERT INTO Parking_lots VALUES ('Partners Way Deck', '851 Partners Way, Raleigh, NC, 27606')");
			statement.executeUpdate("INSERT INTO Parking_lots VALUES ('Dan Allen Parking Deck', '110 Dan Allen Dr, Raleigh, NC, 27607')");
			
			//insert vehicle demo data
			statement.executeUpdate("INSERT INTO Vehicles VALUES ('SBF', 'G-T-R-Nismo', 'Pearl White TriCoat', 'Nissan', 2024)");
			statement.executeUpdate("INSERT INTO Vehicles VALUES ('Clay1', 'Model S', 'Ultra Red', 'Tesla', 2023)");
			statement.executeUpdate("INSERT INTO Vehicles VALUES ('Hicks1', 'M2 Coupe', 'Zandvoort Blue', 'BMW', 2024)");
			statement.executeUpdate("INSERT INTO Vehicles VALUES ('Garcia1', 'Continental GT Speed', 'Blue Fusion', 'Bentley', 2024)");
			statement.executeUpdate("INSERT INTO Vehicles VALUES ('CRICKET', 'Civic SI', 'Sonic Gray Pearl', 'Honda', 2024)");
			statement.executeUpdate("INSERT INTO Vehicles VALUES ('PROFX', 'Taycan Sport Turismo', 'Frozenblue Metallic', 'Porsche', 2024)");
			
			//insert permit demo data
			//Permit #1
			statement.executeUpdate("INSERT INTO Permit VALUES ('VSBF1C', 'Commuter', '2023-01-01', '2024-01-01', '06:00:00')");
			try {
			statement.executeUpdate("INSERT INTO Zones VALUES ('V', 'Poulton Deck', '1021 Main Campus Dr, Raleigh, NC, 27606')");
			}catch (SQLException e) {
				
			}
			statement.executeUpdate("INSERT INTO Permission_to_Park VALUES ('VSBF1C', 'Poulton Deck', '1021 Main Campus Dr, Raleigh, NC, 27606', 'V');");
			statement.executeUpdate("INSERT INTO Given_to_Visitor VALUES ('7729119111', 'VSBF1C')");
			statement.executeUpdate("INSERT INTO Given_to_Vehicles VALUES ('SBF', 'VSBF1C')");
			statement.executeUpdate("INSERT INTO Spaces VALUES (1, 'regular', 0, 'Poulton Deck', '1021 Main Campus Dr, Raleigh, NC, 27606')");
			ResultSet result1 = statement.executeQuery("select Permit_id, Permit_type, zone_id, Driver_id as associateID, licenseNo, Space_type, Start_date, Expiration_date, "
					+ "Expiration_time  from Permit natural join Permission_to_Park natural join Given_to_Visitor natural join Given_to_Vehicles natural join Spaces "
					+ "where Permit_id = 'VSBF1C';");
			while (result1.next()) {
			    String permitID = result1.getString("Permit_id");
			    String permitType = result1.getString("Permit_type");
			    String zoneID = result1.getString("zone_id");
			    String associatedID = result1.getString("Driver_id");
			    String carLicenseNum = result1.getString("licenseNo");
			    String spaceType = result1.getString("Space_type");
			    String startDate = result1.getString("Start_date");
			    String expirationDate = result1.getString("Expiration_date");
			    String expirationTime = result1.getString("Expiration_time");
			   
			    //Print Permit #1
			    System.out.println("Permit #1");
			    System.out.println("permitID: "+ permitID + " permitType: "+ permitType + " zoneID: "+ zoneID + " associatedID: "+ associatedID + " carLicenseNum: "+ carLicenseNum + 
			    		" spaceType: "+ spaceType + " startDate: "+ startDate + " expirationDate: "+ expirationDate + " expirationTime: "+ expirationTime);
			}
			result1.close();
			
			//Permit #2
			statement.executeUpdate("INSERT INTO Permit VALUES ('EJC1R', 'Residential', '2010-01-01', '2030-01-01', '06:00:00')");
			try {
				statement.executeUpdate("INSERT INTO Zones VALUES ('A', 'Poulton Deck', '1021 Main Campus Dr, Raleigh, NC, 27606')");
			}catch (SQLException e) {
				
			}
			statement.executeUpdate("INSERT INTO Permission_to_Park VALUES ('EJC1R', 'Poulton Deck', '1021 Main Campus Dr, Raleigh, NC, 27606', 'A');");
			statement.executeUpdate("INSERT INTO Given_to_Employee VALUES ('266399121', 'EJC1R')");
			statement.executeUpdate("INSERT INTO Given_to_Vehicles VALUES ('Clay1', 'EJC1R')");
			statement.executeUpdate("INSERT INTO Spaces VALUES (null, 'Electric', 0, 'Poulton Deck', '1021 Main Campus Dr, Raleigh, NC, 27606')");
			ResultSet result2 = statement.executeQuery("select Permit_id, Permit_type, zone_id, Driver_id as associateID, licenseNo, Space_type, Start_date, Expiration_date, "
					+ "Expiration_time  from Permit natural join Permission_to_Park natural join Given_to_Employee natural join Given_to_Vehicles natural join Spaces "
					+ "where Permit_id = 'EJC1R'");
			while (result2.next()) {
			    String permitID = result2.getString("Permit_id");
			    String permitType = result2.getString("Permit_type");
			    String zoneID = result2.getString("zone_id");
			    String associatedID = result2.getString("Driver_id");
			    String carLicenseNum = result2.getString("licenseNo");
			    String spaceType = result2.getString("Space_type");
			    String startDate = result2.getString("Start_date");
			    String expirationDate = result2.getString("Expiration_date");
			    String expirationTime = result2.getString("Expiration_time");
			   
			  //Print Permit #2
			    System.out.println("Permit #2");
			    System.out.println("permitID: "+ permitID + " permitType: "+ permitType + " zoneID: "+ zoneID + " associatedID: "+ associatedID + " carLicenseNum: "+ carLicenseNum + 
			    		" spaceType: "+ spaceType + " startDate: "+ startDate + " expirationDate: "+ expirationDate + " expirationTime: "+ expirationTime);
			}
			result2.close();
			
			//Permit #3
			statement.executeUpdate("INSERT INTO Permit VALUES ('EJH2C', 'Commuter', '2023-01-01', '2024-01-01', '06:00:00')");
			try{
				statement.executeUpdate("INSERT INTO Zones VALUES ('A', 'Poulton Deck', '1021 Main Campus Dr, Raleigh, NC, 27606')");
			}catch (SQLException e) {
				
			}
			statement.executeUpdate("INSERT INTO Permission_to_Park VALUES ('EJH2C', 'Poulton Deck', '1021 Main Campus Dr, Raleigh, NC, 27606', 'A');");
			statement.executeUpdate("INSERT INTO Given_to_Employee VALUES ('366399121', 'EJH2C')");
			statement.executeUpdate("INSERT INTO Given_to_Vehicles VALUES ('Hicks1', 'EJH2C')");
			statement.executeUpdate("INSERT INTO Spaces VALUES (null, 'regular', 0, 'Poulton Deck', '1021 Main Campus Dr, Raleigh, NC, 27606')");
			ResultSet result3 = statement.executeQuery("select Permit_id, Permit_type, zone_id, Driver_id as associateID, licenseNo, Space_type, Start_date, Expiration_date, "
					+ "Expiration_time  from Permit natural join Permission_to_Park natural join Given_to_Employee natural join Given_to_Vehicles natural join Spaces "
					+ "where Permit_id = 'EJH2C'");
			while (result3.next()) {
			    String permitID = result3.getString("Permit_id");
			    String permitType = result3.getString("Permit_type");
			    String zoneID = result3.getString("zone_id");
			    String associatedID = result3.getString("Driver_id");
			    String carLicenseNum = result3.getString("licenseNo");
			    String spaceType = result3.getString("Space_type");
			    String startDate = result3.getString("Start_date");
			    String expirationDate = result3.getString("Expiration_date");
			    String expirationTime = result3.getString("Expiration_time");
			   
			  //Print Permit #3
			    System.out.println("Permit #3");
			    System.out.println("permitID: "+ permitID + " permitType: "+ permitType + " zoneID: "+ zoneID + " associatedID: "+ associatedID + " carLicenseNum: "+ carLicenseNum + 
			    		" spaceType: "+ spaceType + " startDate: "+ startDate + " expirationDate: "+ expirationDate + " expirationTime: "+ expirationTime);
			}
			result3.close();
			
			//Permit #4
			statement.executeUpdate("INSERT INTO Permit VALUES ('EIG3C', 'Commuter', '2023-01-01', '2024-01-01', '06:00:00')");
			try{
				statement.executeUpdate("INSERT INTO Zones VALUES ('A', 'Partners Way Deck', '851 Partners Way, Raleigh, NC, 27606')");
			}catch (SQLException e) {
				
			}
			statement.executeUpdate("INSERT INTO Permission_to_Park VALUES ('EIG3C', 'Partners Way Deck', '851 Partners Way, Raleigh, NC, 27606', 'A');");
			statement.executeUpdate("INSERT INTO Given_to_Employee VALUES ('466399121', 'EIG3C')");
			statement.executeUpdate("INSERT INTO Given_to_Vehicles VALUES ('Garcia1', 'EIG3C')");
			statement.executeUpdate("INSERT INTO Spaces VALUES (null, 'regular', 0, 'Partners Way Deck', '851 Partners Way, Raleigh, NC, 27606')");
			ResultSet result4 = statement.executeQuery("select Permit_id, Permit_type, zone_id, Driver_id as associateID, licenseNo, Space_type, Start_date, Expiration_date, "
					+ "Expiration_time  from Permit natural join Permission_to_Park natural join Given_to_Employee natural join Given_to_Vehicles natural join Spaces "
					+ "where Permit_id = 'EIG3C'");
			while (result4.next()) {
			    String permitID = result4.getString("Permit_id");
			    String permitType = result4.getString("Permit_type");
			    String zoneID = result4.getString("zone_id");
			    String associatedID = result4.getString("Driver_id");
			    String carLicenseNum = result4.getString("licenseNo");
			    String spaceType = result4.getString("Space_type");
			    String startDate = result4.getString("Start_date");
			    String expirationDate = result4.getString("Expiration_date");
			    String expirationTime = result4.getString("Expiration_time");
			   
			  //Print Permit #3
			    System.out.println("Permit #4");
			    System.out.println("permitID: "+ permitID + " permitType: "+ permitType + " zoneID: "+ zoneID + " associatedID: "+ associatedID + " carLicenseNum: "+ carLicenseNum + 
			    		" spaceType: "+ spaceType + " startDate: "+ startDate + " expirationDate: "+ expirationDate + " expirationTime: "+ expirationTime);
			}
			result4.close();
			
			//Permit #5
			statement.executeUpdate("INSERT INTO Permit VALUES ('SST1R', 'Residential', '2023-01-01', '2023-09-30', '06:00:00')");
			try{
				statement.executeUpdate("INSERT INTO Zones VALUES ('AS', 'Partners Way Deck', '851 Partners Way, Raleigh, NC, 27606')");
			}catch (SQLException e) {
				
			}
			statement.executeUpdate("INSERT INTO Permission_to_Park VALUES ('SST1R', 'Partners Way Deck', '851 Partners Way, Raleigh, NC, 27606', 'AS');");
			statement.executeUpdate("INSERT INTO Given_to_Student VALUES ('122765234', 'SST1R')");
			statement.executeUpdate("INSERT INTO Given_to_Vehicles VALUES ('CRICKET', 'SST1R')");
			statement.executeUpdate("INSERT INTO Spaces VALUES (null, 'Compact Car', 0, 'Partners Way Deck', '851 Partners Way, Raleigh, NC, 27606')");
			ResultSet result5 = statement.executeQuery("select Permit_id, Permit_type, zone_id, Driver_id as associateID, licenseNo, Space_type, Start_date, Expiration_date, "
					+ "Expiration_time  from Permit natural join Permission_to_Park natural join Given_to_Student natural join Given_to_Vehicles natural join Spaces "
					+ "where Permit_id = 'SST1R'");
			while (result5.next()) {
			    String permitID = result5.getString("Permit_id");
			    String permitType = result5.getString("Permit_type");
			    String zoneID = result5.getString("zone_id");
			    String associatedID = result5.getString("Driver_id");
			    String carLicenseNum = result5.getString("licenseNo");
			    String spaceType = result5.getString("Space_type");
			    String startDate = result5.getString("Start_date");
			    String expirationDate = result5.getString("Expiration_date");
			    String expirationTime = result5.getString("Expiration_time");
			   
			  //Print Permit #5
			    System.out.println("Permit #5");
			    System.out.println("permitID: "+ permitID + " permitType: "+ permitType + " zoneID: "+ zoneID + " associatedID: "+ associatedID + " carLicenseNum: "+ carLicenseNum + 
			    		" spaceType: "+ spaceType + " startDate: "+ startDate + " expirationDate: "+ expirationDate + " expirationTime: "+ expirationTime);
			}
			result5.close();
			
			//Permit #6
			statement.executeUpdate("INSERT INTO Permit VALUES ('VCX1SE', 'Special event', '2023-01-01', '2023-11-15', '06:00:00')");
			try{
				statement.executeUpdate("INSERT INTO Zones VALUES ('V', 'Dan Allen Parking Deck', '110 Dan Allen Dr, Raleigh, NC, 27607')");
			}catch (SQLException e) {
				
			}
			statement.executeUpdate("INSERT INTO Permission_to_Park VALUES ('VCX1SE', 'Dan Allen Parking Deck', '110 Dan Allen Dr, Raleigh, NC, 27607', 'V');");
			statement.executeUpdate("INSERT INTO Given_to_Visitor VALUES ('9194789124', 'VCX1SE')");
			statement.executeUpdate("INSERT INTO Given_to_Vehicles VALUES ('PROFX', 'VCX1SE')");
			statement.executeUpdate("INSERT INTO Spaces VALUES (null, 'Handicap', 0, 'Dan Allen Parking Deck', '110 Dan Allen Dr, Raleigh, NC, 27607')");
			ResultSet result6 = statement.executeQuery("select Permit_id, Permit_type, zone_id, Driver_id as associateID, licenseNo, Space_type, Start_date, Expiration_date, "
					+ "Expiration_time  from Permit natural join Permission_to_Park natural join Given_to_Visitor natural join Given_to_Vehicles natural join Spaces "
					+ "where Permit_id = 'VCX1SE'");
			while (result6.next()) {
			    String permitID = result6.getString("Permit_id");
			    String permitType = result6.getString("Permit_type");
			    String zoneID = result6.getString("zone_id");
			    String associatedID = result6.getString("Driver_id");
			    String carLicenseNum = result6.getString("licenseNo");
			    String spaceType = result6.getString("Space_type");
			    String startDate = result6.getString("Start_date");
			    String expirationDate = result6.getString("Expiration_date");
			    String expirationTime = result6.getString("Expiration_time");
			   
			  //Print Permit #6
			    System.out.println("Permit #6");
			    System.out.println("permitID: "+ permitID + " permitType: "+ permitType + " zoneID: "+ zoneID + " associatedID: "+ associatedID + " carLicenseNum: "+ carLicenseNum + 
			    		" spaceType: "+ spaceType + " startDate: "+ startDate + " expirationDate: "+ expirationDate + " expirationTime: "+ expirationTime);
			}
			result6.close();
			
			//insert Citation demo data
			//Citation #1
			statement.executeUpdate("INSERT INTO Citations VALUES ('NP1', 40, 'No Permit', '2021-10-11', '08:00:00', 1, 0)");
			statement.executeUpdate("INSERT INTO of VALUES ('NP1', 'Dan Allen Parking Deck', '110 Dan Allen Dr, Raleigh, NC, 27607')");
			statement.executeUpdate("INSERT INTO Vehicles VALUES ('VAN-9910', 'Macan GTS', 'Papaya Metallic', Null, Null)");
			statement.executeUpdate("INSERT INTO issued_to VALUES ('VAN-9910', 'NP1')");
			ResultSet result7 = statement.executeQuery("select Citation_no, Citation_date, Citation_time, Payment_status, Parking_lots_name, Category, Fee, licenseNo, model, color "
					+ "FROM Citations natural join of CROSS JOIN Vehicles "+
					"WHERE Citation_no = 'NP1' AND licenseNo = 'VAN-9910'");
			
			//Print Citation #1
			while (result7.next()) {
			    String citationNum = result7.getString("Citation_no");
			    String citationDate = result7.getString("Citation_date");
			    String citationTime = result7.getString("Citation_time");
			    Boolean paymentStatus = result7.getBoolean("Payment_status");
			    String lotName = result7.getString("Parking_lots_name");
			    String category = result7.getString("Category");
			    int fee = result7.getInt("Fee");
			    String licenseNum = result7.getString("licenseNo");
			    String model = result7.getString("model");
			    String color = result7.getString("color");
			   
			  //Print Permit #6
			    System.out.println("Citation #1");
			    System.out.println("CitationNum: "+ citationNum + " CitationDate: "+ citationDate + " CitationTime: "+ citationTime + " paymentStatus: "+ paymentStatus + 
			    		" lotName: "+ lotName + " category: "+ category + " fee: "+ fee + " licenseNum: "+ licenseNum + " model: "+ model + " color: "+ color);
			}
			result7.close();
			
			//Citation #2
			statement.executeUpdate("INSERT INTO Citations VALUES ('EP1', 30, 'Expired Permit', '2023-10-01', '08:00:00', 0, 0)");
			statement.executeUpdate("INSERT INTO of VALUES ('EP1', 'Poulton Deck', '1021 Main Campus Dr, Raleigh, NC, 27606')");
			try{
				statement.executeUpdate("INSERT INTO Vehicles VALUES ('CRICKET', 'Civic SI', 'Sonic Gray Pearl', Null, Null)");
			}catch(SQLException e) {
				
			}
			statement.executeUpdate("INSERT INTO issued_to VALUES ('CRICKET', 'EP1')");
			ResultSet result8 = statement.executeQuery("select Citation_no, Citation_date, Citation_time, Payment_status, Parking_lots_name, Category, Fee, licenseNo, model, color "
					+ "FROM Citations natural join of CROSS JOIN Vehicles "+
					"WHERE Citation_no = 'EP1' AND licenseNo = 'CRICKET'");
			
			//Print Citation #2
			while (result8.next()) {
			    String citationNum = result8.getString("Citation_no");
			    String citationDate = result8.getString("Citation_date");
			    String citationTime = result8.getString("Citation_time");
			    Boolean paymentStatus = result8.getBoolean("Payment_status");
			    String lotName = result8.getString("Parking_lots_name");
			    String category = result8.getString("Category");
			    int fee = result8.getInt("Fee");
			    String licenseNum = result8.getString("licenseNo");
			    String model = result8.getString("model");
			    String color = result8.getString("color");
			   
			  //Print Citation #2
			    System.out.println("Citation #2");
			    System.out.println("CitationNum: "+ citationNum + " CitationDate: "+ citationDate + " CitationTime: "+ citationTime + " paymentStatus: "+ paymentStatus + 
			    		" lotName: "+ lotName + " category: "+ category + " fee: "+ fee + " licenseNum: "+ licenseNum + " model: "+ model + " color: "+ color);
			}
			result8.close();			
			statement.executeUpdate("INSERT INTO owns (Driver_id, licenseNo) Values ('7729119111', 'SBF'), ('266399121', 'Clay1'), ('366399121', 'Hicks1'), ('466399121', 'Garcia1'), ('122765234', 'CRICKET'), ('9194789124', 'PROFX')");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// When connect to database delete all tables
	static void connectToDatabase() throws ClassNotFoundException, SQLException {
		Class.forName("org.mariadb.jdbc.Driver");
		
		String user = "slee82";
		String password = "200476988";
		
		connection = DriverManager.getConnection(jdbcURL, user, password);
		statement = connection.createStatement();
		
		try {
			//delete all tables
			statement.executeUpdate("DROP TABLE Given_to_Student");
			statement.executeUpdate("DROP TABLE Given_to_Visitor");
			statement.executeUpdate("DROP TABLE Given_to_Employee");
			statement.executeUpdate("DROP TABLE owns");
			statement.executeUpdate("DROP TABLE issued_to");
			statement.executeUpdate("DROP TABLE makes");
			statement.executeUpdate("DROP TABLE of");
			statement.executeUpdate("DROP TABLE Given_to_Vehicles");
			statement.executeUpdate("DROP TABLE Permission_to_Park");
			statement.executeUpdate("DROP TABLE Driver");
			statement.executeUpdate("DROP TABLE Vehicles");
			statement.executeUpdate("DROP TABLE Citations");
			statement.executeUpdate("DROP TABLE Visitors");
			statement.executeUpdate("DROP TABLE Student");
			statement.executeUpdate("DROP TABLE Employee");
			statement.executeUpdate("DROP TABLE Security");
			statement.executeUpdate("DROP TABLE Zones");
			statement.executeUpdate("DROP TABLE Spaces");
			statement.executeUpdate("DROP TABLE Parking_lots");
			statement.executeUpdate("DROP TABLE Permit");
		}catch (SQLException e) {
			
		}
	}
		
	static void close() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (result != null) {
			try {
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
