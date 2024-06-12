// Acknowledgments: This example is a modification of code provided
// by Dimitri Rakitine. Further modified by Shrikanth N C for MySql(MariaDB)
// support
// Relpace all $USER$ with your unity id and $PASSWORD$ with your 9 digit
// student id or updated password (if changed)

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Date;
import java.util.Scanner;

public class informationProcessing {

    /**
     * Creates new driver as well as adding the drivers id to their respective
     * status table. Uses transaction to ensure the driver id fits the status
     * they are assigned to.
     *
     * @param conn
     *            connection used to call commit methods
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @return true if successful and false if error occurs
     * @author Chris
     */
    static Boolean insertDriver ( final Connection conn, final Statement stmt, final Scanner sc ) throws SQLException {

        System.out.print( "Enter driver id:" );
        final String id = sc.nextLine();
        System.out.print( "Enter driver name:" );
        final String name = sc.nextLine();
        System.out.print( "Enter driver status:" );
        String status = sc.next();
        status = status.toUpperCase();
        String sql = "INSERT INTO Driver VALUES('%s', '%s', '%s')";
        sql = String.format( sql, id, name, status );
        try {
            conn.setAutoCommit( false );
            stmt.executeUpdate( sql );
            if ( status.equals( "S" ) ) {
                if ( id.length() != 9 ) {
                    conn.rollback();
                    conn.setAutoCommit( true );
                    return false;
                }
                String sql1 = "INSERT INTO Student VALUES('%s')";
                sql1 = String.format( sql1, id );
                stmt.executeUpdate( sql1 );
                conn.commit();
            }
            else if ( status.equals( "V" ) ) {
                if ( id.length() != 10 ) {
                    conn.rollback();
                    conn.setAutoCommit( true );
                    return false;
                }
                String sql1 = "INSERT INTO Visitor VALUES('%s')";
                sql1 = String.format( sql1, id );
                stmt.executeUpdate( sql1 );
                conn.commit();
            }
            else if ( status.equals( "E" ) ) {
                if ( id.length() != 9 ) {
                    conn.rollback();
                    conn.setAutoCommit( true );
                    return false;
                }
                String sql1 = "INSERT INTO Employee VALUES('%s')";
                sql1 = String.format( sql1, id );
                stmt.executeUpdate( sql1 );
                conn.commit();
            }
            else {
                conn.rollback();
                conn.setAutoCommit( true );
                return false;
            }
            conn.setAutoCommit( true );
            System.out.println( "Succesfully added driver" );
            return true;
        }
        catch ( final Exception e ) {
            System.out.println( "Error adding driver" );
            conn.rollback();
            return false;
        }
    }

    /**
     * updates driver with new information and changes the status table it is in
     * if the status is changed
     *
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @return true if successful and false if error occurs
     * @author Chris
     *
     */
    static Boolean updateDriver ( final Statement stmt, final Scanner sc ) {

        System.out.print( "Enter driver id:" );
        final String id = sc.nextLine();
        System.out.print( "Enter new driver id:" );
        final String id1 = sc.nextLine();
        System.out.print( "Enter new driver name:" );
        final String name = sc.nextLine();
        System.out.print( "Enter new driver status:" );
        final String status = sc.next();
        status.toUpperCase();
        String sql = "UPDATE Driver SET Name = '%s',Driver_id = '%s', Status = '%s' WHERE Driver_id = '%s'";
        sql = String.format( sql, name, id1, status, id );
        String status1 = null;
        try {
            final ResultSet chosenStatus = stmt
                    .executeQuery( "SELECT Status FROM Driver WHERE Driver_id = '" + id + "'" );
            if ( chosenStatus.next() ) {
                status1 = chosenStatus.getString( "Status" );
            }
        }
        catch ( final SQLException e1 ) {
        }
        try {
            if ( status1.equals( "S" ) ) {
                String sql1 = "DELETE FROM Student WHERE Driver_id = '%s'";
                sql1 = String.format( sql1, id );
                stmt.executeUpdate( sql1 );
            }
            else if ( status1.equals( "V" ) ) {
                String sql1 = "DELETE FROM Visitors WHERE Driver_id = '%s'";
                sql1 = String.format( sql1, id );
                stmt.executeUpdate( sql1 );
            }
            else if ( status1.equals( "E" ) ) {
                String sql1 = "DELETE FROM Employee WHERE Driver_id = '%s'";
                sql1 = String.format( sql1, id );
                stmt.executeUpdate( sql1 );
            }
            final int affectedRows = stmt.executeUpdate( sql );
            if ( status.equals( "S" ) ) {
                String sql1 = "INSERT INTO Student VALUES('%s')";
                sql1 = String.format( sql1, id1 );
                stmt.executeUpdate( sql1 );
            }
            else if ( status.equals( "V" ) ) {
                String sql1 = "INSERT INTO Visitor VALUES('%s')";
                sql1 = String.format( sql1, id1 );
                stmt.executeUpdate( sql1 );
            }
            else if ( status.equals( "E" ) ) {
                String sql1 = "INSERT INTO Employee VALUES('%s')";
                sql1 = String.format( sql1, id1 );
                stmt.executeUpdate( sql1 );
            }
            else {

                throw new Exception( "Invalid status" );
            }
            if ( affectedRows > 0 ) {
                System.out.println( "Succesfully updated driver" );
                return true;
            }
            else {
                System.out.println( "Error updating driver" );
                return false;
            }
        }
        catch ( final Exception e ) {
            System.out.println( "Error updating driver" );
            return false;
        }
    }

