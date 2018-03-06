(function() {
    'use strict';

    angular
        .module('projectApp', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar'
        ]).directive('checkOnDatabase',['$http',function ($http) {
            return {
                restrict: 'A',
                require: 'ngModel',
                link: function (scope, elements, attribute, ctrl) {
                    var validate={
                        table:attribute.table,
                        column:attribute.column,
                        value:attribute.ngModel
                    };
                    scope.$watch(attribute.ngModel,function (newVal,oldVal) {

                        if(newVal!=oldVal) {
                            validate.value=newVal;
                            $http({
                                method: 'POST',
                                url: 'out/utility/checkOnDatabase/',
                                data: validate
                            }).success(function (data) {
                                ctrl.$setValidity('isUnique',data.isUnique);
                            }).error(function (data) {
                                ctrl.$setValidity('isUnique',false);
                            });
                        }
                    })
                }
            }
        }])
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler','$rootScope','$state'];

    function run(stateHandler, translationHandler,$rootScope,$state) {
        stateHandler.initialize();
        translationHandler.initialize();
        $rootScope.currentDate=new Date();

        $rootScope.getActive=function(active) {
          return active?"Active":"Inactive";
        };

        $rootScope.back = function (st,rld) {
            $state.go(st,{},{reload:rld})
        };
        $rootScope.back = function (st) {
            $state.go(st,{},{reload:false})
        };
    }
})();

