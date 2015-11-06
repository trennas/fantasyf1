var app = angular.module('ff1App', [ 'ngCookies' ]);
app.controller('controller',
function($scope, $http) {
    $scope.report = {};
    $scope.teams = [];
    $scope.completedRounds = [];
    
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
});