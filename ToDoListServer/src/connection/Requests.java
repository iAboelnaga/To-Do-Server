/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import serverEntity.User;
import DataBase.DataBaseHandler;
import Enums.REQUEST;
import Enums.RESPOND_CODE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.ParseException;
import java.util.List;
import serverEntity.CommentsModel;
import serverEntity.NotificationModel;
import serverEntity.Task;
import serverEntity.TeamMate;
import serverEntity.myList;
import serverGUI.PortCheck;

/**
 *
 * @author Aboelnaga
 */
public class Requests implements ClientRequest {

    DataBaseHandler repository;

    public Requests() {
        repository = DataBaseHandler.createDB();
    }

    public JSONObject post(String[] paramter, JSONObject body, RequesManager handler) throws JSONException {

        if (paramter[1].equals("list")) {
            try {
                myList list;

                list = getTodoObject(body);

                int resullt = repository.insertList(list);

                return resullt != -1 ? new JSONObject("{id:" + resullt + "}") : new JSONObject("{Error:\"Error insert list \"}");
            } catch (SQLException ex) {
                try {
                    System.out.println(ex.getMessage());
                    return new JSONObject("{Error:\"Error insert list \"}");
                } catch (JSONException ex1) {
                    Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } catch (JSONException ex) {
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        if (paramter[1].equals("register")) {
            try {
                String userName = body.getString("username");
                String email = body.getString("email");
                String password = body.getString("password");
                int insertResult2 = repository.insertUser(email, userName, password);

                body = new JSONObject();
                if (insertResult2 == 1) {
                    body.put("result", "1");
                } else {
                    body.put("result", "User already exist in DB");
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        if (paramter[1].equals(REQUEST.LOGIN)) {
            try {
                String email2 = body.getString("email");
                String password = body.getString("password");
                User user2 = new User(email2, password);
                User user = getUserFromJson(body);

                JSONObject respond = repository.getUser(user2);
                System.out.println("respond is " + respond.toString());
                // get last one been add to victor
                System.out.println("respond again " + respond.getInt("Code"));
                if (respond != null && respond.getInt("Code") == RESPOND_CODE.SUCCESS) {
                    //add user to server clients
                    int userId = respond.getInt("id");
                    String email = respond.getString("email");
                    if (Client.isInVector(userId)) {
                        respond.put("Code", RESPOND_CODE.IS_LOGIN);
                        return respond;
                    }

                    Client client = new Client(userId, email, handler);
                    //ArrayList<User> friends = repository.getUserFriends(userId);
                    //Client.notifiyFriends(user, friends, REQUEST.FRIEND_ONLINE);
                    Client.addClient(client);

                } else {
                    System.out.println("login respond faild, not added to portListener any client");
                }

                System.out.println(Client.getclientVector().size());
                for (Client clientt : Client.getclientVector()) {
                    System.out.println(clientt.getClientName());
                }

                return respond;
            } catch (JSONException ex) {
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (paramter[1].equals("AddTeamMate")) {
            int id = repository.selectId(body.getString("email"));
            if (id != -1) {
                int result = repository.insertRequestTeamMate(body.getInt("userId"), id, "wait");
                int result2 = repository.insertRequestTeamMate(id, body.getInt("userId"), "wait");
                System.out.println(result2);
                switch (result) {
                    case 1:
                        body.put("result", "1");
                        break;
                    case 2:
                        body.put("result", "2");
                        break;
                    default:
                        body.put("result", "3");
                        break;
                }
            } else {
                body.put("result", "4");

            }

        }
        if (paramter[1].equals("AddTask")) {
            int result = repository.ownerInsertTask(body.getString("description"), body.getString("bg_color"), body.getString("status"), body.getInt("listId"));

            switch (result) {
                case 1:
                    body.put("result", "1");
                    break;
                case 2:
                    body.put("result", "2");
                    break;
            }

        }
        if (paramter[1].equals("UpdateTask")) {
            int result = repository.ownerUpdateTask(body.getInt("id"), body.getString("description"), body.getString("bg_color"), body.getString("status"));

            switch (result) {
                case 1:
                   
                    if(body.getString("status").equals("done"))
                    {
                        List<Integer> userIDs = repository.selectAllUsersInListWithTask(2);
                        System.out.println("IDNUM : "+ userIDs);
                
                        String taskDesc ="description";
                        PortCheck.SendALL(userIDs,taskDesc);
                    }
                     body.put("result", "1");
                    break;
                case 2:
                    body.put("result", "2");
                    break;
            }

        }
         if (paramter[1].equals("UpdateTaskInWorkInProject")) {
            int result = repository.normalUserupdateTask(body.getInt("id"),body.getString("status"));

            switch (result) {
                case 1:
                     if(body.getString("status").equals("done"))
                    {
                        List<Integer> userIDs = repository.selectAllUsersInListWithTask(2);
                        System.out.println("IDNUM : "+ userIDs);
                
                        String taskDesc ="description";
                        PortCheck.SendALL(userIDs,taskDesc);
                    }
                     body.put("result", "1");
                    break;
                case 2:
                    body.put("result", "2");
                    break;
            }

        }
        if (paramter[1].equals(REQUEST.TeamMateState)) {
            String result = repository.changeRequestTeamMateState(body.getInt("userID"), body.getInt("teamMate"), body.getString("state"));
            repository.changeRequestTeamMateState(body.getInt("teamMate"), body.getInt("userID"), body.getString("state"));

            body.put("result", result);
        }

        if (paramter[1].equals(REQUEST.TaskState)) {
            String result = repository.changeRequestAssignTaskstate(body.getInt("userID"), body.getString("state"));
            body.put("result", result);
        }
        if (paramter[1].equals("AddComment")) {
            int result = repository.AddCommentOnTask(body.getInt("taskId"), body.getInt("userId"), body.getString("commentDate"), body.getString("commentText"));

            switch (result) {
                case 1:
                    body.put("result", "1");
                    break;
                case 2:
                    body.put("result", "2");
                    break;

            }

        }
        if (paramter[1].equals("assignTask")) {
            try {
                int userId = body.getInt("userId");
                int itemId = body.getInt("ItemId");
                int listId = body.getInt("listId");
                int state = repository.userInList(userId, listId);
                int status = repository.ownerAssignTask(userId,itemId,"wait");
                if (status > 0) {
                    // notifyCollaborator
                }
                return status != -1 ? new JSONObject("{id:" + status + "}") : new JSONObject("{Error:\"Error \"}");
            } catch (JSONException ex) {
                System.err.println("cannot add new task member");
            }

        }

        return body;
    }

    @Override
    public JSONObject get(String[] paramter
    ) {

        if (paramter[1].equals("todo")) {
            try {
                //  get user data
                User user = repository.getUserData(Integer.parseInt(paramter[2]));

                // get user todo list
                ArrayList<myList> toDoList = repository.selectOwnedLists(Integer.parseInt(paramter[2]));
                //get shared todo 
                ArrayList<myList> shared = repository.selectWorkInLists(Integer.parseInt(paramter[2]));

                Gson gson = new GsonBuilder().create();

                // convert shared List to json
                String sharedArray = gson.toJson(shared);
                JSONArray sharedJSONArray = new JSONArray(sharedArray);
                // convert todoList to jsonArray
                String TodoArray = gson.toJson(toDoList);

                JSONArray todojsonArray = new JSONArray(TodoArray);
                // convert user to json
                JSONObject userJosn = user.getUserAsJson();
                // add todolist to user 
                userJosn.put("todo_list", todojsonArray);
                // add shared List 
                userJosn.put("shared_list", sharedJSONArray);

                return userJosn;

            } catch (SQLException ex) {
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                System.out.println(ex);
            }
        }
        //TODO
        if (paramter[1].equals("list_details")) {
            myList list;
            list = repository.selectList(Integer.parseInt(paramter[2]));

            //JSONObject todoJson = list
            //return list;
        }
        if (paramter[1].equals("tasks")) {
            try {
                User user = repository.getUserData(Integer.parseInt(paramter[2]));
                int listID = repository.getListWithTitle(paramter[3]);
                System.out.println("list id " + listID);
                ArrayList<Task> tasks = repository.selectAllTasksInList(listID);
                Gson gson = new GsonBuilder().create();
                String tasksArray = gson.toJson(tasks);
                JSONArray tasksJson = new JSONArray(tasksArray);
                JSONObject userJosn = user.getUserAsJson();
                userJosn.put("tasks", tasksJson);
                return userJosn;

            } catch (SQLException ex) {
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        if (paramter[1].equals("GetTeamMates")) {
            try {
                ArrayList<TeamMate> TeamMates = new ArrayList<>();
                TeamMates = repository.selectAllTeamMates(Integer.parseInt(paramter[2]));
                JSONArray res = new JSONArray(TeamMates);
                System.out.println(res);
                JSONObject jsonBbjOfTeamMates = new JSONObject();
                jsonBbjOfTeamMates.put("result", res);
                return jsonBbjOfTeamMates;
            } catch (JSONException ex) {
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (paramter[1].equals("GetTasks")) {
            try {

                ArrayList<Task> tasks = repository.selectAllTasksInList(Integer.parseInt(paramter[2]));
                JSONArray res = new JSONArray(tasks);
                System.out.println(res);
                JSONObject jsonBbjOfTaskS = new JSONObject();
                jsonBbjOfTaskS.put("result", res);
                return jsonBbjOfTaskS;
            } catch (JSONException ex) {
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (paramter[1].equals("GetComments")) {
            try {
                ArrayList<CommentsModel> Comments = new ArrayList<>();
                Comments = repository.RetriveAllCommentOnTask(Integer.parseInt(paramter[2]));
                JSONArray res = new JSONArray(Comments);
                System.out.println(res);
                JSONObject jsonBbjOfTeamMates = new JSONObject();
                jsonBbjOfTeamMates.put("result", res);
                return jsonBbjOfTeamMates;
            } catch (JSONException ex) {
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (paramter[1].equals(REQUEST.NOTIFICATION)) {
            try {
                System.out.println("MyRequest " + paramter[2]);
                //repository.se
                List<NotificationModel> notifyList = repository.selectAllRequestsTeamMate(Integer.parseInt(paramter[2]));
                List<NotificationModel> notifyList2 = repository.selectAllRequestsAssignTask(Integer.parseInt(paramter[2]));

                notifyList.addAll(notifyList2);

                JSONObject jsonBbjOfNotify = new JSONObject();
                JSONArray jsArray = new JSONArray(notifyList);
                jsonBbjOfNotify.put("result", jsArray);
                return jsonBbjOfNotify;
            } catch (JSONException ex) {
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;

    }

    @Override
    public int put(String[] paramter, JSONObject body) {
        //TODO
        int resullt = 0;
        if (paramter[1].equals("updateList")) {
            try {
                myList list = getTodoObject(body);
                resullt = repository.updateList(list.getId(), list.getTitle(), list.getAssignDate(), list.getDeadlineDate());
            } catch (JSONException | ParseException ex) {
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return resullt;
    }

    @Override
    public JSONObject delete(String[] paramter) {
        if (paramter[1].equals("RemoveTeamMate")) {
            try {
                int respond = repository.RemoveTeamMate(Integer.parseInt(paramter[2]), Integer.parseInt(paramter[3]));
                int respond2 = repository.RemoveTeamMate(Integer.parseInt(paramter[3]), Integer.parseInt(paramter[2]));

                System.out.println(respond);
                JSONObject respondobj = new JSONObject();
                respondobj.put("result", respond);
                return respondobj;
            } catch (JSONException ex) {
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (paramter[1].equals("RemoveList")) {
            try {
                int respond = repository.DeleteList(Integer.parseInt(paramter[2]));
                System.out.println(respond);
                JSONObject respondobj = new JSONObject();
                respondobj.put("result", respond);
                return respondobj;
            } catch (JSONException ex) {
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(paramter[1].equals("RemoveTask"))
        {
            try {
                repository.ownerDeleteTask(Integer.parseInt(paramter[2]));
                JSONObject respondobj = new JSONObject();
                respondobj.put("result", "1");
                return respondobj;
            } catch (JSONException ex) {
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    private ArrayList<User> getFriendsList(JSONArray friendsJson) throws JSONException {
        ArrayList<User> friends = new ArrayList<>();
        for (int i = 0; i < friendsJson.length(); i++) {
            JSONObject json = friendsJson.getJSONObject(i);
            friends.add(new User(json.getInt("ID"), json.getString("USER_NAME")));
        }
        return friends;
    }

    private User getUserFromJson(JSONObject body) {
        User user = new User();
        try {
            String email = body.getString("email");
            String password = body.getString("password");
            user.setEmail(email);
            user.setPassword(password);
        } catch (JSONException ex) {
            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, null, ex);
        }

        return user;
    }

    private myList getTodoObject(JSONObject body) throws JSONException, ParseException {
        myList toDoList = new myList(body.getInt("listId"),body.getString("title"), body.getString("assignDate"), body.getString("deadLineDate"), body.getInt("ownerId"));
        if (body.has("id")) {
            toDoList.setId(body.getInt("id"));
        }
        return toDoList;
    }

}
