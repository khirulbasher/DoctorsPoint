(function() {
    'use strict';

    angular
        .module('projectApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('thana', {
            parent: 'entity',
            url: '/thana?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projectApp.thana.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/thana/thanas.html',
                    controller: 'ThanaController',
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
                    $translatePartialLoader.addPart('thana');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('thana-detail', {
            parent: 'thana',
            url: '/thana/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projectApp.thana.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/thana/thana-detail.html',
                    controller: 'ThanaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('thana');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Thana', function($stateParams, Thana) {
                    return Thana.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'thana',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('thana-detail.edit', {
            parent: 'thana-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/thana/thana-dialog.html',
                    controller: 'ThanaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Thana', function(Thana) {
                            return Thana.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('thana.new', {
            parent: 'thana',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/thana/thana-dialog.html',
                    controller: 'ThanaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                code: null,
                                active: false,
                                latitude: null,
                                longitude: null,
                                lastModifiedBy: null,
                                lastModifyDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('thana', null, { reload: 'thana' });
                }, function() {
                    $state.go('thana');
                });
            }]
        })
        .state('thana.edit', {
            parent: 'thana',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/thana/thana-dialog.html',
                    controller: 'ThanaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Thana', function(Thana) {
                            return Thana.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('thana', null, { reload: 'thana' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('thana.delete', {
            parent: 'thana',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/thana/thana-delete-dialog.html',
                    controller: 'ThanaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Thana', function(Thana) {
                            return Thana.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('thana', null, { reload: 'thana' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
