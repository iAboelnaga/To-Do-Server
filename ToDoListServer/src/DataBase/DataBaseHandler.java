/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.ClientDriver;
import org.json.JSONException;
import org.json.JSONObject;
import serverEntity.Entity;
import serverEntity.Items;
import serverEntity.User;
import serverEntity.myList;
import serverEntity.Task;

import Enums.RESPOND_CODE;
import connection.Client;
import java.util.List;
import serverEntity.CommentsModel;
import serverEntity.NotificationModel;
import serverEntity.TeamMate;

public class DataBaseHandler {

    private Connection connection;
    private Statement statement;
    //static DataBaseHandler access = new DataBaseHandler();
    private ResultSet result;
    private static DataBaseHandler handeller;

    private DataBaseHandler() {
        //open database connection
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/ToDo_Project", "root", "root");
            System.out.println("connect to database successfully");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("open connection failed , please try again ");
        }
    }

    public static DataBaseHandler createDB() {
        if (handeller == null) {
            synchronized (DataBaseHandler.class) {
                handeller = new DataBaseHandler();
            }
        }
        return handeller;
    }

    // query insert data on USERS table
    public int insertIntoUsers(User user) {
        try {
            PreparedStatement pst = connection.prepareStatement("INSERT INTO USERS (USER_NAME,EMAIL,PASSWORD) VALUES (?,?,?)");
            pst.setString(1, user.getUserName());
            pst.setString(2, user.getEmail());
            pst.setString(3, user.getPassword());
            pst.executeUpdate();
            return 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /*a*/
    public User getUserData(int id) throws SQLException {
        PreparedStatement pre = connection.prepareStatement("Select * from Users where user_id = ?");
        pre.setInt(1, id);
        User user;
        try (ResultSet set = pre.executeQuery()) {
            set.next();
            user = new User(id, set.getString(2), set.getString(3), set.getString(4));
        }
        pre.close();

        return user;

    }

    public int insertList(myList list) throws SQLException {
        PreparedStatement pre = connection.prepareStatement("insert into LISTS (title,assigndate,deadlinedate,owner_id)VALUES (?,?,?,?)");
        pre.setString(1, list.getTitle());
        pre.setString(2, list.getAssignDate());
        pre.setString(3, list.getDeadlineDate());
        pre.setInt(4, list.getOwnerId());
        //pre.setString(5, list.);
        //pre.setString(6, list.getDescription());
        int result = pre.executeUpdate();
        pre.close();
        if (result != 0) {
            return getListWithTitle(list.getTitle());
        } else {
            return -1;
        }

    }

    public int getListWithTitle(String title) throws SQLException {
        PreparedStatement pre = connection.prepareStatement("Select LIST_ID from LISTS where title = ?");
        pre.setString(1, title);
        int id;
        try (ResultSet set = pre.executeQuery()) {
            set.next();
            id = set.getInt(1);
        }
        pre.close();
        return id;
    }

    public int insertUser(String email, String userName, String password) {
        int x = -10;
        try {
            String insertString = "INSERT INTO USERS (USER_NAME,EMAIL,PASSWORD)  VALUES  (" + "'" + userName + "' , " + "'" + email + "' , " + "'" + password + "'" + ")";
            PreparedStatement pst = connection.prepareStatement(insertString);
            x = pst.executeUpdate();
            System.out.println("ffffffff" + x);
            pst.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
        return x;
    }

    // query select Email and password from USERS table
    //check select email and password when email found and handle passowrd by code
    /* public ArrayList<Task> selectAllTasksInList(int listId) {
        ResultSet rs = null;
      
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            PreparedStatement pst = connection.prepareStatement("select TASK.TASK_ID,TASK.TASK_DESC,TASK.BG_COLOR,TASK.TASK_STATUS ,TASK.L_ID,TASK_ASSIGN_USER.USER_ID,USERS.USER_NAME,TASK_ASSIGN_USER.TASK_STATUS from TASK,TASK_ASSIGN_USER ,USERS WHERE L_ID = ? AND TASK.TASK_ID = TASK_ASSIGN_USER.TASK_ID AND USERS.USER_ID = TASK_ASSIGN_USER.TASK_ID ");

            pst.setInt(1, listId);
            rs = pst.executeQuery();
            while (rs.next()) {
              
                Task myTask = new Task(rs.getInt("TASK_ID"), rs.getString("TASK_DESC"), rs.getString("BG_COLOR"), rs.getString("TASK_STATUS"), rs.getInt("L_ID"),rs.getInt("USER_ID"),rs.getString("USER_NAME"),rs.getString("TASK_STATUS"));
                tasks.add(myTask);
            }
            System.out.println("select All Tasks In List, successed");
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return tasks;
    }*/
    public User selectEmail(String email, String pass) {
        User user = new User();
        ResultSet result;
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM USERS WHERE EMAIL = ?");
            pst.setString(1, email);
            result = pst.executeQuery();
            if (result.next()) {
                do {
                    //System.out.println("email is " + result.getString(3) + " password is " + result.getString(4) + " id = "+result.getString(1) +" username "+result.getString(2));
                    //handle if passowrd correct or not      
                    if (result.getString(4).equals(pass)) {
                        System.out.println("logged in successfully ");
                        user = new User();
                        user.setId(result.getInt(1));
                        user.setUserName(result.getString(2));
                        user.setEmail(result.getString(3));
                        user.setPassword(result.getString(4));
                    } else {
                        System.out.println("password incorrect ");
                    }
                } while (result.next());
            } else {
                System.out.println("Email not found , please enter correct email");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            result = null;
        }
        return user;
    }

    //get user with user object
    public JSONObject getUser(User user) {
        JSONObject respondJson = new JSONObject();
        ResultSet result;
        if (user == null) {
            try {
                respondJson.put("Code", RESPOND_CODE.FAILD);
                return respondJson;
            } catch (JSONException ex) {
                System.out.println("get user exception");
                Logger.getLogger(DataBaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            try {

                String email = user.getEmail();
                String password = user.getPassword();

                PreparedStatement statement = connection.prepareStatement("SELECT * FROM USERS WHERE EMAIL = ? AND PASSWORD =?");
                statement.setString(1, email);
                statement.setString(2, password);
                result = statement.executeQuery();
                //boolean hasRow = result.next();
                if (result.next()) {
                    int userId = result.getInt(1);
                    String resultUserName = result.getString(2);
                    String resultEmail = result.getString(3);
                    String resultPassword = result.getString(4);
                    respondJson.put("Code", RESPOND_CODE.SUCCESS);
                    respondJson.put("id", userId);
                    respondJson.put("email", resultEmail);
                    respondJson.put("username", resultUserName);
                    respondJson.put("password", resultPassword);

                } else {
                    respondJson.put("Code", RESPOND_CODE.FAILD);

                    System.out.println("no row with this email or passwords");
                }

            } catch (SQLException | JSONException ex) {
                System.out.println("Repository class , getUser method exception");
            }
        }
        return respondJson;
    }

    //query select projects that user owned 
    public ArrayList<myList> selectOwnedLists(int owner_id) {
        ArrayList<myList> list = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM LISTS WHERE OWNER_ID = ?");
            pst.setInt(1, owner_id);
            result = pst.executeQuery();
            while (result.next()) {
                myList mlist = new myList(result.getInt("LIST_ID"), result.getString("TITLE"), result.getString("ASSIGNDATE"), result.getString("DEADLINEDATE"), result.getInt("OWNER_ID"));
                list.add(mlist);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            list = null;
        }
        return list;
    }

    //query select projects that user work
    public ArrayList<myList> selectWorkInLists(int user_id) {
        ArrayList<myList> list = new ArrayList<>();

        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM USERS_WORK_LIST , LISTS  WHERE USER_ID = ? AND USERS_WORK_LIST.LIST_ID = LISTS.LIST_ID");
            pst.setInt(1, user_id);
            result = pst.executeQuery();
            while (result.next()) {
                myList mlist = new myList(result.getInt("LIST_ID"), result.getString("TITLE"), result.getString("ASSIGNDATE"), result.getString("DEADLINEDATE"), result.getInt("OWNER_ID"));
                list.add(mlist);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            result = null;
        }
        return list;
    }

    //save request team mate
    //query insert request to Team_Mate table
    public int insertRequestTeamMate(int userID, int teamMateID, String status) {
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        try {
            PreparedStatement pst1 = connection.prepareStatement("INSERT INTO USER_HAS_TEAMMATE (TEAMMATE_ID,USER_ID,TEAMMATE_STATUS,DATE) VALUES (?,?,?,?)");
            pst1.setInt(1, teamMateID);
            pst1.setInt(2, userID);
            pst1.setString(3, status);
            pst1.setString(4, date.toString());
            pst1.executeUpdate();
            // insertRequestTeamMate(teamMateID,userID ,status);
            System.out.println("inserted correctly");
            return 1;

        } catch (SQLException ex) {
            if (ex instanceof SQLIntegrityConstraintViolationException) {
                System.out.println("this request sent before");
                return 2;
            } else {
                ex.printStackTrace();
                System.out.println("can`t insert this row , ensure the data inserted is correct ");
                return 3;
            }
        }

    }

    public int RemoveTeamMate(int userId, int TeamMateId) {
        try {
            PreparedStatement pst = connection.prepareStatement("DELETE FROM USER_HAS_TEAMMATE WHERE USER_ID = ? AND TEAMMATE_ID = ? ");
            pst.setInt(1, userId);
            pst.setInt(2, TeamMateId);
            pst.executeUpdate();
            return 1;

        } catch (SQLException ex) {
            return -1;
        }
    }
      public ArrayList<Task> selectAllTasksInList(int listId) {
        ResultSet rs = null;
        ResultSet rs2 = null;
        String UserAsignName = "No One";
        String UserAsignStatus = "Not Assigned";
        int UserAssignTaskId = 0;
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            PreparedStatement pst = connection.prepareStatement("select * from TASK WHERE L_ID = ?");
            pst.setInt(1, listId);
            rs = pst.executeQuery();
            while (rs.next()) {
               
                rs2 = selectUserAssignTask(rs.getInt("TASK_ID"));
                 while (rs2.next()){
                      UserAssignTaskId = rs2.getInt("USER_ID");
                      UserAsignName = selectName(rs2.getInt("USER_ID"));
                      UserAsignStatus = rs2.getString("TASK_STATUS");
                 }
                
               // UserAsignName = selectName(rs2.getInt("USER_ID"));
                Task myTask = new Task(rs.getInt("TASK_ID"), rs.getString("TASK_DESC"), rs.getString("BG_COLOR"), rs.getString("TASK_STATUS"), rs.getInt("L_ID"),UserAssignTaskId,UserAsignName,UserAsignStatus);
                tasks.add(myTask);
            }
            System.out.println("select All Tasks In List, successed");
        } catch (SQLException ex) {
            System.out.println("select All Tasks In List, failed");
        }

        return tasks;
    }
       public String selectName(int id) {
        try {
            ResultSet rs = null;
            PreparedStatement pst = connection.prepareStatement("SELECT USER_NAME FROM USERS WHERE USER_ID = ? ");
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("USER_NAME");
            }
        } catch (SQLException ex) {
            System.out.println("Cant select the email");

        }
        return "No One";

    }


    public ResultSet selectUserAssignTask(int TaskId) {

        ResultSet rs = null;
        try {
            PreparedStatement pst = connection.prepareStatement("select * from TASK_ASSIGN_USER WHERE TASK_ID = ?");
            pst.setInt(1, TaskId);
            rs = pst.executeQuery();
            

        } catch (SQLException ex) {
            System.out.println("select All Tasks In List, failed");
        }
        return rs;
    }


    public ArrayList<TeamMate> selectAllTeamMates(int userID) {
        ArrayList<TeamMate> TeamMates = new ArrayList<>();
        try {
            ResultSet rs = null;

            PreparedStatement pst = connection.prepareStatement("SELECT TEAMMATE_ID ,TEAMMATE_STATUS FROM  USER_HAS_TEAMMATE  WHERE USER_ID = ?  ");
            pst.setInt(1, userID);

            rs = pst.executeQuery();
            while (rs.next()) {
                int teamMateId = rs.getInt("TEAMMATE_ID");
                String status = rs.getString("TEAMMATE_STATUS");
                String userName = selectName(teamMateId);

                boolean online = Client.isInVector(teamMateId);
                TeamMate myTeamMate = new TeamMate(userName, teamMateId, status, online);
                TeamMates.add(myTeamMate);
            }
        } catch (SQLException ex) {
            System.out.println("print exception " + ex);
            TeamMates = null;
        }
        return TeamMates;
    }

   

    public ArrayList<NotificationModel> selectAllRequestsAssignTask(int userID) {

        ArrayList<NotificationModel> notificationModels = new ArrayList<>();

        try {
            ResultSet rs = null;
            PreparedStatement pst = connection.prepareStatement("SELECT TASK_ASSIGN_USER.Task_ID , USERS.USER_NAME, TASK.TASK_DESC , LISTS.TITLE FROM  USERS , TASK ,TASK_ASSIGN_USER , LISTS  WHERE TASK.TASK_ID = TASK_ASSIGN_USER.TASK_ID AND LISTS.LIST_ID = TASK.L_ID AND TASK_ASSIGN_USER.USER_ID = ?  AND USERS.USER_ID = LISTS.OWNER_ID  AND  TASK_ASSIGN_USER.TASK_STATUS =? ");
            pst.setInt(1, userID);
            pst.setString(2, "wait");
            rs = pst.executeQuery();
            while (rs.next()) {
                NotificationModel notificationModel = new NotificationModel(rs.getString("TITLE"), rs.getString("USER_NAME"), rs.getInt("Task_ID"), rs.getString("TASK_DESC"), "DATE");
                notificationModels.add(notificationModel);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            notificationModels = null;
        }
        return notificationModels;
    }

    public int selectId(String EMAIL) {
        try {

            PreparedStatement pst = connection.prepareStatement("SELECT USER_ID FROM USERS WHERE EMAIL = ? ");
            pst.setString(1, EMAIL);
            result = pst.executeQuery();
            if (result.next()) {
                return result.getInt("USER_ID");
            }
        } catch (SQLException ex) {
            System.out.println("Cant select the email");

        }
        return -1;

    }

    public ArrayList<NotificationModel> selectAllRequestsTeamMate(int userID) {
        ArrayList<NotificationModel> notificationModels = new ArrayList<>();

        try {
            ResultSet rs = null;
            String state = "wait";
            PreparedStatement pst = connection.prepareStatement("SELECT  USER_ID , DATE FROM  USER_HAS_TEAMMATE  WHERE  TEAMMATE_ID = ?  AND TEAMMATE_STATUS = ? ");
            pst.setInt(1, userID);
            pst.setString(2, state);
            rs = pst.executeQuery();
            while (rs.next()) {
                String userName = selectName(rs.getInt("USER_ID"));
                NotificationModel notificationModel = new NotificationModel(userName, rs.getInt("USER_ID"), rs.getString("DATE"));
                notificationModels.add(notificationModel);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            notificationModels = null;
        }
        return notificationModels;
    }

    //change state of team Mate request 
    public String changeRequestTeamMateState(int teamMate, int userID, String state) {
        String response;
        try {
            if (state.equals("accept")) {
                PreparedStatement pst = connection.prepareStatement("UPDATE USER_HAS_TEAMMATE SET TEAMMATE_STATUS = ?  WHERE USER_ID = ? AND TEAMMATE_ID =?");
                pst.setString(1, state);
                pst.setInt(2, userID);
                pst.setInt(3, teamMate);
                pst.executeUpdate();
                response = "request accepted ";
            } else {
                PreparedStatement pst = connection.prepareStatement("DELETE FROM USER_HAS_TEAMMATE   WHERE USER_ID = ? AND TEAMMATE_ID =?");
                pst.setInt(1, userID);
                pst.setInt(2, teamMate);
                pst.executeUpdate();
                response = "request rejected ";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            response = " can`t change state error on database";
        }
        return response;
    }

    //change state of assign task
    public String changeRequestAssignTaskstate(int userID, String state) {
        String response;
        try {
            if (state.equals("accept")) {
                PreparedStatement pst = connection.prepareStatement("UPDATE TASK_ASSIGN_USER SET TASK_STATUS = ?  WHERE USER_ID = ? ");
                pst.setString(1, state);
                pst.setInt(2, userID);
                pst.executeUpdate();
                response = "request accepted ";
            } else {
                PreparedStatement pst = connection.prepareStatement("DELETE FROM TASK_ASSIGN_USER WHERE USER_ID = ?");
                pst.setInt(1, userID);
                pst.executeUpdate();
                response = "request rejected ";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            response = " can`t change state error on database";
        }
        return response;
    }
    //query select task  

    public myList selectList(int listId) {
        ResultSet rs = null;
        myList mlist = new myList();
        try {
            PreparedStatement pst = connection.prepareStatement("select * from LISTS WHERE LIST_ID = ?");
            pst.setInt(1, listId);
            rs = pst.executeQuery();
            mlist.setId(result.getInt("LIST_ID"));
            mlist.setTitle(result.getString("TITLE"));
            mlist.setAssignDate(result.getString("ASSIGNDATE"));
            mlist.setDeadlineDate(result.getString("DEADLINEDATE"));
            mlist.setOwnerId(result.getInt("OWNER_ID"));
            System.out.println("select successfuly");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return mlist;
    }
    //ONLY USED WHEN OWNER SELECT LIST FROM OWNED LIST 

    public int updateList(int listId, String title, String assignDate, String deadlineDate) {
        try {
            PreparedStatement pst = connection.prepareStatement("UPDATE LISTS SET  TITLE = ?,ASSIGNDATE = ?,DEADLINEDATE = ? WHERE LIST_ID = ?");
            pst.setString(1, title);
            pst.setString(2, assignDate);
            pst.setString(3, deadlineDate);
            pst.setInt(4, listId);
            pst.executeUpdate();
            return 1;
        } catch (SQLException ex) {

            return -1;

        }

    }
    //ONLY USED WHEN OWNER SELECT LIST FROM OWNED LIST 

    public int DeleteList(int listId) {
        try {
            PreparedStatement pst = connection.prepareStatement("DELETE FROM LISTS "
                    + "WHERE LIST_ID = ?");
            pst.setInt(1, listId);
            pst.executeUpdate();
            return 1;

        } catch (SQLException ex) {
            return -1;
        }
    }

    public ResultSet selectUserAssigninTask(int tasklId) {
        ResultSet rs = null;
        try {
            PreparedStatement pst = connection.prepareStatement("select * from TASK_ASSIGN_USER WHERE Task_ID = ?");
            pst.setInt(1, tasklId);
            rs = pst.executeQuery();

        } catch (SQLException ex) {
            System.out.println("select User Assign in Task, failed");
        }
        return rs;
    }

    /*public ArrayList<Task> selectAllTasksInList(int listId) {
        ResultSet rs = null;
      
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            PreparedStatement pst = connection.prepareStatement("select TASK.TASK_ID,TASK.TASK_DESC,TASK.BG_COLOR,TASK.TASK_STATUS ,TASK.L_ID,TASK_ASSIGN_USER.USER_ID,USERS.USER_NAME,TASK_ASSIGN_USER.TASK_STATUS from TASK,TASK_ASSIGN_USER ,USERS WHERE L_ID = ? AND TASK.TASK_ID = TASK_ASSIGN_USER.TASK_ID AND USERS.USER_ID = TASK_ASSIGN_USER.TASK_ID ");

            pst.setInt(1, listId);
            rs = pst.executeQuery();
            while (rs.next()) {
              
                Task myTask = new Task(rs.getInt("TASK_ID"), rs.getString("TASK_DESC"), rs.getString("BG_COLOR"), rs.getString("TASK_STATUS"), rs.getInt("L_ID"),rs.getInt("USER_ID"),rs.getString("USER_NAME"),rs.getString("TASK_STATUS"));
                tasks.add(myTask);
            }
            System.out.println("select All Tasks In List, successed");
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return tasks;
    }*/
   /* public ArrayList<Task> selectAllTasksInList(int listId) {
        ResultSet rs = null;
        ResultSet rs2 = null;
        String UserAsignName = " ";
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            PreparedStatement pst = connection.prepareStatement("select * from TASK WHERE L_ID = ?");
            pst.setInt(1, listId);
            rs = pst.executeQuery();
            while (rs.next()) {
               
                rs2 = selectUserAssignTask(rs.getInt("TASK_ID"));
                rs2.next();
                UserAsignName = selectName(rs2.getInt("USER_ID"));
                Task myTask = new Task(rs.getInt("TASK_ID"), rs.getString("TASK_DESC"), rs.getString("BG_COLOR"), rs.getString("TASK_STATUS"), rs.getInt("L_ID"),rs2.getInt("USER_ID"),UserAsignName,rs2.getString("TASK_STATUS"));
                tasks.add(myTask);
            }
            System.out.println("select All Tasks In List, successed");
        } catch (SQLException ex) {
            System.out.println("select All Tasks In List, failed");
        }

        return tasks;
    }

    public ResultSet selectUserAssignTask(int TaskId) {

        ResultSet rs = null;
        try {
            PreparedStatement pst = connection.prepareStatement("select * from TASK_ASSIGN_USER WHERE TASK_ID = ?");
            pst.setInt(1, TaskId);
            rs = pst.executeQuery();
            

        } catch (SQLException ex) {
            System.out.println("select All Tasks In List, failed");
        }
        return rs;
    }*/

    public int ownerInsertTask(String taskDesc, String bg_color, String taskStatus, int listId) {
        try {
            PreparedStatement pst = connection.prepareStatement("INSERT INTO TASK (TASK_DESC,BG_COLOR,TASK_STATUS, L_ID)"
                    + "VALUES ( ?, ?, ?, ?)");
            pst.setString(1, taskDesc);
            pst.setString(2, bg_color);
            pst.setString(3, taskStatus);
            pst.setInt(4, listId);
            pst.executeUpdate();
            return 1;
        } catch (SQLException ex) {
            return 2;
        }
    }
     public int userInList(int userId, int listId) {
        try {
            PreparedStatement pst = connection.prepareStatement("INSERT INTO USERS_WORK_LIST (USER_ID,LIST_ID)"
                    + "VALUES ( ?, ?)");
            pst.setInt(1, userId);
            pst.setInt(2, listId);

            pst.executeUpdate();
            return 1;
        } catch (SQLException ex) {
            return 2;
        }
    }

    public int ownerUpdateTask(int taskId, String taskDesc, String bg_color, String taskStatus) {
        try {
            PreparedStatement pst = connection.prepareStatement("UPDATE TASK SET TASK_DESC = ?,BG_COLOR = ?,TASK_STATUS = ? WHERE TASK_ID = ? ");
            pst.setString(1, taskDesc);
            pst.setString(2, bg_color);
            pst.setString(3, taskStatus);
            pst.setInt(4, taskId);
            pst.executeUpdate();
            return 1;
            

        } catch (SQLException ex) {
            return 2;
        }
    }

    public int ownerAssignTask(int taskId, int uerId, String status) {
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        String TASK_ASSIGN_DATE = date.toString();
        try {
            PreparedStatement pst = connection.prepareStatement("INSERT INTO TASK_ASSIGN_USER (TASK_ID,USER_ID,TASK_STATUS,TASK_ASSIGN_DATE ) VALUES (?, ?, ? ,?) ");
            pst.setInt(1, taskId);
            pst.setInt(2, uerId);
            pst.setString(3, status);
            pst.setString(4, TASK_ASSIGN_DATE);
            int res = pst.executeUpdate();
            System.out.println("gggggggggggg"+res);
            return 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;

        }
    }

    public String ownerUpdateUserAssignTask(int taskId, int uerId, String status) {
        try {
            PreparedStatement pst = connection.prepareStatement("UPDATE TASK_ASSIGN_USER SET USER_ID = ?,TASK_STATUS=? WHERE TASK_ID = ? ");
            pst.setInt(1, uerId);
            pst.setString(2, status);
            pst.setInt(3, taskId);
            pst.executeUpdate();
            return ("Update UserAssignTask ,successed");

        } catch (SQLException ex) {
            return ("Update UserAssignTask ,failed");
        }
    }

    public int normalUserupdateTask(int taskId, String taskStatus) {
        try {
            PreparedStatement pst = connection.prepareStatement("UPDATE TASK "
                    + "SET TASK_STATUS=? WHERE TASK_ID = ?");
            pst.setString(1, taskStatus);
            pst.setInt(2, taskId);
            pst.executeUpdate();
            return 1;

        } catch (SQLException ex) {
            return 2;
        }
    }

    public int AddCommentOnTask(int taskId, int uerId, String commentDate, String commentText) {
        try {
            PreparedStatement pst = connection.prepareStatement("INSERT INTO USER_COMMENT_TASK (TASK_ID,USER_ID,COMMENT_DATE,COMMENT_TEXT )"
                    + "VALUES (?, ?, ?,?)");
            pst.setInt(1, taskId);
            pst.setInt(2, uerId);
            pst.setString(3, commentDate);
            pst.setString(4, commentText);

            pst.executeUpdate();
            return 1;

        } catch (SQLException ex) {
            return 2;
        }
    }

    public ArrayList<CommentsModel> RetriveAllCommentOnTask(int taskId) {
        ResultSet rs = null;
        ArrayList<CommentsModel> comm = new ArrayList<CommentsModel>();

        try {
            PreparedStatement pst = connection.prepareStatement("select * from USER_COMMENT_TASK WHERE TASK_ID = ? ");
            pst.setInt(1, taskId);
            rs = pst.executeQuery();

            while (rs.next()) {
                String userName = selectName(rs.getInt("USER_ID"));
                CommentsModel mcomm = new CommentsModel(rs.getInt("TASK_ID"), rs.getInt("USER_ID"), userName, rs.getString("COMMENT_DATE"), rs.getString("COMMENT_TEXT"));
                comm.add(mcomm);
            }
            System.out.println("Retrive Comment On Task,successed");

        } catch (SQLException ex) {
            System.out.println("Retrive Comment On Task,failed");
        }
        return comm;
    }

    public void ownerDeleteTask(int TaskId) {
        DeleteFromCommentTable(TaskId);
        DeleteFromAssignUserTable(TaskId);
        DeleteFromTaskTable(TaskId);

    }

    public String DeleteFromTaskTable(int TaskId) {
        try {
            PreparedStatement pst = connection.prepareStatement("DELETE FROM TASK "
                    + "WHERE TASK_ID = ?");
            pst.setInt(1, TaskId);
            pst.executeUpdate();
            return ("Delete From Task Table,successed");

        } catch (SQLException ex) {
            return ("Delete From Task Table,failed");
        }
    }

    public String DeleteFromCommentTable(int TaskId) {
        try {
            PreparedStatement pst = connection.prepareStatement("DELETE FROM USER_COMMENT_TASK "
                    + "WHERE TASK_ID = ?");
            pst.setInt(1, TaskId);

            pst.executeUpdate();
            return ("Delete From Comment Table,successed");

        } catch (SQLException ex) {
            return ("Delete From Comment Table,failed");
        }
    }

    public String DeleteFromAssignUserTable(int TaskId) {
        try {
            PreparedStatement pst = connection.prepareStatement("DELETE FROM TASK_ASSIGN_USER "
                    + "WHERE TASK_ID = ?");
            pst.setInt(1, TaskId);
            pst.executeUpdate();
            return ("Delete From AssignUser Table,successed");

        } catch (SQLException ex) {
            return ("Delete From AssignUser Table, failed");
        }
    }

    //SELECT COUNT(column_name) FROM table_name
    public int getNumberOfUsers() {
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT COUNT(*) FROM USERS");
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return -2;

        } catch (SQLException ex) {
            return -1;
        }
    }

    public int getNumberOfProjects() {
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT COUNT(*) FROM LISTS");
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return -2;

        } catch (SQLException ex) {
            return -1;
        }
    }

    // close connection 
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
            System.out.println("data base closed");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
public String selectListID(int TaskID) {
        try {
            ResultSet rs = null;
            PreparedStatement pst = connection.prepareStatement("SELECT L_ID FROM TASK WHERE TASK_ID = ? ");
            pst.setInt(1, TaskID);
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("L_ID");
            }
        } catch (SQLException ex) {
            System.out.println("Cant select the ListID");

        }
        return null;

    }
        
        
        public List<Integer> selectAllUsersInListWithTask(int TaskID) {
        try {
            List<Integer> lists = new ArrayList<>();
            int l_id = Integer.parseInt(selectListID(TaskID));
            ResultSet rs = null;
            PreparedStatement pst = connection.prepareStatement("SELECT TASK_ASSIGN_USER.USER_ID FROM TASK_ASSIGN_USER , TASK WHERE TASK.L_ID = ? AND TASK.TASK_ID = TASK_ASSIGN_USER.TASK_ID ");
            pst.setInt(1, l_id);
            rs = pst.executeQuery();
            if (rs.next()) {
                lists.add(rs.getInt("USER_ID"));
            }
            return lists;
        } catch (SQLException ex) {
            System.out.println("Cant select the usersID");

        }
        return null;

    }
}
