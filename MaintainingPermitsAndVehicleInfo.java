import java.sql.*;

public class MaintainingPermitsAndVehicleInfo {

	static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/slee82";
	private static Connection connection = null;
	private static Statement statement = null;
	private static ResultSet result = null;
	
	public void connectToDatabase() throws ClassNotFoundException, SQLException {
		Class.forName("org.mariadb.jdbc.Driver");
		String user = "slee82";
		String password = "200476988";
		
		connection = DriverManager.getConnection(jdbcURL, user, password);
	}
	
	/**
     * Assign permits to drivers if driver is a student he can have 1 permit.
     * If driver is a visitor he can have 1 permit, and if driver is an employee he can have at most 2 permits.
     * @param driver_id
     *            Driver_id in Driver relation 
     * @param permit_id
     * 			  Permit_id in Permit
     * @return true if successful and false if error occurs
     * @author Lee
     */
	public boolean assingingPermitsToDrivers(String driver_id, String permit_id){
		String status = null;
		String permitId = null;
		
		try {
			connectToDatabase();
	        PreparedStatement pstmt = connection.prepareStatement("SELECT Status FROM Driver WHERE Driver_id = ?");
	        pstmt.setString(1, driver_id);
	        ResultSet result = pstmt.executeQuery();
	        
	        if (result.next()){
	        	status = result.getString("Status");
	        }
	        result.close();
	        
	        pstmt = connection.prepareStatement("SELECT Permit_id FROM Permit WHERE Permit_id = ?");
	        pstmt.setString(1, permit_id);
	        result = pstmt.executeQuery();
	        
	        if (result.next()){
	        	permitId = result.getString("Permit_id");
	        }
	        result.close();
	       
			if("S".equalsIgnoreCase(status)){
				int numOfPermit = -1;
				pstmt = connection.prepareStatement("select count(*) from Given_to_Student where Driver_id = ?");
				pstmt.setString(1, driver_id);
		        result = pstmt.executeQuery();
		        if (result.next()) {
		        	numOfPermit = result.getInt(1);
		        }
		        result.close();
		        if (numOfPermit==0){
		        	PreparedStatement insertStmt = connection.prepareStatement(
		            		"INSERT INTO Given_to_Student (Driver_id, Permit_id) VALUES(?, ?)"
		            );
		            insertStmt.setString(1,  driver_id);
		            insertStmt.setString(2,  permit_id);
		            int affectedRows = insertStmt.executeUpdate();
		            pstmt.close();
		            if(affectedRows>0) {
		            	System.out.println("Success to assign");
		            	return true;
		            }
		            System.out.println("Fail to assign");
		            return false;
		        }
	            
			}else if("E".equalsIgnoreCase(status)) {
				int numOfPermit = -1;
				pstmt = connection.prepareStatement("select count(*) from Given_to_Employee where Driver_id = ?");
				pstmt.setString(1, driver_id);
		        result = pstmt.executeQuery();
		        if (result.next()) {
		        	numOfPermit = result.getInt(1);
		        }
		        result.close();
		        if (numOfPermit<2) {
		        	PreparedStatement insertStmt = connection.prepareStatement(
		            		"INSERT INTO Given_to_Employee (Driver_id, Permit_id) VALUES(?, ?)"
		         );
		         insertStmt.setString(1,  driver_id);
		         insertStmt.setString(2,  permit_id);
		         int affectedRows= insertStmt.executeUpdate();
		         pstmt.close();
		         if(affectedRows>0) {
		        	 System.out.println("Success to assign");
		        	 return true;
		         }
		         System.out.println("Fail to assign");
		         return false;
		        }
				 
			}else if("V".equalsIgnoreCase(status)) {
				int numOfPermit = -1;
				pstmt = connection.prepareStatement("select count(*) from Given_to_Visitor where Driver_id = ?");
				pstmt.setString(1, driver_id);
		        ResultSet result2 = pstmt.executeQuery();
		        if (result2.next()) {
		        	numOfPermit = result2.getInt(1);
		        }
		        result2.close();
		        if (numOfPermit==0){
		        	PreparedStatement insertStmt = connection.prepareStatement(
		            		"INSERT INTO Given_to_Visitor (Driver_id, Permit_id) VALUES(?, ?)"
		            );
		            insertStmt.setString(1,  driver_id);
		            insertStmt.setString(2,  permit_id);
		            int affectedRows = insertStmt.executeUpdate();
		            pstmt.close();
		            if(affectedRows>0) {
		            	System.out.println("Success to assign");
		            	return true;
		            }
		            System.out.println("Fail to assign");
		            return false;
		        }
			}
		}catch(SQLException e) {
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		close();
        System.out.println("Fail to assign");
		return false;
	}
	
	/**
     * Enter vehicle info
     *
     * @param licenseNo
     *            licenseNo in Vehicles relation 
     * @param model
     * 			  Model in Vehicles relation
     * @Param color
     * 			  Color in Vehicles relation
     * @Param manf
     * 			  Manf in Vehicles relation
     * @Param year
     * 			  Year in Vehicles relation
     * @return true if successful and false if error occurs
     * @author Lee
     */
	public boolean enterVehicleInfo(String licenseNo, String model, String color, String manf, int year) {
		try {
			connectToDatabase();
			PreparedStatement insertStmt = connection.prepareStatement(
            		"INSERT INTO Vehicles (licenseNo, Model, Color, Manf, Year) VALUES(?, ?, ?, ?, ?)"
            );
	        insertStmt.setString(1, licenseNo);
	        insertStmt.setString(2, model);
	        insertStmt.setString(3, color);
	        insertStmt.setString(4, manf);
	        insertStmt.setInt(5, year);
	        int affectedRows = insertStmt.executeUpdate();
	        insertStmt.close();
	        if(affectedRows>0) {
	        	return true;
	        }
	        return false;
		}catch(SQLException e) {
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		close();
		return false;
	}
	
	/**
     * Update vehicle info
     *
     * @param licenseNo
     *            licenseNo in Vehicles relation 
     * @param model
     * 			  Model in Vehicles relation
     * @Param color
     * 			  Color in Vehicles relation
     * @Param manf
     * 			  Manf in Vehicles relation
     * @Param year
     * 			  Year in Vehicles relation
     * @return true if successful and false if error occurs
     * @author Lee
     */
	public boolean updateVehicleInfo(String licenseNo, String model, String color, String manf, int year){
		try {
			connectToDatabase();
			PreparedStatement updateStmt = connection.prepareStatement(
            		"UPDATE Vehicles SET model = ?, color = ?, manf = ?, year = ? where licenseNo = ?"
            );
	        updateStmt.setString(1, model);
	        updateStmt.setString(2, color);
	        updateStmt.setString(3, manf);
	        updateStmt.setInt(4, year);
	        updateStmt.setString(5, licenseNo);
	        int affectedRows = updateStmt.executeUpdate();
	        updateStmt.close();
	        if(affectedRows > 0) {
	        	return true;
	        }
	        return false;
		}catch(SQLException e) {
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		close();
		return false;
	}
	
	/**
     * Delete vehicle info
     * Print "Deletion complete" when succeed else print "Deletion failed" 
     * @param licenseNo
     *            licenseNo in Vehicles relation 
     * @return true if successful and false if error occurs
     * @author Lee
     */
	public void deleteVehicleInfo(String licenseNo) {
		try {
			connectToDatabase();
			PreparedStatement deleteStmt = connection.prepareStatement(
            		"DELETE FROM Vehicles where licenseNo = ?"
            );
			deleteStmt.setString(1, licenseNo);
			int affectedRows = deleteStmt.executeUpdate();
			deleteStmt.close();
			if(affectedRows>0) {
				System.out.println("Deletion complete");
			}
		}catch (SQLException e) {
			System.out.println("Deletion failed");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Deletion failed");
		}
	}
	
	/**
     * Add vehicle to permit
     *
     * @param licenseNo
     *            licenseNo in Vehicles relation 
     * @param permit_id
     * 			  Permit_id in Permit relation
     * @return true if successful and false if error occurs
     * @author Lee
     */
	public boolean addVehicleToPermit(String licenseNo, String permit_id) {
		 try {
		        connectToDatabase();

		        // Check if licenseNo exists
		        try (PreparedStatement pstmt1 = connection.prepareStatement("SELECT licenseNo FROM Vehicles WHERE licenseNo = ?")) {
		            pstmt1.setString(1, licenseNo);
		            try (ResultSet result = pstmt1.executeQuery()) {
		                if (!result.next()) {
		                    System.out.println("licenseNo is not valid");
		                    result.close();
		                    return false;
		                }
		            }
		        }

		        // Check if permit_id exists
		        try (PreparedStatement pstmt2 = connection.prepareStatement("SELECT permit_id FROM Permit WHERE Permit_id = ?")) {
		            pstmt2.setString(1, permit_id);
		            try (ResultSet result = pstmt2.executeQuery()) {
		                if (!result.next()) {
		                    System.out.println("Permit_id is not valid");
		                    result.close();
		                    return false;
		                }
		            }
		            try (PreparedStatement pstmt3 = connection.prepareStatement("select count(*) from Given_to_Vehicles where Permit_id = ?")){
		            	pstmt3.setString(1, permit_id);
				        result = pstmt3.executeQuery();
				        if (result.next()) {
				        	if(result.getInt(1)>=1) {
				        		result.close();
				        		return false;
				        	}
				        }
				        result.close();
		            }
		        }

		        // Insert new record
		        try (PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO Given_to_Vehicles (licenseNo, Permit_id) VALUES(?, ?)")) {
		            insertStmt.setString(1, licenseNo);
		            insertStmt.setString(2, permit_id);
		            boolean r = insertStmt.executeUpdate() > 0;
		            insertStmt.close();
		            return r;
		        }

		    } catch (SQLException e) {
		        // Consider logging the exception
		        return false;
		    } catch (ClassNotFoundException e) {
		        // Handle or rethrow the exception
		        e.printStackTrace();
		        return false;
		    } finally {
		        // Ensure to close the database connection
		        close();
		    }
	}
	
	/**
     * Delete vehicle from permit
     *
     * @param licenseNo
     *            licenseNo in Vehicles relation 
     * @param permit_id
     * 			  Permit_id in Permit relation
     * @return true if successful and false if error occurs
     * @author Lee
     */
	public boolean deleteVehicleFromPermit(String licenseNo, String permit_id){
		try {
			connectToDatabase();
			 // Check if licenseNo exists
	        try (PreparedStatement pstmt1 = connection.prepareStatement("SELECT licenseNo FROM Vehicles WHERE licenseNo = ?")) {
	            pstmt1.setString(1, licenseNo);
	            try (ResultSet result = pstmt1.executeQuery()) {
	                if (!result.next()) {
	                    System.out.println("licenseNo is not valid");
	                    result.close();
	                    return false;
	                }
	            }
	        }

	        // Check if permit_id exists
	        try (PreparedStatement pstmt2 = connection.prepareStatement("SELECT permit_id FROM Permit WHERE Permit_id = ?")) {
	            pstmt2.setString(1, permit_id);
	            try (ResultSet result = pstmt2.executeQuery()) {
	                if (!result.next()) {
	                    System.out.println("Permit_id is not valid");
	                    result.close();
	                    return false;
	                }
	            }   
	        }
			PreparedStatement deleteStmt = connection.prepareStatement(
            		"DELETE FROM Given_to_Vehicles where licenseNo = ? AND permit_id = ?"
            );
			deleteStmt.setString(1, licenseNo);
			deleteStmt.setString(2, permit_id);
			int affectedRows= deleteStmt.executeUpdate();
			deleteStmt.close();
			
			PreparedStatement deleteStmt1 = connection.prepareStatement(
            		"DELETE FROM Permit where permit_id = ?"
            );
			
			deleteStmt1.setString(1, permit_id);
			deleteStmt1.executeUpdate();
			deleteStmt1.close();
			
			if(affectedRows>0) {
				return true;
			}
			return false;
		}catch (SQLException e) {
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
     * Add owner of vehicle
     *
     * @param driver_id
     *            Driver_id in Driver relation 
     * @param licenseNo
     * 			  licenseNo in Vehicles relation
     * @return true if successful and false if error occurs
     * @author Lee
     */
	public boolean addOwnerOfVehicle(String driver_id, String licenseNo) {
		try {
			connectToDatabase();
			// Check if driver_id is valid 
			try (PreparedStatement pstmt1 = connection.prepareStatement("SELECT Driver_id FROM Driver WHERE Driver_id = ?")) {
		            pstmt1.setString(1, driver_id);
		            try (ResultSet result = pstmt1.executeQuery()) {
		                if (!result.next()) {
		                    System.out.println("driver_id is not valid");
		                    result.close();
		                    pstmt1.close();
		                    return false;
		                }
		            }
		        }

		        // Check if licenseNo valid
		        try (PreparedStatement pstmt2 = connection.prepareStatement("SELECT licenseNo FROM Vehicles WHERE licenseNo = ?")) {
		            pstmt2.setString(1, licenseNo);
		            try (ResultSet result = pstmt2.executeQuery()) {
		                if (!result.next()) {
		                    System.out.println("licenseNo is not valid");
		                    result.close();
		                    pstmt2.close();
		                    return false;
		                }
		            }   
		        }
			PreparedStatement insertStmt = connection.prepareStatement(
            		"INSERT INTO owns (Driver_id, licenseNo) VALUES(?, ?)"
            );
	        insertStmt.setString(1, driver_id);
	        insertStmt.setString(2, licenseNo);
	        int affectedRows = insertStmt.executeUpdate();
	        insertStmt.close();
	        if(affectedRows>0) {
	        	return true;
	        }
	        return false;
		}catch(SQLException e) {
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		close();
		return false;
	}
	
	/**
     * Update owner of vehicle
     *
     * @param driver_id
     *            Driver_id in Driver relation 
     * @param licenseNo
     * 			  licenseNo in Vehicles relation
     * @return true if successful and false if error occurs
     * @author Lee
     */
	public boolean updateOwnerOfVehicle(String driver_id, String licenseNo) {
		try {
			connectToDatabase();
			// Check if driver_id is valid 
			try (PreparedStatement pstmt1 = connection.prepareStatement("SELECT Driver_id FROM Driver WHERE Driver_id = ?")) {
				pstmt1.setString(1, driver_id);
				try (ResultSet result = pstmt1.executeQuery()) {
					if (!result.next()) {
						System.out.println("driver_id is not valid");
					    result.close();
					    pstmt1.close();
					    return false;
					}
				}
			}

			// Check if licenseNo valid
			try (PreparedStatement pstmt2 = connection.prepareStatement("SELECT licenseNo FROM Vehicles WHERE licenseNo = ?")) {
				pstmt2.setString(1, licenseNo);
				try (ResultSet result = pstmt2.executeQuery()) {
					if (!result.next()) {
						System.out.println("licenseNo is not valid");
					    result.close();
					    pstmt2.close();
					    return false;
					}
				}   
			}
			PreparedStatement updateStmt = connection.prepareStatement(
            		"UPDATE owns SET Driver_id = ? where licenseNo = ?"
            );
	        updateStmt.setString(1, driver_id);
	        updateStmt.setString(2, licenseNo);
	        int affectedRows = updateStmt.executeUpdate();
	        updateStmt.close();
	        if(affectedRows>0) {
	        	return true;
	        }
	        return false;
		}catch(SQLException e) {
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		close();
		return false;
	}
	
	
	/**
     * Remove owner of vehicle
     *
     * @param driver_id
     *            Driver_id in Driver relation 
     * @param licenseNo
     * 			  licenseNo in Vehicles relation
     * @return true if successful and false if error occurs
     * @author Lee
     */
	public boolean removeOwnerOfVehicle(String driver_id, String licenseNo) {
		try {
			connectToDatabase();
			// Check if driver_id is valid 
			try (PreparedStatement pstmt1 = connection.prepareStatement("SELECT Driver_id FROM Driver WHERE Driver_id = ?")) {
				pstmt1.setString(1, driver_id);
				try (ResultSet result = pstmt1.executeQuery()) {
					if (!result.next()) {
						System.out.println("driver_id is not valid");
						result.close();
						pstmt1.close();
						return false;
					}
				}
			}

			// Check if licenseNo valid
			try (PreparedStatement pstmt2 = connection.prepareStatement("SELECT licenseNo FROM Vehicles WHERE licenseNo = ?")) {
				pstmt2.setString(1, licenseNo);
				try (ResultSet result = pstmt2.executeQuery()) {
				if (!result.next()) {
					System.out.println("licenseNo is not valid");
					result.close();
					pstmt2.close();
					return false;
					}
				}   
			}
			PreparedStatement deleteStmt = connection.prepareStatement(
            		"DELETE FROM owns where licenseNo = ? AND driver_id = ?"
            );
			deleteStmt.setString(1, licenseNo);
			deleteStmt.setString(2, driver_id);
			int affectedRows= deleteStmt.executeUpdate();
			deleteStmt.close();
			if(affectedRows>0) {
				return true;
			}
			return false;
			}catch (SQLException e) {
				return false;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return false;
			}
	
	/**
     * Close the connection
     * @author Lee
     */
	public void close() {
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
