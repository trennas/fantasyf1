var app = angular.module('ff1App', [ 'ngCookies', 'angular-loading-bar' ]);
app.controller('controller',
function($scope, $http, $location) {    
    $scope.teams = [];
    $scope.events = [];
    $scope.drivers = [];
    $scope.cars = [];
    $scope.engines = [];
    $scope.rules = [];
    $scope.event;
    $scope.team = {};
    $scope.bestTheoreticalTeam = {};
    $scope.driver;
    $scope.car;
    $scope.engine;    
    $scope.status;
    $scope.seasonStarted;
    $scope.seasonStartDate;
    
    $('#spinner').hide();
    
    $scope.getLeagueTable = function() {
        $('#leagueSpinner').show();
        $('#raceSpinner').show();
        $http.get('teams')
            .success(function(response) {
                $scope.teams = response;                
                $scope.getBestTheoreticalTeam();
                $scope.getEvents();                
            })
            .error(function(response, status) {
                $('#leagueSpinner').hide();
                alert("Status: " + status);                
            });        
    };
    
    $scope.getComponents = function() {
    	$scope.getAllDrivers();
    	$scope.getCars();
    	$scope.getEngines();
    };
    
    $scope.saveComponents = function() {
        $('#spinner').show();
        
        $http.post('saveengines', $scope.engines)
        .success(function(response) {
        	$http.post('savecars', $scope.cars)
            .success(function(response) {
            	$http.post('savedrivers', $scope.drivers)
                .success(function(response) {
                	$scope.getComponents();
                	$scope.status = "Components Updated.";
                    $('#spinner').hide();
                });
            });
        });
    };
    
    $scope.getBestTheoreticalTeam = function() {        
        $http.get('besttheoreticalteam')
            .success(function(response) {
                $scope.bestTheoreticalTeam = response;
                $('#leagueSpinner').hide();
            })
            .error(function(response, status) {
                alert("Status: " + status);    
                $('#leagueSpinner').hide();
            });        
    };
    
    $scope.getBestTheoreticalTeamForRound = function() {
        $http.get('besttheoreticalteamforround?round=' + ($location.search()).round)
            .success(function(response) {
            	$scope.bestTheoreticalTeam = response;
            })
            .error(function(response, status) {
                alert("Status: " + status);                
            });        
    };
    
    $scope.getTeams = function() {
        $http.get('teams')
            .success(function(response) {
                $scope.teams = response;
            })
            .error(function(response, status) {
                alert("Status: " + status);                
            });        
    };
    
    $scope.getRules = function() {
        $http.get('getRules')
            .success(function(response) {
                $scope.rules = response;
            })
            .error(function(response, status) {
                alert("Status: " + status);                
            });        
    };
    
    $scope.getSeasonStartDate = function() {
        $http.get('seasonstartdate')
            .success(function(response) {
                $scope.seasonStartDate = response;
            })
            .error(function(response, status) {
                alert("Status: " + status);                
            });        
    };
    
    $scope.saveResult = function(result) {
        $('#spinner').show();
        $http.post('saveresult', result)
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
    
    $scope.addDriver = function() {
        $scope.drivers.push({});
    };
    $scope.deleteDriver = function(index) {
        $scope.drivers.splice(index, 1);
    };
    
    $scope.addCar = function() {
        $scope.cars.push({});
    };
    $scope.deleteCar = function(index) {
        $scope.cars.splice(index, 1);
    };
    
    $scope.addEngine = function() {
        $scope.engines.push({});
    };
    $scope.deleteEngine = function(index) {
        $scope.engines.splice(index, 1);
    };
    
    $scope.refreshResult = function(event) {
        $('#spinner').show();
        $http.get('refreshresult?round=' + event)
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
    
    $scope.deleteTeam = function(id) {
        $http.get('deleteteam?id=' + id)
            .success(function(response) {
                if(response == 1) {
                    $scope.status = "Team Deleted";
                } else {
                    $scope.status = "Couldn't Delete Team";
                }
            })
            .error(function(response, status) {
                $('#spinner').hide();
                alert("Status: " + status);
            });
    };
    
    $scope.deleteResult = function(event) {
        $('#spinner').show();
        $http.get('deleteevent?round=' + event)
            .success(function(response) {
                $('#spinner').hide();
                if(response == 1) {
                    $scope.status = "Result deleted";
                } else {
                    $scope.status = "Unable to delete result";
                }
            })
            .error(function(response, status) {
                $('#spinner').hide();
                alert("Status: " + status);
            });
    };
    
    $scope.refreshAllResults = function(event) {
        $('#leagueSpinner').show();
        $('#raceSpinner').show();
        $scope.teams = [];
        $scope.events = [];
        $scope.bestTheoreticalTeam = {};
        $http.get('refreshAllResults')
            .success(function(response) {                
                $scope.status = "All results refreshed";                
                $scope.getLeagueTable();
            })
            .error(function(response, status) {
                $('#leagueSpinner').hide();
                $('#raceSpinner').hide();
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
    
    $scope.getSeasonStarted = function() {
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
        
        var numericArray = [];
        for (var item in team.drivers) {
            numericArray.push(team.drivers[item]);
        }
        team.drivers = numericArray;
        
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
    
    $scope.getDriver = function(number) {
        $http.get('driver?number=' + number)
            .success(function(response) {
                $scope.driver = response;
            })
            .error(function(response, status) {
                alert("Status: " + status);
            });
    };
    
    $scope.getDriverByName = function(name) {
        $http.get('driverbyname?name=' + name)
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
    
    $scope.getAllDrivers = function() {
        $http.get('alldrivers')
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
    
    $scope.addEvent = function(round) {
        $http.get('newevent')
            .success(function(response) {
                $scope.event = response;
            })
            .error(function(response, status) {
                alert("Status: " + status);
            });
    };
    
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