(function() {
    'use strict';

    angular
        .module('ninjaskinsApp')
        .controller('CreditDepositController', CreditDepositController);

    CreditDepositController.$inject = ['Principal', 'JhiLanguageService', '$translate', 'CreditDeposit'];

    function CreditDepositController (Principal, JhiLanguageService, $translate, CreditDeposit) {
        var vm = this;

        vm.error = null;
        vm.save = save;
        vm.creditDepositAccount = null;
        vm.success = null;
        vm.creditDeposit = null;

        /**
         * Store the "creditDeposit account" in a separate variable, and not in the shared "account" variable.
         */
        var copyCreditDepositAccount = function (account) {
            return {
                login: account.login
            };
        };

        Principal.identity().then(function(account) {
            vm.creditDepositAccount = copyCreditDepositAccount(account);
            vm.creditDeposit = CreditDeposit.get();
        });

        function save () {
            vm.isSaving = true;
            CreditDeposit.update(vm.creditDeposit, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            vm.error = null;
            vm.success = 'OK';

            Principal.identity(true).then(function(creditDeposit) {
                vm.creditDepositAccount = copyCreditDepositAccount(creditDeposit);
                vm.creditDeposit.creditBalance += vm.creditDeposit.depositCredits;
            });
        }

        function onSaveError () {
            vm.isSaving = false;
            vm.success = null;
            vm.error = 'ERROR';
        }
    }
})();
