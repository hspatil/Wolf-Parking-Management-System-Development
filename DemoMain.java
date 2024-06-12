import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class DemoMain {
    static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/slee82";

    public static void main ( final String[] args ) {
    	//Initialize database and insert demo data.
    	InsertDemoData demo = new InsertDemoData();
    	demo.initialize();
    	
        try {

            // Load the driver. This creates an instance of the driver
            // and calls the registerDriver method to make MariaDB Thin
            // driver, available to clients.

            Class.forName( "org.mariadb.jdbc.Driver" );

            final String user = "slee82";
            final String passwd = "200476988";

            Connection conn = null;
            Statement stmt = null;
            final ResultSet rs = null;
            PreparedStatement myStmt = null; 
            
            try {

                // Get a connection from the first driver in the
                // DriverManager list that recognizes the URL jdbcURL

                conn = DriverManager.getConnection( jdbcURL, user, passwd );

                // Create a statement object that will be sending your
                // SQL statements to the DBMS

                stmt = conn.createStatement();
                final Scanner sc = new Scanner( System.in );
                Scanner scanner = new Scanner(System.in);
                int userOption = 0;
                MaintainingPermitsAndVehicleInfo mpav = new MaintainingPermitsAndVehicleInfo();
                
                while ( userOption != 4 ) {
                    System.out.println( "1.Administator" );
                    System.out.println( "2.Security" );
                    System.out.println( "3.Driver" );
                    System.out.println("4.Exit");
                    System.out.print( "Select a user:" );
                    userOption = sc.nextInt();
                    switch ( userOption ) {
                        case 1:
                            int adminOption = 0;
                            while ( adminOption != 32 ) {
                                System.out.println( "1.Create new parking lot" );
                                System.out.println( "2.Update parking lot" );
                                System.out.println( "3.Delete parking lot" );
                                System.out.println( "4.Create new zone" );
                                System.out.println( "5.Update zone" );
                                System.out.println( "6.Delete zone" );
                                System.out.println( "7.Create new space" );
                                System.out.println( "8.Update space" );
                                System.out.println( "9.Delete space" );
                                System.out.println( "10.Create new permit" );
                                System.out.println( "11.Update permit" );
                                System.out.println( "12.Delete permit" );
                                System.out.println( "13.Assign zone to parking lot" );
                                System.out.println( "14.Assign type to space" );
                                System.out.println("15.Check validity of a permit");


                                System.out.println("16.Get citation report");
                                System.out.println("17.Get lot citation report");
                                System.out.println("18.Get lot zone report");
                                System.out.println("19.Get number of car violations");
                                System.out.println("20.Get the number of employee permits in the particular zone");
                                System.out.println("21.Get specific permit information.");
                                System.out.println("22.Get open space numbers");
                                    
                                
                                System.out.println("23.Assigning Permits to drivers");
                                System.out.println("24.Entering vehicle information");
                                System.out.println("25.Updating vehicle information");
                                System.out.println("26.Deleting vehicle information");
                                System.out.println("27.Adding vehicle to permit");
                                System.out.println("28.Deleting vehicle from permit");
                                System.out.println("29.Adding vehicle to owner (driver)");
                                System.out.println("30.Updating vehicle to owner(driver)");
                                System.out.println("31.Removing vehicle to owner(driver)");
                                System.out.println("32.Return to user options" );
                                System.out.println( "\nSelect an option:" );
                                
                                adminOption = sc.nextInt();
                                sc.nextLine();
                                switch ( adminOption ) {
                                    case 1:
                                        informationProcessing.insertParkingLot( stmt, sc );
                                        break;
                                    case 2:
                                        informationProcessing.updateParkingLot( stmt, sc );
                                        break;
                                    case 3:
                                        informationProcessing.deleteParkingLot( stmt, sc );
                                        break;
                                    case 4:
                                        informationProcessing.insertZone( conn, stmt, sc );
                                        break;
                                    case 5:
                                        informationProcessing.updateZone( stmt, sc );
                                        break;
                                    case 6:
                                        informationProcessing.deleteZone( stmt, sc );
                                        break;
                                    case 7:
                                        informationProcessing.insertSpace( stmt, sc );
                                        break;
                                    case 8:
                                        informationProcessing.updateSpace( stmt, sc );
                                        break;
                                    case 9:
                                        informationProcessing.deleteSpace( stmt, sc );
                                        break;
                                    case 10:
                                        informationProcessing.insertPermit( stmt, sc );
                                        break;
                                    case 11:
                                        informationProcessing.updatePermit( stmt, sc );
                                        break;
                                    case 12:
                                        informationProcessing.deletePermit( stmt, sc );
                                        break;
                                    case 13:
                                        informationProcessing.assignZoneToParkingLot( stmt, sc );
                                        break;
                                    case 14:
                                        informationProcessing.assignTypeToSpace( stmt, sc );
                                        break;

                                    case 15:
                                        informationProcessing.permitValidity(myStmt, conn, sc, rs);
                                        break;

                                    case 16:
                                        informationProcessing.getCitationReport(stmt, sc, rs);
                                        break;
                                    case 17:
                                        informationProcessing.getLotCitationReport(myStmt, conn, sc, rs);
                                        break;
                                    case 18:
                                        informationProcessing.getLotZoneReport(stmt, sc, rs);
                                        break;
                                    case 19:
                                        informationProcessing.getNumberOfCarViolationReport(stmt, sc,rs);
                                        break;
                                    case 20:
                                        informationProcessing.getNumberOfEmployeePermit(myStmt, conn, sc,rs);
                                        break;
                                    case 21:
                                        informationProcessing.getPermitInfo(myStmt, conn, sc,rs);
                                        break;
                                    case 22:
                                        informationProcessing.getOpenSpaces(myStmt, conn, sc,rs);
                                        break;
                                    case 23:
                                    	System.out.println("type driver_id: ");
                        				String driver_id = scanner.nextLine();
                        				System.out.println("type permit_id: ");
                        				String permit_id = scanner.nextLine();
                        				System.out.println(mpav.assingingPermitsToDrivers(driver_id, permit_id));
                        				break;
                                    case 24:
                                    	System.out.println("type licenseNo: ");
                        				String licenseNo = scanner.nextLine();
                        				System.out.println("type model: ");
                        				String model = scanner.nextLine();
                        				System.out.println("type color: ");
                        				String color = scanner.nextLine();
                        				System.out.println("type manf: ");
                        				String manf = scanner.nextLine();
                        				System.out.println("type year: ");
                        				int year = scanner.nextInt();
                        				System.out.println(mpav.enterVehicleInfo(licenseNo, model, color, manf, year));
                        				break;		
                                    case 25:
                                    	System.out.println("type licenseNo: ");
                        				licenseNo = scanner.nextLine();
                        				System.out.println("type model: ");
                        				model = scanner.nextLine();
                        				System.out.println("type color: ");
                        				color = scanner.nextLine();
                        				System.out.println("type manf: ");
                        				manf = scanner.nextLine();
                        				System.out.println("type year: ");
                        				year = scanner.nextInt();
                        				System.out.println(mpav.updateVehicleInfo(licenseNo, model, color, manf, year));
                        				break;
                                    case 26:
                                    	System.out.println("type licenseNo: ");
                        				licenseNo = scanner.nextLine();
                        				mpav.deleteVehicleInfo(licenseNo);
                        				break;
                                    case 27:
                                    	System.out.println("type licenseNo: ");
                        				licenseNo = scanner.nextLine();
                        				System.out.println("type permit_id: ");
                        				permit_id = scanner.nextLine();
                        				System.out.println(mpav.addVehicleToPermit(licenseNo, permit_id));
                        				break;
                                    case 28:
                                    	System.out.println("type licenseNo: ");
                        				licenseNo = scanner.nextLine();
                        				System.out.println("type permit_id: ");
                        				permit_id = scanner.nextLine();
                        				System.out.println(mpav.deleteVehicleFromPermit(licenseNo, permit_id));
                        				break;
                                    case 29:
                                    	System.out.println("type driver_id: ");
                        				driver_id = scanner.nextLine();
                        				System.out.println("type licenseNo: ");
                        				licenseNo = scanner.nextLine();
                        				System.out.println(mpav.addOwnerOfVehicle(driver_id, licenseNo));
                        				break;
                                    case 30:
                                    	System.out.println("type driver_id: ");
                        				driver_id = scanner.nextLine();
                        				System.out.println("type licenseNo: ");
                        				licenseNo = scanner.nextLine();
                        				System.out.println(mpav.updateOwnerOfVehicle(driver_id, licenseNo));
                        				break;
                                    case 31:
                                    	System.out.println("type driver_id: ");
                        				driver_id = scanner.nextLine();
                        				System.out.println("type licenseNo: ");
                        				licenseNo = scanner.nextLine();
                        				System.out.println(mpav.removeOwnerOfVehicle(driver_id, licenseNo));
                        				break;
                                    case 32:
                                    	break;
                                        
                                                                            
                                    default:
                                        System.out.println( "Enter valid option" );
                                        break;
                                }
                            }
                            break;
                        case 2:
                            int securityOption = 0;
                            while ( securityOption != 8 ) {
                                System.out.println( "1.Update citation payment" );
                                System.out.println("2.Maintain citation.");
                                System.out.println("3.Generate citation.");
                                System.out.println("4.Delete citation.");
                                System.out.println("5.Assigning Permits to drivers.");
                                System.out.println("6.Adding vehicle to permit");
                                System.out.println("7.Deleting vehicle from permit");
                                System.out.println( "8.Return to user options" );
                                System.out.print( "Select an option:" );
                                securityOption = sc.nextInt();
                                sc.nextLine();
                                switch ( securityOption ) {
                                    case 1:
                                        informationProcessing.updateCitationPayment( stmt, sc );
                                        break;
                                    case 2:
                                    	informationProcessing.maintainCitation(stmt, rs);
                                    	break;
                                    case 3:
                                    	informationProcessing.generateCitation(myStmt, conn, sc);
                                    	break;
                                    case 4:
                                    	informationProcessing.deleteCitation(myStmt, conn, stmt, sc, rs);
                                    	break;
                                    case 5:
                                    	System.out.println("type driver_id: ");
                        				String driver_id = scanner.nextLine();
                        				System.out.println("type permit_id: ");
                        				String permit_id = scanner.nextLine();
                        				System.out.println(mpav.assingingPermitsToDrivers(driver_id, permit_id));
                        				break;
                                    case 6:
                                    	System.out.println("type licenseNo: ");
                        				String licenseNo = scanner.nextLine();
                        				System.out.println("type permit_id: ");
                        				permit_id = scanner.nextLine();
                        				System.out.println(mpav.addVehicleToPermit(licenseNo, permit_id));
                        				break;
                                    case 7:
                                    	System.out.println("type licenseNo: ");
                        				licenseNo = scanner.nextLine();
                        				System.out.println("type permit_id: ");
                        				permit_id = scanner.nextLine();
                        				System.out.println(mpav.deleteVehicleFromPermit(licenseNo, permit_id));
                        				break;
                                    case 8:
                                    	break;
                                    default:
                                        System.out.println( "Enter valid option" );
                                        break;
                                }
                            }
                            break;
                            
                        case 3:
                            int driverOption = 0;
                            while ( driverOption != 6 ) {
                                System.out.println( "1.Create new driver" );
                                System.out.println( "2.Update driver" );
                                System.out.println( "3.Delete driver" );
                                System.out.println( "4.Request citation appeal" );
                                System.out.println( "5.Pay citation" );
                                System.out.println( "6.Return to user options" );
                                System.out.print( "Select an option:" );
                                driverOption = sc.nextInt();
                                sc.nextLine();
                                switch ( driverOption ) {
                                    case 1:
                                        informationProcessing.insertDriver( conn, stmt, sc );
                                        break;
                                    case 2:
                                        informationProcessing.updateDriver( stmt, sc );
                                        break;
                                    case 3:
                                        informationProcessing.deleteDriver( stmt, sc );
                                        break;
                                    case 4:
                                        informationProcessing.requestCitationAppeal( stmt, sc );
                                        break;
                                    case 5:
                                    	informationProcessing.payCitation(myStmt, stmt, conn, sc, rs);
                                }
                            }
                            break;
                        case 4:

                            break;
                        default:
                            System.out.println( "Enter a valid option" );
                    }
                }
                sc.close();

            }
            finally {
                close( rs );
                close( stmt );
                close( conn );
                close (myStmt);
            }
        }
        catch ( final Throwable oops ) {
            oops.printStackTrace();
        }
    }

    static void close ( final Connection conn ) {
        if ( conn != null ) {
            try {
                conn.close();
            }
            catch ( final Throwable whatever ) {
            }
        }
    }
    
    static void close ( final PreparedStatement myStmt ) {
        if ( myStmt != null ) {
            try {
            	myStmt.close();
            }
            catch ( final Throwable whatever ) {
            }
        }
    }

    static void close ( final Statement st ) {
        if ( st != null ) {
            try {
                st.close();
            }
            catch ( final Throwable whatever ) {
            }
        }
    }

    static void close ( final ResultSet rs ) {
        if ( rs != null ) {
            try {
                rs.close();
            }
            catch ( final Throwable whatever ) {
            }
        }
    }
}