    /**
     * deletes driver from driver table and its respective status table
     *
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @return true if successful and false if error occurs
     * @author Chris
     */
    static Boolean deleteDriver ( final Statement stmt, final Scanner sc ) {

        System.out.print( "Enter driver id:" );
        final String id = sc.nextLine();
        String sql = "DELETE FROM Driver WHERE Driver_id = '%s';";
        sql = String.format( sql, id );
        String status = null;
        try {
            final ResultSet chosenStatus = stmt
                    .executeQuery( "SELECT Status FROM Driver WHERE Driver_id = '" + id + "'" );
            if ( chosenStatus.next() ) {
                status = chosenStatus.getString( "Status" );
            }
        }
        catch ( final SQLException e1 ) {
        }
        try {
            if ( status.equals( "S" ) ) {
                String sql1 = "DELETE FROM Student WHERE Driver_id = '%s'";
                sql1 = String.format( sql1, id );
                stmt.executeUpdate( sql1 );
            }
            else if ( status.equals( "V" ) ) {
                String sql1 = "DELETE FROM Visitors WHERE Driver_id = '%s'";
                sql1 = String.format( sql1, id );
                stmt.executeUpdate( sql1 );
            }
            else if ( status.equals( "E" ) ) {
                String sql1 = "DELETE FROM Employee WHERE Driver_id = '%s'";
                sql1 = String.format( sql1, id );
                stmt.executeUpdate( sql1 );
            }
            final int affectedRows = stmt.executeUpdate( sql );
            if ( affectedRows > 0 ) {
                System.out.println( "Succesfully deleted driver" );
                return true;
            }
            else {
                System.out.println( "No driver found" );
                return false;
            }
        }
        catch ( final Exception e ) {
            System.out.println( "Error deleting driver" );
            return false;
        }
    }

    /**
     * Creates new parking lot with name and address
     *
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @return true if successful and false if error occurs
     * @author Chris
     */
    static Boolean insertParkingLot ( final Statement stmt, final Scanner sc ) {

        System.out.print( "Enter parking lot name:" );
        final String name = sc.nextLine();
        System.out.print( "Enter parking lot address:" );
        final String address = sc.nextLine();
        String sql = "INSERT INTO Parking_lots VALUES('%s', '%s')";
        sql = String.format( sql, name, address );
        try {
            stmt.executeUpdate( sql );
            System.out.println( "Succesfully added parking lot" );
            return true;
        }
        catch ( final Exception e ) {
            System.out.println( "Error adding parking Lot" );
            return false;
        }
    }

    /**
     * updates parking lot with new name and address
     *
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @return true if successful and false if error occurs
     * @author Chris
     */
    static Boolean updateParkingLot ( final Statement stmt, final Scanner sc ) {

        System.out.print( "Enter parking lot name:" );
        final String name = sc.nextLine();
        System.out.print( "Enter parking lot address:" );
        final String address = sc.nextLine();
        System.out.print( "Enter new parking lot name:" );
        final String name1 = sc.nextLine();
        System.out.print( "Enter new parking lot address:" );
        final String address1 = sc.nextLine();
        String sql = "UPDATE Parking_lots SET Parking_lots_name='%s', Parking_lots_address = '%s' WHERE Parking_lots_name = '%s' AND Parking_lots_address = '%s'";
        sql = String.format( sql, name1, address1, name, address );
        try {
            final int affectedRows = stmt.executeUpdate( sql );

            if ( affectedRows > 0 ) {
                System.out.println( "Succesfully updated parking lot" );
                return true;
            }
            else {
                System.out.println( "No parking lot found" );
                return false;
            }
        }
        catch ( final Exception e ) {
            System.out.println( e);
            return false;
        }
    }

    /**
     * deletes parking lot that matches entered keys
     *
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @return true if successful and false if error occurs
     * @author Chris
     */
    static Boolean deleteParkingLot ( final Statement stmt, final Scanner sc ) {

        System.out.print( "Enter parking lot name:" );
        final String name = sc.nextLine();
        System.out.print( "Enter parking lot address:" );
        final String address = sc.nextLine();
        String sql = "DELETE FROM Parking_lots WHERE Parking_lots_name = '%s' AND Parking_lots_address = '%s'";
        sql = String.format( sql, name, address );
        try {
            final int affectedRows = stmt.executeUpdate( sql );
            if ( affectedRows > 0 ) {
                System.out.println( "Succesfully deleted parking lot" );
                return true;
            }
            else {
                System.out.println( "No parking lot found" );
                return false;
            }
        }
        catch ( final Exception e ) {
            System.out.println( "error deleting parking lot" );
            return false;
        }
    }

