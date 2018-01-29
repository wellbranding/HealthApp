<?php

class DbOperation
{
    private $con;

    function __construct()
    {
        require_once dirname(__FILE__) . '/DbConnect.php';
        $db = new DbConnect();
        $this->con = $db->connect();
    }

    //Method to create a new user
    function registerUser($email, $uid, $name)
    {
        if (!$this->isUserExist($uid)) {
            $password = md5($pass);
            $stmt = $this->con->prepare("INSERT INTO Users (mail, uid, displayname) VALUES (?, ?, ?)");
            $stmt->bind_param("sss", $email, $uid, $name);
            if ($stmt->execute())
                return USER_CREATED;
            return USER_CREATION_FAILED;
        }
        return USER_CREATED;
    }
    function getUserByUid($uid)
    {
        $stmt = $this->con->prepare("SELECT id, mail, uid, displayname FROM Users WHERE uid = ?");
        $stmt->bind_param("s", $uid);
        $stmt->execute();
        $stmt->bind_result($id, $mail, $uid, $displayname);
        $stmt->fetch();
        $user = array();
        $user['id'] = $id;
        $user['mail'] = $mail;
        $user['uid'] = $uid;
        $user['displayname'] = $displayname;
        return $user;
    }


    //Method for user login
    function userLogin($email, $pass)
    {
        $password = md5($pass);
        $stmt = $this->con->prepare("SELECT id FROM users WHERE email = ? AND password = ?");
        $stmt->bind_param("ss", $email, $password);
        $stmt->execute();
        $stmt->store_result();
        return $stmt->num_rows > 0;
    }
   function registerUserwithmail($email, $uid, $passwrod)
    {
        if (!$this->isUserExist($uid)) {
            $password = md5($password);
            $stmt = $this->con->prepare("INSERT INTO Users (email, uid, password) VALUES (?, ?, ?)");
            $stmt->bind_param("sss", $email, $uid, $password);
            if ($stmt->execute())
                return USER_CREATED;
            return USER_CREATION_FAILED;
        }
        return USER_EXIST;

    }

