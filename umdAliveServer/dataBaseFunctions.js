//loads the MongoDB package
var mongojs = require("mongojs");

var url = 'mongodb://akka.d.umn.edu:12750/umdAliveDatabase';

//array of collections we will use
var collections = ['clubs', 'users', 'events'];

var assert = require('assert');

var DBRef = mongojs(url, collections);

//the following are anonymous functions that wil be used in index.js

//Club Calls
module.exports.createClub = function(clubData, callback) {
  DBRef.collection('clubs').save(clubData, function(err, result){
    if(err){
      console.log(err)
    } else {
      DBRef.collection('users').findOne({"userID": clubData.members.admin},function(err, doc){
        if (err){
          console.log(err);
        } else {
          doc.clubs.push(clubData._id + "");
          DBRef.collection('users').update({"userID": clubData.members.admin}, doc, function(err, doc){
            if (err){
              console.log(err);
            } else {
              callback(result);
            }
          });
        }
      });
    }
  });
};

module.exports.editClub = function(clubID, clubData){
  DBRef.collection('clubs').update({"_id":mongojs.ObjectId(clubID)},clubData,function(err, result){
    if (err){
      console.log(err);
    } else {
        //Completed
        console.log(result);
    }
  });
};

module.exports.getClub = function(clubID, callback) {
  DBRef.collection('clubs').findOne({"_id": mongojs.ObjectId(clubID)}, function (err, doc){
    if (err){
      console.log(err);
    } else {
      DBRef.collection('users').find({"clubs" : clubID}).toArray(function (err, userDocs){
        doc.members.regular = [];
        for (i = 0; i < userDocs.length; i++){
          if (userDocs[i].userID == doc.members.admin){
            doc.members.admin = userDocs[i];
          } else {
            doc.members.regular.push(userDocs[i]);
          }
        }
        DBRef.collection('events').find({"club": clubID}).toArray(function (err, eventDocs){
          doc.events = eventDocs;
          callback(doc);
        });
      });
    }
  });
};

module.exports.getAllClubs = function(callback) {
	DBRef.collection('clubs').find({}).toArray(function(err, docs) {
		if (err){
      console.log(err);
		} else {
      var allClubsObject = {
        "clubs" : docs
      };
			callback(allClubsObject);
		}
	});
};

module.exports.joinClub = function(userID, clubID){
  DBRef.collection('users').findOne({"userID": userID}, function (err, doc){
    doc.clubs.push(clubID);
    DBRef.collection('users').update({"userID": userID}, doc, function (err, result){
      if (err){
        console.log(err);
      } else {
          //Completed
          console.log(result);
      }
    });
  });
  DBRef.collection('clubs').findOne({"_id": mongojs.ObjectId(clubID)}, function (err, doc){
    doc.members.regular.push(userID);
    DBRef.collection('clubs').update({"_id": mongojs.ObjectId(clubID)}, doc, function (err, result){
      if (err){
        console.log(err);
      } else {
          //Completed
          console.log(result);
      }
    });
  });
};

module.exports.leaveClub = function(userID, clubID){
  DBRef.collection('users').findOne({"userID": userID}, function (err, doc){
    var index = doc.clubs.indexOf(clubID);
    if (index > -1){
      doc.clubs.splice(index, 1);
      console.log(doc);
    }
    DBRef.collection('users').update({"userID": userID}, doc, function (err, result){
      if (err){
        console.log(err);
      } else {
          //Completed
          console.log(result);
      }
    });
  });
  DBRef.collection('clubs').findOne({"_id": mongojs.ObjectId(clubID)}, function (err, doc){
    var index = doc.members.regular.indexOf(userID);
    if (index > -1){
      doc.members.regular.splice(index, 1);
      console.log(doc);
    }
    DBRef.collection('clubs').update({"_id": mongojs.ObjectId(clubID)}, doc, function (err, result){
      if (err){
        console.log(err);
      } else {
          //Completed
          console.log(result);
      }
    });
  });
};

module.exports.deleteClub = function (clubID){
    DBRef.collection('clubs').findOne({"_id": mongojs.ObjectId(clubID)}, function (err, doc){
        if (err){
            console.log(err);
        }
        else {
            //Might cause error of $pull referencing non-array but doesn't affect performance of deletetion
            DBRef.collection('users').update({},{$pull:{clubs:{$in:[clubID]}}},{multi: true}, function (err,result){
                if (err) {
                    console.log(err);
                }
                else {
                    //Completed
                    console.log(result);
                }
            });
        }
        DBRef.collection('events').remove({"club": clubID}, false, function (err, result){
            if (err){
                console.log(err);
            } else {
                //Completed
                console.log(result);
            }
        });
        DBRef.collection('clubs').remove({"_id":mongojs.ObjectId(clubID)}, false, function (err, result){
            if (err){
                console.log(err);
            } else {
                //Completed
                console.log(result);
            }
        });
    });
};