    /**
     * creates new zone which is connected to a parking lot Uses transaction to
     * ensure the zone id is valid and if not it rolls back the query
     *
     * @param conn
     *            connection used to call commit methods
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @return true if successful and false if error occurs
     * @author Chris
     */
    static Boolean insertZone ( final Connection conn, final Statement stmt, final Scanner sc ) throws SQLException {
        System.out.print( "Enter zone ID:" );
        final String id = sc.nextLine();
        System.out.print( "Enter parking lot name:" );
        final String name = sc.nextLine();
        System.out.print( "Enter parking lot address:" );
        final String address = sc.nextLine();
        String sql = "INSERT INTO Zones VALUES('%s', '%s','%s')";
        sql = String.format( sql, id, name, address );
        try {
            conn.setAutoCommit( false );
            stmt.executeUpdate( sql );
            if ( id.equals( "A" ) || id.equals( "B" ) || id.equals( "C" ) || id.equals( "D" ) || id.equals( "AS" )
                    || id.equals( "BS" ) || id.equals( "CS" ) || id.equals( "DS" ) || id.equals( "V" ) ) {
                conn.commit();
                conn.setAutoCommit( true );
                System.out.println( "Succesfully added zone" );
                return true;
            }
            else {
                conn.rollback();
                conn.setAutoCommit( true );
                System.out.println( "Error adding zone" );
                return false;
            }
        }
        catch ( final Exception e ) {
            conn.rollback();
            conn.setAutoCommit( true );
            System.out.println( "Error adding zone" );
            return false;
        }
    }

    /**
     * updates zone which only allows changing the zone ID since it is connected
     * to a parking lot
     *
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @return true if successful and false if error occurs
     * @author Chris
     */
    static Boolean updateZone ( final Statement stmt, final Scanner sc ) {
        System.out.print( "Enter zone ID:" );
        final String id = sc.nextLine();
        System.out.print( "Enter parking lot name:" );
        final String name = sc.nextLine();
        System.out.print( "Enter parking lot address:" );
        final String address = sc.nextLine();
        System.out.print( "Enter new zone ID:" );
        final String id1 = sc.nextLine();
        String sql = "UPDATE Zones SET Zone_id='%s' WHERE Parking_lots_name = '%s' AND Parking_lots_address = '%s' AND zone_ID='%s'";
        sql = String.format( sql, id1, name, address, id );
        try {
            final int affectedRows = stmt.executeUpdate( sql );
            if ( affectedRows > 0 ) {
                System.out.println( "Succesfully updated zone" );
                return true;
            }
            else {
                System.out.println( "No zone found" );
                return false;
            }
        }
        catch ( final Exception e ) {
            System.out.println( "Error updating zone" );
            return false;
        }
    }

    /**
     * deletes zone that matches entered keys
     *
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @return true if successful and false if error occurs
     * @author Chris
     */
    static Boolean deleteZone ( final Statement stmt, final Scanner sc ) {
        System.out.print( "Enter zone ID:" );
        final String id = sc.nextLine();
        System.out.print( "Enter parking lot name:" );
        final String name = sc.nextLine();
        System.out.print( "Enter parking lot address:" );
        final String address = sc.nextLine();
        String sql = "DELETE FROM Zones WHERE Parking_lots_name = '%s' AND Parking_lots_address = '%s' AND zone_ID='%s'";
        sql = String.format( sql, name, address, id );
        try {
            final int affectedRows = stmt.executeUpdate( sql );
            if ( affectedRows > 0 ) {
                System.out.println( "Succesfully deleted zone" );
                return true;
            }
            else {
                System.out.println( "No zone found" );
                return false;
            }
        }
        catch ( final Exception e ) {
            System.out.println( "error deleting zone" );
            return false;
        }
    }

    /**
     * creates space with type and avaliablity for a parking lot
     *
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @return true if successful and false if error occurs
     * @author Chris
     */
    static Boolean insertSpace ( final Statement stmt, final Scanner sc ) throws Exception {
        System.out.print( "Enter space type:" );
        final String type = sc.nextLine();
        System.out.print( "Enter Space Availabilty:" );
        final String available = sc.nextLine();
        int availability;
        if ( ( available.toLowerCase().equals( "true" ) ) ) {
            availability = 1;
        }
        else {
            availability = 0;
        }
        System.out.print( "Enter parking lot name:" );
        final String name = sc.nextLine();
        System.out.print( "Enter parking lot address:" );
        final String address = sc.nextLine();
        String sql = "INSERT INTO Spaces VALUES(null,'%s','%d', '%s','%s')";
        sql = String.format( sql, type, availability, name, address );
        try {
            stmt.executeUpdate( sql );
            System.out.println( "Succesfully added space" );
            return true;
        }
        catch ( final Exception e ) {
            System.out.println( e );
            return false;
        }
    }

    /**
     * updates space in a parking lots
     *
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @return true if successful and false if error occurs
     * @author Chris
     */

