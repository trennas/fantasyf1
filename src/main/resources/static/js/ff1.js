var app = angular.module('myApp', [ 'ngCookies' ]);
app.controller('controller',
function($scope, $http) {
    $scope.report = {};
    $scope.reports = [];
    $scope.completedReports = [];
    $scope.search = function() {
        $('#spinner').show();
        $http
            .get('reports')
            .success(
                    function(response) {
                        $scope.reports = response;
                        $scope.ready = 0;
                        $scope.reserved = 0;
                        $scope.completed = 0;
                        $scope.completedReports = [];
                        for (var i = 0; i < $scope.reports.length; i++) {
                            if (typeof $scope.reports[i]['completedBy'] === 'string') {
                                $scope.completed++;
                                $scope.completedReports
                                        .push($scope.reports[i]);
                            } else if (typeof $scope.reports[i]['reservedBy'] === 'string') {
                                $scope.reserved++;
                            } else {
                                $scope.ready++;
                            }
                        }
                        $('#spinner').hide();
                    }).error(function(response, status) {
                alert("Status: " + status);
                $('#spinner').hide();
            });
    };
    $scope.open = function(id) {
        $('#main-wrapper').hide();
        $('#spinner').show();
        $('tr').removeClass('info');
        $('#sprn' + id).parent().addClass('info');
        $http
                .get('report?id=' + id)
                .success(
                        function(response) {
                            $scope.report = response;
                            var reportHtml = '';
                            var i = 1;
                            var lastHeading = '';
                            $.each($scope.report, function(k, v) {
                                if(!k.match(/^(xml|id|submitTime|reservedBy|completedBy)$/)) {
                                    if(k.indexOf('-') > -1) {
                                    var x = k.split("-");
                                    if(x[0] !== lastHeading) {
                                        lastHeading = x[0];
                                        reportHtml += '<tr><td colspan="3"><h4>'+x[0]+'</h4></td><td>';
                                    }
                                    }
                                    var field = k.replace(/.*-/, '');
                                    reportHtml += '<tr><td>'+i+'</td><td>'+field+'</td><td>'+v+'</td><td><a href="#" onclick="selectText(this.parentNode.parentNode.childNodes[2]); return false;">copy</a></td></tr>';
                                    i++;
                                }
                            });
                            $('#reportTable').html(reportHtml);
                            $('#spinner').hide();
                            $('#main-wrapper').show();
                        }).error(function(response, status) {
                    $('#spinner').hide();
                    alert("Status: " + status);
                });
    };

    var formatXml = function(xmlIn) {
        var reg = /(>)\s*(<)(\/*)/g;
        var wsexp = / *(.*) +\n/g;
        var contexp = /(<.+>)(.+\n)/g;
        var xml = xmlIn.replace(reg, '$1\n$2$3').replace(wsexp, '$1\n').replace(contexp,
                '$1\n$2');
        var formatted = '';
        var lines = xml.split('\n');
        var indent = 0;
        var lastType = 'other';
        // 4 types of tags - single, closing, opening, other (text, doctype,
        // comment) - 4*4 = 16 transitions
        var transitions = {
            'single->single' : 0,
            'single->closing' : -1,
            'single->opening' : 0,
            'single->other' : 0,
            'closing->single' : 0,
            'closing->closing' : -1,
            'closing->opening' : 0,
            'closing->other' : 0,
            'opening->single' : 1,
            'opening->closing' : 0,
            'opening->opening' : 1,
            'opening->other' : 1,
            'other->single' : 0,
            'other->closing' : -1,
            'other->opening' : 0,
            'other->other' : 0
        };

        for (var i = 0; i < lines.length; i++) {
            var ln = lines[i];
            var single = Boolean(ln.match(/<.+\/>/));
            var closing = Boolean(ln.match(/<\/.+>/));
            var opening = Boolean(ln.match(/<[^!].*>/));
            var type = single ? 'single' : closing ? 'closing'
                    : opening ? 'opening' : 'other';
            var fromTo = lastType + '->' + type;
            lastType = type;
            var padding = '';

            indent += transitions[fromTo];
            for (var j = 0; j < indent; j++) {
                padding += '  ';
            }
            if (fromTo === 'opening->closing') {
                formatted = formatted.substr(0, formatted.length - 1) + ln + '\n';
            }
            else {
                formatted += padding + ln + '\n';
            }
        }

        return formatted;
    };

    $scope.getXml = function() {
        $('#spinner').show();
        $http
                .get('reportxml?id=' + $scope.report.id)
                .success(
                        function(response) {
                            $scope.report.xml = formatXml(response);
                        }).error(function(response, status) {
                    alert("Status: " + status);
                });
        $('#spinner').hide();
    };
    $scope.reserve = function(id) {
        $http.get('reserve?id=' + id).success(
                function() {
                    $scope.open(id);
                    $scope.search();
                }).error(function(response) {
            alert(response.message);
            $scope.open(id);
            $scope.search();
        });
    };
    $scope.cancel = function(id) {
        $http.get('cancel?id=' + id).success(
                function() {
                    $scope.search();
                    $scope.open(id);
                }).error(function(response) {
            alert(response.message);
        });
    };
    $scope.complete = function(id) {
        if (confirm("Are you sure you wish to mark this report as completed.")) {
            $http.get('complete?id=' + id).success(
                    function() {
                        $scope.search();
                        $scope.open(id);
                    }).error(function(response) {
                alert(response.message);
            });
        }
    };
    $scope.objectKeys = function(obj) {
        return Object.keys(obj);
    };
    $scope.clearDate = function() {
        $('#fromDate').datepicker('setDate', null);
        $('#toDate').datepicker('setDate', null);
        $scope.search();
    };
    $scope.searchByDateRange = function() {
        var startTime = "T00:00:00";
        var endTime = "T23:59:59";
        $('#spinner').show();
        $('#main-wrapper').hide();
        $http
            .get('reportsdaterange?fromDate=' + $('#fromDate').val() + startTime + '&toDate=' + $('#toDate').val() + endTime)
            .success(
                    function(response) {
                        $scope.reports = response;
                        $scope.total = $scope.reports.length;
                        $scope.totalQa = 0;
                        $scope.totalComparison = 0;
                        for (var i = 0; i < $scope.reports.length; i++) {
                            if (typeof $scope.reports[i]['serviceProviderReferenceNumber'] === 'string' &&
                                    $scope.reports[i]['serviceProviderReferenceNumber'].indexOf("UME") === 0) {
                                $scope.totalComparison++;
                            } else if (typeof $scope.reports[i]['serviceProviderReferenceNumber'] === 'string' &&
                                    $scope.reports[i]['serviceProviderReferenceNumber'].indexOf("LV") === 0) {
                                $scope.totalQa++;
                            }
                        }
                        $('#spinner').hide();
                    }).error(function(response, status) {
                alert("Status: " + status);
                $('#spinner').hide();
            });
    };
    $( document ).ready(function() {
        $('.input-daterange').datepicker({
            todayBtn: "linked",
            autoclose: true,
            todayHighlight: true,
            language: "en-GB",
            format: "dd/mm/yyyy",
            orientation: "top",
            date: new Date()
        });
        $("#fromDate").datepicker("setDate", new Date());
        $("#toDate").datepicker("setDate", new Date());
        $('.input-daterange').datepicker()
        .on('changeDate', function(){
            $scope.searchByDateRange();
        });
        $scope.searchByDateRange();
    });
});

function selectText( containerid ) {

    var node = containerid;
    var range;
    if ( document.selection ) {
        range = document.body.createTextRange();
        range.moveToElementText( node  );
        range.select();
    } else if ( window.getSelection ) {
        range = document.createRange();
        range.selectNodeContents( node );
        window.getSelection().removeAllRanges();
        window.getSelection().addRange( range );
    }
    document.execCommand('copy');
}