//User Calls
module.exports.createUser = function(userData){
  DBRef.collection('users').save(userData, function(err, result){
    if (err){
      console.log(err)
    } else {
        //Completed
        console.log(result);
    }
  });
};

module.exports.editUser = function(userID, userData){
  DBRef.collection('users').update({"userID": userID},userData,function(err, result){
    if (err){
      console.log(err);
    } else {
        //Completed
        console.log(result);
    }
  });
};

module.exports.getUser = function(userID, callback) {
  DBRef.collection('users').findOne({"userID": userID}, function (err, doc){
    if (err){
      console.log(err);
    } else {
      //Completed
      if (doc != null){
        DBRef.collection('clubs').find({"members.admin": userID}).toArray(function (err, adminDocs){
          DBRef.collection('clubs').find({"members.regular": userID}).toArray(function (err, regularDocs){
            doc.clubs = adminDocs.concat(regularDocs);
            callback(doc);
          });
        });
      } else {
        callback(doc);
      }
    }
  });
};

//Event Calls
module.exports.createEvent = function(eventData, callback){
  DBRef.collection('events').save(eventData, function(err, result){
	console.log(result);
    if (err){
      console.log(err);
    } else {
      DBRef.collection('clubs').findOne({"_id": mongojs.ObjectId(result.club)}, function (err, doc){
        if (err){
          console.log(err);
        } else {
            //Completed
            console.log(doc);
        }
        doc.events.push(result._id + "");
        DBRef.collection('clubs').update({"_id": mongojs.ObjectId(result.club)}, doc, function (err, result){
          if (err){
            console.log(err);
          } else {
            //Completed
          }
        });
      });
      //Completed
	  callback({"eventID" : result._id});
    }
  });
};

module.exports.editEvent = function(eventID, eventData){
  DBRef.collection('events').update({"_id":mongojs.ObjectId(eventID)},eventData,function(err, result){
    if (err){
      console.log(err);
    } else {
        //Completed
        console.log(result);
    }
  });
};

module.exports.getEvent = function(eventID, callback) {
	DBRef.collection('events').findOne({"_id": mongojs.ObjectId(eventID)}, function (err, doc){
    if (err){
      console.log(err);
    } else {
      //Completed
      DBRef.collection('clubs').findOne({"_id": mongojs.ObjectId(doc.club)}, function (err, clubDoc){
        doc.club = clubDoc;
        callback(doc);
      });
    }
  });
};

module.exports.getAllEvents = function(callback){
  DBRef.collection('events').find().toArray(function(err, docs){
    if (err){
      console.log(err);
    } else {
        var allEventsObject = {
          "events" : docs
        }
      callback(allEventsObject);
    }
  });
};

module.exports.deleteEvent = function (eventID){
  DBRef.collection('events').findOne({"_id": mongojs.ObjectId(eventID)}, function (err, doc){
    if (err){
      console.log(err);
    } else {
      DBRef.collection('clubs').findOne({"_id": mongojs.ObjectId(doc.club)}, function (err, clubDoc){
        var index = clubDoc.events(eventID);
        if (index > -1){
          clubDoc.events.splice(index, 1);
        }
        DBRef.collection('clubs').update({"_id": mongojs.ObjectId(doc.club)}, clubDoc, function (err, result){
          if (err){
            console.log(err);
          } else {
              //Completed
              console.log(result);
          }
        });
      });
      DBRef.collection('events').remove({"_id": mongojs.ObjectId(eventID)}, false, function (err, result){
        if (err){
          console.log(err);
        } else {
            //Completed
            console.log(result);
        }
      });
    }
  });
};
//Comment Calls
module.exports.createComment = function(commentData, callback){
    DBRef.collection('comments').save(commentData, function(err,result){
        console.log(result);
        if (err){
            console.log(err);
        } else {
            DBRef.collection('events').findOne({"_id": mongojs.ObjectId(result.events)},function (err, doc){
                if (err){
                    console.log(err);
                } else {
                    //took from create event as a sub in
                }
                doc.events.push(result._id + "");
                DBRef.collection('events').update({"_id":mongojs.ObjectId(result.events)},doc,function(err, result){
                    if
                        (err){
                            console.log(err);
                        }
                    else
                    {
                        //took from create event as a sub in
                    }
                });
            });
            //Completed
            callback({"commentID" : result._id});
        }
    });
};
