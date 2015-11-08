var app = angular.module('ff1App', [ 'ngCookies' ]);
app.controller('controller',
function($scope, $http, $location) {    
    $scope.teams = [];
    $scope.events = [];
    $scope.drivers = [];
    $scope.cars = [];
    $scope.engines = [];
    $scope.event;
    $scope.driver;
    $scope.car;
    $scope.engine;    
    
    $scope.getLeagueTable = function() {
        $('#spinner').show();
        $http.get('teams')
            .success(function(response) {
                $scope.teams = response;
            })
            .error(function(response, status) {
                alert("Status: " + status);
            });
        $('#spinner').hide();
    };
    
    $scope.getEvents = function() {
        $('#spinner').show();
        $http.get('events')
            .success(function(response) {
                $scope.events = response;
            })
            .error(function(response, status) {
                alert("Status: " + status);
            });
        $('#spinner').hide();
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