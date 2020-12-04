//loads the MongoDB package
var mongojs = require("mongojs");

var url = 'mongodb://akka.d.umn.edu:12750/umdAliveDatabase';

//array of collections we will use
var collections = ['clubs', 'users', 'events', 'commentsView', 'comments', 'reports'];

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

            function getEventsDoc() {
                return new Promise((resolve, reject) => {
                    DBRef.collection('events').find({"club": clubID}).toArray(function (err, doc) {
                        if (err) {
                            console.log(err);
                            return reject('Error');
                        }
                        else {
                            //Completed
                            console.log(doc);
                            return resolve(doc);
                        }
                    });
                });
            }
            function removeComments(eventsDoc){
                var i;
                for (i=0; i < eventsDoc.length; i++) {
                    DBRef.collection('comments').remove({"commentsView":eventsDoc[i].commentsView}, false, function(err,result){
                        if (err) {
                            console.log(err);
                        }
                        else {
                            //Completed
                            console.log(result);
                        }
                        });
                    DBRef.collection('commentsView').remove({"_id": mongojs.ObjectId(eventsDoc[i].commentsView)}, false, function (err,result){
                        if (err) {
                            console.log(err);
                        }
                        else {
                            //Completed
                            console.log(result);
                        }
                    });
                }
            }
            function removeEvents() {
                DBRef.collection('events').remove({"club": clubID}, false, function (err, result){
                    if (err){
                        console.log(err);
                } else {
                    //Completed
                    console.log(result);
                }
                });
            }

            function removeClub() {
                DBRef.collection('clubs').remove({"_id":mongojs.ObjectId(clubID)}, false, function (err, result){
                    if (err){
                        console.log(err);
                    } else {
                        //Completed
                        console.log(result);
                    }
                });
            }

            getEventsDoc()
                .then((doc) => {
                    removeComments(doc);
                })
                .then(() => {
                    removeEvents();
                })
                .then(() => {
                    removeClub();
                })
                .catch(err => {
                    console.log(err);
                })
        }
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
module.exports.createEvent = function(eventData, commentsViewData, callback){
    DBRef.collection('events').save(eventData, function(err, result){
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
                        console.log(result);
                    }
                });
            });
            DBRef.collection('commentsView').save(commentsViewData, function(errComment,resultComment){
                if (err) {
                console.log(errComment);
                }
                else {
                DBRef.collection('commentsView').findOne({"_id": mongojs.ObjectId(resultComment._id)}, function (errCommentView, docCommentView) {
                    resultCommentID = resultComment._id + "";
                    if (errCommentView) {
                        console.log(errCommentView);
                    }
                    else {
                        console.log(docCommentView);
                    }
                    DBRef.collection('commentsView').update({"_id": resultComment._id}, {$set: {"eventID": result._id + ""}}, function (err3, result3) {
                        if (err3) {
                            console.log(err3);
                        }
                        else {
                            //Completed
                            console.log(result3);
                        }
                    });
                });
                    DBRef.collection('events').findOne({"_id": mongojs.ObjectId(result._id)}, function (errComment, docComment) {
                        if (err) {
                            console.log(errComment);
                        }
                        else {
                            console.log(docComment);
                        }
                        DBRef.collection('events').update({"_id": result._id}, {$set: {"commentsView": resultComment._id + ""}}, function (err2, result2) {
                            if (err2) {
                                console.log(err2);
                            }
                            else {
                                //Completed
                                console.log(result2);
                            }
                        });
                    });
                }
                //Completed
                callback({"eventID" : result._id,
                          "commentsViewID" : resultComment._id
                         });
            });
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
        }
        else {
            DBRef.collection('clubs').update({"_id": mongojs.ObjectId(doc.club)}, {$pull:{events:{$in:[eventID]}}}, function (err, result){
                if (err){
                    console.log(err);
                } else {
                    //Completed
                    console.log(result);
                }
            });
        }
        function removeComments() {
            return new Promise((resolve, reject) => {
                DBRef.collection('comments').remove({"commentsView": doc.commentsView}, false, function(err, result) {
                    if (err) {
                        console.log(err);
                        return reject(err);
                    }
                    else {
                        //Completed
                        console.log(result);
                    }
                });
                DBRef.collection('commentsView').remove({"eventID" : eventID}, false, function(err, result) {
                    if (err) {
                        console.log(err);
                        return reject(err);
                    }
                    else {
                        //Completed
                        console.log(result);
                    }
                });
                return resolve();
            });
        }
        function removeEvents() {
            DBRef.collection('events').remove({"_id": mongojs.ObjectId(eventID)}, false, function (err, result){
                if (err){
                    console.log(err);
                }
                else {
                    //Completed
                    console.log(result);
                }
            });
        }
        removeComments()
            .then(() => {
                removeEvents();
            })
            .catch(err => {
                console.log(err);
            })
    });
};

