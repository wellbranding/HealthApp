<?php

use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';
require_once '../includes/DbOperation.php';

//Creating a new app with the config to show errors
$app = new \Slim\App([
    'settings' => [
        'displayErrorDetails' => true
    ]
]);


//registering a new user
$app->post('/register', function (Request $request, Response $response) {
        $requestData = $request->getParsedBody();
        $name = $requestData['name'];
        $email = $requestData['email'];
        $uid = $requestData['uid'];

        $db = new DbOperation();
        $responseData = array();

        $result = $db->registerUser($email, $uid, $name);

        if ($result == USER_CREATED) {
            $responseData['error'] = false;
            $responseData['message'] = 'Registered successfully';
            $responseData['user'] = $db->getUserByEmail($email);
        } elseif ($result == USER_CREATION_FAILED) {
            $responseData['error'] = true;
            $responseData['message'] = 'Some error occurred';
        } elseif ($result == USER_EXIST) {
            $responseData['error'] = true;
            $responseData['message'] = 'This email already exist, please login';
        }

        $response->getBody()->write(json_encode($responseData));
});
$app->post('/loginwithmail', function (Request $request, Response $response) {
  //  $requestData = $request->getParsedBody();
    $passwrod = $requestData['password'];
    $email = $requestData['email'];
    $uid = $requestData['uid'];

    $db = new DbOperation();
    $responseData = array();

    $result = $db->registerUserwithmail($email, $uid, $passwrod);

    if ($result == USER_CREATED) {
        $responseData['error'] = false;
        $responseData['message'] = 'Registered successfully';
  //      $responseData['user'] = $db->getUserByEmail($email);
    } elseif ($result == USER_CREATION_FAILED) {
        $responseData['error'] = true;
        $responseData['message'] = 'Some error occurred';
    } elseif ($result == USER_EXIST) {
        $responseData['error'] = true;
        $responseData['message'] = 'This email already exist, please login';
    }

    $response->getBody()->write(json_encode($responseData));
});


//user login route
$app->post('/login', function (Request $request, Response $response) {
    if (isTheseParametersAvailable(array('email', 'password'))) {
        $requestData = $request->getParsedBody();
        $email = $requestData['email'];
        $password = $requestData['password'];

        $db = new DbOperation();

        $responseData = array();

        if ($db->userLogin($email, $password)) {
            $responseData['error'] = false;
            $responseData['user'] = $db->getUserByEmail($email);
        } else {
            $responseData['error'] = true;
            $responseData['message'] = 'Invalid email or password';
        }

        $response->getBody()->write(json_encode($responseData));
    }
});

//getting all users
$app->get('/users', function (Request $request, Response $response) {
    $db = new DbOperation();
    $users = $db->getAllUsers();
    $response->getBody()->write(json_encode(array("users" => $users)));
});

$app->get('/getAllSharedDiets', function (Request $request, Response $response) {
    $db = new DbOperation();
    $UserId = $request->getQueryParam('UserId');
    $SharedFoodListDatabase = $request->getQueryParam('SharedFoodListDatabase');

    $whichtime = $request->getQueryParam('whichtime');
    $users = $db->getAllSharedDiets($UserId, $SharedFoodListDatabase);
    $response->getBody()->write(json_encode(array("selectedFoodretrofits" => $users)));
});
//getting messages for a user
$app->get('/messages/{id}', function (Request $request, Response $response) {
    $userid = $request->getAttribute('id');
    $db = new DbOperation();
    $messages = $db->getMessages($userid);
    $response->getBody()->write(json_encode(array("messages" => $messages)));
});

//updating a user
$app->post('/update/{id}', function (Request $request, Response $response) {
    if (isTheseParametersAvailable(array('name', 'email', 'password', 'gender'))) {
        $id = $request->getAttribute('id');

        $requestData = $request->getParsedBody();

        $name = $requestData['name'];
        $email = $requestData['email'];
        $password = $requestData['password'];
        $gender = $requestData['gender'];


        $db = new DbOperation();

        $responseData = array();

        if ($db->updateProfile($id, $name, $email, $password, $gender)) {
            $responseData['error'] = false;
            $responseData['message'] = 'Updated successfully';
            $responseData['user'] = $db->getUserByEmail($email);
        } else {
            $responseData['error'] = true;
            $responseData['message'] = 'Not updated';
        }

        $response->getBody()->write(json_encode($responseData));
    }
});


