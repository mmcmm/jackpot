(function() {
    'use strict';

    angular
        .module('ninjaskinsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('credit-deposit', {
            parent: 'account',
            url: '/credit-deposit',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ninjaskinsApp.creditDeposit.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/credit-deposit/credit-deposit.html',
                    controller: 'CreditDepositController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('creditDeposit');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