//Comment Calls
module.exports.createComment = function(commentData, callback){
    DBRef.collection('comments').save(commentData, function(err,result){
        console.log(result);
        if (err){
            console.log(err);
        } else {
            DBRef.collection('commentsView').findOne({"_id": mongojs.ObjectId(result.commentsView)},function (err, doc){
                if (err){
                    console.log(err);
                } else {
                    console.log(doc);
                }
                doc.comments.push(result._id + "");
                DBRef.collection('commentsView').update({"_id":mongojs.ObjectId(result.commentsView)}, doc,function(err, result){
                    if
                        (err){
                            console.log(err);
                        }
                    else
                    {
                        console.log(result);
                    }
                });
            });
            //Completed
            callback({"commentID" : result._id});
        }
    });
};

//needs to be tested
//Updates the comment to what it was edited to
module.exports.editComment = function(commentID, commentData){
  DBRef.collection('comments').update({"_id":mongojs.ObjectId(commentID)},commentData,function(err, result){
    if (err){
      console.log(err);
    } else {
        console.log(result);
    }
  });
};

//Retrieves the comment
module.exports.getComment = function(commentID, callback) {
    DBRef.collection('comments').findOne({"_id": mongojs.ObjectId(commentID)}, function (err, doc){
    if (err){
      console.log(err);
    } else {
      DBRef.collection('commentsView').findOne({"_id": mongojs.ObjectId(doc.commentsView)}, function (err, commentsViewDoc){
          DBRef.collection('events').findOne({"_id": mongojs.ObjectId(commentsViewDoc.eventID)}, function (err, eventDoc) {
              DBRef.collection('clubs').findOne({"_id": mongojs.ObjectId(eventDoc.club)}, function (err, clubDoc) {
                  eventDoc.club = clubDoc;
                  commentsViewDoc.eventID = eventDoc;
                  doc.commentsView = commentsViewDoc;
                  callback(doc);
              });
          });
      });
    }
  });
};

//needs to be tested
//Retrieves all the comments in the database
module.exports.getAllComments = function(callback) {
    DBRef.collection('comments').find({}).toArray(function(err, docs) {
	if (err){
            console.log(err);
	} else {
            var allCommentsObject = {
                "comments" : docs
            };
	    callback(allCommentsObject);
	}
    });
};
//Deletes the comment
module.exports.deleteComment = function (commentID){
    DBRef.collection('comments').findOne({"_id": mongojs.ObjectId(commentID)}, function (err, doc){
        if (err){
            console.log(err);
        } else {
            DBRef.collection('commentsView').update({"_id": mongojs.ObjectId(doc.commentsView)}, {$pull:{comments:{$in:[commentID]}}},function (err, result) {
                if (err) {
                    console.log(err);
                }
                else {
                    //Completed
                    console.log(result);
                }
            });
        }
        DBRef.collection('comments').remove({"_id": mongojs.ObjectId(commentID)}, false, function (err, result){
            if (err){
                console.log(err);
            } else {
                console.log(result);
            }
        });
    });
};

//Retrieves the commentsView
module.exports.getCommentsView = function (commentsViewID, callback){
    DBRef.collection('commentsView').findOne({"_id": mongojs.ObjectId(commentsViewID)},function (err, doc) {
        if (err) {
            console.log(err);
        }
        else {
            //Completed
            DBRef.collection('events').findOne({"_id": mongojs.ObjectId(doc.eventID)}, function (err, eventDoc) {
                doc.eventID = eventDoc;
            });
            DBRef.collection('comments').find({"commentsView": commentsViewID}).toArray(function (err, commentsDoc) {
                doc.comments = commentsDoc;
                callback(doc);
            });
        }
    });
};

//retrieves the reports from users
module.exports.sendReports = function (reportData, callback){
	DBRef.collection('reports').save(reportData, function(err, result) {
	console.log(result);
	if (err) {
	    console.log(result);
	} else {
	    DBRef.collection('reports').findOne({"_id": mongojs.ObjectId(result.reports)}, function (err, doc){
		if (err) {
		    console.log(err);
		} else {
		    console.log(doc);
		}
	    //Completed
	    callback({"reportID" : result._id});
	}
      });
 };
	