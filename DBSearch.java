/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timeclock;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author cmanson
 */
public class DBSearch {

    //private static Connection con;
    //private static Time t;
    //final static String sunday = Time.getSunday();
    public DBSearch() throws SQLException, ClassNotFoundException {
        try {
            Connection con = DBConnect.connect();
            System.out.println("Connection made.");
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error: " + ex);
        }
    }

    public static ObservableList<TimeSheet> getWeekTimeList() throws SQLException, Exception {
        ObservableList<TimeSheet> data = FXCollections.observableArrayList();
        //System.out.println("Calling getSunday");
        String sunday = Time.getSunday();
        String eow = Time.addADay(7);
        Integer[] appList;
        Float[] totalLine = new Float[8];
        for (int z=0; z<8; z++) {
            totalLine[z] = 0f;
        }
        appList = appsThisWeek(sunday, eow);
        if (appList.length > 0) {
            for (int i = 0; i < appList.length; i++) {
                Boolean active = false;
                if (clockedIn()) {
                    if (activeAppID() == appList[i]) {
                        active = true;
                    }
                }
                String appName = getAppAcro(appList[i]);
                Float[] day = new Float[7];
                Float appTotal = 0f;
                for (int dow = 0; dow < 7; dow++) {
                    day[dow] = timeToday(Time.addADay(dow), appList[i]);
                    appTotal += day[dow];
                    totalLine[dow] += day[dow];
                    totalLine[7] += day[dow];
                }
                
                data.add(new TimeSheet(active, appList[i], appName, Time.formatTime(day[0]), 
                        Time.formatTime(day[1]), Time.formatTime(day[2]), Time.formatTime(day[3]), 
                        Time.formatTime(day[4]), Time.formatTime(day[5]), Time.formatTime(day[6]),
                        Time.formatTime(appTotal)));
            }
            data.add(new TimeSheet(false,0,"Total",Time.formatTime(totalLine[0]),Time.formatTime(totalLine[1]),
                    Time.formatTime(totalLine[2]),Time.formatTime(totalLine[3]),Time.formatTime(totalLine[4]),
                    Time.formatTime(totalLine[5]),Time.formatTime(totalLine[6]),Time.formatTime(totalLine[7])));
        }

        //data.add(new TimeSheet(true,2, "LDTRS", Time.formatTime(0.0f), 1.5f, 8f, 9f, 10f, .5f, 2.5f, 25.5f));
        //data.add(new TimeSheet(false,1, "NAR", 0.0f, 2.5f, 2.5f, 2.5f, 2.5f, .5f, 2.5f, 10.5f));
        return data;

    }

    public static String getAppAcro(Integer app_id) throws Exception {
        String app_acro = null;
        try (Connection con = DBConnect.connect()) {
            try (Statement s = con.createStatement()) {
                String sql = "select app_acro from apps where id=" + app_id + " and active=true";
                s.execute(sql);
                ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
                if (rs != null) {
                    while (rs.next()) {
                        app_acro = rs.getString("app_acro");
                    }
                }
            }
            return app_acro;
        }
    }
    
    public static Integer getAppID(String appAcro) throws Exception {
        Integer appID = null;
        try (Connection con = DBConnect.connect()) {
            try (Statement s = con.createStatement()) {
                String sql = "select id from apps where app_acro='" + appAcro + "' and active=true";
                s.execute(sql);
                ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
                if (rs != null) {
                    while (rs.next()) {
                        appID = rs.getInt("id");
                    }
                }
            }
            return appID;
        }
    }

    public static ObservableList<String> getAppList() throws Exception {

        try (Connection con = DBConnect.connect()) {
            try (Statement s = con.createStatement()) {
                String sql = "select distinct(app_acro) from apps where active=true";
                ResultSet rs = s.executeQuery(sql);
                ObservableList<String> list = FXCollections.observableArrayList();

                while (rs.next()) {
                    list.add(rs.getString("app_acro"));
                    //System.out.println(list.size());
                }

                s.close();
                return list;
            }

        }

    }

