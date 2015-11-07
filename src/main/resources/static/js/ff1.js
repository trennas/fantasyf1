var app = angular.module('ff1App', [ 'ngCookies' ]);
app.controller('controller',
function($scope, $http, $location) {    
    $scope.teams = [];
    $scope.events = [];
    $scope.event;
    
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