    static Boolean updateSpace ( final Statement stmt, final Scanner sc ) {
        System.out.print( "Enter space number:" );
        final int id = sc.nextInt();
        sc.nextLine();
        System.out.print( "Enter parking lot name:" );
        final String name = sc.nextLine();
        System.out.print( "Enter parking lot address:" );
        final String address = sc.nextLine();
        System.out.print( "Enter new space type:" );
        final String type = sc.nextLine();
        System.out.print( "Enter space Availabilty:" );
        final String available = sc.nextLine();
        int availability;
        if ( ( available.toLowerCase().equals( "true" ) ) ) {
            availability = 1;
        }
        else {
            availability = 0;
        }
        String sql = "UPDATE Spaces SET Space_type='%s',Status='%s' WHERE Parking_lots_name = '%s' AND Parking_lots_address = '%s' AND Space_number='%d'";
        sql = String.format( sql, type, availability, name, address, id );
        try {
            final int affectedRows = stmt.executeUpdate( sql );
            if ( affectedRows > 0 ) {
                System.out.println( "Succesfully updated space" );
                return true;
            }
            else {
                System.out.println( "No space found" );
                return false;
            }
        }
        catch ( final Exception e ) {
            System.out.println( "Error updating space" );
            return false;
        }
    }

    /**
     * deletes space within a parking lot
     *
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @return true if successful and false if error occurs
     * @author Chris
     */
    static Boolean deleteSpace ( final Statement stmt, final Scanner sc ) {
        System.out.print( "Enter space number:" );
        final int id = sc.nextInt();
        sc.nextLine();
        System.out.print( "Enter parking lot name:" );
        final String name = sc.nextLine();
        System.out.print( "Enter parking lot address:" );
        final String address = sc.nextLine();
        String sql = "DELETE FROM Spaces WHERE Parking_lots_name = '%s' AND Parking_lots_address = '%s' AND Space_number='%d'";
        sql = String.format( sql, name, address, id );
        try {
            final int affectedRows = stmt.executeUpdate( sql );
            if ( affectedRows > 0 ) {
                System.out.println( "Succesfully deleted space" );
                return true;
            }
            else {
                System.out.println( "No space found" );
                return false;
            }
        }
        catch ( final Exception e ) {
            System.out.println( "Error deleting space" );
            return false;
        }
    }

    /**
     * creates permit for parking in a certain time period
     *
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @return true if successful and false if error occurs
     * @author Chris
     */
    static Boolean insertPermit ( final Statement stmt, final Scanner sc ) throws Exception {
        System.out.print( "Enter permit id:" );
        final String id = sc.nextLine();
        System.out.print( "Enter permit type:" );
        final String type = sc.nextLine();
        System.out.print( "Enter permit start date in form of year-month-day" );
        final String startDate = sc.nextLine();
        System.out.print( "Enter permit end date in form of year-month-day" );
        final String endDate = sc.nextLine();
        System.out.print( "Enter permit end time in form of 24hour:minute:second" );
        final String endTime = sc.nextLine();
        String sql = "INSERT INTO Permit VALUES('%s','%s','%s', '%s','%s')";
        sql = String.format( sql, id, type, startDate, endDate, endTime );
        try {
            stmt.executeUpdate( sql );
            System.out.println( "Succesfully added permit" );
            return true;
        }
        catch ( final Exception e ) {
            System.out.println( "Error adding permit" );
            return false;
        }
    }

    /**
     * sets permits values to new values
     *
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @return true if successful and false if error occurs
     * @author Chris
     */
    static Boolean updatePermit ( final Statement stmt, final Scanner sc ) {
        System.out.print( "Enter permit id:" );
        final String id = sc.nextLine();
        System.out.print( "Enter new permit id:" );
        final String id1 = sc.nextLine();
        System.out.print( "Enter new permit type:" );
        final String type = sc.nextLine();
        System.out.print( "Enter new permit start date in form of year-month-day" );
        final String startDate = sc.nextLine();
        System.out.print( "Enter new permit end date in form of year-month-day" );
        final String endDate = sc.nextLine();
        System.out.print( "Enter new permit end time in form of 24hour:minute:second" );
        final String endTime = sc.nextLine();
        String sql = "UPDATE Permit SET Permit_id='%s',Permit_type='%s',Start_date='%s',Expiration_date='%s',Expiration_time='%s' WHERE Permit_id = '%s'";
        sql = String.format( sql, id1, type, startDate, endDate, endTime, id );
        try {
            final int affectedRows = stmt.executeUpdate( sql );
            if ( affectedRows > 0 ) {
                System.out.println( "Succesfully updated permit" );
                return true;
            }
            else {
                System.out.println( "No permit found" );
                return false;
            }
        }
        catch ( final Exception e ) {
            System.out.println( "Error updating permit" );
            return false;
        }
    }

