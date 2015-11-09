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
    
    $scope.getLeagueTable = function() {
        $('#spinner').show();
        $http.get('teams')
            .success(function(response) {
                $scope.teams = response;
                $scope.getEvents();                
            })
            .error(function(response, status) {
                $('#spinner').hide();
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
        $http.post('saveResult', result)
            .success(function(response) {
                $scope.status = "Success";
            })
            .error(function(response, status) {
                alert("Status: " + status);
            });
    };
    
    $scope.refreshResult = function(event) {
        $http.get('refreshResult?round=' + event)
            .success(function(response) {
                $scope.status = "Result refreshed";
            })
            .error(function(response, status) {
                alert("Status: " + status);
            });
    };
    
    $scope.refreshAllResults = function(event) {
        $http.get('refreshAllResults')
            .success(function(response) {
                $scope.status = "All results refreshed";
            })
            .error(function(response, status) {
                alert("Status: " + status);
            });
    };
    
    $scope.getEvents = function() {
        $http.get('events')
            .success(function(response) {
                $scope.events = response;
                $('#spinner').hide();
            })
            .error(function(response, status) {
                $('#spinner').hide();
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