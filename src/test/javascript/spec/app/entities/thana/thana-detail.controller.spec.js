'use strict';

describe('Controller Tests', function() {

    describe('Thana Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockThana, MockDistrict;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockThana = jasmine.createSpy('MockThana');
            MockDistrict = jasmine.createSpy('MockDistrict');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Thana': MockThana,
                'District': MockDistrict
            };
            createController = function() {
                $injector.get('$controller')("ThanaDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'projectApp:thanaUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