    /**
     * deletes permit that has inputed id
     *
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @return true if successful and false if error occurs
     * @author Chris
     */
    static Boolean deletePermit ( final Statement stmt, final Scanner sc ) {
        System.out.print( "Enter permit id:" );
        final String id = sc.nextLine();
        String sql = "DELETE FROM Permit WHERE Permit_id='%s'";
        sql = String.format( sql, id );
        try {
            stmt.executeUpdate( sql );
            System.out.println( "Succesfully deleted permit" );
            return true;
        }
        catch ( final Exception e ) {
            System.out.println( "Error deleting permit" );
            return false;
        }
    }

    /**
     * assign zone to parking lot, since zone has parking lot keys as foreign
     * keys works same as insert zone
     *
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @return true if successful and false if error occurs
     * @author Chris
     */
    static Boolean assignZoneToParkingLot ( final Statement stmt, final Scanner sc ) {
        System.out.print( "Enter Zone ID:" );
        final String id = sc.nextLine();
        System.out.print( "Enter parking lot name:" );
        final String name = sc.nextLine();
        System.out.print( "Enter parking lot address:" );
        final String address = sc.nextLine();
        String sql = "INSERT INTO Zones VALUES('%s', '%s','%s')";
        sql = String.format( sql, id, name, address );
        try {
            stmt.executeUpdate( sql );
            System.out.println( "Succesfully assigned zone to parking lot" );
            return true;
        }
        catch ( final Exception e ) {
            System.out.println( "Error assigning Zone" );
            return false;
        }
    }

    /**
     * updates the type a certain space has
     *
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @return true if successful and false if error occurs
     * @author Chris
     */
    static Boolean assignTypeToSpace ( final Statement stmt, final Scanner sc ) {
        System.out.print( "Enter space number:" );
        final int id = sc.nextInt();
        sc.nextLine();
        System.out.print( "Enter parking lot name:" );
        final String name = sc.nextLine();
        System.out.print( "Enter parking lot address:" );
        final String address = sc.nextLine();
        System.out.print( "Enter new space type:" );
        final String type = sc.nextLine();
        String sql = "UPDATE Spaces SET Space_type='%s' WHERE Parking_lots_name = '%s' AND Parking_lots_address = '%s' AND Space_number='%d'";
        sql = String.format( sql, type, name, address, id );
        try {
            final int affectedRows = stmt.executeUpdate( sql );
            if ( affectedRows > 0 ) {
                System.out.println( "Succesfully assigned space type" );
                return true;
            }
            else {
                System.out.println( "No space found" );
                return false;
            }
        }
        catch ( final Exception e ) {
            System.out.println( "Error updating space type" );
            return false;
        }
    }

    /**
     * requests that the input citation be appealed and updates the appealed
     * field to show this
     *
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @return true if successful and false if error occurs
     * @author Chris
     */
    static Boolean requestCitationAppeal ( final Statement stmt, final Scanner sc ) {
        System.out.print( "Enter citation ID:" );
        final String id = sc.nextLine();
        String sql = "UPDATE Citations SET Appealed=1 WHERE Citation_no = '%s'";
        sql = String.format( sql, id );
        try {
            final int affectedRows = stmt.executeUpdate( sql );
            if ( affectedRows > 0 ) {
                System.out.println( "Succesfully appealed citation" );
                return true;
            }
            else {
                System.out.println( "No citation found" );
                return false;
            }
        }
        catch ( final Exception e ) {
            System.out.println( "Error requesting citation appeal" );
            return false;
        }
    }

    /**
     * changes a citations payment status from unpaid to paid.
     *
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @return true if successful and false if error occurs
     * @author Chris
     */
    static Boolean updateCitationPayment ( final Statement stmt, final Scanner sc ) {
        System.out.print( "Enter citation ID:" );
        final String id = sc.nextLine();
        String sql = "UPDATE Citations SET Payment_status=0 WHERE Citation_no = '%s'";
        sql = String.format( sql, id );
        try {
            final int affectedRows = stmt.executeUpdate( sql );
            if ( affectedRows > 0 ) {
                System.out.println( "Succesfully updated citation payment" );
                return true;
            }
            else {
                System.out.println( "No citation found" );
                return false;
            }
        }
        catch ( final Exception e ) {
            System.out.println( "Error updating citation payment" );
            return false;
        }
    }

    /**
     * generates a citation report for those who have not paid the citation fee.
     *
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @param rs
     *            rs used to store the result of the query
     * @return true if successful and false if error occurs
     * @author Harshvardhan
     */
    static Boolean getCitationReport ( final Statement statement, final Scanner sc, ResultSet rs ) {
        try {
            rs = statement.executeQuery( "SELECT * from Citations WHERE Payment_status = 0" );
        }
        catch ( final SQLException e ) {
            e.printStackTrace();
            return false;
        }
        try {
            while ( rs.next() ) {
                final String c_id = rs.getString( "Citation_no" );
                final int fee = rs.getInt( "Fee" );
                final String category = rs.getString( "Category" );
                final Date date = rs.getDate( "Citation_date" );
                final Time time = rs.getTime( "Citation_time" );
                final Boolean payment_status = rs.getBoolean( "Payment_status" );
                System.out.println( "Citation No | Fees | Category | Start Date | Citation Time | Payment Status" );
                System.out.println(
                        c_id + " | " + fee + " | " + category + " | " + date + " | " + time + " | " + payment_status );
                System.out.println();
            }
        }
        catch ( final SQLException e ) {
            e.printStackTrace();
        	return false;
        }
        return true;
    }

