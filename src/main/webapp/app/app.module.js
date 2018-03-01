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
        ])
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