    //Method to send a message to another user
    function sendMessage($from, $to, $title, $message)
    {
        $stmt = $this->con->prepare("INSERT INTO messages (from_users_id, to_users_id, title, message) VALUES (?, ?, ?, ?);");
        $stmt->bind_param("iiss", $from, $to, $title, $message);
        if ($stmt->execute())
            return true;
        return false;
    }
    function isShared($UserId, $whichtime, $date)
    {
        $year = substr($date, 0,4);
        $month = substr($date, 5,7);
        $day = substr($date, 8,10);

        if($whichtime=="Breakfast"){
        $stmt = $this->con->prepare("SELECT ParentSharedFoodsId FROM SharedBreakfasts WHERE UserID = ? AND year(SendDate) = ?
        AND month(SendDate) =? AND day(SendDate) =?");
        $stmt->bind_param("ssss", $UserId, $year, $month, $day);
        $stmt->execute();
        $stmt->store_result();
        return $stmt->num_rows > 0;
        }
        else if($whichtime=="Lunch")
        {
            $year = substr($date, 0,4);
            $month = substr($date, 5,7);
            $day = substr($date, 8,10);
            $stmt = $this->con->prepare("SELECT ParentSharedFoodsId FROM SharedLunches WHERE UserID = ? AND year(SendDate) = ?
            AND month(SendDate) =? AND day(SendDate) =?");
        $stmt->bind_param("ssss", $UserId, $year, $month, $day);
        $stmt->execute();
        $stmt->store_result();
        return $stmt->num_rows > 0;
        }
        else if($whichtime=="Dinner")
        {
            $year = substr($date, 0,4);
            $month = substr($date, 5,7);
            $day = substr($date, 8,10);
            $stmt = $this->con->prepare("SELECT ParentSharedFoodsId FROM SharedDinners WHERE UserID = ? AND year(SendDate) = ?
            AND month(SendDate) =? AND day(SendDate) =?");
         $stmt->bind_param("isss", $UserId, $year, $month, $day);
        $stmt->execute();
        $stmt->store_result();
        return $stmt->num_rows > 0;
        }
        else if($whichtime=="Drinks")
        {
            $year = substr($date, 0,4);
            $month = substr($date, 5,7);
            $day = substr($date, 8,10);
            $stmt = $this->con->prepare("SELECT ParentSharedFoodsId FROM SharedDrinks WHERE UserID = ? AND year(SendDate) = ?
            AND month(SendDate) =? AND day(SendDate) =?");
        $stmt->bind_param("ssss", $UserId, $year, $month, $day);
        $stmt->execute();
        $stmt->store_result();
        return $stmt->num_rows > 0;
        }
        else if($whichtime=="Snacks")
        {
            $year = substr($date, 0,4);
            $month = substr($date, 5,7);
            $day = substr($date, 8,10);
            $stmt = $this->con->prepare("SELECT ParentSharedFoodsId FROM SharedSnacks WHERE UserID = ? AND year(SendDate) = ?
            AND month(SendDate) =? AND day(SendDate) =?");
         $stmt->bind_param("ssss", $UserId, $year, $month, $day);
        $stmt->execute();
        $stmt->store_result();
        return $stmt->num_rows > 0;
        }
    }

    //Method to update profile of user
    function updateProfile($id, $name, $email, $pass, $gender)
    {
        $password = md5($pass);
        $stmt = $this->con->prepare("UPDATE users SET name = ?, email = ?, password = ?, gender = ? WHERE id = ?");
        $stmt->bind_param("ssssi", $name, $email, $password, $gender, $id);
        if ($stmt->execute())
            return true;
        return false;
    }

    //Method to get messages of a particular user
    function getMessages($userid)
    {
        $stmt = $this->con->prepare("SELECT messages.id, (SELECT users.name FROM users WHERE users.id = messages.from_users_id) as `from`, (SELECT users.name FROM users WHERE users.id = messages.to_users_id) as `to`, messages.title, messages.message, messages.sentat FROM messages WHERE messages.to_users_id = ? ORDER BY messages.sentat DESC;");
        $stmt->bind_param("i", $userid);
        $stmt->execute();
        $stmt->bind_result($id, $from, $to, $title, $message, $sent);

        $messages = array();

        while ($stmt->fetch()) {
            $temp = array();

            $temp['id'] = $id;
            $temp['from'] = $from;
            $temp['to'] = $to;
            $temp['title'] = $title;
            $temp['message'] = $message;
            $temp['sent'] = $sent;

            array_push($messages, $temp);
        }

        return $messages;
    }

    //Method to get user by email


    //Method to get all users
    function getAllUsers(){
        $stmt = $this->con->prepare("SELECT id, name, email, gender FROM users");
        $stmt->execute();
        $stmt->bind_result($id, $name, $email, $gender);
        $users = array();
        while($stmt->fetch()){
            $temp = array();
            $temp['id'] = $id;
            $temp['name'] = $name;
            $temp['email'] = $email;
            $temp['gender'] = $gender;
            array_push($users, $temp);
        }
        return $users;
    }
    function addSelectedFood($foodId, $Userid, $Date, $Calories, $Protein, $Fat, $Carbohydrates, $whichtime, $sharedfoodsId)
    {
            $stmt = $this->con->prepare("INSERT INTO ".$whichtime." (foodId, UserId, SendDate, Calories, Protein, Fat, Carbohydrates, SharedFoodsId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            $stmt->bind_param("sisddddi", $foodId, $Userid, $Date, $Calories, $Protein, $Fat, $Carbohydrates, $sharedfoodsId);
            if ($stmt->execute())
                return USER_CREATED;
            return USER_CREATION_FAILED;
    }
    function addSharedFoodList($UserId, $Date, $SharedFoodListDatabase, $Calories, $Protein, $Fat, $Carbohydrates)
    {
        $stmt = $this->con->prepare("INSERT INTO ".$SharedFoodListDatabase." (UserId, SendDate, Calories, Protein, Fat, Carbohydrates) VALUES (?, ?, ?, ?, ?, ?)");
            $stmt->bind_param("isdddd", $UserId, $Date, $Calories, $Protein, $Fat, $Carbohydrates);

            if ($stmt->execute())
                return USER_CREATED;
            return USER_CREATION_FAILED;

    }
    function updateFoods($UserId, $Date, $whichtime, $Calories, $Protein, $Fat, $Carbohydrates )
    {
        $year = substr($Date, 0,4);
        $month = substr($Date, 5,7);
        $day = substr($Date, 8,10);
        if($whichtime=="Breakfast"){
        $stmt = $this->con->prepare("UPDATE Breakfast SET Breakfast.SharedFoodsId=(SELECT SharedBreakfasts.ParentSharedFoodsId
        FROM SharedBreakfasts WHERE year(SharedBreakfasts.SendDate)=? AND month(SharedBreakfasts.SendDate)=? AND day(SharedBreakfasts.SendDate)=?
        AND SharedBreakfasts.UserId=Breakfast.UserId) WHERE year(Breakfast.SendDate)=? AND month(Breakfast.SendDate)=? AND day(Breakfast.SendDate)=?
        AND  Breakfast.UserId =?");
        $stmt->bind_param("sssssss",  $year, $month, $day, $year, $month, $day, $UserId );
        if ($stmt->execute())
            return true;
        return false;
        }
        else if($whichtime=="Lunch")
        {
            $stmt = $this->con->prepare("UPDATE Lunch SET Lunch.SharedFoodsId=(SELECT SharedLunches.ParentSharedFoodsId
            FROM SharedLunches WHERE year(SharedLunches.SendDate)=? AND month(SharedLunches.SendDate)=? AND day(SharedLunches.SendDate)=?
             AND SharedLunches.UserId=Lunch.UserId) WHERE year(Lunch.SendDate)=? AND month(Lunch.SendDate)=? AND day(Lunch.SendDate)=?
              AND  Lunch.UserId =?");

            $stmt->bind_param("sssssss",  $year, $month, $day, $year, $month, $day, $UserId );
            if ($stmt->execute())
                return true;
            return false;
        }
        else if($whichtime=="Dinner")
        {
           $stmt = $this->con->prepare("UPDATE SharedDinners SET SharedDinners.Calories =?, SharedDinners.Protein =? WHERE year(SharedDinners.SendDate)=? AND
           month(SharedDinners.SendDate)=? AND day(SharedDinners.SendDate)=? AND UserId= ?");
           $stmt->bind_param("ddsssi",  $Calories, $Protein, $year, $month, $day, $UserId );
           $stmt->execute();
        $stmt->close();
            $stmt = $this->con->prepare("UPDATE Dinner SET Dinner.SharedFoodsId=(SELECT SharedDinners.ParentSharedFoodsId
            FROM SharedDinners WHERE year(SharedDinners.SendDate)=? AND month(SharedDinners.SendDate)=? AND day(SharedDinners.SendDate)=?
             AND SharedDinners.UserId=Dinner.UserId) WHERE year(Dinner.SendDate)=? AND month(Dinner.SendDate)=? AND day(Dinner.SendDate)=? AND  Dinner.UserId =?");
            $stmt->bind_param("ssssssi",  $year, $month, $day, $year, $month, $day, $UserId );
            if ($stmt->execute())
                return true;
            return false;
        }
        else if($whichtime=="Drinks")
        {
            $stmt = $this->con->prepare("UPDATE Drinks SET Drinks.SharedFoodsId=(SELECT SharedDrinks.ParentSharedFoodsId
            FROM SharedDrinks WHERE year(SharedDrinks.SendDate)=? AND month(SharedDrinks.SendDate)=? AND day(SharedDrinks.SendDate)=?
            AND SharedDrinks.UserId=Drinks.UserId) WHERE  year(Drinks.SendDate)=? AND month(Drinks.SendDate)=? AND day(Drinks.SendDate)=? AND  Drinks.UserId =?");
            $stmt->bind_param("sssssss",  $year, $month, $day, $year, $month, $day, $UserId );
            if ($stmt->execute())
                return true;
            return false;
        }
        else if($whichtime=="Snacks")
        {
            $stmt = $this->con->prepare("UPDATE Snacks SET Snacks.SharedFoodsId=(SELECT SharedSnacks.ParentSharedFoodsId
            FROM SharedSnacks WHERE year(SharedSnacks.SendDate)=? AND month(SharedSnacks.SendDate)=? AND day(SharedSnacks.SendDate)=? AND SharedSnacks.UserId=Snacks.UserId)
             WHERE  year(Snacks.SendDate)=? AND month(Snacks.SendDate)=? AND day(Snacks.SendDate)=? AND Snacks.UserId =?");
            $stmt->bind_param("sssssss",  $year, $month, $day, $year, $month, $day, $UserId );
            if ($stmt->execute())
                return true;
            return false;
        }
        return false;

    }
    function getAllSelectedFoods($UserId, $whichtime, $year, $month, $day){
        echo "hello";
        $stmt = $this->con->prepare("SELECT foodId, UserId, SendDate, Calories, Protein, Fat, Carbohydrates FROM ".$whichtime." WHERE UserId =?
        AND day(SendDate) =? AND year(SendDate)=? AND month(SendDate)=?");
        $stmt->bind_param("isss", $UserId, $day, $year, $month);
        $stmt->execute();
        $stmt->bind_result($foodId, $Userid, $Date, $Calories, $Protein, $Fat, $Carbohydrates);
        $users = array();
        while($stmt->fetch()){
            $temp = array();
            $temp['foodId'] = $foodId;
            $temp['UserId'] = $Userid;
            $temp['SendDate'] = $Date;
            $temp['Calories'] = $Calories;
            $temp['Protein'] = $Protein;
            $temp['Fat'] = $Fat;
            $temp['Carbohydrates'] = $Carbohydrates;
            array_push($users, $temp);
        }
        return $users;
    }
    function getAllSelectedFoodsPrieview($UserId, $whichtime){
        echo "hello";
        $stmt = $this->con->prepare("SELECT foodId, UserId, SendDate, Calories, Protein, Fat, Carbohydrates FROM ".$whichtime." WHERE SharedFoodsId =?");
        $stmt->bind_param("i", $UserId);
        $stmt->execute();
        $stmt->bind_result($foodId, $Userid, $Date, $Calories, $Protein, $Fat, $Carbohydrates);
        $users = array();
        while($stmt->fetch()){
            $temp = array();
            $temp['foodId'] = $foodId;
            $temp['UserId'] = $Userid;
            $temp['SendDate'] = $Date;
            $temp['Calories'] = $Calories;
            $temp['Protein'] = $Protein;
            $temp['Fat'] = $Fat;
            $temp['Carbohydrates'] = $Carbohydrates;
            array_push($users, $temp);
        }
        return $users;
    }
    function getAllSharedDiets($UserId, $SharedFoodListDatabase){
        $stmt = $this->con->prepare("SELECT ParentSharedFoodsId, UserId, SendDate, Calories, Protein, Fat, Carbohydrates FROM ".$SharedFoodListDatabase." WHERE UserId <>?");
        $stmt->bind_param("i", $UserId);
        $stmt->execute();
        $stmt->bind_result($ParentSharedFoodsId, $Userid, $Date, $Calories, $Protein, $Fat, $Carbohydrates);
        $users = array();
        while($stmt->fetch()){
            $temp = array();
            $temp['ParentSharedFoodsId'] = $ParentSharedFoodsId;
            $temp['UserId'] = $Userid;
            $temp['Date'] = $Date;
            $temp['Calories'] = $Calories;
            $temp['Protein'] = $Protein;
            $temp['Fat'] = $Fat;
            $temp['Carbohydrates'] = $Carbohydrates;
            array_push($users, $temp);
        }
        return $users;
    }
   function getAllFilteredSharedDiets($UserId, $SharedFoodListDatabase, $proteinbegin, $proteinend, $caloriesbegin, $caloriesend, $carbohydratesbegin,
    $carbohydratesend, $fatsbegin, $fatsend){
        $stmt = $this->con->prepare("SELECT ParentSharedFoodsId, UserId, SendDate, Calories, Protein, Fat, Carbohydrates FROM ".$SharedFoodListDatabase."
         WHERE SharedDinners.UserId <> ? AND SharedDinners.Calories BETWEEN ? AND ? AND SharedDinners.Carbohydrates BETWEEN ? AND ? AND SharedDinners.Protein BETWEEN ? AND ? AND SharedDinners.Fat BETWEEN ? AND ?");
        $stmt->bind_param("iiiiiiiii", $UserId, $caloriesbegin, $caloriesend, $carbohydratesbegin, $carbohydratesend, $proteinbegin, $proteinend, $fatsbegin, $fatsend );
        $stmt->execute();
        $stmt->bind_result($ParentSharedFoodsId, $Userid, $Date, $Calories, $Protein, $Fat, $Carbohydrates);
        $users = array();
        while($stmt->fetch()){
            $temp = array();
            $temp['ParentSharedFoodsId'] = $ParentSharedFoodsId;
            $temp['UserId'] = $Userid;
            $temp['Date'] = $Date;
            $temp['Calories'] = $Calories;
            $temp['Protein'] = $Protein;
            $temp['Fat'] = $Fat;
            $temp['Carbohydrates'] = $Carbohydrates;
            array_push($users, $temp);
        }
        return $users;
    }
    function qur( $value, $name)
    {

        $stmt = $this->con->prepare("SELECT id, name, email, gender FROM users WHERE " .$name. " = ?");
        $stmt->bind_param("s", $value);
        $stmt->execute();
        $stmt->bind_result($id, $name, $email, $gender);
        $users = array();
        while($stmt->fetch()){
            $temp = array();
            $temp['id'] = $id;
            $temp['name'] = $name;
            $temp['email'] = $email;
            $temp['gender'] = $gender;
            array_push($users, $temp);
        }
        //error_reporting();
      return $users;
    }

    //Method to check if email already exist
    function isUserExist($uid)
    {
        $stmt = $this->con->prepare("SELECT id FROM Users WHERE uid = ?");
        $stmt->bind_param("s", $uid);
        $stmt->execute();
        $stmt->store_result();
        return $stmt->num_rows > 0;
    }
}