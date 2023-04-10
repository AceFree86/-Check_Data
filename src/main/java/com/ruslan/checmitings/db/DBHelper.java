package com.ruslan.checmitings.db;

import java.sql.*;
import java.util.ArrayList;

public class DBHelper {

    private final Connection conn = SQLiteConnection.connect();

    public void insertResult(DataModel model) {
        try {
            if (conn != null) {
                String aql = "INSERT INTO list_meetings(USER_ID,MEETINGS,STATE) VALUES(?,?,?)";
                PreparedStatement ps = conn.prepareStatement(aql);
                ps.setInt(1, model.userId());
                ps.setString(2, model.text());
                ps.setInt(3, model.state());
                ps.executeUpdate();
            } else {
                System.out.println("Failed to connect to database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkRow(String text) {
        try {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String sql = "SELECT MEETINGS FROM list_meetings WHERE MEETINGS = '" + text + "'";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    return true;
                }
                rs.close();
                stmt.close();
            } else {
                System.out.println("Failed to connect to database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateMeetings(int ID, int number, int updateNumber) {
        try {
            if (conn != null) {
                String sql = "UPDATE list_meetings SET STATE = " + updateNumber + " WHERE STATE = '" + number + "' AND ID = '" + ID + "'";
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<DataModel> getMeetings(int number) {
        ArrayList<DataModel> returnList = new ArrayList<>();
        try {
            if (conn != null) {
                String sql = "SELECT * FROM list_meetings WHERE STATE= '" + number + "'";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    do {
                        int id = rs.getInt("ID");
                        int userId = rs.getInt("USER_ID");
                        String name = rs.getString("MEETINGS");
                        int active = rs.getInt("STATE");
                        DataModel user = new DataModel(id, userId, name, active);
                        returnList.add(user);
                    } while (rs.next());
                }
                rs.close();
                stmt.close();
            } else {
                System.out.println("Failed to connect to database.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return returnList;
    }

    public ArrayList<DataModel> getUserInput() {
        ArrayList<DataModel> returnList = new ArrayList<>();
        try {
            if (conn != null) {
                String sql = "SELECT * FROM list_user_input";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    do {
                        int id = rs.getInt("ID");
                        int userId = rs.getInt("USER_ID");
                        String name = rs.getString("USER_INPUT");
                        int active = rs.getInt("STATE");
                        DataModel user = new DataModel(id, userId, name, active);
                        returnList.add(user);
                    } while (rs.next());
                }
                rs.close();
                stmt.close();
            } else {
                System.out.println("Failed to connect to database.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return returnList;
    }
}
