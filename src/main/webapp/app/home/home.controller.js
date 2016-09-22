(function () {
    'use strict';

    angular
        .module('ninjaskinsApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'JackpotDeposit', 'CreditDeposit', 'AllJackpotDeposit', 'CurrentJackpot', ' $timeout'];

    function HomeController($scope, Principal, LoginService, $state, JackpotDeposit, CreditDeposit, AllJackpotDeposit, CurrentJackpot, $timeout) {
        var vm = this;

        vm.error = null;
        vm.save = save;
        vm.jackpotDeposit = null;
        vm.creditDeposit = null;
        vm.allJackpotDeposits = null;
        vm.totalJackpotDeposits = null;
        vm.allJackpotDepositsUsers = {};
        vm.jackpot = CurrentJackpot.get();
        vm.jackpotFee = null;
        vm.jackpotProgressStyle = null;
        vm.jackpotIsDrawing = null;
        vm.jackpotWinner = null;
        vm.jackpotSpinMachine = null;
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
                runSpinMachine();
            });
        }

        function calculateJackpotDepositsData() {
            if (vm.allJackpotDeposits.length) {
                for (var index = 0; index < vm.allJackpotDeposits.length; index++) {
                    vm.totalJackpotDeposits += vm.allJackpotDeposits[index].amount;
                    var user = vm.allJackpotDeposits[index].user;
                    if (typeof vm.allJackpotDepositsUsers[user] === 'undefined') {
                        vm.allJackpotDepositsUsers[user] = {user: user, amount: 0, chance: 0};
                    }
                    vm.allJackpotDepositsUsers[user].amount += vm.allJackpotDeposits[index].amount;
                }
                for (var user2 in vm.allJackpotDepositsUsers) {
                    if (vm.allJackpotDepositsUsers.hasOwnProperty(user2)) {
                        vm.allJackpotDepositsUsers[user2].chance = (vm.allJackpotDepositsUsers[user2].amount / vm.totalJackpotDeposits) * 100;
                    }
                }
                vm.jackpotFee = Math.floor(vm.totalJackpotDeposits * vm.jackpot.percentFee);
                vm.totalJackpotDeposits -= vm.jackpotFee;
            }
        }

        function runSpinMachine() {
            if (vm.allJackpotDeposits.length == (vm.jackpot.minDepositsNr)) {
                var lis = "";
                for (var index = 0; index < vm.allJackpotDeposits.length; index++) {
                    lis += "<li>" + vm.allJackpotDeposits[index].user + "</li>"
                }
                angular.element("#jackpot-progress-bar").hide();
                angular.element("#spinMachine").append(lis).css("display", "inline-block");
                vm.jackpotSpinMachine = angular.element("#spinMachine").slotMachine({
                    active: 0,
                    delay: vm.allJackpotDeposits.length * 250
                });
                vm.jackpotSpinMachine.shuffle();
            }
        }

        function stopSpinMachine(winner) {
            if (vm.jackpotSpinMachine.running) { // we make sure machine running
                vm.jackpotWinner = winner;
                var winnerIndex = 0;
                for (var index = 0; index < vm.allJackpotDeposits.length; index++) {
                    if (vm.allJackpotDeposits[index].user == winner) {
                        winnerIndex = index;
                    }
                }
                vm.jackpotSpinMachine.setRandomize(function () {
                        return winnerIndex;
                    }
                );
                vm.jackpotSpinMachine.stop();
                angular.element("#jackpot-winround").show();
                $timeout(clearJackpotRound, vm.jackpot.delayAfterWinner * 1000);
            }
        }

        function clearJackpotRound() {
            vm.allJackpotDeposits = [];
            angular.element("#spinMachine").delay().hide();
            angular.element("#jackpot-winround").hide();
            vm.jackpotSpinMachine.destroy();
        }
    }
})();