    public static Float timeToday(String todayDate, Integer app_id) throws Exception {
        float total = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try (Connection con = DBConnect.connect()) {
            try (Statement s = con.createStatement()) {
                String sql = "select t.start, t.end from timesheet t where app_id=" + app_id
                        + " and (t.start between #" + todayDate
                        + " 00:00:00# and #" + todayDate + " 23:59:59#)";

                ResultSet rs = s.executeQuery(sql);
                while (rs.next()) {
                    String start = rs.getString("start");
                    String end = rs.getString("end");

                    Date startDate = sdf.parse(start);
                    Date endDate;
                    try {
                        endDate = sdf.parse(end);
                    } catch (Exception e) {
                        endDate = new Date();
                    }

                    total += ((endDate.getTime() / 1000) - (startDate.getTime() / 1000));
                }
            }
            return total / 60;
        }
    }

    public static Integer[] appsThisWeek(String sunday, String endOfWeek) throws Exception {
        try (Connection con = DBConnect.connect()) {

            try (Statement s = con.createStatement()) {
                String sql = "select distinct(app_id) from timesheet,apps where apps.id = timesheet.app_id and "
                        + "(start between #" + sunday
                        + " 00:00:00# and #" + endOfWeek + " 23:59:59#)"
                        + " order by app_id";

                ResultSet rs = s.executeQuery(sql);
                int size = 0;
                while (rs.next()) {
                    size++;
                }
                Integer[] list = new Integer[size];

                rs = s.executeQuery(sql);
                int count = 0;
                while (rs.next()) {
                    list[count] = rs.getInt("app_id");
                    count++;
                }

                return list;
            }
        }
    }

    public static Boolean clockedIn() throws Exception {
        try (Connection con = DBConnect.connect()) {
            try (Statement s = con.createStatement()) {
                String sql = "select max(id) from timesheet";
                ResultSet rs = s.executeQuery(sql);
                if (rs != null) {
                    rs.next();
                    Integer id = rs.getInt(1);
                    sql = "select end from timesheet where id=" + id;
                    rs = s.executeQuery(sql);
                    if (rs != null) {
                        rs.next();
                        try {
                            if (rs.getString(1).isEmpty()) {
                                return true;
                            } else {
                                return false;
                            }
                        } catch (Exception e) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                } else {
                    return true;
                }
            } catch (Exception ex) {
                Logger.getLogger(DBSearch.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Logger.getLogger(DBSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static Integer activeAppID() throws Exception {
        Integer id = null;
        try (Connection con = DBConnect.connect()) {
            try (Statement s = con.createStatement()) {
                String sql = "select max(id) from timesheet";
                ResultSet rs = s.executeQuery(sql);
                if (rs != null) {
                    rs.next();
                    id = rs.getInt(1);
                }
            } catch (Exception ex) {
                Logger.getLogger(DBSearch.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Logger.getLogger(DBSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public static String openTime() throws Exception {
        String openSheet = null;
        try (Connection con = DBConnect.connect()) {
            try (Statement s = con.createStatement()) {
                String sql = "select max(id) from timesheet";

                ResultSet rs = s.executeQuery(sql);
                if (rs != null) {
                    rs.next();
                    Integer id = rs.getInt(1);
                    sql = "select start from timesheet where id=" + id;
                    rs = s.executeQuery(sql);
                    if (rs != null) {
                        rs.next();
                        openSheet = rs.getString("start");
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(DBSearch.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Logger.getLogger(DBSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
        return openSheet;
    }
    
    public static void clockOut(String endTime) {
        try (Connection con = DBConnect.connect()) {
            try (Statement s = con.createStatement()) {
                String sql = "select max(id) from timesheet";

                ResultSet rs = s.executeQuery(sql);
                if (rs != null) {
                    rs.next();
                    Integer id = rs.getInt(1);
                    sql = "update timesheet set end=#" + endTime + "# where id=" + id;
                    s.execute(sql);
                }
            } catch (Exception ex) {
                Logger.getLogger(DBSearch.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Logger.getLogger(DBSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void clockIn(String startTime, Integer appID) {
        try (Connection con = DBConnect.connect()) {
            try (Statement s = con.createStatement()) {
                String sql = "insert into timesheet (app_id, start)"
                        + " values (" + appID + ",#" + startTime + "#)";
                s.execute(sql);
            } catch (Exception ex) {
                Logger.getLogger(DBSearch.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Logger.getLogger(DBSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
