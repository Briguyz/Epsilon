//loads the MongoDB package
var mongojs = require("mongojs");

var url = 'mongodb://ukko.d.umn.edu:32893/umdAliveDatabase';

//array of collections we will use
var collections = ['clubs', 'users', 'posts'];

//clubs: name, pictures, description, events, members
//users: name, picture, major

var assert = require('assert');

var mongoDBRef = mongojs(url, collections);

//the following are anonymous functions that wil be used in index.js

/**
* Inserts json info of a new club into the clubs collection.
* If clubss collection doesn't exist, it is created.
* @param clubData - JSON data of the club
*/
module.exports.insertClub = function(clubData) {

    //console.log(clubData);

	var temp = clubData.clubName;

    var title = temp.split('/').join('_');

    mongoDBRef.collection('clubs').save({club: title, clubData}, function(err, result){
        if(err || !result) console.log("Club failed to save in database.");
        else console.log("Club inserted into clubs collection in MongoDB.");
    });
};

/**
* Inserts json info of a new post into the posts collection.
* If posts collection doesn't exist, it is created.
* @param postData - JSON data of the post
*/
module.exports.insertPost = function(postData) {
	//HERE! if the server is broken, it's totally because I changed postData.clubName to postData.title
    mongoDBRef.collection('posts').save({post: postData.title, postData}, function(err, result){
        if(err || !result) console.log("Post failed to save in database.");
        else console.log("Post inserted into posts collection in MongoDB.");
    });
};

/**
* Inserts json info of a new user into the posts collection.
* If posts collection doesn't exist, it is created.
* @param userData - JSON data of the post
*/
module.exports.insertUser = function(userData) {
var temp = userData.email;


    //console.log(userData);
    mongoDBRef.collection('users').save({user: userData.email, userData}, function(err, result){
        if(err || !result) console.log("User failed to save in database.");
        else console.log("User inserted into users collection in MongoDB.");
    });
};

/**
* Finds a specific club
* @param clubName - string of club's name
* @param callback - the function that the result is sent to
*/
module.exports.findClub = function(clubName, callback) {
    mongoDBRef.collection('clubs').find({club: clubName}).toArray(function(err, docs) {
	    if(!err){
	        console.log("Found the following records");
	        console.log(docs);
	        callback(docs);
	    }
	    else console.log("Club not found.")
    });
};
/**
*Identifies user being used
*
*
*
*/
module.exports.findUser = function(email, callback){
    console.log("--------------------\n");
    mongoDBRef.collection('users').find({user: email}).toArray(function(err,docs){
    if(!err){
    console.log("Found the following records");
    console.log(docs);
    callback(docs);
    }
    });
    };

module.exports.getCollection = function(collectionName, callback) {
    var cursor = mongoDBRef.collection(collectionName).find(function(err, docs) {
            if(err || !docs) {
    	    console.log("Error in retrieving collection\n");
    	}
            else {
    	    callback(docs);
    	}
        });
};

module.exports.delete = function(clubName){
	mongoDBRef.collection('clubs').remove({'club' : clubName}, function(err, obj){
		if (err) throw err;
		else console.log(clubName + " deleted");
	});

};

module.exports.deleteUser = function(userName){
	mongoDBRef.collection('users').remove({'user' : userName}, function (err, obj){
		if (err) throw err;
		else console.log(userName + " deleted");
	});
};

module.exports.deletePost = function(title){
	mongoDBRef.collection('posts').remove({'post' : title}, function (err, obj){
		if (err) throw err;
		else console.log(title + " deleted");
	});
};