    /**
     * generates a citation report between a specific date range per parking and
     * lot.
     *
     * @param stmt
     *            statement object used to call queries
     * @param sc
     *            scanner used to read in input for queries
     * @param conn
     *            connection used to call commit methods
     * @param rs
     *            rs used to store the result of the query
     * @return true if successful and false if error occurs
     * @author Harshvardhan
     */
    static Boolean getLotCitationReport ( PreparedStatement myStmt, final Connection conn, final Scanner sc,
            ResultSet rs ) {
        final String sql = "select p.Parking_lots_name, p.zone_id, COUNT(*) as numberofCitations from Permission_to_Park p inner join Given_to_Vehicles g on p.Permit_id = g.Permit_id inner join issued_to i on i.licenseNo = g.licenseNo inner join Citations c on c.Citation_no = i.Citation_no where c.Citation_date BETWEEN ? and ? and  p.zone_id = ?";
        try {
            myStmt = conn.prepareStatement( sql );
        }
        catch ( final SQLException e ) {
            e.printStackTrace();
            return false;
        }
        System.out.println( "Enter the date range between which you want to get the citation report: " );
        System.out.println( "Enter the date 1 for the range: " );
        final String date1 = sc.next();
        final java.sql.Date sdate1 = java.sql.Date.valueOf( date1 );
        System.out.println( "Enter the date 2 for the range: " );
        final String date2 = sc.next();
        final java.sql.Date sdate2 = java.sql.Date.valueOf( date2 );
        System.out.println("Enter the zone id to find the number of citations: ");
        String zone_id2 = sc.next();
        try {
            myStmt.setDate( 1, sdate1 );
        }
        catch ( final SQLException e ) {
            e.printStackTrace();
            return false;
        }
        try {
            myStmt.setDate( 2, sdate2 );
            myStmt.setString(3, zone_id2);
        }
        catch ( final SQLException e ) {
            e.printStackTrace();
            return false;
        }
        try {
            rs = myStmt.executeQuery();
        }
        catch ( final SQLException e ) {
            e.printStackTrace();
            return false;
        }

        try {
            while ( rs.next() ) {
                final String parkingLotName = rs.getString( "Parking_lots_name" );
                final String zone_id = rs.getString( "zone_id" );
                final int numOfCitations = rs.getInt( "numberOfCitations" );
                System.out.println( "Parking Lot Name | Zone Id | Number of Citations" );
                System.out.println( parkingLotName + " | " + zone_id + " | " + numOfCitations );
            }
        }
        catch ( final SQLException e ) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static boolean getLotZoneReport ( final Statement statement, final Scanner sc, ResultSet rs ) {
        try {
            rs = statement.executeQuery( "SELECT Parking_lots_name, zone_ID FROM Permission_to_Park ORDER BY Parking_lots_name" );
        }
        catch ( final SQLException e ) {
            e.printStackTrace();
        }
        try {
            while ( rs.next() ) {
                final String parkingLotName = rs.getString( "Parking_lots_name" );
                final String zone_id = rs.getString( "zone_id" );
                System.out.println( "Parking Lot Name | Zone Id" );
                System.out.println( parkingLotName + " | " + zone_id );
            }
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static boolean getNumberOfCarViolationReport ( final Statement statement, final Scanner sc, ResultSet rs ) {
        try {
            rs = statement.executeQuery( "SELECT i.licenseNo, c.citation_no, c.payment_status \r\n"
                    + "FROM issued_to i \r\n" + "INNER JOIN Citations c \r\n" + "ON i.citation_no = c.citation_no \r\n"
                    + "WHERE c.payment_status = 0" );
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            while ( rs.next() ) {
                final String license_no = rs.getString( "licenseNo" );
                final String c_id = rs.getString( "Citation_no" );
                final boolean payment_status = rs.getBoolean( "Payment_status" );
                System.out.println( "License No | Citation No | Payment Status" );
                System.out.println( license_no + " | " + c_id + " | " + payment_status );
            }
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static boolean getNumberOfEmployeePermit ( PreparedStatement myStmt, final Connection conn, final Scanner sc,
            ResultSet rs ) {
        final String sql3 = "SELECT COUNT(*) as numberOfEmployeePermits FROM Permission_to_Park p INNER JOIN Given_to_Employee e ON p.permit_id = e.permit_id WHERE p.zone_id = ?";
        try {
            myStmt = conn.prepareStatement( sql3 );
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println( "Enter the zone id for which you need the report: " );
        final String zone_id = sc.next();
        try {
            myStmt.setString( 1, zone_id );
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        try {
            rs = myStmt.executeQuery();
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        // result = statement.executeQuery("SELECT COUNT(*) as
        // numberOfEmployeePermits\r\n"
        // + "FROM Permission_to_park p \r\n"
        // + "INNER JOIN Given_to_Employee e \r\n"
        // + "ON p.permit_id = e.permit_id \r\n"
        // + "WHERE p.zone_id = 'A'");
        try {
            while ( rs.next() ) {
                final int num = rs.getInt( "numberOfEmployeePermits" );
                System.out.println( "Number of Employee Permits in the zone " + zone_id );
                System.out.println( num );
            }
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static boolean getPermitInfo ( PreparedStatement myStmt, final Connection conn, final Scanner sc, ResultSet rs ) {
        final String sql4 = "SELECT p.* FROM Permit p WHERE p.permit_id IN (SELECT permit_id FROM Given_to_Student s WHERE driver_id = ? UNION ALL SELECT permit_id FROM Given_to_Visitor v WHERE driver_id = ? UNION ALL SELECT permit_id FROM Given_to_Employee e WHERE driver_id = ?)";
        try {
            myStmt = conn.prepareStatement( sql4 );
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println( "Enter the driver id for which you want to get Permit information: " );
        final String driver_id = sc.next();
        try {
            myStmt.setString( 1, driver_id );
            myStmt.setString( 2, driver_id );
            myStmt.setString( 3, driver_id );
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        try {
            rs = myStmt.executeQuery();
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            while ( rs.next() ) {
                final String permit_id = rs.getString( "Permit_id" );
                final String permit_type = rs.getString( "Permit_type" );
                final Date s_date = rs.getDate( "Start_date" );
                final Date e_date = rs.getDate( "Expiration_date" );
                final Time time = rs.getTime( "Expiration_time" );
                System.out.println( "Permit Id | Permit Type | Start Date | Expiration date | Expiration Time" );
                System.out.println( permit_id + " | " + permit_type + " | " + s_date + " | " + e_date + " | " + time );
            }
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static boolean getOpenSpaces ( PreparedStatement myStmt, final Connection conn, final Scanner sc, ResultSet rs ) {
        final String sql5 = "SELECT Space_number, Parking_lots_name FROM Spaces WHERE Space_type = ? AND Parking_lots_name = ? AND Status = ?";
        try {
            myStmt = conn.prepareStatement( sql5 );
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println( "Enter the Space type: " );
        final String space_type = sc.nextLine();
        System.out.println( "Enter the Parking Lot Name: " );
        final String parkingLotName = sc.nextLine();
        System.out.println( "Enter the Status (0 for empty and 1 for occupied): " );
        final String status = sc.next();
        try {
            myStmt.setString( 1, space_type );
            myStmt.setString( 2, parkingLotName );
            myStmt.setString( 3, status );
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        try {
            rs = myStmt.executeQuery();
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        try {
            while ( rs.next() ) {
                final int spaceNum = rs.getInt( "Space_number" );
                final String parkingLotName1 = rs.getString( "Parking_lots_name" );
                System.out.println( "Space Number | Parking Lot Name" );
                System.out.println( spaceNum + " | " + parkingLotName1 );
            }
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static boolean permitValidity ( PreparedStatement myStmt, final Connection conn, final Scanner sc, ResultSet rs ) {
        final String sql1 = "SELECT p.Permit_id, o.licenseNo, d.Name, CASE WHEN CURDATE() BETWEEN p.start_date AND p.Expiration_date THEN 'Valid' ELSE 'Invalid' END AS permit_validity FROM Permit p INNER JOIN Permission_to_Park ptp ON ptp.permit_id = p.permit_id INNER JOIN Given_to_Vehicles v ON ptp.Permit_id = v.Permit_id INNER JOIN owns o ON v.licenseNo = o.licenseNo INNER JOIN Driver d ON o.Driver_id = d.Driver_id WHERE v.licenseNo = ? AND d.Name = ? AND zone_id = ?";
        try {
            myStmt = conn.prepareStatement( sql1 );
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println( "Enter License No: " );
        final String licenseNo = sc.nextLine();
        System.out.println( "Enter name of the driver: " );
        final String name = sc.nextLine();
        System.out.println( "Enter the zone id: " );
        final String zone_id = sc.next();
        try {
            myStmt.setString( 1, licenseNo );
            myStmt.setString( 2, name );
            myStmt.setString( 3, zone_id );
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        try {
            rs = myStmt.executeQuery();
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        try {
            while ( rs.next() ) {
                final String permit_id = rs.getString( "Permit_id" );
                final String license_no = rs.getString( "licenseNo" );
                final String name1 = rs.getString( "Name" );
                final String permit_validity = rs.getString( "permit_validity" );
                System.out.println( "Permit Id | License No | Name | Permit Validity" );
                System.out.println( permit_id + " " + license_no + " " + name1 + " " + permit_validity );
            }
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    static boolean maintainCitation ( final Statement statement, ResultSet rs ) {
        // TODO Auto-generated method stub
        try {
            rs = statement.executeQuery( "Select * from Citations" );
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        try {
            while ( rs.next() ) {
                final String c_id = rs.getString( "Citation_no" );
                final int fee_1 = rs.getInt( "Fee" );
                final String category_1 = rs.getString( "Category" );
                final Date date_1 = rs.getDate( "Citation_date" );
                final Time time = rs.getTime( "Citation_time" );
                final Boolean payment_status_1 = rs.getBoolean( "Payment_status" );
                System.out.println( "Citation No | Fees | Category | Start Date | Citation Time | Payment Status" );
                System.out.println(
                        c_id + " " + fee_1 + " " + category_1 + " " + date_1 + " " + time + " " + payment_status_1 );
                System.out.println();
            }
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static boolean generateCitation ( PreparedStatement myStmt, final Connection conn, final Scanner sc ) {
        // TODO Auto-generated method stub
        final String sql = "INSERT INTO Citations (Citation_no, Fee, Category, Citation_date, Citation_time, Payment_status, Appealed) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            myStmt = conn.prepareStatement( sql );
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println( "Enter citation no: " );
        final String citation_no = sc.next();
        System.out.println( "Enter fee: " );
        final int fee = sc.nextInt();
        System.out.println( "Enter the category: " );
        final String category = sc.next();
        System.out.println( "Enter date (YYYY-MM-DD): " );
        final String date5 = sc.next();
        final java.sql.Date sdate = java.sql.Date.valueOf( date5 );
        System.out.println( "Enter time (HH:mm:ss): " );
        final String time_1 = sc.next();
        System.out.println( "Enter true for fees paid and false for fees not-paid: " );
        final Boolean payment_status = sc.nextBoolean();

        try {
            myStmt.setString( 1, citation_no );
            myStmt.setInt( 2, fee );
            myStmt.setString( 3, category );
            myStmt.setDate( 4, sdate );
            myStmt.setObject( 5, java.time.LocalTime.parse( time_1 ) );
            myStmt.setBoolean( 6, payment_status );
            myStmt.setBoolean( 7, false );
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            myStmt.executeUpdate();
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static boolean deleteCitation ( PreparedStatement myStmt, final Connection conn, final Statement statement,
            final Scanner sc, ResultSet rs ) {
        // TODO Auto-generated method stub
        final String sql_2 = "DELETE c \r\n" + "FROM Citations c\r\n"
                + "INNER JOIN issued_to i ON c.Citation_no = i.Citation_no \r\n"
                + "WHERE c.Citation_no = ? AND i.LicenseNo = ?";
        try {
            myStmt = conn.prepareStatement( sql_2 );
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        System.out.println( "Enter citation no: " );
        final int citation_no_1 = sc.nextInt();
        System.out.println( "Enter license no: " );
        final String license_no_1 = sc.next();
        try {
            myStmt.setInt( 1, citation_no_1 );
            myStmt.setString( 2, license_no_1 );
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        try {
            myStmt.executeUpdate();
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        try {
            rs = statement.executeQuery( "Select * from Citations" );
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        try {
            while ( rs.next() ) {
                final int c_id = rs.getInt( "Citation_no" );
                final int fee_3 = rs.getInt( "Fee" );
                final String category_3 = rs.getString( "Category" );
                final Date date_3 = rs.getDate( "Citation_date" );
                final Time time = rs.getTime( "Citation_time" );
                final Boolean payment_status_3 = rs.getBoolean( "Payment_status" );
                System.out.println( "Citation No | Fees | Category | Start Date | Citation Time | Payment Status" );
                System.out.println(
                        c_id + " " + fee_3 + " " + category_3 + " " + date_3 + " " + time + " " + payment_status_3 );
                System.out.println();
            }
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static boolean payCitation ( PreparedStatement myStmt, final Statement statement, final Connection conn,
            final Scanner sc, ResultSet rs ) {
        final String sql_1 = "UPDATE Citations set Payment_status = \r\n" + "CASE WHEN fee > 0 THEN TRUE\r\n"
                + "ELSE Payment_Status \r\n" + "END,\r\n" + "fee = \r\n" + "CASE WHEN fee > 0 THEN 0 \r\n"
                + "ELSE fee \r\n" + "END\r\n" + "WHERE citation_no = ?";
        try {
            myStmt = conn.prepareStatement( sql_1 );
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        System.out.println( "Enter citation no: " );
        final String citation_no_2 = sc.next();
        try {
            myStmt.setString( 1, citation_no_2 );
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        try {
            myStmt.executeQuery();
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        try {
            rs = statement.executeQuery( "Select * from Citations" );
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        try {
            while ( rs.next() ) {
                final String c_id = rs.getString( "Citation_no" );
                final int fee_3 = rs.getInt( "Fee" );
                final String category_3 = rs.getString( "Category" );
                final Date date_3 = rs.getDate( "Citation_date" );
                final Time time = rs.getTime( "Citation_time" );
                final Boolean payment_status_3 = rs.getBoolean( "Payment_status" );
                System.out.println( "Citation No | Fees | Category | Start Date | Citation Time | Payment Status" );
                System.out.println(
                        c_id + " " + fee_3 + " " + category_3 + " " + date_3 + " " + time + " " + payment_status_3 );
                System.out.println();
            }
        }
        catch ( final SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

}