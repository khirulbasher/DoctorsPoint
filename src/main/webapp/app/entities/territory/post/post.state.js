(function() {
    'use strict';

    angular
        .module('projectApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('post', {
            parent: 'entity',
            url: '/post?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TERRITORY'],
                pageTitle: 'projectApp.post.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/territory/post/posts.html',
                    controller: 'PostController',
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
                    $translatePartialLoader.addPart('post');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('post-detail', {
            parent: 'post',
            url: '/post/{id}',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TERRITORY'],
                pageTitle: 'projectApp.post.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/territory/post/post-detail.html',
                    controller: 'PostDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('post');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Post', function($stateParams, Post) {
                    return Post.get({id : $stateParams.id}).$promise;
                }]
            }
        }).state('post.new', {
            parent: 'post',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TERRITORY']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/territory/post/post-dialog.html',
                    controller: 'PostDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('post');
                    return $translate.refresh();
                }],
                entity: function () {
                    return null;
                }
            }
        })
        .state('post.edit', {
            parent: 'post',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TERRITORY']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/territory/post/post-dialog.html',
                    controller: 'PostDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('post');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Post', function($stateParams, Post) {
                    return Post.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('post.delete', {
            parent: 'post',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TERRITORY']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entity-global-dialog.html',
                    controller: 'EntityGlobalController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        obj: ['$stateParams','Post','$rootScope', function($stateParams, Post,$rootScope) {
                            return {
                                title:'Post Delete Operation',
                                callback: function() {
                                    Post.delete({id:$stateParams.id},function () {
                                        $rootScope.$broadcast('post','loadAll');
                                    });
                                }
                            }
                        }]
                    }
                }).result.then(function() {
                    $state.go('post', null, { reload: 'post' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