//sending message to user
$app->post('/sendmessage', function (Request $request, Response $response) {
    if (isTheseParametersAvailable(array('from', 'to', 'title', 'message'))) {
        $requestData = $request->getParsedBody();
        $from = $requestData['from'];
        $to = $requestData['to'];
        $title = $requestData['title'];
        $message = $requestData['message'];

        $db = new DbOperation();

        $responseData = array();

        if ($db->sendMessage($from, $to, $title, $message)) {
            $responseData['error'] = false;
            $responseData['message'] = 'Message sent successfully';
        } else {
            $responseData['error'] = true;
            $responseData['message'] = 'Could not send message';
        }

        $response->getBody()->write(json_encode($responseData));
    }
});
$app->get('/getSelectedFoods', function (Request $request, Response $response) {
    $UserId = $request->getQueryParam('UserId');
    $whichtime = $request->getQueryParam('whichtime');
    $year = $request->getQueryParam('year');
    $month = $request->getQueryParam('month');
    $day = $request->getQueryParam('day');
    $db = new DbOperation();
    $users = $db->getAllSelectedFoods($UserId, $whichtime, $year, $month, $day);
    $response->getBody()->write(json_encode(array("users" => $users)));
});
$app->get('/getSelectedFoodsPrieview', function (Request $request, Response $response) {
    $getParentSharedFoodsId = $request->getQueryParam('getParentSharedFoodsId');
    $foodselection = $request->getQueryParam('foodselection');
    $db = new DbOperation();
    $users = $db->getAllSelectedFoodsPrieview($getParentSharedFoodsId, $foodselection);
    $response->getBody()->write(json_encode(array("users" => $users)));
});
$app->get('/IsShared', function (Request $request, Response $response) {
    $UserId = $request->getQueryParam('UserId');
    $date = $request->getQueryParam('date');
    $whichtime = $request->getQueryParam('whichtime');
    $db = new DbOperation();
    $responseData = array();
    $result = $db->isShared($UserId, $whichtime, $date);
    if (!$result) {
        $responseData['error'] = false;
        echo "maziau";
       // echo $result;
        $responseData['message'] = 'notfound';
       // $responseData['user'] = $db->getUserByEmail($email);
    }
     else {
         echo "daugiau";
        $responseData['error'] = true;
        $responseData['message'] = 'Some error occurred';
    } 
    $response->getBody()->write(json_encode($responseData));
});

$app->post('/addSelectedFood', function (Request $request, Response $response) {
    $requestData = $request->getParsedBody();
    $foodId = $requestData['foodId'];
    $UserId = $requestData['UserId'];
    $Date = $requestData['Date'];
    $Calories = $requestData['Calories'];
    $Protein = $requestData['Protein'];
    $Fat = $requestData['Fat'];
    $Carbohydrates = $requestData['Carbohydrates'];
    $whichtimee = $requestData['whichtime'];
    $sharedfoodsId = $requestData['sharedfoodId'];

    $db = new DbOperation();
    $responseData = array();

    $result = $db->addSelectedFood($foodId, $UserId, $Date, $Calories, $Protein, $Fat, $Carbohydrates, $whichtimee, $sharedfoodsId);

    if ($result == USER_CREATED) {
        $responseData['error'] = false;
        $responseData['message'] = 'Registered successfully';
       // $responseData['user'] = $db->getUserByEmail($email);
    } elseif ($result == USER_CREATION_FAILED) {
        $responseData['error'] = true;
        $responseData['message'] = 'Some error occurred';
    } 

    $response->getBody()->write(json_encode($responseData));
});
$app->post('/addSharedList', function (Request $request, Response $response) {
    $requestData = $request->getParsedBody();
    $UserId = $requestData['UserId'];
    $Date = $requestData['Date'];
    $whichtime = $requestData['whichtime'];
    $SharedFoodListDatabase = $requestData['SharedFoodListDatabase'];

    $db = new DbOperation();
    $responseData = array();

    $canyoushare = $db->isShared($UserId, $whichtime, $Date);
    if(!$canyoushare){
    $result = $db->addSharedFoodList($UserId, $Date, $SharedFoodListDatabase);
    if ($result == USER_CREATED) {
        $updateresult =  $db->updateFoods($UserId, $Date, $whichtime);
        echo $updateresult;
        $responseData['error'] = false;
        $responseData['message'] = 'Registered successfully';
       // $responseData['user'] = $db->getUserByEmail($email);
    } elseif ($result == USER_CREATION_FAILED) {
        $responseData['error'] = true;
        $responseData['message'] = 'Some error occurred';
    } 
}
else
{
    $db->updateFoods($UserId, $Date, $whichtime);
    $responseData['error'] = false;
    $responseData['message'] = 'cantinsertmore';
}

    $response->getBody()->write(json_encode($responseData));
});

//function to check parameters
function isTheseParametersAvailable($required_fields)
{
    $error = false;
    $error_fields = "";
    $request_params = $_REQUEST;

    foreach ($required_fields as $field) {
        if (!isset($request_params[$field]) || strlen(trim($request_params[$field])) <= 0) {
            $error = true;
            $error_fields .= $field . ', ';
        }
    }

    if ($error) {
        $response = array();
        $response["error"] = true;
        $response["message"] = 'Required field(s) ' . substr($error_fields, 0, -2) . ' is missing or empty';
        echo json_encode($response);
        return false;
    }
    return true;
}


$app->run();