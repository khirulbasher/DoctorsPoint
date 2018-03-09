'use strict';

describe('Controller Tests', function() {

    describe('AccountInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAccountInfo, MockThana, MockPost;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAccountInfo = jasmine.createSpy('MockAccountInfo');
            MockThana = jasmine.createSpy('MockThana');
            MockPost = jasmine.createSpy('MockPost');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AccountInfo': MockAccountInfo,
                'Thana': MockThana,
                'Post': MockPost
            };
            createController = function() {
                $injector.get('$controller')("AccountInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'projectApp:accountInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
