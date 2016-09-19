(function() {
    'use strict';

    angular
        .module('ninjaskinsApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'JackpotDeposit', 'CreditDeposit'];

    function HomeController ($scope, Principal, LoginService, $state, JackpotDeposit, CreditDeposit) {
        var vm = this;

        vm.error = null;
        vm.save = save;
        vm.jackpotDeposit = null;
        vm.creditDeposit = null;
        vm.account = null;
        vm.success = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
            vm.creditDeposit = CreditDeposit.get();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

        // Principal.identity().then(function() {
        //     vm.jackpotDeposit = JackpotDeposit.get();
        // });

        function save () {
            vm.isSaving = true;
            JackpotDeposit.update(vm.jackpotDeposit, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            vm.error = null;
            vm.success = 'OK';
        }

        function onSaveError () {
            vm.isSaving = false;
            vm.success = null;
            vm.error = 'ERROR';
        }
    }
})();
