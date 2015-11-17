var app = angular.module('ff1App', [ 'ngCookies' ]);
app.controller('controller',
function($scope, $http, $location) {    
    $scope.teams = [];
    $scope.events = [];
    $scope.drivers = [];
    $scope.cars = [];
    $scope.engines = [];
    $scope.event;
    $scope.team;
    $scope.driver;
    $scope.car;
    $scope.engine;    
    $scope.status;
    $scope.seasonStarted;
    
    $scope.getLeagueTable = function() {
        $('#leagueSpinner').show();
        $('#raceSpinner').show();
        $http.get('teams')
            .success(function(response) {
                $scope.teams = response;
                $('#leagueSpinner').hide();
                $scope.getEvents();                
            })
            .error(function(response, status) {
                $('#leagueSpinner').hide();
                alert("Status: " + status);                
            });        
    };
    
    $scope.getTeams = function() {
        $('#spinner').show();
        $http.get('teams')
            .success(function(response) {
                $scope.teams = response;
                $('#spinner').hide();
            })
            .error(function(response, status) {
                $('#spinner').hide();
                alert("Status: " + status);                
            });        
    };
    
    $scope.saveResult = function(result) {
        $('#spinner').show();
        $http.post('saveResult', result)
            .success(function(response) {
                $scope.event = response;
                $scope.status = "Success";
                $('#spinner').hide();
            })
            .error(function(response, status) {
                $('#spinner').hide();
                alert("Status: " + status);
            });
    };
    
    $scope.updateRemark = function(index, remark) {
        $scope.event.remarks[index] = remark;
    };
    
    $scope.deleteRemark = function(index) {
        $scope.event.remarks.splice(index, 1);
    };  
    
    $scope.addRemark = function() {
        $scope.event.remarks.push("");
    };
    
    $scope.refreshResult = function(event) {
        $('#spinner').show();
        $http.get('refreshResult?round=' + event)
            .success(function(response) {
                $('#spinner').hide();
                $scope.event = response;
                $scope.status = "Result refreshed";
            })
            .error(function(response, status) {
                $('#spinner').hide();
                alert("Status: " + status);
            });
    };
    
    $scope.refreshAllResults = function(event) {
        $('#spinner').show();
        $http.get('refreshAllResults')
            .success(function(response) {                
                $scope.status = "All results refreshed";
                $('#spinner').hide();
            })
            .error(function(response, status) {
                $('#spinner').hide();
                alert("Status: " + status);
            });
    };
    
    $scope.myTeam = function() {
        $('#spinner').show();
        $http.get('myteam')
            .success(function(response) {
                $scope.team = response;                
                $scope.team.confirmPassword = $scope.team.password;
                $('#spinner').hide();
            })
            .error(function(response, status) {
                $('#spinner').hide();
                alert("Status: " + status);
            });
    };
    
    $scope.seasonStarted = function() {
        $http.get('seasonstarted')
            .success(function(response) {
                $scope.seasonStarted = response;
            })
            .error(function(response, status) {
                alert("Status: " + status);
            });
    };
    
    $scope.saveMyTeam = function(team) {
        $('#spinner').show();
        $http.post('savemyteam', team)
            .success(function(response) {
                $scope.status = response.message;
                $('#spinner').hide();
            })
            .error(function (data, status, headers, config) {
                $('#spinner').hide();
                alert("Status: " + data);
            });
    };
    
    $scope.getEvents = function() {
        $http.get('events')
            .success(function(response) {
                $scope.events = response;
                $('#raceSpinner').hide();
            })
            .error(function(response, status) {
            	$('#raceSpinner').hide();
                alert("Status: " + status);
            });
    };
    
    $scope.getTeam = function(id) {
        $http.get('team?id=' + id)
            .success(function(response) {
                $scope.team = response;
            })
            .error(function(response, status) {
                alert("Status: " + status);
            });
    };
    
    $scope.getDriver = function(name) {
        $http.get('driver?name=' + name)
            .success(function(response) {
                $scope.driver = response;
            })
            .error(function(response, status) {
                alert("Status: " + status);
            });
    };
    
    $scope.getCar = function(name) {
        $http.get('car?name=' + name)
            .success(function(response) {
                $scope.car = response;
            })
            .error(function(response, status) {
                alert("Status: " + status);
            });
    };
    
    $scope.getEngine = function(name) {
        $http.get('engine?name=' + name)
            .success(function(response) {
                $scope.engine = response;
            })
            .error(function(response, status) {
                alert("Status: " + status);
            });
    };
    
    $scope.getDrivers = function() {
        $http.get('drivers')
            .success(function(response) {
                $scope.drivers = response;
            })
            .error(function(response, status) {
                alert("Status: " + status);
            });
    };
    
    $scope.getCars = function() {
        $http.get('cars')
            .success(function(response) {
                $scope.cars = response;
            })
            .error(function(response, status) {
                alert("Status: " + status);
            });
    };
    
    $scope.getEngines = function() {
        $http.get('engines')
            .success(function(response) {
                $scope.engines = response;
            })
            .error(function(response, status) {
                alert("Status: " + status);
            });
    };
    
    $scope.getEvent = function(round) {
        $http.get('event?round=' + round)
            .success(function(response) {
                $scope.event = response;
            })
            .error(function(response, status) {
                alert("Status: " + status);
            });
    };
    
    $scope.getUnspecifiedEvent = function() {
    	$('#spinner').show();
    	//http://localhost:8080/race/#/!/?round=3
    	var round = ($location.search()).round;
    	$scope.getEvent(round);
    	$('#spinner').hide();
    }
    
    $scope.getLength = function(obj) {
        return Object.keys(obj).length;
    }
});

app.filter('range', function() {
  return function(input, min, max) {
    min = parseInt(min); //Make string input int
    max = parseInt(max);
    for (var i=min; i<=max; i++)
      input.push(i);
    return input;
  };
});