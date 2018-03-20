(function() {
    'use strict';

    angular
        .module('projectApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('notification', {
            parent: 'entity',
            url: '/notification?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT'],
                pageTitle: 'projectApp.notification.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mgt/notification/notifications.html',
                    controller: 'NotificationController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('notification');
                    $translatePartialLoader.addPart('priority');
                    $translatePartialLoader.addPart('actionType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('notification-detail', {
            parent: 'notification',
            url: '/notification/{id}',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT'],
                pageTitle: 'projectApp.notification.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mgt/notification/notification-detail.html',
                    controller: 'NotificationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('notification');
                    $translatePartialLoader.addPart('priority');
                    $translatePartialLoader.addPart('actionType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Notification', function($stateParams, Notification) {
                    return Notification.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('notification.new', {
            parent: 'notification',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mgt/notification/notification-dialog.html',
                    controller: 'NotificationDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('notification');
                    $translatePartialLoader.addPart('priority');
                    $translatePartialLoader.addPart('actionType');
                    return $translate.refresh();
                }],
                entity: function () {
                    return null;
                }
            }
        })
        .state('notification.edit', {
            parent: 'notification',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mgt/notification/notification-dialog.html',
                    controller: 'NotificationDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('notification');
                    $translatePartialLoader.addPart('priority');
                    $translatePartialLoader.addPart('actionType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Notification', function($stateParams, Notification) {
                    return Notification.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('notification.delete', {
            parent: 'notification',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entity-global-dialog.html',
                    controller: 'EntityGlobalController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        obj: ['$stateParams','Notification','$rootScope', function($stateParams, Notification,$rootScope) {
                            return {
                                title:'Notification Delete Operation',
                                callback: function() {
                                    Notification.delete({id:$stateParams.id},function () {
                                        $rootScope.$broadcast('notification','loadAll');
                                    });
                                }
                            }
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification', null, { reload: 'notification' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
