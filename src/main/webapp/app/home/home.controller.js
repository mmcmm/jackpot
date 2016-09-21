(function () {
    'use strict';

    angular
        .module('ninjaskinsApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'JackpotDeposit', 'CreditDeposit', 'AllJackpotDeposit'];

    function HomeController($scope, Principal, LoginService, $state, JackpotDeposit, CreditDeposit, AllJackpotDeposit) {
        var vm = this;

        vm.error = null;
        vm.save = save;
        vm.jackpotDeposit = null;
        vm.creditDeposit = null;
        vm.allJackpotDeposits = null;
        vm.totalJackpotDeposits = null;
        vm.allJackpotDepositsUsers = {};
        vm.jackpot = null;
        vm.account = null;
        vm.success = null;
        vm.jackpotRoundHash = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });

        getAccount();
        showAllJackpotDeposit();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            }).finally(function () {
                if (vm.isAuthenticated()) {
                    vm.creditDeposit = CreditDeposit.get();
                }
            });
        }

        function register() {
            $state.go('register');
        }

        function save() {
            vm.isSaving = true;
            JackpotDeposit.update(vm.jackpotDeposit, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess(result) {
            vm.isSaving = false;
            vm.error = null;
            vm.success = 'OK';
            vm.creditDeposit.creditBalance -= vm.jackpotDeposit.amount;
        }

        function onSaveError() {
            vm.isSaving = false;
            vm.success = null;
            vm.error = 'ERROR';
        }

        function showAllJackpotDeposit() {
            vm.allJackpotDeposits = AllJackpotDeposit.get(null, function () {
                calculateJackpotDepositsData();
            });
        }

        function calculateJackpotDepositsData() {
            if (vm.allJackpotDeposits.length) {
                for (var index = 0; index < vm.allJackpotDeposits.length; index++) {
                    if (vm.allJackpotDeposits[index].amount == -1) {
                        vm.jackpotRoundHash = vm.allJackpotDeposits[index].user;
                        continue;
                    }
                    vm.totalJackpotDeposits += vm.allJackpotDeposits[index].amount;
                    var user = vm.allJackpotDeposits[index].user;
                    if (typeof vm.allJackpotDepositsUsers[user] === 'undefined') {
                        vm.allJackpotDepositsUsers[user] = {user: user, amount: 0, chance: 0};
                    }
                    vm.allJackpotDepositsUsers[user].amount += vm.allJackpotDeposits[index].amount;
                }
                for (var user2 in vm.allJackpotDepositsUsers) {
                    if (vm.allJackpotDepositsUsers.hasOwnProperty(user2)) {
                        vm.allJackpotDepositsUsers[user2].chance =
                            (vm.allJackpotDepositsUsers[user2].amount / vm.totalJackpotDeposits).toFixed(4) * 100;
                    }
                }
            }
        }
    }
})();
